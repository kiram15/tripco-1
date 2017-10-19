package edu.csu2017fa314.T02.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.lang.Math;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.PrintWriter;
import java.util.Collections;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;


public class Hub {
    String[] infoArray;
    Map<String, Integer> columns = new LinkedHashMap<String, Integer>();
    Map<Integer, String> reverseC = new LinkedHashMap<Integer, String>();
    ArrayList<Location> finalLocations = new ArrayList<Location>();
    ArrayList<Distance> shortestItinerary = new ArrayList<Distance>();

    public void readFile(String fileName) {
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
                for (int i = 0; i < props.length; ++i) {
                    if (i == columns.get("name")) {
                        objectName = props[i].trim();
                    } else if (i == columns.get("latitude")) {
                        objectLatitude = props[i].trim();
                    } else if (i == columns.get("longitude")) {
                        objectLongitude = props[i].trim();
                    } else {
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

    public ArrayList<Distance> shortestTrip() {


        //Adjacency matrix that holds all gcds
        Object[][] gcds = calcAllGcds();

        //keep track of the city that the shortest trip starts from
        Location shortestTripStart = finalLocations.get(0);
        //keep track of the shortest distance
        int shortestTripDistance = 999999999;
        int row = 0;

        //Create a huge distance to use for inital comparison
        LinkedHashMap<String, String> info = new LinkedHashMap<String,String>();
        Location bigD1 = new Location("New Zealand", -41.28650, 174.77620, info);
        Location bigD2 = new Location("Madrid", 40.41680, -3.70380, info);
        Distance hugeDistance = new Distance(bigD1, bigD2);

        //temp array list to keep track of the cities we have been to
        ArrayList<Location> traveledTo = new ArrayList<Location>();

        //for each location in the finalLocations array list: picking a starting city
        for (Location l : finalLocations) {
            //set the first city in the finalLocations array list to our current location
            Location currentLocation = l;
            int tripDistance = 0;

            //while there are still more cities to travel to
            while (traveledTo.size() < finalLocations.size()) {
                for (int i = 0; i < finalLocations.size(); i++) {
                    if (finalLocations.get(i).equals(currentLocation)) { row = i; }
                }
                traveledTo.add(currentLocation);
                if (traveledTo.size() == finalLocations.size()) { break; }
                Distance shortestDistance = hugeDistance;
                for(int i = 1; i < gcds[0].length; i++) { //because we aren't including initial location
                    Distance d = (Distance) gcds[row][i];
                    if (!traveledTo.contains(d.getEndID()) && (d.getGcd() < shortestDistance.getGcd())) {
                        shortestDistance = d;
                    }
                }
                //tripDistance += shortestDistance.getGcd();
                currentLocation = shortestDistance.getEndID();
            }
            //making traveledTo empty again
            traveledTo = new ArrayList<Location>();

            //add the distance back to the original city
            Object[] backAround = gcds[row];
            //grab the distance from the current city to original city
            Distance temp = new Distance(currentLocation, l);
            for (int i = 1; i < backAround.length; i++) {
                Distance d = (Distance) backAround[i];
                //add to tripDistance
                if (temp.equals(d)) { tripDistance += d.getGcd(); }
            }

            //apply 2opt
            checkImprovement(traveledTo);

            //get the updated trip distance after 2opt
            ArrayList<Distance> traveledDistances = locationsToDistances(traveledTo);
            for (int i = 0; i < traveledDistances.size(); i++) {
                tripDistance += traveledDistances.get(i).getGcd();
            }

            //compare the final distance to the stored shortest distance
            if (tripDistance < shortestTripDistance) {
                //if the trip was shorter then store distance and starting city
                shortestTripDistance = tripDistance;
                shortestTripStart = l;
            }
        }

        //recalulate the shortest trip for than one particular city:
//        //store in an ArrayList<Distance> to be returned
//        ArrayList<Distance> shortestIt = new ArrayList<Distance>();
        //start final trip at the predetermined shortest trip start
        Location currentLocation = shortestTripStart;

        ArrayList<Location> traveledToFinal = new ArrayList<Location>();
        //while there are still more cities to travel to
        while (traveledToFinal.size() < finalLocations.size()) {
            for (int i = 0; i < finalLocations.size(); i++) {
                if (finalLocations.get(i).equals(currentLocation)) {
                    row = i;
                }
            }
            traveledToFinal.add(currentLocation);
            if (traveledToFinal.size() == finalLocations.size()) {
                break;
            }
            Distance shortestDistance = hugeDistance;
            for (int i = 1; i < gcds[0].length; i++) { //because we aren't including first Location
                Distance d = (Distance) gcds[row][i];
                if (!traveledToFinal.contains(d.getEndID()) && (d.getGcd() < shortestDistance.getGcd())) {
                    shortestDistance = d;
                }
            }
            //shortestIt.add(shortestDistance);
            currentLocation = shortestDistance.getEndID();
        }

        //add the distance back to the original city
        Object[] backAround = gcds[row];
        //grab the distance from the current city to original city
        Distance temp = new Distance(currentLocation, shortestTripStart);
        for (int i = 1; i < backAround.length; i++) {
            Distance d = (Distance) backAround[i];
            //add to tripDistance
            //if (temp.equals(d)) { traveledToFinal.add(d); }
        }


//        Object[] backAround = gcds[row];
//        //grab the distance from the current city to original city
//        Distance temp = new Distance(currentLocation, shortestTripStart);
//        shortestIt.add(temp);
//        shortestItinerary = shortestIt;

        //apply 2opt
        checkImprovement(traveledToFinal);

        //convert traveledToFinal location array to a distance array
        ArrayList<Distance> updatedShortestIt = locationsToDistances(traveledToFinal);


        shortestItinerary = updatedShortestIt;
        return updatedShortestIt;
    }

    //will return an array list with each city listed once, with the shortest city as its end
    public Object[][] calcAllGcds() {
        Object[][] GCDS = new Object[finalLocations.size()][finalLocations.size()+1];
        for (int i = 0; i < finalLocations.size(); i++) {
            GCDS[i][0] = finalLocations.get(i);
            for (int j = 0; j < finalLocations.size(); j++) {
                Location startID = finalLocations.get(i);
                Location endID = finalLocations.get(j);
                Distance d = new Distance(startID, endID);
                GCDS[i][j+1] = d;
            }

        }
        return GCDS;
    }

    public void checkImprovement(ArrayList<Location> traveled) {
        boolean improvement = true;
        while (improvement) {
            improvement = false;
            for (int i = 0; i <= traveled.size() - 3; i++) { // check n>4
                for (int k = i + 2; k < traveled.size() - 1; k++) {
                    Distance ii1 = new Distance(traveled.get(i), traveled.get(i + 1));
                    Distance kk1 = new Distance(traveled.get(k), traveled.get(k + 1));
                    Distance ik = new Distance(traveled.get(i), traveled.get(k));
                    Distance i1k1 = new Distance(traveled.get(i + 1), traveled.get(k + 1));
                    double delta = (-ii1.getGcd()) - kk1.getGcd() + ik.getGcd() + i1k1.getGcd();
                    if (delta < 0) { //improvement?
                        optSwap2(traveled, i + 1, k);
                        improvement = true;
                    }
                }
            }
        }
    }

    public ArrayList<Distance> locationsToDistances(ArrayList<Location> locations) {
        ArrayList<Distance> finalDistances = new ArrayList<Distance>();
        for (int i = 0; i < locations.size(); i++) {
            if (i == locations.size() - 1) { //distance of last back to first
                Distance base = new Distance(locations.get(i), locations.get(0));
                finalDistances.add(base);
            } else {
                Distance d = new Distance(locations.get(i), locations.get(i + 1));
                finalDistances.add(d);
                }
        }
        return finalDistances;
    }

    public void optSwap2(ArrayList<Location> traveledTo, int i1, int k) { // swap in place
        while (i1 < k) {
            Location temp = traveledTo.get(i1);
            traveledTo.set(i1, traveledTo.get(k));
            traveledTo.set(k, temp);
            i1++;
            k--;
        }
    }

    public void drawSVG(String COmap) throws FileNotFoundException{
        //create printWriter to CoMapTripCo svg
        try {
            PrintWriter pw = new PrintWriter(new File("COmapTripCo.svg"));
            //copy COmap svg into CoMapTripCo svg (dont read last two line [</g> </svg>])
            LinkedList<String> ll = new LinkedList<String>();
            try{
                Scanner br = new Scanner(new File(COmap));
                String line;
                while(br.hasNext()) {
                    line = br.nextLine();
                    ll.addLast(line);
                }
                for(int i = 0; i < (ll.size()-3); i++){
                    pw.println(ll.get(i));
                    pw.flush();
                }
                br.close();
            }catch(IOException e){
                System.out.println("ERROR: FAILED");
                System.exit(0);
            }

            pw.println("</g>");

            //draw lines from start to end locations
            double originStartLat = 0.0;
            double originStartLon = 0.0;
            double finalEndLat = 0.0;
            double finalEndLon = 0.0;
            boolean first = false;
            double unitHeight = 176.7706; //COmap height-(38*2)/4
            double unitWidth = 141.515329; //COmap width-(38*2)/7

            for(Distance d : shortestItinerary){
                if(!first){
                    originStartLat = d.getStartID().getLatitude();
                    originStartLon = d.getStartID().getLongitude();
                    first = true;
                }
                double startLat = d.getStartID().getLatitude();
                double startLon = d.getStartID().getLongitude();
                double endLat = d.getEndID().getLatitude();
                double endLon = d.getEndID().getLongitude();

                //absVal of lat/lons
                startLat = Math.abs(startLat);
                startLon = Math.abs(startLon);
                endLat = Math.abs(endLat);
                endLon = Math.abs(endLon);

                finalEndLat = d.getEndID().getLatitude();
                finalEndLon = d.getEndID().getLongitude();

                double x1 = ((109 - startLon) * unitWidth) + 38;
                double y1 = ((41 - startLat) * unitHeight) + 38;
                double x2 = ((109 - endLon) * unitWidth) + 38;
                double y2 = ((41 - endLat) * unitHeight) + 38;
                pw.println("  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" id=\"svg_1\"/>");
                pw.flush();
            }

            //absVal of lat/lons
            finalEndLat = Math.abs(finalEndLat);
            finalEndLon = Math.abs(finalEndLon);
            originStartLat = Math.abs(originStartLat);
            originStartLon = Math.abs(originStartLon);

            //draw last line connected end point with start
            double endX1 = ((109 - finalEndLon) * unitWidth) + 38;
            double endY1 = ((41 - finalEndLat) * unitHeight) + 38;
            double endX2 = ((109 - originStartLon) * unitWidth) + 38;
            double endY2 = ((41 - originStartLat) * unitHeight) + 38;
            pw.flush();

            pw.println("  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + endX1 + "\" y1=\"" + endY1 + "\" x2=\"" + endX2 + "\" y2=\"" + endY2 + "\" id=\"svg_1\"/>");
            
            pw.println("</svg>");

            pw.close();

        }catch(FileNotFoundException e){
            System.out.println("Error: File not found");
            System.exit(0);
        }

    }

}
