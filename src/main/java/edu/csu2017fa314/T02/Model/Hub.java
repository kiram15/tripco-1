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
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
//import java.io.IOUtils;

public class Hub {
    //String[] infoArray;
    Map<String, Integer> columns = new LinkedHashMap<String, Integer>();
    Map<Integer, String> reverseC = new LinkedHashMap<Integer, String>();
    ArrayList<Location> finalLocations = new ArrayList<Location>();
    ArrayList<Distance> shortestItinerary = new ArrayList<Distance>();
    boolean miles = true;
    String optimization = "";

    //three necessary getters
    public ArrayList<Distance> getShortestItinerary(){
        return this.shortestItinerary;
    }

    public boolean getMiles(){
        return this.miles;
    }

    public String getOptimization(){
        return this.optimization;
    }

    //three necessary setters
    public void setShortestItinerary(ArrayList<Distance> shortest){
        this.shortestItinerary = shortest;
    }

    public void setMiles(boolean mile){
        this.miles = mile;
    }

    public void setOptimization(String opt){
        this.optimization = opt;
    }

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
                try{
                    //give order of column header to storeColumnHeaders
                    String colHeaders = "airports_ID, airports_Code, airports_Type, airports_Name, airports_Latitude, airports_Longitude, airports_Elevation, airports_Continent, airports_Iso_country, airports_Iso_region, airports_Municipality, airports_Scheduled_service, airports_Gps_code, airports_Iata_code, airports_Local_code, airports_Home_link, airports_Wikipedia_link, airports_Keywords, "
                            + "regions_ID, regions_Code, regions_Local_code, regions_Name, regions_Continent, regions_Iso_country, regions_Wikipedia_link, regions_Keywords, "
                            + "countries_ID, countries_Code, countries_Name, countries_Continent, countries_Wikipedia_link, countries_Keywords, "
                            + "continents_ID, continents_Name, continents_Code, continents_Wikipedia_link";
                    storeColumnHeaders(colHeaders);

                    String allTblsSearchQ = "select airports.id as airports_ID, airports.code as airports_Code, airports.type as airports_Type, airports.name as airports_Name, airports.latitude as airports_Latitude, airports.longitude as airports_Longitude, airports.elevation as airports_Elevation, airports.continent as airports_Continent, airports.iso_country as airports_Iso_country, airports.iso_region as airports_Iso_region, airports.municipality as airports_Municipality, airports.scheduled_service as airports_Scheduled_service, airports.gps_code as airports_Gps_code, airports.iata_code as airports_Iata_code, airports.local_code as airports_Local_code, airports.home_link as airports_Home_link, airports.wikipedia_link as airports_Wikipedia_link, airports.keywords as airports_Keywords, "
                            + "regions.id as regions_ID, regions.code as regions_Code, regions.local_code as regions_Local_code, regions.name as regions_Name, regions.continent as regions_Continent, regions.iso_country as regions_Iso_country, regions.wikipedia_link as regions_Wikipedia_link, regions.keywords as regions_Keywords, "
                            + "countries.id as countries_ID, countries.code as countries_Code, countries.name as countries_Name, countries.continent as countries_Continent, countries.wikipedia_link as countries_Wikipedia_link, countries.keywords as countries_Keywords, "
                            + "continents.id as continents_ID, continents.name as continents_Name, continents.code as continents_Code, continents.wikipedia_link as continents_Wikipedia_link "
                            + "from continents "
                            + "inner join countries on countries.continent = continents.code "
                            + "inner join regions on regions.iso_country = countries.code "
                            + "inner join airports on airports.iso_region = regions.code "
                            + "where "
                            + "airports.id like '%" + searchingFor + "%' or airports.code like '%" + searchingFor + "%' or airports.type like '%" + searchingFor + "%' or airports.name like '%" + searchingFor + "%' or airports.latitude like '%" + searchingFor + "%' or airports.longitude like '%" + searchingFor + "%' or airports.elevation like '%" + searchingFor + "%' or airports.continent like '%" + searchingFor + "%' or airports.iso_country like '%" + searchingFor + "%' or airports.iso_region like '%" + searchingFor + "%' or airports.municipality like '%" + searchingFor + "%' or airports.scheduled_service like '%" + searchingFor + "%' or airports.gps_code like '%" + searchingFor + "%' or airports.iata_code like '%" + searchingFor + "%' or airports.local_code like '%" + searchingFor + "%' or airports.home_link like '%" + searchingFor + "%' or airports.wikipedia_link like '%" + searchingFor + "%' or airports.keywords like '%" + searchingFor + "%' or "
                            + "regions.id like '%" + searchingFor + "%' or regions.code like '%" + searchingFor + "%' or regions.local_code like '%" + searchingFor + "%' or regions.name like '%" + searchingFor + "%' or regions.continent like '%" + searchingFor + "%' or regions.iso_country like '%" + searchingFor + "%' or regions.wikipedia_link like '%" + searchingFor + "%' or regions.keywords like '%" + searchingFor + "%' or "
                            + "countries.id like '%" + searchingFor + "%' or countries.code like '%" + searchingFor + "%' or countries.name like '%" + searchingFor + "%' or countries.continent like '%" + searchingFor + "%' or countries.wikipedia_link like '%" + searchingFor + "%' or countries.keywords like '%" + searchingFor + "%' or "
                            + "continents.id like '%" + searchingFor + "%' or continents.name like '%" + searchingFor + "%' or continents.code like '%" + searchingFor + "%' or continents.wikipedia_link like '%" + searchingFor + "%' "
                            + "limit 100;";

                    ResultSet allTblsSearchRS = st.executeQuery(allTblsSearchQ);
                    try{ //parse matched rows
                        while(allTblsSearchRS.next()){ //for each row
                            String matchedRow = "";
                            for(int i = 1; i <= columns.size(); i++) { //traverse row by incrementing columns and storing in a string
                                String rowCol = allTblsSearchRS.getString(i);
                                rowCol = rowCol + ",";
                                matchedRow += rowCol;
                            }
                            parseRow(matchedRow);
                        }
                    } finally { allTblsSearchRS.close(); }
                } finally{ st.close(); }
            } finally { conn.close(); }
        } catch (Exception e) { // catches all exceptions in the nested try's
            System.err.printf("Exception: ");
            System.err.println(e.getMessage());
        }

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
            default:
                shortestItinerary = locationsToDistances(finalLocations);
                break;
        }

    }

    public void storeColumnHeaders(String firstLine){
        String s = firstLine.toLowerCase();
        String[] infoArray = s.split(",");
        for (int i = 0; i < infoArray.length; i++) {
            String infoString = infoArray[i];
            switch (infoString.trim()) { // associating column titles with column num, putting it in map
                case "airports_name":
                    columns.put("airports_name", i);
                    reverseC.put(i, "airports_name");
                    break;
                case "airports_latitude":
                    columns.put("airports_latitude", i);
                    reverseC.put(i, "airports_latitude");
                    break;
                case "airports_longitude":
                    columns.put("airports_longitude", i);
                    reverseC.put(i, "airports_longitude");
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
            if (i == columns.get("airports_name")) {
                objectName = props[i].trim();
            } else if (i == columns.get("airports_latitude")) {
                objectLatitude = props[i].trim();
            } else if (i == columns.get("airports_longitude")) {
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
                tripDistance += shortestDistance.getGcd();
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
        //convert traveledToFinal location array to a distance array
        shortestItinerary = locationsToDistances(traveledToFinal);
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

            //add the distance back to the original cit
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
            checkImprovement2(traveledTo);

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
        checkImprovement2(traveledToFinal);
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
    private void checkImprovement2(ArrayList<Location> traveled) {
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
                        optSwap(traveled, i + 1, k);
                        improvement = true;
                    }
                }
            }
        }
    }

    //determines all the possible areas that 2opt could improve in a given arraylist of locations
    private void checkImprovement3(ArrayList<Location> traveled) {
        boolean improvement = true;
        //while there is still possible improvements to be made
        while (improvement) {
            improvement = false;
            for (int i = 0; i <= traveled.size() - 5; i++) {
                //starts at i+2 because i, i+1, start at j
                for (int j = i + 2; j < traveled.size() - 3; j++)
                    //starts at i+4 because i, i+1, j, j+1, start at k
                    for (int k = i + 4; k < traveled.size() - 1; k++) {
                        Distance ii1 = new Distance(traveled.get(i), traveled.get(i+1), miles);
                        Distance ij = new Distance(traveled.get(i), traveled.get(j), miles);
                        Distance ij1 = new Distance(traveled.get(i), traveled.get(j+1), miles);
                        Distance ik = new Distance(traveled.get(i), traveled.get(k), miles);
                        Distance ik1 = new Distance(traveled.get(i), traveled.get(k+1), miles);

                        Distance i1j = new Distance(traveled.get(i+1), traveled.get(j), miles);
                        Distance i1j1 = new Distance(traveled.get(i+1), traveled.get(j+1), miles);
                        Distance i1k = new Distance(traveled.get(i+1), traveled.get(k), miles);
                        Distance i1k1 = new Distance(traveled.get(i+1), traveled.get(k+1), miles);

                        Distance jj1 = new Distance(traveled.get(j), traveled.get(j+1), miles);
                        Distance jk = new Distance(traveled.get(j), traveled.get(k), miles);
                        Distance jk1 = new Distance(traveled.get(j), traveled.get(k+1), miles);

                        Distance j1k = new Distance(traveled.get(j+1), traveled.get(k), miles);
                        Distance j1k1 = new Distance(traveled.get(j+1), traveled.get(k+1), miles);

                        Distance kk1 = new Distance(traveled.get(k), traveled.get(k+1), miles);


                        //won't the arraylist be changing after everyone of these swaps?
                        //create a temp for each one? complexity?

                        // ------------- SWAP 1 ------------- (orange 1)
                        // (i, j) (i+1, j+1) (k, k+1)
                        double delta1 = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                                + ij.getGcd() + i1j1.getGcd() + kk1.getGcd();

                        if (delta1 < 0) { //improvement?
                            optSwap(traveled, i + 1, j); //swap i+1 and j
                            improvement = true;

                            int updatedDistance1 = 0;
                            ArrayList<Distance> traveledDistances = locationsToDistances(traveled);
                            for (int a = 0; a < traveledDistances.size(); a++) {
                                updatedDistance1 += traveledDistances.get(a).getGcd();
                            }
                            System.out.println("New Distance (1): " + updatedDistance1);
                        }

                        // ------------- SWAP 2 ------------- (orange 2)
                        // (i, i+1) (j, k) (j+1, k+1)

                        double delta2 = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                                + ii1.getGcd() + jk.getGcd() + j1k1.getGcd();

                        if (delta2 < 0) { //improvement?
                            optSwap(traveled, j + 1, k); //swap j+1 and k
                            improvement = true;

                            int updatedDistance2 = 0;
                            ArrayList<Distance> traveledDistances = locationsToDistances(traveled);
                            for (int a = 0; a < traveledDistances.size(); a++) {
                                updatedDistance2 += traveledDistances.get(a).getGcd();
                            }
                            System.out.println("New Distance (2): " + updatedDistance2);
                        }

                        // ------------- SWAP 3 ------------- (orange 3)
                        // (i, k) (j+1, j) (i+1, k+1)

                        double delta3 = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                                + ik.getGcd() + jj1.getGcd() + i1k1.getGcd();

                        if (delta3 < 0) { //improvement?
                            optSwap(traveled, i + 1, k); //reverse i+1 through k
                            improvement = true;

                            int updatedDistance3 = 0;
                            ArrayList<Distance> traveledDistances = locationsToDistances(traveled);
                            for (int a = 0; a < traveledDistances.size(); a++) {
                                updatedDistance3 += traveledDistances.get(a).getGcd();
                            }
                            System.out.println("New Distance (3): " + updatedDistance3);
                        }

                        // ------------- SWAP 4 ------------- (blue 1)
                        // (i, j) (i+1, k) (j+1, k+1)

                        double delta4 = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                                + ij.getGcd() + i1k.getGcd() + j1k1.getGcd();

                        if (delta4 < 0) { //improvement?
                            optSwap(traveled, i + 1, j); //swap i+1 and j
                            optSwap(traveled, j + 1, k); //swap j+1 and k
                            improvement = true;

                            int updatedDistance4 = 0;
                            ArrayList<Distance> traveledDistances = locationsToDistances(traveled);
                            for (int a = 0; a < traveledDistances.size(); a++) {
                                updatedDistance4 += traveledDistances.get(a).getGcd();
                            }
                            System.out.println("New Distance (4): " + updatedDistance4);
                        }

                        // ------------- SWAP 5 ------------- (blue 2)
                        // (i, k) (j+1, i+1) (j, k+1)  --- swap j+1 and k, switch two middle groups
                        double delta5 = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                                + ik.getGcd() + i1j1.getGcd() + jk1.getGcd();

                        if (delta5 < 0) { //improvement?
                            optSwap(traveled, i + 1, j); //swap j+1 and k
                            replaceSegment(i + 1, j + 1, k, traveled);

                            int updatedDistance5 = 0;
                            ArrayList<Distance> traveledDistances = locationsToDistances(traveled);
                            for (int a = 0; a < traveledDistances.size(); a++) {
                                updatedDistance5 += traveledDistances.get(a).getGcd();
                            }
                            System.out.println("New Distance (5): " + updatedDistance5);
                        }

                        // ------------- SWAP 6 ------------- (blue 3)
                        // (i, j+1) (k, j) (i+1, k+1)  --- swap i+1 and j, switch two middle groups
                        double delta6 = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                                + ij1.getGcd() + jk.getGcd() + i1k1.getGcd();

                        if (delta6 < 0) { //improvement?
                            optSwap(traveled, i+1, j);
                            replaceSegment(i + 1, j + 1, k, traveled);

                            int updatedDistance6 = 0;
                            ArrayList<Distance> traveledDistances = locationsToDistances(traveled);
                            for (int a = 0; a < traveledDistances.size(); a++) {
                                updatedDistance6 += traveledDistances.get(a).getGcd();
                            }
                            System.out.println("New Distance (6): " + updatedDistance6);
                        }

                        // ------------- SWAP 7 ------------- (green 1)
                        // (i, j+1) (k, i+1) (j, k+1)  --- switch two middle groups
                        double delta7 = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                                + ij1.getGcd() + i1k.getGcd() + jk1.getGcd();

                        if (delta7 < 0) { //improvement?
                            replaceSegment(i + 1, j + 1, k, traveled);

                            int updatedDistance7 = 0;
                            ArrayList<Distance> traveledDistances = locationsToDistances(traveled);
                            for (int a = 0; a < traveledDistances.size(); a++) {
                                updatedDistance7 += traveledDistances.get(a).getGcd();
                            }
                            System.out.println("New Distance (7): " + updatedDistance7);
                        }

                        //assert that the old distance - delta = new distance
                        //possible infinite loop
                    }
            }
        }
    }

    //preforms the swap method for 2opt and 3opt
    private void optSwap(ArrayList<Location> traveledTo, int i1, int k) { // swap in place
        while (i1 < k) {
            //swap i+1 and k
            Location temp = traveledTo.get(i1);
            traveledTo.set(i1, traveledTo.get(k));
            traveledTo.set(k, temp);
            i1++;
            k--;
        }
    }

    //switches all the elements from (i+1 to j) with the elements from (j+1 to k)
    public void replaceSegment(int i1, int j1, int k, ArrayList<Location> traveled) {
        ArrayList <Location> list2 = new ArrayList<Location>();
        for(int x = k; x > j1; x--) { //for all the elements from j+1 to k
            list2.add(traveled.remove(x)); //remove them from the original list in backwards order
        }

        for (int i = 0; i < list2.size(); i++) {
            traveled.add(i1, list2.get(i)); // add them back to thsi list at the i+1 index
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

        try {
            InputStream in = getClass().getResourceAsStream("/WorldMap.svg");
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));

            File WorldMapFile;


            //System.out.println(this.getClass().getResourceAsStream("/WorldMap.svg"));
            // copy COmap svg into CoMapTripCo svg (dont read last two line [</g> </svg>])
            LinkedList<String> ll = new LinkedList<String>();

            String line;
            while ((line = buf.readLine()) != null) {

                ll.addLast(line);
            }
            for (int i = 0; i < (ll.size() - 3); i++) {
                SVG += ll.get(i);
            }
            buf.close();
        } catch (IOException e) {
            System.out.println("ERROR: FAILED TO WRITE SVG. Caught after trying to create a scanner");
            System.out.println("Exception: " + e);
            System.exit(0);
        }
        SVG += "</g>";

        //draw lines from start to end locations
        double originStartLat = 0.0;
        double originStartLon = 0.0;
        double finalEndLat = 0.0;
        double finalEndLon = 0.0;
        boolean first = false;
        double unit = 2.84444444444444;

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
                    startLat = 512 - (Math.abs(-90-startLat) * unit);
                }
                else if (startLat > 0) { //lat is positive
                    startLat = (90-startLat) * unit;
                }
                else{
                    startLat = 256;
                }

                if (endLat < 0) { //lat is negative
                    endLat = 512 - (Math.abs(-90-endLat) * unit);
                }
                else if (endLat > 0) { //lat is positive
                    endLat = (90-endLat) * unit;
                }
                else{
                    endLat = 256;
                }

                if (startLon < 0) { //lon is neg
                    startLon = (Math.abs(-180-startLon) * unit);
                }
                else if (startLon > 0) { //lon is positive
                    startLon = 1024 - (180-startLon) * unit;
                }
                else{
                    startLon = 512;
                }

                if (endLon < 0) { //lon is neg
                    endLon = (Math.abs(-180-endLon) * unit);
                }
                else if (endLon > 0) { //lon is positive
                    endLon = 1024 - (180-endLon) * unit;
                }
                else{
                    endLon = 512;
                }


                finalEndLat = d.getEndID().getLatitude();
                finalEndLon = d.getEndID().getLongitude();

                //compute the length of the line in pixels
                //use basic distance formula to compute the distance between the two points
                //distance = sqr((x2-x1)^2 + (y2-y1)^2))
                double distance =Math.sqrt((Math.pow((endLon-startLon),2)) + (Math.pow((endLat-startLat),2)));

                //if longer than 512 pixels, then you need to draw it around the back
                if(distance < 512){
                    double x1 = startLon;
                    double y1 = startLat;
                    double x2 = endLon;
                    double y2 = endLat;
                    SVG += "  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"2\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" id=\"svg_1\"/>";
                }
                else{
                    //call helper method to get the two lines that need to wrap around the map
                    SVG += wrapAround(startLon, startLat, endLon, endLat);
                }
            }

            if (finalEndLat < 0) { //lat is negative
                finalEndLat = 512 - (Math.abs(-90-finalEndLat) * unit);
            }
            else if (finalEndLat > 0) { //lat is positive
                finalEndLat = (90-finalEndLat) * unit;
            }
            else {
                finalEndLat = 256;
            }

            if (originStartLat < 0) { //lat is negative
                originStartLat = 512 - (Math.abs(-90-originStartLat) * unit);
            }
            else if (originStartLat > 0) { //lat is positive
                originStartLat = (90-originStartLat) * unit;
            }
            else{
                originStartLat = 256;
            }

            if (originStartLon < 0) { //lon is neg
                originStartLon = (Math.abs(-180-originStartLon) * unit);
            }
            else if (originStartLon > 0) { //lon is positive
                originStartLon = 1024 - (180-originStartLon) * unit;
            }
            else{
                originStartLon = 512;
            }

            if (finalEndLon < 0) { //lon is neg
                finalEndLon = (Math.abs(-180-finalEndLon) * unit);
            }
            else if (finalEndLon > 0) { //lon is positive
                finalEndLon = 1024 - (180-finalEndLon) * unit;
            }
            else{
                finalEndLon = 512;
            }

            double distancefinal =Math.sqrt((Math.pow((originStartLon-finalEndLon),2)) + (Math.pow((originStartLat-finalEndLat),2)));
            if(distancefinal < 512) {
                //draw last line connected end point with start
                double endX1 = finalEndLon;
                double endY1 = finalEndLat;
                double endX2 = originStartLon;
                double endY2 = originStartLat;
                SVG += "  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"2\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + endX1 + "\" y1=\"" + endY1 + "\" x2=\"" + endX2 + "\" y2=\"" + endY2 + "\" id=\"svg_1\"/>";
            }
            else{
                SVG += wrapAround(finalEndLon, finalEndLat, originStartLon, originStartLat);
            }
            SVG += "</svg>";
        }
        return SVG;

    }

    private String wrapAround(double x1, double y1, double x2, double y2){
        String aroundBackLines = "";
        //reflect both points across the y axis
        //do midpoint formula to get the point in the middle
        // M = ( (x1 + x2)/2 ,  (y1 + y2)/2  )
        double midX = (x1 + x2) / 2;
        double midY = (y1 + y2) / 2;
        //now have two points that make a line and can be reflected across and axis
        //To reflect: Go one point at a time:
        //the point is the new axis
        //figure out the distance between the mid point's x and the og point's x
        //then add/subtract that distance (depending on which direction you're going) for your new x
        // you will keep the same y value


        //left point first - (x1, y1)
        double leftDFromAxis = midX - x1;
        double newLeftX = x1 - leftDFromAxis;
        if(newLeftX < 512){
            newLeftX = 0;
        }
        else{
            newLeftX = 1024;
        }

        //new line needs to be drawn from (new left x to x1)
        aroundBackLines += "  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"2\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + newLeftX + "\" y1=\"" + midY + "\" x2=\"" + x1 + "\" y2=\"" + y1 + "\" id=\"svg_1\"/>";
        //right point next - (x2, y2)
        double rightDFromAxis = x2 - midX;
        double newRightX = x2 + rightDFromAxis;
        if(newRightX < 512){
            newRightX = 0;
        }
        else {
            newRightX = 1024;
        }

        //new line needs to be drawn from (new left x to x1)
        aroundBackLines += "  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"2\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + x2 + "\" y1=\"" + y2 + "\" x2=\"" + newRightX + "\" y2=\"" + midY + "\" id=\"svg_1\"/>";
        return aroundBackLines;
    }

}