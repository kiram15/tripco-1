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
import edu.csu2017fa314.T02.Model.gMap;

import java.io.IOException;
import java.io.*;
import java.lang.reflect.Array;
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
         //another listener (for download)
         post("/download", (rec, res) -> {
             download(rec, res);
             // return the raw HttpServletResponse from the Response
             return rec.raw();
         });
     }

     // called by testing method if the client requests an svg
     /*private Object serveSvg() {
         System.out.println("calling serveSVG");
         Gson gson = new Gson();
         ArrayList<gMap> content = h.drawSVG();
         // Instead of writing the SVG to a file, we send it in plaintext back to the client to be rendered inline
         //System.out.println("*******------------******* SVG STRING: " + content + "*******------------*******");
         //ServerSvgResponse ssres = new ServerSvgResponse(120, 100, content);
         return gson.toJson(ssres, ServerSvgResponse.class);
     }*/

     // called by testing method if client requests a search
     private Object serveQuery(String searched, boolean miles, String optimization) {
         Gson gson = new Gson();
         //QueryBuilder q = new QueryBuilder("user", "pass"); // Create new QueryBuilder instance and pass in credentials //TODO update credentials
         //String queryString = String.format("SELECT * FROM airports WHERE municipality LIKE '%%%s%%' OR name LIKE '%%%s%%' OR type LIKE '%%%s%%' LIMIT 10", searched, searched, searched);
         //ArrayList<Location> queryResults = q.query(queryString);
         h.setMiles(miles);
         h.setOptimization(optimization);

         h.searchDatabase(this.user, this.password, searched, false);

         //System.out.println("after search database");
         ArrayList<Location> trip = h.getSearchedLocations();
         // Create object with svg file path and array of matching database entries to return to server
         ServerQueryResponse sRes = new ServerQueryResponse(trip); //TODO update file path to your svg, change to "./testing.png" for a sample image

         //System.out.println("Sending \"" + sRes.toString() + "\" to server.");

         //Convert response to json
         return gson.toJson(sRes, ServerQueryResponse.class);
     }

     // TODO: called by testing method if client requests a plan
     private Object servePlan(ArrayList<String> selected, boolean miles, String optimization) {
         Gson gson = new Gson();

         h.setMiles(miles);
         h.setOptimization(optimization);

         h.finalLocationsFromWeb(selected);
         //System.out.println("after search database");
         ArrayList<Distance> trip = h.getShortestItinerary();
         System.out.println("Trip: " + trip);
         // Create object with svg file path and array of matching database entries to return to server
         ArrayList<gMap> content = h.drawSVG();
         System.out.println("*******------------******* SVG STRING: " + content + "*******------------*******");
         ServerPlanResponse sRes = new ServerPlanResponse(trip, 120, 100, content); //TODO update file path to your svg, change to "./testing.png" for a sample image

         //System.out.println("Sending \"" + sRes.toString() + "\" to server.");

         //Convert response to json
         return gson.toJson(sRes, ServerPlanResponse.class);
     }

     private Object serveUpload(ArrayList<String> locations, boolean miles, String optimization){
         Gson gson = new Gson();
         System.out.println("Serving Upload: " + locations);

         String queryString = "SELECT * FROM airports WHERE ";
         for(int i = 0; i < locations.size(); ++i){
             if (i == locations.size() - 1) {
                  queryString += "code LIKE '%" + locations.get(i) + "%';";
              } else {
                  queryString += "code LIKE '%" + locations.get(i) + "%' OR ";
              }
         }


         h.setMiles(miles);
         h.setOptimization(optimization);
         h.clearFinalLocations();
         h.searchDatabase(this.user, this.password, queryString, true);
         ArrayList<Location> trip = h.getSearchedLocations();

         ServerQueryResponse sRes = new ServerQueryResponse(trip);

         return gson.toJson(sRes, ServerQueryResponse.class);
     }

     private Object download(Request rec, Response res){
         // As before, parse the request and convert it to a Java class with Gson:
         JsonParser parser = new JsonParser();
         JsonElement elm = parser.parse(rec.body());
         Gson gson = new Gson();

         ServerRequest sRec = gson.fromJson(elm, ServerRequest.class);
         //need to set different headers to write the file
         setHeadersFile(res);

         writeFile(res, sRec.getDescription());

         return res;

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
         System.out.println(elm);
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
            return serveQuery(sRec.getDescription().get(0), miles, o);
         // see if the user is looking for the map:
        }
         else if(sRec.getRequest().equals("upload")){

             return serveUpload(sRec.getDescription(),miles, o);

        }
        else if(sRec.getRequest().equals("plan")) {
            return servePlan(sRec.getDescription(), miles, o);
        }
        else {
            return null;
        }

     }

     private void writeFile(Response res, ArrayList<String> locations) {
          try {
              // Write our file directly to the response rather than to a file
              PrintWriter fileWriter = new PrintWriter(res.raw().getOutputStream());
              // Ideally, the user will be able to name their own trips. We hard code it here:
              fileWriter.println("{ \"title\" : \"The Coolest Trip\",\n" +
                      "  \"destinations\" : [");
              for (int i = 0; i < locations.size(); i++) {
                  if (i < locations.size() - 1) {
                      fileWriter.println("\"" + locations.get(i) + "\",");
                  } else {
                      fileWriter.println("\"" + locations.get(i) + "\"]}");
                  }
              }
              // Important: flush and close the writer or a blank file will be sent
              fileWriter.flush();
              fileWriter.close();

          } catch (IOException e) {
              e.printStackTrace();
          }
      }

     private void setHeaders(Response res) {
         // Declares returning type json
         res.header("Content-Type", "application/json");

         // Ok for browser to call even if different host host
         res.header("Access-Control-Allow-Origin", "*");
         res.header("Access-Control-Allow-Headers", "*");
     }

     private void setHeadersFile(Response res) {
         /* Unlike the other responses, the file request sends back an actual file. This means
          that we have to work with the raw HttpServletRequest that Spark's Response class is built
          on.
         */
          // First, add the same Access Control headers as before
          res.raw().addHeader("Access-Control-Allow-Origin", "*");
          res.raw().addHeader("Access-Control-Allow-Headers", "*");
          // Set the content type to "force-download." Basically, we "trick" the browser with
          // an unknown file type to make it download the file instead of opening it.
          res.raw().setContentType("application/force-download");
          res.raw().addHeader("Content-Disposition", "attachment; filename=\"selection.json\"");
     }

 }
