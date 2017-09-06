package edu.csu2017fa314.T02.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.lang.Math;


public class Hub{
    String[] infoArray;
    Map<String, Integer> columns = new LinkedHashMap<String, Integer>();
    ArrayList<Location> finalLocations = new ArrayList<>();
    static ArrayList<Location> kLocations = new ArrayList<>();
    ArrayList<Distance> distances = new ArrayList<Distance>();

    public ArrayList<Location> readFile(String fileName) {
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
                        case "altitude":
                            columns.put("altitude", i);
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
                int objectAltitude = Integer.parseInt(newArray[columns.get("altitude")].trim());

                Location location = new Location(objectID, objectName, objectCity, objectLatitude,
                        objectLongitude, objectAltitude);

                finalLocations.add(location);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        addDistance();
        return finalLocations;
    }
    
    public void addDistance(){ //loops through the finalLocations array, calculating gcd between each possible two locations and adding these as distance objects(startID, endID, distance between them) to distances array
        for(int start = 0; start < finalLocations.size(); start++){
            for(int end = start + 1; end < finalLocations.size(); end++){
                String startID = (finalLocations.get(start)).getID();
                String endID = (finalLocations.get(end)).getID();
                int dist = greatCirDist((finalLocations.get(start)).getLatitude(), (finalLocations.get(start)).getLongitude(), (finalLocations.get(end)).getLatitude(), (finalLocations.get(end)).getLongitude());
                Distance d = new Distance(startID, endID, dist);
                distances.add(d);
            }
        }
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
}
