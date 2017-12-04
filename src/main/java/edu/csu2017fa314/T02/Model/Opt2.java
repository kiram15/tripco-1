package edu.csu2017fa314.T02.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Opt2 extends Hub {

    //method that will return a distance for a single location
    public int shortestTripDistance(ArrayList<Location> selectedLocations, Location startLocation){
        //Adjacency matrix that holds all gcds
        Object[][] gcds = calcAllGcds(selectedLocations);

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
                if (!traveledTo.contains(dis.getEndID()) && (dis.getGcd() < shortestDis.getGcd())) {
                    shortestDis = dis;
                }
            }
            currentLocation = shortestDis.getEndID();
        }

        //add the distance back to the original cit
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
        checkImprovement2(traveledTo);

        //get the updated trip distance after 2opt
        ArrayList<Distance> traveledDistances = locationsToDistances(traveledTo);
        for (int i = 0; i < traveledDistances.size(); i++) {
            tripDistance += traveledDistances.get(i).getGcd();
        }

        return tripDistance;
    }

    //master method for when user selects 2opt optimization (calls all helpers)
    public ArrayList<Distance> buildShortestTrip(
            ArrayList<Location> selectedLocations, Location startLocation) {
        //Adjacency matrix that holds all gcds
        Object[][] gcds = calcAllGcds(selectedLocations);
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
            //because we aren't including first Location
            for (int i = 1; i < gcds[0].length; i++) {
                Distance dis = (Distance) gcds[row][i];
                if (!traveledToFinal.contains(dis.getEndID()) && (dis.getGcd() < shortestDis.getGcd())) {
                    shortestDis = dis;
                }
            }
            currentLocation = shortestDis.getEndID();
        }

        //apply 2opt
        checkImprovement2(traveledToFinal);
        //convert traveledToFinal location array to a distance array
        shortestItinerary = locationsToDistances(traveledToFinal);
        return shortestItinerary;
    }

    //determines all the possible areas that 2opt could improve in a given arraylist of locations
    public void checkImprovement2(ArrayList<Location> traveled) {
        boolean improvement = true;
        //while there is still possible improvements to be made
        while (improvement) {
            improvement = false;
            for (int i = 0; i <= traveled.size() - 3; i++) { // check n>4
                for (int k = i + 2; k < traveled.size() - 1; k++) {
                    Distance ii1 = new Distance(traveled.get(i), traveled.get(i + 1), miles);
                    Distance kk1 = new Distance(traveled.get(k), traveled.get(k + 1), miles);
                    Distance ik = new Distance(traveled.get(i), traveled.get(k), miles);
                    Distance i1k1 = new Distance(traveled.get(i + 1), traveled.get(k + 1), miles);
                    double delta = (-ii1.getGcd()) - kk1.getGcd() + ik.getGcd() + i1k1.getGcd();
                    if (delta < 0) { //improvement?
                        super.optSwap(traveled, i + 1, k);
                        improvement = true;
                    }
                }
            }
        }
    }
}
