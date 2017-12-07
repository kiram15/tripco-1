package edu.csu2017fa314.T02.Model;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class Test3opt {
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

    // ------------------- Test Shorter Trip 3opt -------------------

    @Test
    public void testShorterTrip3Opt(){
        //tests shorterTrip3Opt by making a call to storeColumnHeaders and
        //parseRow which then calls the shorter trip method.
        // The shorterTrip method does not return anything,
        // but does set the value of hub's shortestItinerary
        Hub h0 = new Hub("ThreeOpt");
        h0.storeColumnHeaders("id,airports_name,city,airports_latitude,"
                                + "airports_longitude,elevation");
        h0.parseRow("kiram15,~kira,~fort collins,~34.0,~-92.0,~10");    //A
        h0.parseRow("alnolte,~amber,~denver,~34.0,~-88,~10");           //B
        h0.parseRow("nkacirek,~nicole,~boulder,~37.0,~-85.0,~10");      //C
        h0.parseRow("emictosh,~emerson,~littleton,~40.0,~-88.0,~10");   //D
        h0.parseRow("maddic,~maddi,~loveland,~40.0,~-92.0,~10");      //E
        h0.parseRow("jamesp,~james,~godrics hollow,~37.0,~-95.0,~10");//F
        Location dd1 = new Location("emerson", 40.0, -88.0, null);
        Object[][] gcds = h0.calcAllGcds(h0.selectedLocations);
        h0.createItinerary(gcds, dd1);
        Opt3 o3 = new Opt3(gcds);
        o3.buildShortestTrip(h0.selectedLocations, dd1);

        ArrayList<Distance> check1 = fillShortTrip3Opt();
        ArrayList<Distance> check2 = h0.shortestItinerary;

        assertEquals(fillShortTrip3Opt(), h0.shortestItinerary);
    }

    private ArrayList<Distance> fillShortTrip3Opt(){
        Location aa = new Location("kira", 34.0, -92.0, null);
        Location bb = new Location("amber", 34.0, -88.0, null);
        Location cc = new Location("nicole", 37.0, -85.0, null);
        Location dd = new Location("emerson", 40.0, -88.0, null);
        Location ee = new Location("maddi", 40.0, -92.0, null);
        Location ff = new Location("james", 37.0, -95.0, null);

        Distance dd0 = new Distance(aa, bb, miles);
        Distance dd1 = new Distance(bb, cc, miles);
        Distance dd2 = new Distance(cc, dd, miles);
        Distance dd3 = new Distance(dd, ee, miles);
        Distance dd4 = new Distance(ee, ff, miles);
        Distance dd5 = new Distance(ff, aa, miles);

        ArrayList<Distance> checkAgainst = new ArrayList<Distance>();
        checkAgainst.add(dd3);
        checkAgainst.add(dd4);
        checkAgainst.add(dd5);
        checkAgainst.add(dd0);
        checkAgainst.add(dd1);
        checkAgainst.add(dd2);

        return checkAgainst;
    }

    @Test
    public void test3Opt1(){
        Hub h0 = new Hub();
        //test where b and c should switch
        Location aa = new Location("A", 34.0, -92.0, null);
        Location bb = new Location("B", 34.0, -88.0, null);
        Location cc = new Location("C", 37.0, -85.0, null);
        Location dd = new Location("D", 40.0, -88.0, null);
        Location ee = new Location("E", 40.0, -92.0, null);
        Location ff = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkOne = new ArrayList<Location>();
        checkOne.add(aa);
        checkOne.add(cc);
        checkOne.add(bb);
        checkOne.add(dd);
        checkOne.add(ee);
        checkOne.add(ff);

        Opt3 o3 = new Opt3(h0.calcAllGcds(h0.selectedLocations));
        ArrayList<Location> after3opt = o3.checkImprovement3(checkOne);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt2(){
        Hub h0 = new Hub();
        //test where e and d should switch
        Location aa = new Location("A", 34.0, -92.0, null);
        Location bb = new Location("B", 34.0, -88.0, null);
        Location cc = new Location("C", 37.0, -85.0, null);
        Location dd = new Location("D", 40.0, -88.0, null);
        Location ee = new Location("E", 40.0, -92.0, null);
        Location ff = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkTwo = new ArrayList<Location>();
        checkTwo.add(aa);
        checkTwo.add(bb);
        checkTwo.add(cc);
        checkTwo.add(ee);
        checkTwo.add(dd);
        checkTwo.add(ff);

        Opt3 o3 = new Opt3(h0.calcAllGcds(h0.selectedLocations));
        ArrayList<Location> after3opt = o3.checkImprovement3(checkTwo);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt3(){
        Hub h0 = new Hub();
        //test where b through e should reverse
        Location aa = new Location("A", 34.0, -92.0, null);
        Location bb = new Location("B", 34.0, -88.0, null);
        Location cc = new Location("C", 37.0, -85.0, null);
        Location dd = new Location("D", 40.0, -88.0, null);
        Location ee = new Location("E", 40.0, -92.0, null);
        Location ff = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkThree = new ArrayList<Location>();
        checkThree.add(aa);
        checkThree.add(ee);
        checkThree.add(dd);
        checkThree.add(cc);
        checkThree.add(bb);
        checkThree.add(ff);

        Opt3 o3 = new Opt3(h0.calcAllGcds(h0.selectedLocations));
        ArrayList<Location> after3opt = o3.checkImprovement3(checkThree);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt4(){
        Hub h0 = new Hub();
        //test where b and c should switch, and d and e should switch
        Location aa = new Location("A", 34.0, -92.0, null);
        Location bb = new Location("B", 34.0, -88.0, null);
        Location cc = new Location("C", 37.0, -85.0, null);
        Location dd = new Location("D", 40.0, -88.0, null);
        Location ee = new Location("E", 40.0, -92.0, null);
        Location ff = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkFour = new ArrayList<Location>();
        checkFour.add(aa);
        checkFour.add(cc);
        checkFour.add(bb);
        checkFour.add(ee);
        checkFour.add(dd);
        checkFour.add(ff);

        Opt3 o3 = new Opt3(h0.calcAllGcds(h0.selectedLocations));
        ArrayList<Location> after3opt = o3.checkImprovement3(checkFour);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt5(){
        Hub h0 = new Hub();
        //test where d and e should switch, and switch two middle groups
        Location aa = new Location("A", 34.0, -92.0, null);
        Location bb = new Location("B", 34.0, -88.0, null);
        Location cc = new Location("C", 37.0, -85.0, null);
        Location dd = new Location("D", 40.0, -88.0, null);
        Location ee = new Location("E", 40.0, -92.0, null);
        Location ff = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkFive = new ArrayList<Location>();
        checkFive.add(aa);
        checkFive.add(dd);
        checkFive.add(ee);
        checkFive.add(cc);
        checkFive.add(bb);
        checkFive.add(ff);

        Opt3 o3 = new Opt3(h0.calcAllGcds(h0.selectedLocations));
        ArrayList<Location> after3opt = o3.checkImprovement3(checkFive);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt6(){
        Hub h0 = new Hub();
        //test where b and c should switch, and switch two middle groups
        Location aa = new Location("A", 34.0, -92.0, null);
        Location bb = new Location("B", 34.0, -88.0, null);
        Location cc = new Location("C", 37.0, -85.0, null);
        Location dd = new Location("D", 40.0, -88.0, null);
        Location ee = new Location("E", 40.0, -92.0, null);
        Location ff = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkSix = new ArrayList<Location>();
        checkSix.add(aa);
        checkSix.add(ee);
        checkSix.add(dd);
        checkSix.add(bb);
        checkSix.add(cc);
        checkSix.add(ff);

        Opt3 o3 = new Opt3(h0.calcAllGcds(h0.selectedLocations));
        ArrayList<Location> after3opt = o3.checkImprovement3(checkSix);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt7(){
        Hub h0 = new Hub();
        //test where b and c should switch, and switch two middle groups
        Location aa = new Location("A", 34.0, -92.0, null);
        Location bb = new Location("B", 34.0, -88.0, null);
        Location cc = new Location("C", 37.0, -85.0, null);
        Location dd = new Location("D", 40.0, -88.0, null);
        Location ee = new Location("E", 40.0, -92.0, null);
        Location ff = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkSeven = new ArrayList<Location>();
        checkSeven.add(aa);
        checkSeven.add(dd);
        checkSeven.add(ee);
        checkSeven.add(bb);
        checkSeven.add(cc);
        checkSeven.add(ff);

        Opt3 o3 = new Opt3(h0.calcAllGcds(h0.selectedLocations));
        ArrayList<Location> after3opt = o3.checkImprovement3(checkSeven);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    private ArrayList<Location> optimalShortTrip3Opt(){
        Location aa = new Location("A", 34.0, -92.0, null);
        Location bb = new Location("B", 34.0, -88.0, null);
        Location cc = new Location("C", 37.0, -85.0, null);
        Location dd = new Location("D", 40.0, -88.0, null);
        Location ee = new Location("E", 40.0, -92.0, null);
        Location ff = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkAgainst = new ArrayList<Location>();
        checkAgainst.add(aa);
        checkAgainst.add(bb);
        checkAgainst.add(cc);
        checkAgainst.add(dd);
        checkAgainst.add(ee);
        checkAgainst.add(ff);

        return checkAgainst;
    }

}
