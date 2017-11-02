package edu.csu2017fa314.T02.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.lang.Math;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.lang.ClassLoader;

public class Hub {
    String[] infoArray;
    Map<String, Integer> columns = new LinkedHashMap<String, Integer>();
    Map<Integer, String> reverseC = new LinkedHashMap<Integer, String>();
    ArrayList<Location> finalLocations = new ArrayList<Location>();
    public ArrayList<Distance> shortestItinerary = new ArrayList<Distance>();
    public boolean miles = true;
    public String optimization = "";


    public void searchDatabase(String username, String password, String searchingFor){
        finalLocations.clear();
        shortestItinerary.clear();
        columns.clear();
        reverseC.clear();
        searchingFor = searchingFor.toLowerCase();
        String myDriver = "com.mysql.jdbc.Driver"; // add dependencies in pom.xml
        String myUrl = "jdbc:mysql://faure.cs.colostate.edu/cs314";
        //String myUrl = "jdbc:mysql://localhost/cs314"; // Use this line if tunneling 3306 traffic through shell

        try { // connect to the database
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, username, password);
            try { // create a statement
                Statement st = conn.createStatement();
                try { // submit a query to get column headers
                    String q1 = "select column_name from information_schema.columns where table_name='airports';";
                    ResultSet rs1 = st.executeQuery(q1);
                    try { // iterate through the query results and give string of column headers to storeColumnHeaders
                        String headers = "";
                        while (rs1.next()){
                            String h = rs1.getString(1);
                            h = h + ",";
                            headers += h;
                        }
                        storeColumnHeaders(headers);

                        try{ //search for searchingFor string in all columns
                            st = conn.createStatement();

                            String q2 = "select * from airports where name like '%" + searchingFor + "%' or type like '%" + searchingFor + "%' or id like '%" + searchingFor + "%' or latitude like '%" + searchingFor + "%' or longitude like '%" + searchingFor + "%' or municipality like '%" + searchingFor + "%' or elevation like '%" + searchingFor + "%' or home_link like '%" + searchingFor + "%' or wikipedia_link like '%" + searchingFor + "%' order by name;";
                            ResultSet rs2 = st.executeQuery(q2);
                            try{ //parse matched rows
                                int count = 0;

                                while(rs2.next() && count <= 49){ //for each row
                                    String matchedRow = "";
                                    for(int i = 1; i <= columns.size(); i++) { //traverse row by incrementing columns and storing in a string
                                        String rowCol = rs2.getString(i);
                                        rowCol = rowCol + ",";
                                        matchedRow += rowCol;
                                    }

                                    parseRow(matchedRow);
                                    ++count;
                                }
                            } finally { rs2.close(); }
                        } finally{ st.close(); }

                    } finally { rs1.close(); }
                } finally {}

            } finally { conn.close(); }
        } catch (Exception e) { // catches all exceptions in the nested try's
            System.err.printf("Exception: ");
            System.err.println(e.getMessage());
        }
        //call rest of hub
        //switch statement that calls the specific shortest trip method based on selected optimization
        switch(optimization){
            case "None":
                shortestItinerary = locationsToDistances(finalLocations);
                break;
            case "NearestNeighbor":
                shortestTripNN();
                break;
            case "TwoOpt":
                shortestTrip2Opt();
                break;
            case "ThreeOpt":
                shortestTrip3Opt();
                break;
        }
    }

    public void storeColumnHeaders(String firstLine){
        String s = firstLine.toLowerCase();
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

    public void parseRow(String row){
        row = row.toLowerCase();
        String[] props = row.split(",");
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

    //master method for when user selects Nearest Neighbor optimization (calls all helpers)
    public void shortestTripNN(){

    }

    //master method for when user selects 2opt optimization (calls all helpers)
    public void shortestTrip2Opt() {
        //Adjacency matrix that holds all gcds
        Object[][] gcds = calcAllGcds();

        //keep track of the city that the shortest trip starts from
        Location shortestTripStart = finalLocations.get(0);
        //keep track of the shortest distance
        int shortestTripDistance = 999999999;
        //row is the current row in the adjancency matrix where the current location is
        int row = 0;

        //Create a huge distance to use for inital comparison
        LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
        Location bigD1 = new Location("New Zealand", -41.28650, 174.77620, info);
        Location bigD2 = new Location("Madrid", 40.41680, -3.70380, info);
        Distance hugeDistance = new Distance(bigD1, bigD2, miles);

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
                    if (finalLocations.get(i).equals(currentLocation)) {
                        row = i;
                    }
                }
                traveledTo.add(currentLocation);
                if (traveledTo.size() == finalLocations.size()) {
                    break;
                }
                Distance shortestDistance = hugeDistance;
                for (int i = 1; i < gcds[0].length; i++) { //because we aren't including initial location
                    Distance d = (Distance) gcds[row][i];
                    if (!traveledTo.contains(d.getEndID()) && (d.getGcd() < shortestDistance.getGcd())) {
                        shortestDistance = d;
                    }
                }
                currentLocation = shortestDistance.getEndID();
            }

            //add the distance back to the original city
            Object[] backAround = gcds[row];
            //grab the distance from the current city to original city
            Distance temp = new Distance(currentLocation, l, miles);
            for (int i = 1; i < backAround.length; i++) {
                Distance d = (Distance) backAround[i];
                //add to tripDistance
                if (temp.equals(d)) {
                    tripDistance += d.getGcd();
                }
            }

            //apply 2opt
            checkImprovement(traveledTo);

            //get the updated trip distance after 2opt
            ArrayList<Distance> traveledDistances = locationsToDistances(traveledTo);
            for (int i = 0; i < traveledDistances.size(); i++) {
                tripDistance += traveledDistances.get(i).getGcd();
            }

            //making traveledTo empty again
            traveledTo = new ArrayList<Location>();

            //compare the final distance to the stored shortest distance
            if (tripDistance < shortestTripDistance) {
                //if the trip was shorter then store distance and starting city
                shortestTripDistance = tripDistance;
                shortestTripStart = l;
            }
        }

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
            currentLocation = shortestDistance.getEndID();
        }

        //apply 2opt
        checkImprovement(traveledToFinal);
        //convert traveledToFinal location array to a distance array
        shortestItinerary = locationsToDistances(traveledToFinal);
     }

    //master method for when user selects 2opt optimization (calls all helpers)
    public void shortestTrip3Opt(){

    }

    //will return an array list with each city listed once, with the shortest city as its end
    private Object[][] calcAllGcds() {
        Object[][] GCDS = new Object[finalLocations.size()][finalLocations.size()+1];
        for (int i = 0; i < finalLocations.size(); i++) {
            //get the initial Location
            GCDS[i][0] = finalLocations.get(i);
            for (int j = 0; j < finalLocations.size(); j++) {
                //for all the Distances in the row
                Location startID = finalLocations.get(i);
                Location endID = finalLocations.get(j);
                Distance d = new Distance(startID, endID, miles);
                GCDS[i][j+1] = d; //j+1 because of the Location in the first column
            }
        }
        return GCDS;
    }

    //determines all the possible areas that 2opt could improve in a given arraylist of locations
    private void checkImprovement(ArrayList<Location> traveled) {
        boolean improvement = true;
        //while there is still possible improvements to be made
        while (improvement) {
            improvement = false;
            for (int i = 0; i <= traveled.size() - 3; i++) { // check n>4
                for (int k = i + 2; k < traveled.size() - 1; k++) {
                    Distance ii1 = new Distance(traveled.get(i), traveled.get(i + 1), miles);
                    Distance kk1 = new Distance(traveled.get(k), traveled.get(k + 1), miles);
                    Distance ik = new Distance(traveled.get(i), traveled.get(k), miles);
                    Distance i1k1 = new Distance(traveled.get(i + 1), traveled.get(k + 1), miles);
                    double delta = (-ii1.getGcd()) - kk1.getGcd() + ik.getGcd() + i1k1.getGcd();
                    if (delta < 0) { //improvement?
                        optSwap2(traveled, i + 1, k);
                        improvement = true;
                    }
                }
            }
        }
    }

    //preforms the swap method for 2opt
    private void optSwap2(ArrayList<Location> traveledTo, int i1, int k) { // swap in place
        while (i1 < k) {
            //swap i+1 and k
            Location temp = traveledTo.get(i1);
            traveledTo.set(i1, traveledTo.get(k));
            traveledTo.set(k, temp);
            i1++;
            k--;
        }
    }

    //transforms an arrayList of location objects into an arrayList of distance objects using the
    //location objects in the order they are passed in
    private ArrayList<Distance> locationsToDistances(ArrayList<Location> locations) {
        ArrayList<Distance> finalDistances = new ArrayList<Distance>();
        for (int i = 0; i < locations.size(); i++) {
            if (i == locations.size() - 1) { //distance of last back to first
                Distance base = new Distance(locations.get(i), locations.get(0), miles);
                finalDistances.add(base);
            } else {
                Distance d = new Distance(locations.get(i), locations.get(i + 1), miles);
                finalDistances.add(d);
            }
        }
        return finalDistances;
    }

    public String drawSVG() throws FileNotFoundException {
        String SVG = "";

        //ClassLoader classLoader = this.getClass().getClassLoader();
        String filepath = "src/main/resources/WorldMap.svg";
        File WorldMapFile = new File(filepath);

        //copy COmap svg into CoMapTripCo svg (dont read last two line [</g> </svg>])
        LinkedList<String> ll = new LinkedList<String>();
        try {
            Scanner br = new Scanner(WorldMapFile);
            String line;
            while (br.hasNext()) {
                line = br.nextLine();

                ll.addLast(line);
            }
            for (int i = 0; i < (ll.size() - 3); i++) {
                SVG += ll.get(i);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("ERROR: FAILED TO WRITE SVG. Caught after trying to create a scanner");
            System.exit(0);
        }
        SVG += "</g>";

        //draw lines from start to end locations
        double originStartLat = 0.0;
        double originStartLon = 0.0;
        double finalEndLat = 0.0;
        double finalEndLon = 0.0;
        boolean first = false;
        double unitHeight = 3.6; //512/180 WorldMap Height - unit
        double unitWidth = 2.08; //1024/360 WorldMap width - unit

        if (!shortestItinerary.isEmpty()) {
            for (Distance d : shortestItinerary) {
                if (!first) {
                    originStartLat = d.getStartID().getLatitude();
                    originStartLon = d.getStartID().getLongitude();
                    first = true;
                }
                double startLat = d.getStartID().getLatitude();
                double startLon = d.getStartID().getLongitude();
                double endLat = d.getEndID().getLatitude();
                double endLon = d.getEndID().getLongitude();

                if (startLat < 0) { //lat is negative
                    startLat = Math.abs(startLat);
                    startLat *= 2;
                }
                if (endLat < 0) { //lat is negative
                    endLat = Math.abs(endLat);
                    endLat *= 2;
                }
                if (startLon > 0) { //lon is positive - double
                    startLon *= 2;
                }
                if (startLon < 0) { //lon is neg - abs
                    startLon = Math.abs(startLon);
                }
                if (endLon > 0) { //lon is positive - double
                    endLon *= 2;
                }
                if (endLon < 0) { //lon is neg - abs
                    endLon = Math.abs(endLon);
                }


                finalEndLat = d.getEndID().getLatitude();
                finalEndLon = d.getEndID().getLongitude();

                double x1 = (startLon * unitWidth);
                double y1 = (startLat * unitHeight);
                double x2 = (endLon * unitWidth);
                double y2 = (endLat * unitHeight);
                SVG += "  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" id=\"svg_1\"/>";
            }

        if (finalEndLat < 0) { //lat is negative
            finalEndLat = Math.abs(finalEndLat);
            finalEndLat *= 2;
        }
        if (originStartLat < 0) { //lat is negative
            originStartLat = Math.abs(originStartLat);
            originStartLat *= 2;
        }
        if (finalEndLon > 0) { //lon is positive - double
            finalEndLon *= 2;
        }
        if (finalEndLon < 0) { //lon is neg - abs
            finalEndLon = Math.abs(finalEndLon);
        }
        if (originStartLon > 0) { //lon is positive - double
            originStartLon *= 2;
        }
        if (originStartLon < 0) { //lon is neg - abs
            originStartLon = Math.abs(originStartLon);
        }

        //draw last line connected end point with start
        double endX1 = ((finalEndLon) * unitWidth);
        double endY1 = ((finalEndLat) * unitHeight);
        double endX2 = ((originStartLon) * unitWidth);
        double endY2 = ((originStartLat) * unitHeight);
        SVG += "  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + endX1 + "\" y1=\"" + endY1 + "\" x2=\"" + endX2 + "\" y2=\"" + endY2 + "\" id=\"svg_1\"/>";

        SVG += "</svg>";
    }

        return SVG;

    }

}
