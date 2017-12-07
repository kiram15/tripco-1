package edu.csu2017fa314.T02.Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class Test2opt {
    private Model mm;
    private Hub hh;
    private Location l1;
    private Location l2;
    private ArrayList<Distance> shortTrip;
    private boolean miles = true;

    /** Executes before the tests run
    * so that they're all set up
    */
    @Before
    public void setUp() throws Exception {
        mm = new Model();
        hh = new Hub();
        l1 = new Location("test", 37, -102, null);
        l2 = new Location("test2", 41, -109, null);
        shortTrip = new ArrayList<Distance>();
    }

    // ------------------- Test Shorter Trip 2opt -------------------

    @Test
    public void testShorterTrip2Opt(){
        //tests shorterTrip by making a call to storeColumnHeaders
        // and parseRow which then calls the shorter trip method.
        // The shorterTrip method does not return anything,
        // but does set the valu of hub's shortestItinerary
        Hub h0 = new Hub("TwoOpt");
        h0.storeColumnHeaders("id,airports_name,city,airports_latitude,airports_longitude,elevation~");
        h0.parseRow("kiram15,~kira,~fort collins,~40.0,~50.0,~10");
        h0.parseRow("alnolte,~amber,~denver,~60.0,~70.5,~10");
        h0.parseRow("nkacirek,~nicole,~boulder,~100.0,~60.0,~10");
        h0.parseRow("emictosh,~emerson,~littleton,~45.0,~55.0,~10");
        Location n1 = new Location("amber", 60.0, 70.5, null);
        h0.createItinerary(h0.calcAllGcds(h0.selectedLocations), n1);
        Opt2 o2 = new Opt2();
        o2.buildShortestTrip(h0.selectedLocations, n1);
        assertEquals(fillShortTrip2Opt(), h0.shortestItinerary);
    }

    private ArrayList<Distance> fillShortTrip2Opt(){
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
