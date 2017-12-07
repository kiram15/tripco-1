package edu.csu2017fa314.T02.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Opt3 extends Hub {

    private Object[][] gcds;

    public Opt3(Object[][] gcds){
        this.gcds = gcds;
    }

    /** takes the selected location and start location
     * calculates the trip distance with that starting location and returns it
     */
    public int shortestTripDistance(ArrayList<Location> selectedLocations, Location startLocation){
        //Adjacency matrix that holds all gcds
        //Object[][] gcds = calcAllGcds(selectedLocations);

        //row is the current row in the adjancency matrix where the current location is
        int row = 0;

        //Create a huge distance to use for inital comparison
        LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
        Location bigD1 = new Location("New Zealand", -41.28650, 174.77620, info);
        Location bigD2 = new Location("Madrid", 40.41680, -3.70380, info);
        Distance hugeDistance = new Distance(bigD1, bigD2, miles);

        //temp array list to keep track of the cities we have been to
        ArrayList<Location> traveledTo = new ArrayList<Location>();
        int tripDistance = 0;
        Location currentLocation = startLocation;

        //while there are still more cities to travel to
        while (traveledTo.size() < selectedLocations.size()) {
            for (int i = 0; i < selectedLocations.size(); i++) {
                if (selectedLocations.get(i).equals(currentLocation)) {
                    row = i;
                }
            }
            traveledTo.add(currentLocation);
            if (traveledTo.size() == selectedLocations.size()) {
                break;
            }
            Distance shortestDis = hugeDistance;

            //because we aren't including initial location
            for (int i = 1; i < gcds[0].length; i++) {
                Distance dis = (Distance) gcds[row][i];
                if (!traveledTo.contains(dis.getEndId()) && (dis.getGcd() < shortestDis.getGcd())) {
                    shortestDis = dis;
                }
            }
            currentLocation = shortestDis.getEndId();
        }

        //add the distance back to the original city
        Object[] backAround = gcds[row];

        //grab the distance from the current city to original city
        Distance temp = new Distance(currentLocation, startLocation, miles);
        for (int i = 1; i < backAround.length; i++) {
            Distance dis = (Distance) backAround[i];
            //add to tripDistance
            if (temp.equals(dis)) {
                tripDistance += dis.getGcd();
            }
        }

        //apply 2opt
        checkImprovement3(traveledTo);

        //get the updated trip distance after 2opt
        ArrayList<Distance> traveledDistances = locationsToDistances(traveledTo);
        for (int i = 0; i < traveledDistances.size(); i++) {
            tripDistance += traveledDistances.get(i).getGcd();
        }

        return tripDistance;
    }

    /** master method for when user selects 2opt optimization (calls all helpers)
     * takes the shortestTrip starting point and rebuilds the trip
     * returns the shortest itinerary
     */
    public ArrayList<Distance> buildShortestTrip(
            ArrayList<Location> selectedLocations, Location startLocation) {
        //Adjacency matrix that holds all gcds
        //Object[][] gcds = calcAllGcds(selectedLocations);
        //row is the current row in the adjancency matrix where the current location is
        int row = 0;
        //Create a huge distance to use for inital comparison
        LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
        Location bigD1 = new Location("New Zealand", -41.28650, 174.77620, info);
        Location bigD2 = new Location("Madrid", 40.41680, -3.70380, info);
        Distance hugeDistance = new Distance(bigD1, bigD2, miles);
        //start final trip at the predetermined shortest trip start
        Location currentLocation = startLocation;
        ArrayList<Location> traveledToFinal = new ArrayList<Location>();
        //while there are still more cities to travel to
        while (traveledToFinal.size() < selectedLocations.size()) {
            for (int i = 0; i < selectedLocations.size(); i++) {
                if (selectedLocations.get(i).equals(currentLocation)) {
                    row = i;
                }
            }
            traveledToFinal.add(currentLocation);
            if (traveledToFinal.size() == selectedLocations.size()) {
                break;
            }
            Distance shortestDis = hugeDistance;
            for (int i = 1; i < gcds[0].length; i++) { //because we aren't including first Location
                Distance dis = (Distance) gcds[row][i];
                if (!traveledToFinal.contains(dis.getEndId()) && (dis.getGcd() < shortestDis.getGcd())) {
                    shortestDis = dis;
                }
            }
            currentLocation = shortestDis.getEndId();
        }
        //apply 3opt
        checkImprovement3(traveledToFinal);
        //convert traveledToFinal location array to a distance array
        shortestItinerary = locationsToDistances(traveledToFinal);
        return shortestItinerary;
    }

    /** determines all the possible areas that 3opt could 
    * improve in a given arraylist of locations
    */
    public ArrayList<Location> checkImprovement3(ArrayList<Location> traveled) {
        boolean improvement = true;
        //while there is still possible improvements to be made
        while (improvement) {
            improvement = false;
            // Original 3 opt method has three groups (i, i+1) (j, j+1) (k, k+1), there can
            // be many objects in between these groups, and the letters correspond to the
            // loop variables. These swaps attempt to see if switching the location objects
            // at various indicies results in an overall improvment to distance
            for (int i = 0; i <= traveled.size() - 5; i++) {
                //starts at i+2 because i, i+1, start at j
                for (int j = i + 2; j < traveled.size() - 3; j++) {
                    //starts at j+2 because i, i+1, j, j+1, start at k
                    for (int k = j + 2; k < traveled.size() - 1; k++) {
                        // SWAP 1 - reverse all of the elements between i+1 and j
                        improvement = swap1(i, j, k, traveled, improvement);

                        // SWAP 2 - reverse all of the elements between j+1 and k
                        improvement = swap2(i, j, k, traveled, improvement);

                        // SWAP 3 - reverse i+1 through k
                        improvement = swap3(i, j, k, traveled, improvement);

                        // SWAP 4 - reverse i+1 through j, then reverse j+1 through k
                        improvement = swap4(i, j, k, traveled, improvement);

                        // SWAP 5 - reverse j+1 through k, then swap segments
                        improvement = swap5(i, j, k, traveled, improvement);

                        // SWAP 6 - reverse elements from i+1 through j, then swap segments
                        improvement = swap6(i, j, k, traveled, improvement);

                        // SWAP 7 - swap middle segments
                        improvement = swap7(i, j, k, traveled, improvement);
                    }
                }
            }
        }
        return traveled;
    }

    // ------------- SWAP 1 -------------
    private boolean swap1(int i, int j, int k, ArrayList<Location> traveled, boolean improvement) {
        // (i, j) (i+1, j+1) (k, k+1)
        Distance ii1 = new Distance(traveled.get(i), traveled.get(i+1), miles);
        Distance jj1 = new Distance(traveled.get(j), traveled.get(j+1), miles);
        Distance kk1 = new Distance(traveled.get(k), traveled.get(k+1), miles);

        Distance ij = new Distance(traveled.get(i), traveled.get(j), miles);
        Distance i1j1 = new Distance(traveled.get(i+1), traveled.get(j+1), miles);

        // delta tests if the current state (i, i+1) (j, j+1) (k, k+1) is a greater
        // distance than the proposed change (i, j) (i+1, j+1) (k, k+1)
        double delta = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                + ij.getGcd() + i1j1.getGcd() + kk1.getGcd();

        // if delta < 0, than the proposed change is an improvement
        if (delta < 0) {
            super.optSwap(traveled, i + 1, j); // reverse all of the elements between i+1 and j
            improvement = true;
        }
        return improvement;
    }

    // ------------- SWAP 2 -------------
    private boolean swap2(int i, int j, int k, ArrayList<Location> traveled, boolean improvement) {
        // (i, i+1) (j, k) (j+1, k+1)

        Distance ii1 = new Distance(traveled.get(i), traveled.get(i+1), miles);
        Distance jj1 = new Distance(traveled.get(j), traveled.get(j+1), miles);
        Distance kk1 = new Distance(traveled.get(k), traveled.get(k+1), miles);

        Distance jk = new Distance(traveled.get(j), traveled.get(k), miles);
        Distance j1k1 = new Distance(traveled.get(j+1), traveled.get(k+1), miles);

        // delta tests if the current state (i, i+1) (j, j+1) (k, k+1) is a greater
        // distance than the proposed change (i, i+1) (j, k) (j+1, k+1)
        double delta = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                + ii1.getGcd() + jk.getGcd() + j1k1.getGcd();

        if (delta < 0) { //improvement?
            super.optSwap(traveled, j + 1, k); // reverse all of the elements between j+1 and k
            improvement = true;
        }
        return improvement;
    }

    // ------------- SWAP 3 -------------
    private boolean swap3(int i, int j, int k, ArrayList<Location> traveled, boolean improvement) {
        // (i, k) (j+1, j) (i+1, k+1)

        Distance ii1 = new Distance(traveled.get(i), traveled.get(i+1), miles);
        Distance jj1 = new Distance(traveled.get(j), traveled.get(j+1), miles);
        Distance kk1 = new Distance(traveled.get(k), traveled.get(k+1), miles);

        Distance ik = new Distance(traveled.get(i), traveled.get(k), miles);
        Distance i1k1 = new Distance(traveled.get(i+1), traveled.get(k+1), miles);

        // delta tests if the current state (i, i+1) (j, j+1) (k, k+1) is a greater
        // distance than the proposed change (i, k) (j+1, j) (i+1, k+1)
        double delta = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                + ik.getGcd() + jj1.getGcd() + i1k1.getGcd();

        if (delta < 0) { //improvement?
            super.optSwap(traveled, i + 1, k); //reverse i+1 through k
            improvement = true;
        }
        return improvement;
    }

    // ------------- SWAP 4 -------------
    private boolean swap4(int i, int j, int k, ArrayList<Location> traveled, boolean improvement) {
        // (i, j) (i+1, k) (j+1, k+1)

        Distance ii1 = new Distance(traveled.get(i), traveled.get(i+1), miles);
        Distance jj1 = new Distance(traveled.get(j), traveled.get(j+1), miles);
        Distance kk1 = new Distance(traveled.get(k), traveled.get(k+1), miles);

        Distance ij = new Distance(traveled.get(i), traveled.get(j), miles);
        Distance i1k = new Distance(traveled.get(i+1), traveled.get(k), miles);
        Distance j1k1 = new Distance(traveled.get(j+1), traveled.get(k+1), miles);

        // delta tests if the current state (i, i+1) (j, j+1) (k, k+1) is a greater
        // distance than the proposed change (i, j) (i+1, k) (j+1, k+1)
        double delta = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                + ij.getGcd() + i1k.getGcd() + j1k1.getGcd();

        if (delta < 0) { //improvement?
            super.optSwap(traveled, i + 1, j); //reverse i+1 through j
            super.optSwap(traveled, j + 1, k); //reverse j+1 through k
            improvement = true;
        }
        return improvement;
    }

    // ------------- SWAP 5 -------------
    private boolean swap5(int i, int j, int k, ArrayList<Location> traveled, boolean improvement) {
        // (i, k) (j+1, i+1) (j, k+1)  --- swap j+1 and k, switch two middle groups

        Distance ii1 = new Distance(traveled.get(i), traveled.get(i+1), miles);
        Distance jj1 = new Distance(traveled.get(j), traveled.get(j+1), miles);
        Distance kk1 = new Distance(traveled.get(k), traveled.get(k+1), miles);

        Distance ik = new Distance(traveled.get(i), traveled.get(k), miles);
        Distance i1j1 = new Distance(traveled.get(i+1), traveled.get(j+1), miles);
        Distance jk1 = new Distance(traveled.get(j), traveled.get(k+1), miles);

        // delta tests if the current state (i, i+1) (j, j+1) (k, k+1) is a greater
        // distance than the proposed change (i, k) (j+1, i+1) (j, k+1)
        double delta = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                + ik.getGcd() + i1j1.getGcd() + jk1.getGcd();

        if (delta < 0) { //improvement?
            super.optSwap(traveled, j + 1, k); //reverse j+1 through k
            replaceSegment(i + 1, j + 1, k, traveled); // swap segment 1 and 2
            improvement = true;
        }
        return improvement;
    }

    // ------------- SWAP 6 -------------
    private boolean swap6(int i, int j, int k, ArrayList<Location> traveled, boolean improvement) {
        // (i, j+1) (k, j) (i+1, k+1)  --- swap i+1 and j, switch two middle groups

        Distance ii1 = new Distance(traveled.get(i), traveled.get(i+1), miles);
        Distance jj1 = new Distance(traveled.get(j), traveled.get(j+1), miles);
        Distance kk1 = new Distance(traveled.get(k), traveled.get(k+1), miles);

        Distance ij1 = new Distance(traveled.get(i), traveled.get(j+1), miles);
        Distance jk = new Distance(traveled.get(j), traveled.get(k), miles);
        Distance i1k1 = new Distance(traveled.get(i+1), traveled.get(k+1), miles);

        // delta tests if the current state (i, i+1) (j, j+1) (k, k+1) is a greater
        // distance than the proposed change (i, j+1) (k, j) (i+1, k+1)
        double delta = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                + ij1.getGcd() + jk.getGcd() + i1k1.getGcd();

        if (delta < 0) { //improvement?
            super.optSwap(traveled, i+1, j); //reverse elements from i+1 through j
            replaceSegment(i + 1, j + 1, k, traveled); //swap two middle groups
            improvement = true;
        }
        return improvement;
    }

    // ------------- SWAP 7 -------------
    private boolean swap7(int i, int j, int k, ArrayList<Location> traveled, boolean improvement) {
        // (i, j+1) (k, i+1) (j, k+1)  --- switch two middle groups
        Distance ii1 = new Distance(traveled.get(i), traveled.get(i+1), miles);
        Distance jj1 = new Distance(traveled.get(j), traveled.get(j+1), miles);
        Distance kk1 = new Distance(traveled.get(k), traveled.get(k+1), miles);

        Distance ij1 = new Distance(traveled.get(i), traveled.get(j+1), miles);
        Distance i1k = new Distance(traveled.get(i+1), traveled.get(k), miles);
        Distance jk1 = new Distance(traveled.get(j), traveled.get(k+1), miles);

        // delta tests if the current state (i, i+1) (j, j+1) (k, k+1) is a greater
        // distance than the proposed change (i, j+1) (k, i+1) (j, k+1)
        double delta = -ii1.getGcd() - jj1.getGcd() - kk1.getGcd()
                + ij1.getGcd() + i1k.getGcd() + jk1.getGcd();

        if (delta < 0) { //improvement?
            replaceSegment(i + 1, j + 1, k, traveled);
            improvement = true;
        }
        return improvement;
    }

    // replaceSegment takes all of the elements from the first segment (i+1 through j)
    // and swaps it with the second segment k through j+1
    public void replaceSegment(int i1, int j1, int k, ArrayList<Location> traveled) {
        ArrayList <Location> list2 = new ArrayList<Location>();
        for(int x = k; x >= j1; x--) { //for all the elements from j+1 to k
            list2.add(traveled.remove(x)); //remove them from the original list in backwards order
        }
        for (int i = 0; i < list2.size(); i++) {
            traveled.add(i1, list2.get(i)); // add them back to this list at the i+1 index
        }
        //(i, i+1) (j, j+1) (k, k+1) BEFORE
        //(i,j+1) (k, i+1) (j, k+1) AFTER
    }
}
