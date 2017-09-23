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
import java.util.Set;

public class Hub {
    String[] infoArray;
    Map<String, Integer> columns = new LinkedHashMap<String, Integer>();
    Map<Integer, String> reverseC = new LinkedHashMap<Integer, String>();
    ArrayList<Location> finalLocations = new ArrayList<Location>();



    public ArrayList<Distance> readFile(String fileName) {
        ArrayList<Distance> distances = new ArrayList<Distance>();
        File file = new File(fileName);
        Scanner scnr;
        try {
            scnr = new Scanner(file);
            if (scnr.hasNextLine()) {
                String s = scnr.nextLine();
                s = s.toLowerCase();
                String[] infoArray = s.split(",");

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

                finalLocations.add(location);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return addDistance(distances);
    }

    public ArrayList<Distance> addDistance(ArrayList<Distance> distances) { //loops through the finalLocations array, calculating gcd between each possible two locations and adding these as distance objects(startID, endID, distance between them) to distances array
        for (int start = 0; start < finalLocations.size(); start++) {
            int end = start + 1;
            if (end < finalLocations.size()) {
                Location startID = finalLocations.get(start);
                Location endID = finalLocations.get(end);
                int dist = greatCirDist((finalLocations.get(start)).getLatitude(), (finalLocations.get(start)).getLongitude(), (finalLocations.get(end)).getLatitude(), (finalLocations.get(end)).getLongitude());
                Distance d = new Distance(startID, endID, dist);

                distances.add(d);
            }
        }
        return distances;
    }

    public int greatCirDist(double lat1, double lon1, double lat2, double lon2) {

        double r = 3958.7613; //radius of earth in miles
        double phi1 = Math.toRadians(lat1);
        double lam1 = Math.toRadians(lon1);
        double phi2 = Math.toRadians(lat2);
        double lam2 = Math.toRadians(lon2);
        double dLam = Math.abs(lam1 - lam2);
        double y = Math.sqrt(Math.pow((Math.cos(phi2)*Math.sin(dLam)),2)+Math.pow((Math.cos(phi1)*Math.sin(phi2)-Math.sin(phi1)*Math.cos(phi2)*Math.cos(dLam)),2));
        double x = (Math.sin(phi1)*Math.sin(phi2)+Math.cos(phi1)*Math.cos(phi2)*Math.cos(dLam));
        double dTheta  = Math.atan2(y,x);
        double dist = dTheta * r;
        int gcd = (int) Math.round(dist);
        return gcd;
    }

    public double latLonConvert(String s) {
        String sCopy = s;
        int end;
        String symbol;
        double retVal;
        ArrayList<Double> values = new ArrayList<>();

        //in the loops, uses symbol as stopping point, extracts number, adds to arrayList,
        //and cuts off remaining, then uses the formula to convert into degrees

        if (s.contains("\"") && s.contains("'")) { // case for 106°49'43.24" W
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    symbol = "°";
                } else if (i == 1) {
                    symbol = "'";
                } else {
                    symbol = "\"";
                }
                end = sCopy.indexOf(symbol);
                values.add(Double.parseDouble(sCopy.substring(0, end)));
                sCopy = sCopy.substring(end + 1);
            }
            retVal = (values.get(0) + (values.get(1) / 60) + (values.get(2) / 3600));
            if (sCopy.equals("W") || sCopy.equals("S")) {
                return (retVal * (-1));
            } else {
                return retVal;
            }
        } else if (s.contains("'")) { // case for 106°49.24' W format
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    symbol = "°";
                } else {
                    symbol = "'";
                }
                end = sCopy.indexOf(symbol);
                values.add(Double.parseDouble(sCopy.substring(0, end)));
                sCopy = sCopy.substring(end + 1);
            }
            retVal = (values.get(0) + (values.get(1) / 60));
            if (sCopy.equals("W") || sCopy.equals("S")) {
                return (retVal * (-1));
            } else {
                return retVal;
            }

        } else if (s.contains("°")) { // case for 106.24° format
            end = sCopy.indexOf("°");
            return (Double.parseDouble(sCopy.substring(0, end)));
        } else { // case for -106.24 format
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
            JSONObject startObj = new JSONObject();
            JSONObject endObj = new JSONObject();

            Set<String> startKeys = d.getStartID().getInfoMap().keySet();
            //startObj.put("start", d.getStartID().getName());
            startObj.put("latitude", d.getStartID().getLatitude());
            startObj.put("longitude", d.getStartID().getLongitude());
            for(String s : startKeys){
                startObj.put(s, d.getStartID().getInfoMap().get(s));
            }

            Set<String> endKeys = d.getEndID().getInfoMap().keySet();
            //endObj.put("end", d.getEndID().getName());
            endObj.put("latitude", d.getStartID().getLatitude());
            endObj.put("longitude", d.getStartID().getLongitude());
            for(String s : endKeys){
                endObj.put(s, d.getEndID().getInfoMap().get(s));
            }

            obj.put("start", d.getStartID().getName());
            obj.put("end", d.getEndID().getName());
            obj.put("distance", d.getGcd());
            obj.put("startInfo", startObj);
            obj.put("endInfo", endObj);


            array.add(obj);

        }

        try (PrintWriter write = new PrintWriter(new File("Itinerary.json"))) {
            write.print(array.toJSONString());
            write.close();
        } catch (IOException e) {
            System.out.print("Error: Cannot write to file" + e);
        }
    }

    //helper method to compute the shortest itinerary starting from the given Location
    // param: Location l - starting Location for the itinerary
    //return value - an ArrayList<Distance> that stores the shortest trip starting from l
    public ArrayList<Distance> shortestTripFrom(Location l) {
        //array list to store the shortest itinerary
        ArrayList<Distance> shortestIt = new ArrayList<Distance>();

        //temp array list of the locations - allows us to remove as we visit
        ArrayList<Location> tempLoc = finalLocations;

        // stores the current city you are on in the trip
        Location currentLocation = l;

        while (!tempLoc.isEmpty()) {
            //remove the current location from tempLoc so it's not comparing to itself
            tempLoc.remove(currentLocation);

            //compare the current location to every other location and find the shortest distance between them
            int shortestD = 2000000000;
            Location closest = null;
            for (int i = 0; i < tempLoc.size(); i++) {
                int dist = greatCirDist(currentLocation.getLatitude(), (currentLocation.getLongitude()), tempLoc.get(i).getLatitude(), tempLoc.get(i).getLongitude());

                //if you find a shorter Distance:
                if (dist < shortestD) {
                    shortestD = dist;
                    closest = tempLoc.get(i);
                }
            }

            //add a distance object for the shortest distance from current to closest
            shortestIt.add(new Distance(currentLocation, closest, shortestD));

            //adjust the current Location to be to the closest city
            currentLocation = closest;
        }

        return shortestIt;
    }

}
