package edu.csu2017fa314.T02.Model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class TestModel {
    private Model m;
    private Hub h;
    //private Distance d;
    private Location L1, L2, L3, L4;
    private ArrayList<Distance> shortTrip;
    private boolean miles = true;

    @Before
    public void setUp() throws Exception
    {
        m = new Model();
        h = new Hub();
        L1 = new Location("test", 37, -102, null);
        L2 = new Location("test2", 41, -109, null);
        shortTrip = new ArrayList<Distance>();
    }

    @Test
    public void testGetNumbers()
    {
        assertArrayEquals(m.getNumbers(), new int[] {0, 1, 2, 3, 4, 5});
    }

    // ------------------- Test Great Circle Distance -------------------

    @Test
    public void testDiagonalGCD(){
        Distance d = new Distance(L1, L2, miles);
        assertEquals(466, d.computeGCD(L1, L2, miles));
        L1.setLat(41);
        L2.setLat(37);
        Distance d2 = new Distance(L1, L2, miles);
        assertEquals(466, d.computeGCD(L1, L2, miles));
    }

    @Test
    public void testSameLonGCD(){
        L2.setLat(41);
        L2.setLon(-102);
        L1.setLat(37);
        L1.setLon(-102);
        Distance d = new Distance(L1, L2, miles);
        assertEquals(276, d.computeGCD(L1, L2, miles));
    }

    @Test
    public void testSameLatGCD(){
        L1.setLat(37);
        L2.setLat(37);
        L1.setLon(-102);
        L2.setLon(-109);
        Distance d = new Distance(L1, L2, miles);
        assertEquals(386, d.computeGCD(L1, L2, miles));
    }

    @Test
    public void testSameLocationGCD(){
        Distance d = new Distance(L1, L1, miles);
        assertEquals(0, d.computeGCD(L1, L1, miles));
    }

    @Test
    public void testReverseGCD(){
        Distance d = new Distance(L1, L2, miles);
        assertEquals(d.computeGCD(L1, L2, miles), d.computeGCD(L2, L1, miles));
    }

    // ----------------- Test Lat/Lon Decimal Convert -----------------

    @Test
    public void testDMSLatLon(){
        Hub h = new Hub();
        assertEquals(106.828678, h.latLonConvert("106°49'43.24\" W"), 0.01);
        assertEquals(70.009258, h.latLonConvert("70°0'33.33\" N"), 0.01);
        assertEquals(0.005556, h.latLonConvert("0°0'20\" E"), 0.01);
    }

    @Test
    public void testDMLatLon(){
        Hub h = new Hub();
        assertEquals(99.255556, h.latLonConvert("99°15.13' W"), 0.01);
        assertEquals(70, h.latLonConvert("70°0' S"), 0.01);
    }

    @Test
    public void testDLatLon(){
        Hub h = new Hub();
        assertEquals(99.25, h.latLonConvert("99.25°"), 0.01);
        assertEquals(-106.24, h.latLonConvert("-106.24"), 0.01);
    }

    // ------------------- Test Shorter Trip -------------------

    //
    //    @Test
    //    public void testShorterTrip(){
    //        //tests shorterTrip by making a call to readFile which then calls the shorter trip method
    //        //method should return an ArrayList<Distance> that contains the itinerary for the shortest trip
    //        //given the smaller_test.csv file I'm passing it, it should return the following array list:
    //        Hub h0 = new Hub();
    //        h0.readFile("smaller_test.csv");
    //        assertEquals(fillShortTrip(), h0.shortestTrip());
    //
    //    }

//    private ArrayList<Distance> fillShortTrip() {
//        Location st0 = new Location("two22 brew", h.latLonConvert("39°38'07\" N"), h.latLonConvert("104°45'32\" W"), null);
//        Location st1 = new Location("mad jacks mountain brewery", h.latLonConvert("39°24'05\" N"), h.latLonConvert("105°28'37\" W"), null);
//        Location st2 = new Location("equinox brewing", h.latLonConvert("40°35'17\" N"), h.latLonConvert("105°4'26\" W"), null);
//        Location st3 = new Location("elevation beer company", h.latLonConvert("38°31'06\" N"), h.latLonConvert("106°03'32\" W"), null);
//
//        Distance d0 = new Distance(st0, st1);
//        Distance d1 = new Distance(st1, st3);
//        Distance d2 = new Distance(st3, st2);
//        Distance d3 = new Distance(st2, st0);
//
//        ArrayList<Distance> checkAgainst = new ArrayList<Distance>();
//        checkAgainst.add(d0);
//        checkAgainst.add(d1);
//        checkAgainst.add(d2);
//        checkAgainst.add(d3);
//
//        return checkAgainst;
//    }

    @Test
    public void testShorterTrip(){
        //tests shorterTrip by making a call to storeColumnHeaders and parseRow which then calls the
        //shorter trip method. The shorterTrip method does not return anything, but does set the value
        //of hub's shortestItinerary
        Hub h0 = new Hub();
        h0.storeColumnHeaders("id,name,city,latitude,longitude,elevation,");
        h0.parseRow("kiram15,kira,fort collins, 40.0, 50.0, 10");
        h0.parseRow("alnolte,amber,denver, 60.0, 70.5, 10");
        h0.parseRow("nkacirek,nicole,boulder, 100.0, 60.0, 10");
        h0.parseRow("emictosh,emerson,littleton, 45.0, 55.0, 10");
        h0.shortestTrip();
        assertEquals(fillShortTrip(), h0.shortestItinerary);
    }

    private ArrayList<Distance> fillShortTrip(){
        Location n0 = new Location("kira", 40.0, 50.0, null);
        Location n1 = new Location("amber", 60.0, 70.5, null);
        Location n2 = new Location("nicole", 100.0, 60.0, null);
        Location n3 = new Location("emerson", 45.0, 55.0, null);

        Distance d0 = new Distance(n1, n3, miles);
        Distance d1 = new Distance(n3, n0, miles);
        Distance d2 = new Distance(n0, n2, miles);
        Distance d3 = new Distance(n2, n1, miles);

        ArrayList<Distance> checkAgainst = new ArrayList<Distance>();
        checkAgainst.add(d0);
        checkAgainst.add(d1);
        checkAgainst.add(d2);
        checkAgainst.add(d3);

        return checkAgainst;
    }


}
