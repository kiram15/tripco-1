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
    Map<Integer, String> reverseC = new LinkedHashMap<Integer, String>();
    ArrayList<Location> locations = new ArrayList<Location>();


    public ArrayList<Distance> readFile(String fileName) {
	ArrayList<Distance> distances = new ArrayList<Distance>();
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
                        case "name":
                            columns.put("name", i);
                            reverseC.put(i, "name");
                            break;
                        case "latitude":
                            columns.put("latitude", i);
                            reverseC.put(i, "latitude");
                            break;
                        case "longitude":
                            columns.put("longitude", i);
                            reverseC.put(i, "longitude");
                            break;
                        default:
                            columns.put(infoString.trim(), i);
                            reverseC.put(i, infoString.trim());
                            break;
                    }
                }
            }
            while (scnr.hasNextLine()) {
                String place = scnr.nextLine();
                place = place.toLowerCase();
                String[] props = place.split(","); //column names
                LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();

                String objectName = "";
                String objectLatitude = "";
                String objectLongitude = "";
                //populates necessary info into variables
                for(int i = 0; i < props.length; ++i){
                    if (i == columns.get("name")) {
                        objectName = props[i].trim();
                    }
                    else if (i == columns.get("latitude")){
                        objectLatitude = props[i].trim();
                    }
                    else if (i == columns.get("longitude")){
                        objectLongitude = props[i].trim();
                    }
                    else {
                        info.put(reverseC.get(i), props[i]);
                    }
                }

                double doubleLat = latLonConvert(objectLatitude);
                double doubleLon = latLonConvert(objectLongitude);

                Location location = new Location(objectName, doubleLat, doubleLon, info);

                locations.add(location);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return addDistance(distances);
    }

    public ArrayList<Distance> addDistance(ArrayList<Distance> distances){ //loops through the locations array, calculating gcd between each possible two locations and adding these as distance objects(startName, endName, distance between them) to distances array
        for(int start = 0; start < locations.size(); start++){
	    int end = start + 1;
	    if(end < locations.size()){
                String startName = (locations.get(start)).getName();
                String endName = (locations.get(end)).getName();
                int dist = greatCirDist((locations.get(start)).getLatitude(), (locations.get(start)).getLongitude(), (locations.get(end)).getLatitude(), (locations.get(end)).getLongitude());
                Distance d = new Distance(startName, endName, dist);
                distances.add(d);
            }
        }
        return distances;
    }

    public int greatCirDist(double lat1, double lon1, double lat2, double lon2){
        double r = 3958.7613; //radius of earth in miles
        double phi1 = Math.toRadians(lat1);
        double lam1 = Math.toRadians(lon1);
        double phi2 = Math.toRadians(lat2);
        double lam2 = Math.toRadians(lon2);
        double dLam = Math.abs(lam1-lam2);
        double dTheta = Math.atan(Math.sqrt(Math.pow((Math.cos(phi2)*Math.sin(dLam)),2)+Math.pow((Math.cos(phi1)*Math.sin(phi2)-Math.sin(phi1)*Math.cos(phi2)*Math.cos(dLam)),2))
                                    / (Math.sin(phi1)*Math.sin(phi2)+Math.cos(phi1)*Math.cos(phi2)*Math.cos(dLam)));
        double dist = dTheta*r;
        int gcd = (int)Math.round(dist);
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

            obj.put("start", d.getStartName());
            obj.put("end", d.getEndName());
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
