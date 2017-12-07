package edu.csu2017fa314.T02.Server;


import java.util.ArrayList;
import java.util.Arrays;
import edu.csu2017fa314.T02.Model.Distance;
import edu.csu2017fa314.T02.Model.gMap;
import edu.csu2017fa314.T02.Model.Location;

/**
 * Created by sswensen on 10/1/17.
 */

public class ServerPlanResponse {
    private String response = "plan";
    private ArrayList<Distance> trip;

    //from svgResponse so they're 'called' together
    private ArrayList<gMap> contents;
    private int width;
    private int height;

    public ServerPlanResponse(ArrayList<Distance> trip, int width, int height, ArrayList<gMap> contents) {
        this.trip = trip;
        this.contents = contents;
        this.width = width;
        this.height = height;
        //System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "ServerResponse{"
                + "response='" + response + '\''
                + ", locations=" + trip
                + '}';
    }
}
