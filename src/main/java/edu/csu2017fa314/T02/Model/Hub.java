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

            obj.put("start", d.getStartID().getName());
            obj.put("end", d.getEndID().getName());
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

    //returns an array list of Distances containing the itinerary for the shortest trip
    // this is creating the ArrayList that will be passed to writeJSON
    public ArrayList<Distance> shortestTrip(){
        //Reference array list that holds all the gcds from all the cities
        //write a helper method to calculate the gcd's for all of the cities
        ArrayList<Distance> gcds = calcAllGcds();

        //keep track of the city that the shortest trip starts from
        Location shortestTripStart = finalLocations.get(0);
        //keep track of the shortest distance
        int shortestTripDistance = 999999999;

        //for each location in the finalLocations array list: picking a starting city
        for (location l : finalLocations){
            //set the first city in the finalLocations array list to our current location
            Location currentLocation = finalLocations.get(l);
            //temp array list to keep track of the cities we have been to
            ArrayList<Location> temp = finalLocations;

            int tripDistance = 0;
            //while there are still more cities to travel to
            while(!temp.isEmpty()){
                //remove the currentLocation from the temp array list to signfiy that we have been there
                temp.remove(currentLocation);
                //TODO: grab the Distance object from the ArrayList !!!!!!!!!!!!!!!!
                Distance d =

                // add the gcd to the overall trip distance
                tripDistance += d.getGcd();
                //reassign the currentLocation to the end city
                currentLocation = d.getEndID();
            }

            //TODO: add the distance back to the original city


            //compare the final distance to the stored shortest distance
            if(tripDistance < shortestTripDistance){
                //if the trip was shorter then store distance and starting city
                shortestTripDistance = gcd;
                shortestTripStart = l;
            }
        }


        //recalulate the shortest trip for than one particular city
        //store in an ArrayList<Distance> to be returned
        ArrayList<Distance> shortestItinerary = new ArrayList<Distance>();

        ArrayList<Location> temp = finalLocations;
        Location finalL = shortestTripStart;

        //while there are still more cities to travel to
        while(!temp.isEmpty()){
            //remove the currentLocation from the temp array list to signfiy that we have been there
            temp.remove(finalL);

            //TODO: grab the Distance object from gcds !!!!!!!!!!!!!!!!!!
            Distance finalD = gcds.get()

            //add the distance object to the shortestItinerary array list
            shortestItinerary.add(finalD);

            //reassign the currentLocation to the end city
            finalL = finalD.getEndID();
        }

        //TODO: ADD FROM THE LAST CITY BACK TO THE START CITY


        return shortestItinerary;
    }

    //will return an array list with each city listed once, with the shortest city as its end
    private ArrayList<Distance> calcAllGcds(){
        ArrayList<Distance> gcds = new ArrayList<Distance>();

        //for every location, calculate the GCD to every other location
        for(int i = 0; i < finalLocations.size(); i++){
            for(int j = 0; j < finalLocations.size(); j++){
                //do a check to make sure they are not the same index (aka the same location)
                if(i == j) continue;

                Location startID = finalLocations.get(i);
                Location endID = finalLocations.get(j);
                int dist = greatCirDist((finalLocations.get(start)).getLatitude(), (finalLocations.get(start)).getLongitude(), (finalLocations.get(end)).getLatitude(), (finalLocations.get(end)).getLongitude());
                Distance d = new Distance(startID, endID, dist);


            }
        }

        return gcds;
    }

}
