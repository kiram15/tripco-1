package edu.csu2017fa314.T02.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.lang.Math;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.PrintWriter;
import java.util.Collections;
import java.io.IOException;

public class Hub {
    String[] infoArray;
    Map<String, Integer> columns = new LinkedHashMap<String, Integer>();
    ArrayList<Location> finalLocations = new ArrayList<Location>();
    static ArrayList<Location> kLocations = new ArrayList<Location>();
    ArrayList<Distance> distances = new ArrayList<Distance>();
  
    public ArrayList<Distance> readFile(String fileName) {
        File file = new File(fileName);
        Scanner scnr;
        try {
            scnr = new Scanner(file);
            if (scnr.hasNextLine()) {
                String s = scnr.nextLine();
                s = s.toLowerCase();
                String [] infoArray = s.split(",");

                for (int i = 0; i < infoArray.length; i++) {
                    String infoString = infoArray[i];
                    switch (infoString.trim()) { // associating column titles with column num, putting it in map
                        case "id":
                            columns.put("id", i);
                            break;
                        case "name":
                            columns.put("name", i);
                            break;
                        case "city":
                            columns.put("city", i);
                            break;
                        case "latitude":
                            columns.put("latitude", i);
                            break;
                        case "longitude":
                            columns.put("longitude", i);
                            break;
                        case "elevation":
                            columns.put("elevation", i);
                            break;
                    }
                }
            }
            while (scnr.hasNextLine()) {
                String brewery = scnr.nextLine();
                brewery = brewery.toLowerCase();
                String[] newArray = brewery.split(","); //column names

                String objectID = newArray[columns.get("id")].trim();
                String objectName = newArray[columns.get("name")].trim();
                String objectCity = newArray[columns.get("city")].trim();
                String objectLatitude = newArray[columns.get("latitude")].trim();
                String objectLongitude = newArray[columns.get("longitude")].trim();
                int objectElevation = Integer.parseInt(newArray[columns.get("elevation")].trim());
                

                double doubleLat = latLonConvert(objectLatitude);
                double doubleLon = latLonConvert(objectLongitude);

                Location location = new Location(objectID, objectName, objectCity, doubleLat,
                        doubleLon, objectElevation);

                finalLocations.add(location);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return addDistance();
    }
    
    public ArrayList<Distance> addDistance(){ //loops through the finalLocations array, calculating gcd between each possible two locations and adding these as distance objects(startID, endID, distance between them) to distances array
        for(int start = 0; start < finalLocations.size(); start++){
            for(int end = start + 1; end < finalLocations.size(); end++){
                String startID = (finalLocations.get(start)).getID();
                String endID = (finalLocations.get(end)).getID();
                int dist = greatCirDist((finalLocations.get(start)).getLatitude(), (finalLocations.get(start)).getLongitude(), (finalLocations.get(end)).getLatitude(), (finalLocations.get(end)).getLongitude());
                Distance d = new Distance(startID, endID, dist);
                distances.add(d);
            }
        }
        return distances;
    }
  
    public int greatCirDist(double lat1, double lon1, double lat2, double lon2){
        //set up basic variables
        //double a = 6378.137; //radius of earth in km
        double a = 3958.7613; //radius of earth in miles
        double f = 1/298.257223563; //flattening of ellipsoid
        double b = (1-f)*a; //radius at the poles
        double phi1 = Math.toRadians(lat1); //latitudes into phi
        double phi2 = Math.toRadians(lat2);
        double U1 = Math.atan((1-f)*Math.tan(phi1)); //reduced latitude
        double U2 = Math.atan((1-f)*Math.tan(phi2));
        double L = Math.toRadians(lon2-lon1); //difference between the 2 longitude points
        double lambda = L;
        double sinTheta = 0; double cosTheta = 0; double theta = 0; double sinAlpha = 0; double cos2Alpha = 0; double cos2ThetaM = 0; double C = 0; double lambdaPrev = 0;

        //iteratively calculating until lambda converges
        while(Math.abs(lambda-lambdaPrev) > 1e-12) {
            sinTheta = Math.sqrt(Math.pow(((Math.cos(U2)) * (Math.sin(lambda))), 2) + Math.pow((((Math.cos(U1)) * (Math.sin(U2))) - ((Math.sin(U1)) * (Math.cos(U2)) * (Math.cos(lambda)))), 2));
            cosTheta = ((Math.sin(U1)) * (Math.sin(U2))) + ((Math.cos(U1)) * (Math.cos(U2)) * (Math.cos(lambda)));
            theta = Math.atan2(sinTheta,cosTheta);
            sinAlpha = ((Math.cos(U1)) * (Math.cos(U2)) * (Math.sin(lambda))) / (sinTheta);
            cos2Alpha = 1 - (Math.pow(sinAlpha, 2));
            cos2ThetaM = cosTheta - (((2) * (Math.sin(U1)) * (Math.sin(U2))) / (cos2Alpha));
            C = (f / 16) * cos2Alpha * (4 + f * (4 - (3 * cos2Alpha)));
            lambdaPrev = lambda;
            lambda = L + (1 - C) * f * sinAlpha * (theta + C * sinTheta * (cos2ThetaM + C * cosTheta * (-1 + (2 * cos2ThetaM))));
        }

        //after lambda has converged to the desired degree of accuracy 10E-12 / 0.06mm finish calculation to get distance
        double u2 = cos2Alpha*(((Math.pow(a,2)) - (Math.pow(b,2)))/(Math.pow(b,2)));
        double A = 1 + (u2/16384)*(4096 + (u2*(-768 + (u2*(320-(175*u2))))));
        double B = (u2/1024)*(256 + (u2*(-128 + (u2*(74-(47*u2))))));
        double dTheta = B*sinTheta*(cos2ThetaM + ((0.25*B)*((cosTheta*(-1+(2*cos2ThetaM))) - ((B/6)*cos2ThetaM*(-3+(4*(Math.pow(sinTheta,2))))*(-3+(4*(Math.pow(cos2ThetaM,2))))))));
        double dist = b*A*(theta-dTheta);
        int gcd = Math.round((int)dist);
        return gcd;
    }
   
    public double latLonConvert(String s) {
		String sCopy = s;
		int end; String symbol;
		double retVal;
		ArrayList<Double> values = new ArrayList<>();
		
		//in the loops, uses symbol as stopping point, extracts number, adds to arrayList, 
		//and cuts off remaining, then uses the formula to convert into degrees

		if (s.contains("\"") && s.contains("'")) { // case for 106°49'43.24" W										
			for (int i = 0; i < 3; i++) {
				if (i == 0)      { symbol = "°"; }
				else if (i == 1) { symbol = "'"; }
				else             { symbol = "\""; }
				end = sCopy.indexOf(symbol);
				values.add(Double.parseDouble(sCopy.substring(0, end)));
				sCopy = sCopy.substring(end + 1);
			}
			retVal = (values.get(0) + (values.get(1) / 60) + (values.get(2) / 3600));	
			if (sCopy.equals("W") || sCopy.equals("S")) {
				return (retVal*(-1)); 
			}
			else {
				return retVal;
			}
		} 
		else if (s.contains("'")) { // case for 106°49.24' W format
			for (int i = 0; i < 2; i++) {
				if (i == 0) { symbol = "°"; }
				else        { symbol = "'"; }
				end = sCopy.indexOf(symbol);
				values.add(Double.parseDouble(sCopy.substring(0, end)));
				sCopy = sCopy.substring(end + 1);
			}
			retVal = (values.get(0) + (values.get(1) / 60));
			if (sCopy.equals("W") || sCopy.equals("S")) {
				return (retVal*(-1)); 
			}
			else {
				return retVal;
			}
			
		} 
		else if (s.contains("°")) { // case for 106.24° format
			end = sCopy.indexOf("°");
			return (Double.parseDouble(sCopy.substring(0, end)));
		} 
		else { // case for -106.24 format
			return Double.parseDouble(s);
		}
	}
  
  //method to write the JSON file
    public void writeJSON(ArrayList<Distance> distance) {
        //new JSONarray to add all the strings to
        JSONArray array = new JSONArray();

        //loop through all the distance objects in the distance array
        for (Distance d : distance) {
            JSONObject obj = new JSONObject();

            obj.put("start", d.getStartID());
            obj.put("end", d.getEndID());
            obj.put("distance", d.getGcd());

            array.add(obj);

        }

        try (PrintWriter write = new PrintWriter(new File("Itinerary.json"))) {
            write.print(array.toJSONString());
            write.close();
        } catch (IOException e) {
            System.out.print("Error: Cannot write to file" + e);
        }
    }

}
