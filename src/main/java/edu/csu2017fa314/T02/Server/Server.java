package edu.csu2017fa314.T02.Server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
//import edu.csu2017fa314.DemoServer.Database.QueryBuilder;
import spark.Request;
import spark.Response;
import edu.csu2017fa314.T02.Model.Hub;
import edu.csu2017fa314.T02.Model.Distance;
import edu.csu2017fa314.T02.Model.Location;

import java.io.IOException;
import java.util.ArrayList;

import static spark.Spark.post;
/**
 * Created by sswensen on 10/1/17.
 * Edited for use by emcintos 10/14/17.
 */

 public class Server {

     private String user = "";
     private String password = "";
     private static Hub h = new Hub();

     public static void main(String[] args) {
         //Hub test = h;
         //Server s = new Server(this.user, this.password, test);
         //s.serve();
     }

     public Server(String user, String pass, Hub h){
         this.user = user;
         this.password = pass;
         this.h = h;
     }

     public void serve() {
         Gson g = new Gson();
         post("/testing", (rec, res) -> {
             return g.toJson(testing(rec, res));
         }); // Create new listener
     }

     // called by testing method if the client requests an svg
     private Object serveSvg() {
         Gson gson = new Gson();
         String content = "";
         // Instead of writing the SVG to a file, we send it in plaintext back to the client to be rendered inline
         try {
             //System.out.println("SVG Try:: " + h.shortestItinerary);
             content = h.drawSVG();
         } catch(IOException e){
             System.exit(0);
         }
         ServerSvgResponse ssres = new ServerSvgResponse(120, 100, content);

         return gson.toJson(ssres, ServerSvgResponse.class);
     }

     // called by testing method if client requests a search
     private Object serveQuery(String searched, boolean miles, String optimization) {
         Gson gson = new Gson();
         //QueryBuilder q = new QueryBuilder("user", "pass"); // Create new QueryBuilder instance and pass in credentials //TODO update credentials
         //String queryString = String.format("SELECT * FROM airports WHERE municipality LIKE '%%%s%%' OR name LIKE '%%%s%%' OR type LIKE '%%%s%%' LIMIT 10", searched, searched, searched);
         //ArrayList<Location> queryResults = q.query(queryString);
         h.setMiles(miles);
         h.setOptimization(optimization);

         h.searchDatabase(this.user, this.password, searched);

         //System.out.println("after search database");
         ArrayList<Location> trip = h.getFinalLocations();
         // Create object with svg file path and array of matching database entries to return to server
         ServerQueryResponse sRes = new ServerQueryResponse(trip); //TODO update file path to your svg, change to "./testing.png" for a sample image

         //System.out.println("Sending \"" + sRes.toString() + "\" to server.");

         //Convert response to json
         return gson.toJson(sRes, ServerQueryResponse.class);
     }
     
     // TODO: called by testing method if client requests a plan 
     private Object servePlan(String selected, boolean miles, String optimization) {
         Gson gson = new Gson();
         //QueryBuilder q = new QueryBuilder("user", "pass"); // Create new QueryBuilder instance and pass in credentials //TODO update credentials
         //String queryString = String.format("SELECT * FROM airports WHERE municipality LIKE '%%%s%%' OR name LIKE '%%%s%%' OR type LIKE '%%%s%%' LIMIT 10", searched, searched, searched);
         //ArrayList<Location> queryResults = q.query(queryString);
         h.setMiles(miles);
         h.setOptimization(optimization);

         h.finalLocationsFromWeb(selected);

         //System.out.println("after search database");
         ArrayList<Distance> trip = h.getShortestItinerary();
         // Create object with svg file path and array of matching database entries to return to server
         ServerPlanResponse sRes = new ServerPlanResponse(trip); //TODO update file path to your svg, change to "./testing.png" for a sample image

         //System.out.println("Sending \"" + sRes.toString() + "\" to server.");

         //Convert response to json
         return gson.toJson(sRes, ServerPlanResponse.class);
     }

     private Object testing(Request rec, Response res) {
         // Set the return headers
         setHeaders(res);

         //sets unit boolean to default units (miles)
         boolean miles = true;

         // Init json parser
         JsonParser parser = new JsonParser();

         // Grab the json body from POST
         JsonElement elm = parser.parse(rec.body());

         // Create new Gson (a Google library for creating a JSON representation of a java class)
         Gson gson = new Gson();

         // Create new Object from received JsonElement elm
         // Note that both possible requests have the same format (see app.js)
         ServerRequest sRec = gson.fromJson(elm, ServerRequest.class);

         // The object generated by the frontend should match whatever class you are reading into.
         // Notice how DataClass has name and ID and how the frontend is generating an object with name and ID.
         System.out.println("Got \"" + sRec.toString() + "\" from server.");
         String u = sRec.getUnit();
         String o = sRec.getOptSelection();

         //checks if user wants km or nah
         if(u.equals("km")){
             miles = false;
         }

         // Because both possible requests from the client have the same format,
         // we can check the "type" of request we've received: either "query" or "svg" or "unit"
         if (sRec.getRequest().equals("query")) {
            return serveQuery(sRec.getDescription(), miles, o);
         // see if the user is looking for the map:
        } 
        else if(sRec.getRequest().equals("plan")) {
            return servePlan(sRec.getDescription(), miles, o);
        }
        else {
            return serveSvg();
        }

     }

     private void setHeaders(Response res) {
         // Declares returning type json
         res.header("Content-Type", "application/json");

         // Ok for browser to call even if different host host
         res.header("Access-Control-Allow-Origin", "*");
         res.header("Access-Control-Allow-Headers", "*");
     }
 }
