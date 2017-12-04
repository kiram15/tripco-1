package edu.csu2017fa314.T02.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Hub {
    Map<String, Integer> columns;
    Map<Integer, String> reverseC;
    ArrayList<Location> finalLocations;
    ArrayList<Location> searchedLocations;
    ArrayList<Location> selectedLocations;
    ArrayList<Distance> shortestItinerary;
    boolean miles;
    String optimization;

    //constructor
    public Hub(){
        this.columns = new LinkedHashMap<String, Integer>();
        this.reverseC = new LinkedHashMap<Integer, String>();
        this.finalLocations = new ArrayList<Location>();
        this.searchedLocations = new ArrayList<Location>();
        this.selectedLocations = new ArrayList<Location>();
        this.shortestItinerary = new ArrayList<Distance>();
        this.miles = true;
        this.optimization = "";
    }

    //constructor
    public Hub(String optimization){
        this.columns = new LinkedHashMap<String, Integer>();
        this.reverseC = new LinkedHashMap<Integer, String>();
        this.finalLocations = new ArrayList<Location>();
        this.searchedLocations = new ArrayList<Location>();
        this.selectedLocations = new ArrayList<Location>();
        this.shortestItinerary = new ArrayList<Distance>();
        this.miles = true;
        this.optimization = optimization;
    }

    //three necessary getters
    public ArrayList<Distance> getShortestItinerary(){
        //System.out.println("shortestItinerary: " + this.shortestItinerary);
        return this.shortestItinerary;
    }

    public ArrayList<Location> getFinalLocations(){
        return this.finalLocations;
    }

    public ArrayList<Location> getSearchedLocations(){
        return this.searchedLocations;
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

    public void clearFinalLocations(){ finalLocations.clear(); }

    public void searchDatabase(String username, String password, String searchingFor, boolean upload){
        searchedLocations.clear();
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
                try {
                    //give order of column header to storeColumnHeaders
                    String colHeaders = "airports_ID, airports_Code, airports_Type, airports_Name, airports_Latitude, airports_Longitude, airports_Elevation, airports_Continent, airports_Iso_country, airports_Iso_region, airports_Municipality, airports_Scheduled_service, airports_Gps_code, airports_Iata_code, airports_Local_code, airports_Home_link, airports_Wikipedia_link, airports_Keywords, "
                            + "regions_ID, regions_Code, regions_Local_code, regions_Name, regions_Continent, regions_Iso_country, regions_Wikipedia_link, regions_Keywords, "
                            + "countries_ID, countries_Code, countries_Name, countries_Continent, countries_Wikipedia_link, countries_Keywords, "
                            + "continents_ID, continents_Name, continents_Code, continents_Wikipedia_link";
                    storeColumnHeaders(colHeaders);
                    String allTblsSearchQ = "";

                    if (!upload) {
                        allTblsSearchQ = "select airports.id as airports_ID, airports.code as airports_Code, airports.type as airports_Type, airports.name as airports_Name, airports.latitude as airports_Latitude, airports.longitude as airports_Longitude, airports.elevation as airports_Elevation, airports.continent as airports_Continent, airports.iso_country as airports_Iso_country, airports.iso_region as airports_Iso_region, airports.municipality as airports_Municipality, airports.scheduled_service as airports_Scheduled_service, airports.gps_code as airports_Gps_code, airports.iata_code as airports_Iata_code, airports.local_code as airports_Local_code, airports.home_link as airports_Home_link, airports.wikipedia_link as airports_Wikipedia_link, airports.keywords as airports_Keywords, "
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
                    } else {
                        allTblsSearchQ = searchingFor;
                    }
                    ResultSet allTblsSearchRS = st.executeQuery(allTblsSearchQ);
                    try { //parse matched rows
                        while (allTblsSearchRS.next()) { //for each row
                            String matchedRow = "";
                            for (int i = 1; i <= columns.size(); i++) { //traverse row by incrementing columns and storing in a string
                                String rowCol = allTblsSearchRS.getString(i);
                                rowCol = rowCol + ",";
                                matchedRow += rowCol;
                                if(upload && i == 18){
                                    break;
                                }
                            }
                            parseRow(matchedRow);
                        }
                    } finally {
                        allTblsSearchRS.close();
                    }

                } finally{ st.close(); }
            } finally { conn.close(); }
        } catch (Exception e) { // catches all exceptions in the nested try's
            System.err.printf("Exception: ");
            System.err.println(e.getMessage());
        }
    }

    public void finalLocationsFromWeb(ArrayList<String> desiredLocations){
        selectedLocations.clear();
        //go through each element in the desiredLocations array list and grab the name
        for (String name : desiredLocations){
            //find the location object from finalLocations based on the name
            for(Location l : finalLocations){
                //add this location to selectedLocations
                if(l.getName().contains("&") && name.contains("&")){
                    boolean equals = equalsWithoutAmp(name, l.getName());
                    if(equals){
                        selectedLocations.add(l);
                        break;
                    }
                }
                else{
                    if(l.getName().equals(name)){
                        selectedLocations.add(l);
                        break;
                    }
                }
            }
        }
        try{
            createCallables();
        } catch(InterruptedException ie){
            System.out.println("InterruptedException: " + ie);
            System.exit(1);
        }
        catch(ExecutionException ee){
            System.out.println("ExecutionException: " + ee);
            System.exit(1);
        }
        //createItinerary();
    }

    //deals with extra characters added with ampersands
    private boolean equalsWithoutAmp(String name, String l){
        int index = name.indexOf('&');

        String subName = name.substring(index + 5);
        String subL = l.substring(index + 1);

        if(subName.equals(subL)){
            return true;
        } else{
            return false;
        }
    }

    public void createCallables() throws InterruptedException, ExecutionException {
        //Create thread pool
        ExecutorService pool = Executors.newFixedThreadPool(5);
        
        //List to store distances returned from 
        List<Future<Integer>> results = new ArrayList<>();
        
        //List to store all of the callables from singleTripDistance
        List<Callable<Integer>> callables = new ArrayList<>();
        
        //for every distance, add a singleTripDistance object
        for(int i = 0; i < this.selectedLocations.size(); i++){
            callables.add(singleTripDistance(this.selectedLocations.get(i)));
        }
        System.out.println("successfully added selectedLocations to callables: " + callables.toString());
        
        //get the distance of the shortest Trip from each starting location 
        results = pool.invokeAll(callables);
        System.out.println("InvokedAll");
        pool.shutdown();
        
        //grab the start location that corresponds with shortest distance
        Location startLocation = findStartLocation(results);
        
        //rebuild the trip using the startLocation
        createItinerary();
    }
    
    private Callable<Integer> singleTripDistance(Location currentLocation){
        Callable<Integer> returnValue = new Callable<Integer>(){
            @Override
            public Integer call() throws Exception{
                //this will call the optimizaton method that returns the distance of the single trip
                return TripD(currentLocation);
            }
        };
        return returnValue;
    }

    public int TripD(Location currentLocation){
        int sTripD = 0;
        //switch statement that calls the specific shortest trip method based on selected optimization
        switch(optimization){
            case "None":
                sTripD = 0;
                break;
            case "NearestNeighbor":
                NearestNeighbor nearestOpt = new NearestNeighbor();
                sTripD = nearestOpt.shortestTripDistance(selectedLocations, currentLocation);
                break;
            case "TwoOpt":
                Opt2 twoOpt = new Opt2();
                sTripD = twoOpt.shortestTripDistance(currentLocation);
                break;
            case "ThreeOpt":
                Opt3 threeOpt = new Opt3();
                sTripD = threeOpt.shortestTripDistance(currentLocation);
                break;
            default:
                sTripD = 0;
                break;
        }
        System.out.println("got a sTripD result: " + sTripD);
        return sTripD;
    }

    public void createItinerary(){
        //switch statement that calls the specific shortest trip method based on selected optimization
        switch(optimization){
            case "None":
                shortestItinerary = locationsToDistances(selectedLocations);
                break;
            case "NearestNeighbor":
                NearestNeighbor nearestOpt = new NearestNeighbor();
                setShortestItinerary(nearestOpt.shortestTrip(selectedLocations));
                break;
            case "TwoOpt":
                Opt2 twoOpt = new Opt2();
                setShortestItinerary(twoOpt.shortestTrip(selectedLocations));
                break;
            case "ThreeOpt":
                Opt3 threeOpt = new Opt3();
                setShortestItinerary(threeOpt.shortestTrip(selectedLocations));
                break;
            default:
                shortestItinerary = locationsToDistances(selectedLocations);
                break;
        }
    }
    
    //loops through results and finds the shortest distance
    //then returns the starting location that corresponds to this distance
    public Location findStartLocation(List<Future<Integer>> results)
            throws InterruptedException, ExecutionException {
        int minVal = Integer.MAX_VALUE;
        int minIndex = -1;
        for(int i = 0; i < results.size(); i++){
            if(results.get(i).get() < minVal){
                minVal = results.get(i).get();
                minIndex = i;
            }
        }
        return this.selectedLocations.get(minIndex);
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
        searchedLocations.add(location);
        selectedLocations.add(location);
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

    //preforms the swap method for 2opt and 3opt
    public void optSwap(ArrayList<Location> traveledTo, int i1, int k) { // swap in place
        while (i1 < k) {
            // reverses all the elements from i+1 to k
            // (i, i+1) a b c (k, k+1) BEFORE
            // (i, k) c b a (i+1, k+1) AFTER
            Location temp = traveledTo.get(i1);
            traveledTo.set(i1, traveledTo.get(k));
            traveledTo.set(k, temp);
            i1++;
            k--;
        }
    }

    //will return an array list with each city listed once, with the shortest city as its end
    public Object[][] calcAllGcds(ArrayList<Location> selectedLocations) {
        Object[][] GCDS = new Object[selectedLocations.size()][selectedLocations.size()+1];
        for (int i = 0; i < selectedLocations.size(); i++) {
            //get the initial Location
            GCDS[i][0] = selectedLocations.get(i);
            for (int j = 0; j < selectedLocations.size(); j++) {
                //for all the Distances in the row
                Location startID = selectedLocations.get(i);
                Location endID = selectedLocations.get(j);
                Distance dis = new Distance(startID, endID, miles);
                GCDS[i][j+1] = dis; //j+1 because of the Location in the first column
            }
        }
        return GCDS;
    }

    //transforms an arrayList of location objects into an arrayList of distance objects using the
    //location objects in the order they are passed in
    public ArrayList<Distance> locationsToDistances(ArrayList<Location> locations) {
        ArrayList<Distance> finalDistances = new ArrayList<Distance>();
        for (int i = 0; i < locations.size(); i++) {
            if (i == locations.size() - 1) { //distance of last back to first
                Distance base = new Distance(locations.get(i), locations.get(0), miles);
                finalDistances.add(base);
            } else {
                Distance dis = new Distance(locations.get(i), locations.get(i + 1), miles);
                finalDistances.add(dis);
            }
        }
        return finalDistances;
    }

    public ArrayList<gMap> drawSVG(){
        double firstLocationLat = 0.0;
        double firstLocationLon = 0.0;
        boolean first = true;
        ArrayList<gMap> finalGMap = new ArrayList<gMap>();
        if(shortestItinerary.size() > 0) {
            for (Distance d : shortestItinerary) {
                if (first) {
                    firstLocationLat = d.getStartID().getLatitude();
                    firstLocationLon = d.getStartID().getLongitude();
                    first = false;
                }
                double startLat = d.getStartID().getLatitude();
                double startLon = d.getStartID().getLongitude();
                gMap gm = new gMap(startLat, startLon);
                finalGMap.add(gm);
            }

            double lastEndLat = (shortestItinerary.get(shortestItinerary.size() - 1)).getEndID().getLatitude();
            double lastEndLon = (shortestItinerary.get(shortestItinerary.size() - 1)).getEndID().getLongitude();
            gMap gmLast = new gMap(lastEndLat, lastEndLon);
            finalGMap.add(gmLast);
            gMap gmFirst = new gMap(firstLocationLat, firstLocationLon);
            finalGMap.add(gmFirst);
        }
        return finalGMap;
    }

}
