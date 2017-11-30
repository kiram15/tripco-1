package edu.csu2017fa314.T02.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class NearestNeighbor extends Hub {
    
    //master method for when user selects Nearest Neighbor optimization (calls all helpers)
    public ArrayList<Distance> shortestTrip(ArrayList<Location> selectedLocations){
        //Adjacency matrix that holds all gcds
        //System.out.println("ENTERS NN SHORTEST TRIP");

        Object[][] gcds = calcAllGcds(selectedLocations);

        //keep track of the city that the shortest trip starts from
        Location shortestTripStart = selectedLocations.get(0);

        //keep track of the shortest distance
        int shortestTripDistance = 999999999;
        //row is the current row in the adjancency matrix where the current location is
        int row = 0;

        //Create a huge distance to use for inital comparison
        LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
        Location bigD1 = new Location("New Zealand", -41.28650, 174.77620, info);
        Location bigD2 = new Location("Madrid", 40.41680, -3.70380, info);
        Distance hugeDistance = new Distance(bigD1, bigD2, miles);

        //temp array list to keep track of the cities we have been to
        ArrayList<Location> traveledTo = new ArrayList<Location>();

        //for each location in the selectedLocations array list: picking a starting city
        for (Location l : selectedLocations) {
            //set the first city in the selectedLocations array list to our current location
            Location currentLocation = l;
            int tripDistance = 0;

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
                tripDistance += shortestDis.getGcd();
            }

            //add the distance back to the original city
            Object[] backAround = gcds[row];

            //grab the distance from the current city to original city
            Distance temp = new Distance(currentLocation, l, miles);
            for (int i = 1; i < backAround.length; i++) {
                Distance dis = (Distance) backAround[i];
                //add to tripDistance
                if (temp.equals(dis)) {
                    tripDistance += dis.getGcd();
                }
            }

            //making traveledTo empty again
            traveledTo = new ArrayList<Location>();

            //compare the final distance to the stored shortest distance
            if (tripDistance < shortestTripDistance) {
                //if the trip was shorter then store distance and starting city
                shortestTripDistance = tripDistance;
                shortestTripStart = l;
            }
        }

        //start final trip at the predetermined shortest trip start
        Location currentLocation = shortestTripStart;

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
                if (!traveledToFinal.contains(dis.getEndID()) && (dis.getGcd() < shortestDis.getGcd())) {
                    shortestDis = dis;
                }
            }
            currentLocation = shortestDis.getEndID();
        }

        //convert traveledToFinal location array to a distance array
        //System.out.println("IT IS: " + locationsToDistances(traveledToFinal));
        return locationsToDistances(traveledToFinal);

    }
}
