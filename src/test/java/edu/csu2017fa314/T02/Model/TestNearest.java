package edu.csu2017fa314.T02.Model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class TestNearest {
    private Model mm;
    private Hub hh;
    private Location l1;
    private Location l2;
    private ArrayList<Distance> shortTrip;
    private boolean miles = true;

    @Before
    public void setUp() throws Exception {
        mm = new Model();
        hh = new Hub();
        l1 = new Location("test", 37, -102, null);
        l2 = new Location("test2", 41, -109, null);
        shortTrip = new ArrayList<Distance>();
    }

    // ------------------- Test Shorter Trip Nearest Neighbor -------------------

    @Test
    public void testShorterTripNN(){
        //tests shorterTrip by making a call to storeColumnHeaders and
        //parseRow which then calls the shorter trip method.
        //The shorterTrip method does not return anything, but does set the value
        //of hub's shortestItinerary
        Hub h0 = new Hub("NearestNeighbor");
        h0.storeColumnHeaders("id,airports_Name,city,airports_Latitude,airports_Longitude,elevation,");
        h0.parseRow("kiram15,kira,fort collins, 45.0, 45.0, 10");
        h0.parseRow("alnolte,amber,denver, 22.5, 135.0, 10");
        h0.parseRow("nkacirek,nicole,boulder, 85.0, 175.0, 10");
        h0.parseRow("emictosh,emerson,littleton, 25.0, 90.0, 10");
        Location n0 = new Location("kira", 45.0, 45.0, null);
        h0.createItinerary(h0.calcAllGcds(h0.selectedLocations), n0);
        NearestNeighbor nn = new NearestNeighbor();
        nn.buildShortestTrip(h0.selectedLocations, n0);
        assertEquals(fillShortTripNN(), h0.shortestItinerary);
    }

    private ArrayList<Distance> fillShortTripNN(){
        Location n0 = new Location("kira", 45.0, 45.0, null);
        Location n1 = new Location("amber", 22.5, 135.0, null);
        Location n2 = new Location("nicole", 85.0, 175.0, null);
        Location n3 = new Location("emerson", 25.0, 90.0, null);

        Distance d0 = new Distance(n0, n3, miles);
        Distance d1 = new Distance(n3, n1, miles);
        Distance d2 = new Distance(n1, n2, miles);
        Distance d3 = new Distance(n2, n0, miles);

        ArrayList<Distance> checkAgainst = new ArrayList<Distance>();
        checkAgainst.add(d0);
        checkAgainst.add(d1);
        checkAgainst.add(d2);
        checkAgainst.add(d3);

        return checkAgainst;
    }
}
