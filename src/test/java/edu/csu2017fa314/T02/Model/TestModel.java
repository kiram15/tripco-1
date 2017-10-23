package edu.csu2017fa314.T02.Model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;


public class TestModel
{
    private Model m;
    private Hub h;
    //private Distance d;
    private Location L1, L2, L3, L4;
    private ArrayList<Distance> shortTrip;

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
        Distance d = new Distance(L1, L2);
        assertEquals(466, d.computeGCD(L1, L2));
        L1.setLat(41);
        L2.setLat(37);
        Distance d2 = new Distance(L1, L2);
        assertEquals(466, d.computeGCD(L1, L2));
    }

    @Test
    public void testSameLonGCD(){
        L2.setLat(41);
        L2.setLon(-102);
        L1.setLat(37);
        L1.setLon(-102);
        Distance d = new Distance(L1, L2);
        assertEquals(276, d.computeGCD(L1, L2));
    }

    @Test
    public void testSameLatGCD(){
        L1.setLat(37);
        L2.setLat(37);
        L1.setLon(-102);
        L2.setLon(-109);
        Distance d = new Distance(L1, L2);
        assertEquals(386, d.computeGCD(L1, L2));
    }

    @Test
    public void testSameLocationGCD(){
        Distance d = new Distance(L1, L1);
        assertEquals(0, d.computeGCD(L1, L1));
    }

    @Test
    public void testReverseGCD(){
        Distance d = new Distance(L1, L2);
        assertEquals(d.computeGCD(L1, L2), d.computeGCD(L2, L1));
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

        Distance d0 = new Distance(n1, n3);
        Distance d1 = new Distance(n3, n0);
        Distance d2 = new Distance(n0, n2);
        Distance d3 = new Distance(n2, n1);

        ArrayList<Distance> checkAgainst = new ArrayList<Distance>();
        checkAgainst.add(d0);
        checkAgainst.add(d1);
        checkAgainst.add(d2);
        checkAgainst.add(d3);

        return checkAgainst;
    }
}
