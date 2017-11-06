package edu.csu2017fa314.T02.Server;


import java.util.ArrayList;
import java.util.Arrays;
import edu.csu2017fa314.T02.Model.Distance;
import edu.csu2017fa314.T02.Model.Hub;
import edu.csu2017fa314.T02.Model.Location;

/**
 * Created by sswensen on 10/1/17.
 */

public class ServerQueryResponse {
    private String response = "query";
    private ArrayList<Location> trip;

    public ServerQueryResponse(ArrayList<Location> trip) {

        this.trip = trip;
        //System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "response='" + response + '\'' +
                ", locations=" + trip +
                '}';
    }
}
