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
        //Reference array list that holds all the gcds from all the cities
        //helper method to calculate the gcd's for all of the cities
        HashMap<Location,ArrayList<Distance>> gcds = calcAllGcds();

        //keep track of the city that the shortest trip starts from
        Location shortestTripStart = finalLocations.get(0);
        //keep track of the shortest distance
        int shortestTripDistance = 999999999;

        //for each location in the finalLocations array list: picking a starting city
        for (Location l : finalLocations) {
            //set the first city in the finalLocations array list to our current location
            Location currentLocation = l;
            //temp array list to keep track of the cities we have been to
            ArrayList<Location> traveledTo = new ArrayList<Location>();
            traveledTo.add(currentLocation);
            int tripDistance = 0;

            //while there are still more cities to travel to
            while (traveledTo.size() < finalLocations.size()) {
                //grab the Distance object from the ArrayList
                ArrayList<Distance> distances = gcds.get(currentLocation);
                for(int i = 0; i < distances.size(); i++){
                    Distance d = distances.get(i);
                    if(!traveledTo.contains(d.getEndID())){
                        // add the gcd to the overall trip distance
                        tripDistance += d.getGcd();
                        //reassign the currentLocation to the end city
                        currentLocation = d.getEndID();
                        traveledTo.add(currentLocation);
                        break;
                    }
                }
            }

            //add the distance back to the original city
            //look up current location in the hashmap
            ArrayList<Distance> backAround = gcds.get(currentLocation);

            //grab the distance from the current city to l
            Distance temp = new Distance(currentLocation, l);
            tripDistance += backAround.get(backAround.indexOf(temp)).getGcd();

            //compare the final distance to the stored shortest distance
            if (tripDistance < shortestTripDistance) {
                //if the trip was shorter then store distance and starting city
                shortestTripDistance = tripDistance;
                shortestTripStart = l;
            }
        }

        //recalulate the shortest trip for than one particular city:
        //store in an ArrayList<Distance> to be returned
        ArrayList<Distance> shortestIt = new ArrayList<Distance>();
        //start final trip at the predetermined shortest trip start
        Location currentLocation = shortestTripStart;

        ArrayList<Location> traveledToFinal = new ArrayList<Location>();
        traveledToFinal.add(currentLocation);
        //while there are still more cities to travel to
        while (traveledToFinal.size() < finalLocations.size()) {
            //grab the Distance object from the ArrayList
            ArrayList<Distance> distances = gcds.get(currentLocation);
            for(int i = 0; i < distances.size(); i++){
                Distance d = distances.get(i);
                if(!traveledToFinal.contains(d.getEndID())){
                    shortestIt.add(d);
                    //reassign the currentLocation to the end city
                    currentLocation = d.getEndID();
                    traveledToFinal.add(currentLocation);
                    break;
                }
            }
        }

        //add the distance back to the original city
        //look up current location in the hashmap

        //grab the distance from the current city to l
        Distance temp = new Distance(currentLocation, shortestTripStart);
        shortestIt.add(temp);
        shortestItinerary = shortestIt;

        return shortestIt;
    }

    //will return an array list with each city listed once, with the shortest city as its end
    private HashMap<Location,ArrayList<Distance>> calcAllGcds() {
        HashMap<Location,ArrayList<Distance>> gcds = new HashMap<Location,ArrayList<Distance>>();

        //for every location, calculate the GCD to every other location
        for (int i = 0; i < finalLocations.size(); i++) {
            //allocate array list for given location in hashmap
            gcds.put(finalLocations.get(i), new ArrayList<Distance>());

            for (int j = 0; j < finalLocations.size(); j++) {
                //do a check to make sure they are not the same index (aka the same location)
                if (i == j) continue;

                Location startID = finalLocations.get(i);
                Location endID = finalLocations.get(j);
                Distance d = new Distance(startID, endID);
                gcds.get(finalLocations.get(i)).add(d);

            }
            Collections.sort(gcds.get(finalLocations.get(i)));
        }

        return gcds;
    }

    public void drawSVG(String COmap) throws FileNotFoundException{
        //create printWriter to CoMapTripCo svg

        try {
            PrintWriter pw = new PrintWriter(new File("COmapTripCo.svg"));
            //copy COmap svg into CoMapTripCo svg (dont read last two line [</g> </svg>])
//            LinkedList<String> ll = new LinkedList<String> ();
//            try{
//                Scanner br = new Scanner(new File(COmap));
//                String line;
//                while(br.hasNext()) {
//                    line = br.nextLine();
//                    ll.addLast(line);
//                }
//                for(int i = 0; i < (ll.size()-3); i++) {
//                    pw.println(ll.removeFirst());
//                }
//                br.close();
//            }catch(IOException e){
//                System.out.println("ERROR: FAILED");
//                System.exit(0);
//            }

            pw.println("<svg width=\"1066.6073\" height=\"783.0824\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">");
            pw.println(" <title>Layer 1</title>");
            pw.println("<g>");

            //draw borders
            pw.println("  <rect fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x=\"10\" y=\"10\" width=\"799.955475\" height=\"587.3118\" id=\"svg_2\"/>");

            //draw lines from start to end locations
            double originStartLat = 0.0;
            double originStartLon = 0.0;
            double finalEndLat = 0.0;
            double finalEndLon = 0.0;
            boolean first = false;
            double unitHeight = 146.82795; //COmap height/4
            double unitWidth = 114.279354; //COmap width/7

            for(Distance d : shortestItinerary){
                if(!first){
                    originStartLat = d.getStartID().getLatitude();
                    originStartLon = d.getStartID().getLongitude();
                    first = true;
                }
                double startLat = d.getStartID().getLatitude();
                System.out.println("startLat: " + startLat);
                double startLon = d.getStartID().getLongitude();
                System.out.println("startLon: " + startLon);
                double endLat = d.getEndID().getLatitude();
                System.out.println("endLat: " + endLat);
                double endLon = d.getEndID().getLongitude();
                System.out.println("endLon: " + endLon);

                finalEndLat = d.getEndID().getLatitude();
                finalEndLon = d.getEndID().getLongitude();

                double x1 = ((109 - startLon) * unitWidth) + 10;
                System.out.println("JAVA x1: " + x1);
                double y1 = ((41 - startLat) * unitHeight) + 10;
                double x2 = ((109 - endLon) * unitWidth) + 10;
                double y2 = ((41 - endLat) * unitHeight) + 10;
                pw.println("  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" id=\"svg_1\"/>");
                pw.flush();
            }

            //draw last line connected end point with start
            double endX1 = ((109 - finalEndLon) * unitWidth) + 10;
            double endY1 = ((41 - finalEndLat) * unitHeight) + 10;
            double endX2 = ((109 - originStartLon) * unitWidth) + 10;
            double endY2 = ((41 - originStartLat) * unitHeight) + 10;
            pw.flush();

            pw.println("  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + endX1 + "\" y1=\"" + endY1 + "\" x2=\"" + endX2 + "\" y2=\"" + endY2 + "\" id=\"svg_1\"/>");

            pw.println(" </g>\n" + "</svg>");

            pw.close();

        }catch(FileNotFoundException e){
            System.out.println("Error: File not found");
            System.exit(0);
        }

    }

}
