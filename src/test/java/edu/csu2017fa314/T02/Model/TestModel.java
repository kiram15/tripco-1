package edu.csu2017fa314.T02.Model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TestModel {
    private Model m;
    private Hub h;
    private Location L1, L2, L3, L4;
    private ArrayList<Distance> shortTrip;
    private boolean miles = true;

    /** Executes before the tests run
    * so that they're all set up
    */
    @Before
    public void setUp() throws Exception {
        m = new Model();
        h = new Hub();
        L1 = new Location("test", 37, -102, null);
        L2 = new Location("test2", 41, -109, null);
        shortTrip = new ArrayList<Distance>();
    }

    @Test
    public void testGetNumbers() {
        assertArrayEquals(m.getNumbers(), new int[]{0, 1, 2, 3, 4, 5});
    }

    // --------------------- Location Testing  ---------------------

    @Test
    public void testLtoString() {
        h = new Hub();
        L1 = new Location("test", 100, -50, null);
        String finalString = "Name: 'test', Latitude: '100.0', Longitude: '-50.0";
        String testString = L1.toString();
        assertEquals(finalString, testString);
    }

    // --------------------- Distance Testing  ---------------------

    @Test
    public void testDCompareTo() {
        Location n0 = new Location("kira", 40.0, 50.0, null);
        Location n1 = new Location("amber", 60.0, 70.5, null);
        Location n2 = new Location("nicole", 100.0, 60.0, null);

        Distance d0 = new Distance(n0, n1, miles); //1640
        Distance d1 = new Distance(n1, n2, miles); //2755

        assertEquals((1640 - 2755), d0.compareTo(d1));
    }

    @Test
    public void testDtoString() {
        h = new Hub();
        L1 = new Location("test1", 37, -102, null);
        L2 = new Location("test2", 41, -109, null);
        Distance D1 = new Distance(L1, L2, miles);
        String finalString = "Distance{StartId= 'Name: 'test1', Latitude: '37.0', Longitude: '-102.0', " +
                "EndId= 'Name: 'test2', Latitude: '41.0', Longitude: '-109.0', GCD= '466}";
        String testString = D1.toString();
        assertTrue(finalString.equals(testString));
    }

    @Test
    public void testDEquals() {
        L1 = new Location("test1", 37, -102, null);
        L2 = new Location("test2", 41, -109, null);
        Distance D1 = new Distance(L1, L2, miles);
        assertFalse(D1.equals(L1));
    }

    // ------------------- Test Great Circle Distance -------------------

    @Test
    public void testDiagonalGCD() {
        Distance d = new Distance(L1, L2, miles);
        assertEquals(466, d.computeGcd(L1, L2, miles));
        L1.setLat(41);
        L2.setLat(37);
        Distance d2 = new Distance(L1, L2, miles);
        assertEquals(466, d.computeGcd(L1, L2, miles));
    }

    @Test
    public void testSameLonGCD() {
        L2.setLat(41);
        L2.setLon(-102);
        L1.setLat(37);
        L1.setLon(-102);
        Distance d = new Distance(L1, L2, miles);
        assertEquals(276, d.computeGcd(L1, L2, miles));
        miles = false;
        assertEquals(445, d.computeGcd(L1, L2, miles));
    }

    @Test
    public void testSameLatGCD() {
        L1.setLat(37);
        L2.setLat(37);
        L1.setLon(-102);
        L2.setLon(-109);
        Distance d = new Distance(L1, L2, miles);
        assertEquals(386, d.computeGcd(L1, L2, miles));
    }

    @Test
    public void testSameLocationGCD() {
        Distance d = new Distance(L1, L1, miles);
        assertEquals(0, d.computeGcd(L1, L1, miles));
    }

    @Test
    public void testReverseGCD() {
        Distance d = new Distance(L1, L2, miles);
        assertEquals(d.computeGcd(L1, L2, miles), d.computeGcd(L2, L1, miles));
    }

    // ----------------- Test Lat/Lon Decimal Convert ----------------

    @Test
    public void testDMSLatLon() {
        Hub h = new Hub();
        assertEquals(106.828678, h.latLonConvert("106°49'43.24\" W"), 0.01);
        assertEquals(70.009258, h.latLonConvert("70°0'33.33\" N"), 0.01);
        assertEquals(0.005556, h.latLonConvert("0°0'20\" E"), 0.01);
    }

    @Test
    public void testDMLatLon() {
        Hub h = new Hub();
        assertEquals(99.255556, h.latLonConvert("99°15.13' W"), 0.01);
        assertEquals(70, h.latLonConvert("70°0' S"), 0.01);
    }

    @Test
    public void testDLatLon() {
        Hub h = new Hub();
        assertEquals(99.25, h.latLonConvert("99.25°"), 0.01);
        assertEquals(-106.24, h.latLonConvert("-106.24"), 0.01);
    }

    // ------------------------- Test drawSVG ----------------------------


    @Test
    public void testDrawSVG() {
        LinkedHashMap<String, String> info1 = new LinkedHashMap<>();
        info1.put("extra1", "info1");
        info1.put("extra2", "info2");
        LinkedHashMap<String, String> info2 = new LinkedHashMap<>();
        info2.put("extra1", "info1");
        info2.put("extra2", "info2");
        Hub hA = new Hub();
        Location startL = new Location("denver", 70, 99.255556, info1);
        Location endL = new Location("denver2", 80, 100, info2);
        final Distance dA = new Distance(startL, endL, miles);
        Location startL1 = new Location("denver", -70, -99.255556, info1);
        Location endL1 = new Location("denver2", 80, 100, info2);
        final Distance dA1 = new Distance(startL1, endL1, miles);
        Location startL2 = new Location("denver", 70, 99.255556, info1);
        Location endL2 = new Location("denver2", -80, -100, info2);
        final Distance dA2 = new Distance(startL2, endL2, miles);
        Location startL3 = new Location("denver", -70, 99.255556, info1);
        Location endL3 = new Location("denver2", 80, -100, info2);
        final Distance dA3 = new Distance(startL3, endL3, miles);
        Location startL4 = new Location("denver", 0, 0, info1);
        Location endL4 = new Location("denver2", 0, 0, info2);
        final Distance dA4 = new Distance(startL4, endL4, miles);
        Location startL5 = new Location("california", 36.77, -199.41, info1);
        Location endL5 = new Location("australia", -25.28, 133.775, info2);
        final Distance dA5 = new Distance(startL5, endL5, miles);
        ArrayList<gMap> dSVG;

        //empty shortestItinerary
        dSVG = hA.drawSVG();
        assertEquals(0, dSVG.size());

        hA.shortestItinerary.add(dA4);
        dSVG = hA.drawSVG();
        assertNotEquals(0, dSVG.size());
        gMap gm1 = new gMap(0.0,0.0);
        boolean match = false;
        for(gMap a : dSVG){
            double gmLon = a.lon;
            double gmLat = a.lat;
            if(gm1.lon == gmLon && gm1.lat == gmLat){
                match = true;
                break;
            }
        }
        assertTrue(match);

        hA.shortestItinerary.add(dA);
        dSVG = hA.drawSVG();
        assertNotEquals(0, dSVG.size());
        gMap gm2 = new gMap(70.0, 99.255556);
        match = false;
        for(gMap a : dSVG){
            double gmLon = a.lon;
            double gmLat = a.lat;
            if(gm2.lon == gmLon && gm2.lat == gmLat){
                match = true;
                break;
            }
        }
        assertTrue(match);

        hA.shortestItinerary.add(dA1);
        dSVG = hA.drawSVG();
        assertNotEquals(0, dSVG.size());
        gMap gm3 = new gMap(-70.0, -99.255556);
        match = false;
        for(gMap a : dSVG){
            double gmLon = a.lon;
            double gmLat = a.lat;
            if(gm3.lon == gmLon && gm3.lat == gmLat){
                match = true;
                break;
            }
        }
        assertTrue(match);

        hA.shortestItinerary.add(dA2);
        dSVG = hA.drawSVG();
        assertNotEquals(0, dSVG.size());
        gMap gm4 = new gMap(-80.0, -100.0);
        match = false;
        for(gMap a : dSVG){
            double gmLon = a.lon;
            double gmLat = a.lat;
            if(gm4.lon == gmLon && gm4.lat == gmLat){
                match = true;
                break;
            }
        }
        assertTrue(match);

        hA.shortestItinerary.add(dA3);
        dSVG = hA.drawSVG();
        assertNotEquals(0, dSVG.size());
        gMap gm5 = new gMap(-70.0, 99.255556);
        match = false;
        for(gMap a : dSVG){
            double gmLon = a.lon;
            double gmLat = a.lat;
            if(gm5.lon == gmLon && gm5.lat == gmLat){
                match = true;
                break;
            }
        }
        assertTrue(match);

        hA.shortestItinerary.add(dA5);
        dSVG = hA.drawSVG();
        assertNotEquals(0, dSVG.size());
        gMap gm6 = new gMap(-25.28, 133.775);
        match = false;
        for(gMap a : dSVG){
            double gmLon = a.lon;
            double gmLat = a.lat;
            if(gm6.lon == gmLon && gm6.lat == gmLat){
                match = true;
                break;
            }
        }
        assertTrue(match);
    }

    // ------------------- Test Global Variable getters/setters -----------------------

    @Test
    public void testGetSetShortestItinerary() {
        LinkedHashMap<String, String> info1 = new LinkedHashMap<>();
        info1.put("extra1", "info1");
        info1.put("extra2", "info2");
        LinkedHashMap<String, String> info2 = new LinkedHashMap<>();
        info2.put("extra1", "info1");
        info2.put("extra2", "info2");
        Location startL = new Location("denver", 70, 99.255556, info1);
        Location endL = new Location("denver2", 80, 100, info2);
        Distance dA = new Distance(startL, endL, miles);
        Location startL1 = new Location("denver", -70, -99.255556, info1);
        Location endL1 = new Location("denver2", 80, 100, info2);
        Distance dA1 = new Distance(startL1, endL1, miles);
        ArrayList<Distance> distances = new ArrayList<Distance>();
        distances.add(dA);
        distances.add(dA1);

        Hub hA = new Hub();
        hA.setShortestItinerary(distances);
        assertEquals(distances, hA.shortestItinerary);
        assertEquals(distances, hA.getShortestItinerary());
    }

    @Test
    public void testGetSetMiles() {
        Hub hA = new Hub();
        hA.setMiles(false);
        assertEquals(false, hA.miles);
        assertFalse(hA.getMiles());
    }

    @Test
    public void testGetSetOptimization() {
        Hub hA = new Hub();
        hA.setOptimization("NearestNeighbor");
        assertEquals("NearestNeighbor", hA.optimization);
        assertEquals("NearestNeighbor", hA.getOptimization());
    }

    @Test
    public void testGetFinalLocations(){
        LinkedHashMap<String, String> info1 = new LinkedHashMap<>();
        info1.put("extra1", "info1");
        info1.put("extra2", "info2");
        Location startL = new Location("denver", 70, 99.255556, info1);
        Hub hA = new Hub();
        hA.finalLocations.add(startL);
        assertEquals(startL, hA.getFinalLocations().get(0));
    }

    // --------------------- Test createItinerary/finalLocationsFromWeb --------------------

    @Test
    public void testFinalLocationsFromWeb(){
        LinkedHashMap<String, String> info1 = new LinkedHashMap<>();
        info1.put("extra1", "info1");
        info1.put("extra2", "info2");
        LinkedHashMap<String, String> info2 = new LinkedHashMap<>();
        info2.put("extra1", "info1");
        info2.put("extra2", "info2");
        Hub hA =  new Hub();
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("Denver");
        arr.add("New Hampshire");
        Location startL = new Location("Denver", 70, 99.255556, info1);
        Location endL = new Location("New Hampshire", 80, 100, info2);
        hA.finalLocations.add(startL);
        hA.finalLocations.add(endL);
        hA.finalLocationsFromWeb(arr);

        assertTrue(hA.selectedLocations.contains(startL));
        assertTrue(hA.selectedLocations.contains(endL));
    }

    @Test
    public void testCreateItinerary(){
        LinkedHashMap<String, String> info1 = new LinkedHashMap<>();
        info1.put("extra1", "info1");
        info1.put("extra2", "info2");
        LinkedHashMap<String, String> info2 = new LinkedHashMap<>();
        info2.put("extra1", "info1");
        info2.put("extra2", "info2");
        Location startL = new Location("Denver", 70, 99.255556, info1);
        Location endL = new Location("New Hampshire", 80, 100, info2);
        Hub hC = new Hub();
        hC.selectedLocations.add(startL);
        hC.selectedLocations.add(endL);
        hC.finalLocations.add(startL);
        hC.finalLocations.add(endL);

        Object[][] gcds = hC.calcAllGcds(hC.selectedLocations);
        hC.optimization = "None";
        hC.createItinerary(gcds, startL);
        assertFalse(hC.shortestItinerary.isEmpty());

        hC.shortestItinerary.clear();
        hC.optimization = "NearestNeighbor";
        hC.createItinerary(gcds, startL);
        assertFalse(hC.shortestItinerary.isEmpty());

        hC.shortestItinerary.clear();
        hC.optimization = "TwoOpt";
        hC.createItinerary(gcds, startL);
        assertFalse(hC.shortestItinerary.isEmpty());

        hC.shortestItinerary.clear();
        hC.optimization = "ThreeOpt";
        hC.createItinerary(gcds, startL);
        assertFalse(hC.shortestItinerary.isEmpty());

        hC.shortestItinerary.clear();
        hC.optimization = "DefaultChoice";
        hC.createItinerary(gcds, startL);
        assertFalse(hC.shortestItinerary.isEmpty());
    }
}
