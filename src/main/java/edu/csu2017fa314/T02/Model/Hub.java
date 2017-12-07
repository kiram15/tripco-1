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

    /** The hub object stores the selected trips and locations, as well as
    * the selected optimizations and unit selection, to proved the correct
    * itinerary given the variable provided
    */
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

    /** creates a hub object that stores itinerary, and shortest trips,
    * with passed in optimization choice
    */
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

    public void clearFinalLocations(){ finalLocations.clear();
    }


    /** searches the data base for the keyword searchingFor, unless upload
    * is flagged as true, which causes searchingFor to be treated as a
    * complete query
    */
    public void searchDatabase(String username, String password, String searchFor, boolean upload){

        searchedLocations.clear();
        shortestItinerary.clear();
        columns.clear();
        reverseC.clear();
        searchFor = searchFor.toLowerCase();
        String myDriver = "com.mysql.jdbc.Driver"; // add dependencies in pom.xml
        String myUrl = "jdbc:mysql://faure.cs.colostate.edu/cs314";
        //String myUrl = "jdbc:mysql://localhost/cs314"; use if tunneling 
        try { // connect to the database
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, username, password);
            try { // create a statement
                Statement st = conn.createStatement();
                try {
                    //give order of column header to storeColumnHeaders
                    String colHeaders = "airports_ID, airports_Code, airports_Type, airports_Name,"
                            + " airports_Latitude, airports_Longitude, airports_Elevation, "
                            + "airports_Continent, airports_Iso_country, airports_Iso_region, "
                            + "airports_Municipality, airports_Scheduled_service, "
                            + "airports_Gps_code, airports_Iata_code, airports_Local_code, "
                            + "airports_Home_link, airports_Wikipedia_link, airports_Keywords, "
                            + "regions_ID, regions_Code, regions_Local_code, regions_Name, "
                            + "regions_Continent, regions_Iso_country, regions_Wikipedia_link, "
                            + "regions_Keywords, countries_ID, countries_Code, countries_Name, "
                            + "countries_Continent, countries_Wikipedia_link, countries_Keywords, "
                            + "continents_ID, continents_Name, continents_Code, "
                            + "continents_Wikipedia_link";
                    storeColumnHeaders(colHeaders);
                    String allTblsSearchQ = "";

                    if (!upload) {
                        allTblsSearchQ = "select airports.id as airports_ID, airports.code as "
                                + "airports_Code, airports.type as airports_Type, airports.name "
                                + "as airports_Name, airports.latitude as airports_Latitude, "
                                + "airports.longitude as airports_Longitude, airports.elevation as "
                                + "airports_Elevation, airports.continent as airports_Continent, "
                                + "airports.iso_country as airports_Iso_country, airports."
                                + "iso_region as airports_Iso_region, airports.municipality "
                                + "as airports_Municipality, airports.scheduled_service as "
                                + "airports_Scheduled_service, airports.gps_code as "
                                + "airports_Gps_code, airports.iata_code as "
                                + "airports_Iata_code, airports.local_code as airports_Local_code, "
                                + "airports.home_link as airports_Home_link, "
                                + "airports.wikipedia_link as airports_Wikipedia_link, "
                                + "airports.keywords as airports_Keywords, "
                                + "regions.id as regions_ID, regions.code "
                                + "as regions_Code, regions.local_code as regions_Local_code,"
                                + " regions.name as regions_Name, regions.continent"
                                + " as regions_Continent, regions.iso_country"
                                + " as regions_Iso_country, regions.wikipedia_link as "
                                + "regions_Wikipedia_link, regions.keywords as regions_Keywords, "
                                + "countries.id as countries_ID, countries.code as countries_Code, "
                                + "countries.name as countries_Name, countries.continent as "
                                + "countries_Continent, countries.wikipedia_link as "
                                + "countries_Wikipedia_link, countries.keywords as "
                                + "countries_Keywords, continents.id as continents_ID"
                                + ", continents.name as continents_Name,"
                                + " continents.code as continents_Code, continents."
                                + "wikipedia_link as continents_Wikipedia_link "
                                + "from continents "
                                + "inner join countries on countries.continent = continents.code "
                                + "inner join regions on regions.iso_country = countries.code "
                                + "inner join airports on airports.iso_region = regions.code "
                                + "where "
                                + "airports.id like '%" + searchFor + "%' or airports.code like '%"
                                + searchFor + "%' or airports.type like '%" + searchFor + "%' or "
                                + "airports.name like '%" + searchFor + "%' or airports.latitude "
                                + "like '%" + searchFor + "%' or airports.longitude like '%"
                                + searchFor + "%' or airports.elevation like '%"
                                + searchFor + "%' or " + "airports.continent like '%"
                                + searchFor + "%' or airports.iso_country"
                                + " like '%" + searchFor + "%' or airports.iso_region like '%"
                                + searchFor + "%' or airports.municipality like '%" + searchFor
                                + "%' or " + "airports.scheduled_service like '%" + searchFor
                                + "%' or airports.gps_code" + " like '%" + searchFor
                                + "%' or airports.iata_code like '%" + searchFor
                                + "%' or airports.local_code like '%" + searchFor
                                + "%' or airports.home_link like '%" + searchFor + "%' or airports."
                                + "wikipedia_link like '%" + searchFor + "%' or airports."
                                + "keywords like '%" + searchFor
                                + "%' or regions.id like '%" + searchFor + "%' or "
                                + "regions.code like '%" + searchFor + "%' or regions.local_code "
                                + "like '%" + searchFor + "%' or regions.name like '%" + searchFor
                                + "%' or regions.continent like '%" + searchFor + "%' or "
                                + "regions.iso_country like '%" + searchFor + "%' or regions."
                                + "wikipedia_link like '%" + searchFor + "%' or regions.keywords "
                                + "like '%" + searchFor + "%' or countries.id like '%" + searchFor
                                + "%' or countries.code like '%" + searchFor
                                + "%' or countries.name like '%" + searchFor
                                + "%' or countries.continent like '%" + searchFor
                                + "%' or countries.wikipedia_link like '%" + searchFor
                                + "%' or countries.keywords like '%" + searchFor + "%' or "
                                + "continents.id like '%" + searchFor + "%' or continents.name like '%"
                                + searchFor + "%' or continents.code like '%" + searchFor
                                + "%' or continents.wikipedia_link like '%" + searchFor + "%' "
                                + "limit 100;";
                    } else {
                        allTblsSearchQ = searchFor;
                    }
                    ResultSet allTblsSearchRs = st.executeQuery(allTblsSearchQ);
                    try { //parse matched rows
                        while (allTblsSearchRs.next()) { //for each row
                            String matchedRow = "";
                            //traverse row by incrementing columns and storing in a string
                            for (int i = 1; i <= columns.size(); i++) {
                                String rowCol = allTblsSearchRs.getString(i);
                                rowCol = rowCol + ",";
                                matchedRow += rowCol;
                                if(upload && i == 18){
                                    break;
                                }
                            }
                            parseRow(matchedRow);
                        }
                    } finally {
                        allTblsSearchRs.close();
                    }

                } finally{ st.close(); }
            } finally { conn.close(); }
        } catch (Exception e) { // catches all exceptions in the nested try's
            System.err.printf("Exception: ");
            System.err.println(e.getMessage());
        }
    }

    /** converts the desired selected locations into the array list specified
    *
    */
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
            System.exit(1);
        } catch(ExecutionException ee){
            System.exit(1);
        }
    }

    /** Deals with special characters that cause problems with the
    * searching and selection in uploads
    */
    private boolean equalsWithoutAmp(String name, String ll){
        int index = name.indexOf('&');


        String subName = name.substring(index + 5);
        String subL = ll.substring(index + 1);

        if(subName.equals(subL)){
            return true;
        } else{
            return false;
        }
    }


    private void createCallables() throws InterruptedException, ExecutionException {
        //Create thread pool
        ExecutorService pool = Executors.newFixedThreadPool(6);

        //List to store distances returned from
        List<Future<Integer>> results = new ArrayList<>();

        //List to store all of the callables from singleTripDistance
        List<Callable<Integer>> callables = new ArrayList<>();
        
        //creates the OG GCD array based on the current selectedLocations
        Object[][] gcds = calcAllGcds(selectedLocations);


        //for every distance, add a singleTripDistance object
        for(int i = 0; i < this.selectedLocations.size(); i++){
            callables.add(singleTripDistance(gcds, this.selectedLocations.get(i)));
        }

        //get the distance of the shortest Trip from each starting location
        results = pool.invokeAll(callables);
        pool.shutdown();

        //grab the start location that corresponds with shortest distance
        Location startLocation = findStartLocation(results);
        //System.out.println("StartLocation: " + startLocation.getName());

        //rebuild the trip using the startLocation
        createItinerary(gcds, startLocation);
    }

    private Callable<Integer> singleTripDistance(Object[][] gcds, Location currentLocation){
        Callable<Integer> returnValue = new Callable<Integer>(){
            @Override
            public Integer call() throws Exception{
                //this will call the optimizaton method that returns the distance of the single trip
                return tripDistance(gcds, currentLocation);
            }
        };
        return returnValue;
    }

    private int tripDistance(Object[][] gcds, Location currentLoc){
        int singleTripDist = 0;
        //switch statement that calls the specific shortest trip method
        //based on selected optimization
        switch(optimization){
            case "None":
                singleTripDist = 0;
                break;
            case "NearestNeighbor":
                NearestNeighbor nearestOpt = new NearestNeighbor();
                singleTripDist = nearestOpt.shortestTripDistance(selectedLocations, currentLoc);
                break;
            case "TwoOpt":
                Opt2 twoOpt = new Opt2();
                singleTripDist = twoOpt.shortestTripDistance(selectedLocations, currentLoc);
                break;
            case "ThreeOpt":
                Opt3 threeOpt = new Opt3(gcds);
                singleTripDist = threeOpt.shortestTripDistance(selectedLocations, currentLoc);
                break;
            default:
                singleTripDist = 0;
                break;
        }
        return singleTripDist;
    }

    //loops through results and finds the shortest distance
    //then returns the starting location that corresponds to this distance
    private Location findStartLocation(List<Future<Integer>> results)
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
    
    /** fills shortestItinerary based on shortest startLocation and opt
     */
    public void createItinerary(Object[][] gcds, Location startLocation){
        //switch statement that calls the specific shortest trip method based on selected optimization
        switch(optimization){
            case "None":
                shortestItinerary = locationsToDistances(selectedLocations);
                break;
            case "NearestNeighbor":
                NearestNeighbor nearestOpt = new NearestNeighbor();
                setShortestItinerary(nearestOpt.buildShortestTrip(selectedLocations,startLocation));
                break;
            case "TwoOpt":
                Opt2 twoOpt = new Opt2();
                setShortestItinerary(twoOpt.buildShortestTrip(selectedLocations,startLocation));
                break;
            case "ThreeOpt":
                Opt3 threeOpt = new Opt3(gcds);
                setShortestItinerary(threeOpt.buildShortestTrip(selectedLocations,startLocation));
                break;
            default:
                shortestItinerary = locationsToDistances(selectedLocations);
                break;
        }
    }

    /**
    * stores the info about each airport in the location lists
    */
    public void storeColumnHeaders(String firstLine){
        String ss = firstLine.toLowerCase();
        String[] infoArray = ss.split(",");
        for (int i = 0; i < infoArray.length; i++) {
            String infoString = infoArray[i];
            // associating column titles with column num, putting it in map
            switch (infoString.trim()) {
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

    /**
    * splits up rows to add to the selected locations
    */
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

    /**
    * converts string latitude or longitude (s) to double
    */
    public double latLonConvert(String s) {
        String copyS = s;
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
                end = copyS.indexOf(symbol);
                values.add(Double.parseDouble(copyS.substring(0, end)));
                copyS = copyS.substring(end + 1);
            }
            retVal = (values.get(0) + (values.get(1) / 60) + (values.get(2) / 3600));
            if (copyS.equals("W") || copyS.equals("S")) {
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
                end = copyS.indexOf(symbol);
                values.add(Double.parseDouble(copyS.substring(0, end)));
                copyS = copyS.substring(end + 1);
            }
            retVal = (values.get(0) + (values.get(1) / 60));
            if (copyS.equals("W") || copyS.equals("S")) {
                return (retVal * (-1));
            } else {
                return retVal;
            }

        } else if (s.contains("°")) { // case for 106.24° format
            end = copyS.indexOf("°");
            return (Double.parseDouble(copyS.substring(0, end)));
        } else { // case for -106.24 format
            return Double.parseDouble(s);
        }
    }

    /**
    * performs the swap method for 2opt and 3opt
    */
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

    /**
    * will return an array list with each city listed once, with the shortest city as its end
    */
    public Object[][] calcAllGcds(ArrayList<Location> selectedLocations) {
        Object[][] gcds = new Object[selectedLocations.size()][selectedLocations.size()+1];
        for (int i = 0; i < selectedLocations.size(); i++) {
            //get the initial Location
            gcds[i][0] = selectedLocations.get(i);
            for (int j = 0; j < selectedLocations.size(); j++) {
                //for all the Distances in the row
                Location startId = selectedLocations.get(i);
                Location endId = selectedLocations.get(j);
                Distance dis = new Distance(startId, endId, miles);
                gcds[i][j+1] = dis; //j+1 because of the Location in the first column
            }
        }
        return gcds;
    }

    /** transforms an arrayList of location objects into an arrayList of distance objects using the
    * location objects in the order they are passed in
    */
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

    /** Draws the lines on the google map as an svg overlay
    *   adds to the actual map
    */
    public ArrayList<gMap> drawSVG(){
        double firstLocationLat = 0.0;
        double firstLocationLon = 0.0;
        boolean first = true;
        ArrayList<gMap> finalGMap = new ArrayList<gMap>();
        if(shortestItinerary.size() > 0) {
            for (Distance d : shortestItinerary) {
                if (first) {
                    firstLocationLat = d.getStartId().getLatitude();
                    firstLocationLon = d.getStartId().getLongitude();
                    first = false;
                }
                double startLat = d.getStartId().getLatitude();
                double startLon = d.getStartId().getLongitude();
                gMap gm = new gMap(startLat, startLon);
                finalGMap.add(gm);
            }

            double lastEndLat =
                    (shortestItinerary.get(shortestItinerary.size() - 1)).getEndId().getLatitude();
            double lastEndLon =
                    (shortestItinerary.get(shortestItinerary.size() - 1)).getEndId().getLongitude();
            gMap gmLast = new gMap(lastEndLat, lastEndLon);
            finalGMap.add(gmLast);
            gMap gmFirst = new gMap(firstLocationLat, firstLocationLon);
            finalGMap.add(gmFirst);
        }
        return finalGMap;
    }

}
