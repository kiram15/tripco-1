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
        h0.storeColumnHeaders("id,airports_Name,city,airports_Latitude,airports_Longitude,elevation,");
        h0.parseRow("kiram15,kira,fort collins, 34.0, -92.0, 10");    //A
        h0.parseRow("alnolte,amber,denver, 34.0, -88, 10");           //B
        h0.parseRow("nkacirek,nicole,boulder, 37.0, -85.0, 10");      //C
        h0.parseRow("emictosh,emerson,littleton, 40.0, -88.0, 10");   //D
        h0.parseRow("maddic, maddi, loveland, 40.0, -92.0, 10");      //E
        h0.parseRow("jamesp, james, godrics hollow, 37.0, -95.0, 10");//F
        Location d = new Location("emerson", 40.0, -88.0, null);
        h0.createItinerary(d);
        Opt3 o3 = new Opt3();
        o3.buildShortestTrip(h0.selectedLocations, d);

        ArrayList<Distance> check1 = fillShortTrip3Opt();
        ArrayList<Distance> check2 = h0.shortestItinerary;

        assertEquals(fillShortTrip3Opt(), h0.shortestItinerary);
    }

    private ArrayList<Distance> fillShortTrip3Opt(){
        Location a = new Location("kira", 34.0, -92.0, null);
        Location b = new Location("amber", 34.0, -88.0, null);
        Location c = new Location("nicole", 37.0, -85.0, null);
        Location d = new Location("emerson", 40.0, -88.0, null);
        Location e = new Location("maddi", 40.0, -92.0, null);
        Location f = new Location("james", 37.0, -95.0, null);

        Distance d0 = new Distance(a, b, miles);
        Distance d1 = new Distance(b, c, miles);
        Distance d2 = new Distance(c, d, miles);
        Distance d3 = new Distance(d, e, miles);
        Distance d4 = new Distance(e, f, miles);
        Distance d5 = new Distance(f, a, miles);

        ArrayList<Distance> checkAgainst = new ArrayList<Distance>();
        checkAgainst.add(d3);
        checkAgainst.add(d4);
        checkAgainst.add(d5);
        checkAgainst.add(d0);
        checkAgainst.add(d1);
        checkAgainst.add(d2);

        return checkAgainst;
    }

    @Test
    public void test3Opt1(){
        Hub h0 = new Hub();
        //test where b and c should switch
        Location a = new Location("A", 34.0, -92.0, null);
        Location b = new Location("B", 34.0, -88.0, null);
        Location c = new Location("C", 37.0, -85.0, null);
        Location d = new Location("D", 40.0, -88.0, null);
        Location e = new Location("E", 40.0, -92.0, null);
        Location f = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkOne = new ArrayList<Location>();
        checkOne.add(a);
        checkOne.add(c);
        checkOne.add(b);
        checkOne.add(d);
        checkOne.add(e);
        checkOne.add(f);

        Opt3 o3 = new Opt3();
        ArrayList<Location> after3opt = o3.checkImprovement3(checkOne);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt2(){
        Hub h0 = new Hub();
        //test where e and d should switch
        Location a = new Location("A", 34.0, -92.0, null);
        Location b = new Location("B", 34.0, -88.0, null);
        Location c = new Location("C", 37.0, -85.0, null);
        Location d = new Location("D", 40.0, -88.0, null);
        Location e = new Location("E", 40.0, -92.0, null);
        Location f = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkTwo = new ArrayList<Location>();
        checkTwo.add(a);
        checkTwo.add(b);
        checkTwo.add(c);
        checkTwo.add(e);
        checkTwo.add(d);
        checkTwo.add(f);

        Opt3 o3 = new Opt3();
        ArrayList<Location> after3opt = o3.checkImprovement3(checkTwo);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt3(){
        Hub h0 = new Hub();
        //test where b through e should reverse
        Location a = new Location("A", 34.0, -92.0, null);
        Location b = new Location("B", 34.0, -88.0, null);
        Location c = new Location("C", 37.0, -85.0, null);
        Location d = new Location("D", 40.0, -88.0, null);
        Location e = new Location("E", 40.0, -92.0, null);
        Location f = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkThree = new ArrayList<Location>();
        checkThree.add(a);
        checkThree.add(e);
        checkThree.add(d);
        checkThree.add(c);
        checkThree.add(b);
        checkThree.add(f);

        Opt3 o3 = new Opt3();
        ArrayList<Location> after3opt = o3.checkImprovement3(checkThree);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt4(){
        Hub h0 = new Hub();
        //test where b and c should switch, and d and e should switch
        Location a = new Location("A", 34.0, -92.0, null);
        Location b = new Location("B", 34.0, -88.0, null);
        Location c = new Location("C", 37.0, -85.0, null);
        Location d = new Location("D", 40.0, -88.0, null);
        Location e = new Location("E", 40.0, -92.0, null);
        Location f = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkFour = new ArrayList<Location>();
        checkFour.add(a);
        checkFour.add(c);
        checkFour.add(b);
        checkFour.add(e);
        checkFour.add(d);
        checkFour.add(f);

        Opt3 o3 = new Opt3();
        ArrayList<Location> after3opt = o3.checkImprovement3(checkFour);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt5(){
        Hub h0 = new Hub();
        //test where d and e should switch, and switch two middle groups
        Location a = new Location("A", 34.0, -92.0, null);
        Location b = new Location("B", 34.0, -88.0, null);
        Location c = new Location("C", 37.0, -85.0, null);
        Location d = new Location("D", 40.0, -88.0, null);
        Location e = new Location("E", 40.0, -92.0, null);
        Location f = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkFive = new ArrayList<Location>();
        checkFive.add(a);
        checkFive.add(d);
        checkFive.add(e);
        checkFive.add(c);
        checkFive.add(b);
        checkFive.add(f);

        Opt3 o3 = new Opt3();
        ArrayList<Location> after3opt = o3.checkImprovement3(checkFive);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt6(){
        Hub h0 = new Hub();
        //test where b and c should switch, and switch two middle groups
        Location a = new Location("A", 34.0, -92.0, null);
        Location b = new Location("B", 34.0, -88.0, null);
        Location c = new Location("C", 37.0, -85.0, null);
        Location d = new Location("D", 40.0, -88.0, null);
        Location e = new Location("E", 40.0, -92.0, null);
        Location f = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkSix = new ArrayList<Location>();
        checkSix.add(a);
        checkSix.add(e);
        checkSix.add(d);
        checkSix.add(b);
        checkSix.add(c);
        checkSix.add(f);

        Opt3 o3 = new Opt3();
        ArrayList<Location> after3opt = o3.checkImprovement3(checkSix);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }

    @Test
    public void test3Opt7(){
        Hub h0 = new Hub();
        //test where b and c should switch, and switch two middle groups
        Location a = new Location("A", 34.0, -92.0, null);
        Location b = new Location("B", 34.0, -88.0, null);
        Location c = new Location("C", 37.0, -85.0, null);
        Location d = new Location("D", 40.0, -88.0, null);
        Location e = new Location("E", 40.0, -92.0, null);
        Location f = new Location("F", 37.0, -95.0, null);

        ArrayList<Location> checkSeven = new ArrayList<Location>();
        checkSeven.add(a);
        checkSeven.add(d);
        checkSeven.add(e);
        checkSeven.add(b);
        checkSeven.add(c);
        checkSeven.add(f);

        Opt3 o3 = new Opt3();
        ArrayList<Location> after3opt = o3.checkImprovement3(checkSeven);
        assertEquals(optimalShortTrip3Opt(), after3opt);
    }


    private ArrayList<Location> optimalShortTrip3Opt(){
        Location a = new Location("A", 34.0, -92.0, null);
        Location b = new Location("B", 34.0, -88.0, null);
        Location c = new Location("C", 37.0, -85.0, null);
        Location d = new Location("D", 40.0, -88.0, null);
        Location e = new Location("E", 40.0, -92.0, null);
        Location f = new Location("F", 37.0, -95.0, null);


        ArrayList<Location> checkAgainst = new ArrayList<Location>();
        checkAgainst.add(a);
        checkAgainst.add(b);
        checkAgainst.add(c);
        checkAgainst.add(d);
        checkAgainst.add(e);
        checkAgainst.add(f);

        return checkAgainst;
    }

}
