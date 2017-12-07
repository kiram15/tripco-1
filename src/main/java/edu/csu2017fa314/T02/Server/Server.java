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

    /**server constructor
     *
     * @param user database username
     * @param pass database password
     * @param hub hub object
     */
     public Server(String user, String pass, Hub hub){
         this.user = user;
         this.password = pass;
         this.h = hub;
     }

     //creates the listeners
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
         h.setMiles(miles);
         h.setOptimization(optimization);

         h.searchDatabase(this.user, this.password, searched, false);

         //System.out.println("after search database");
         ArrayList<Location> trip = h.getSearchedLocations();
         // Create object with svg file path and array of matching database entries to return to server
         ServerQueryResponse serveRes = new ServerQueryResponse(trip);

         //Convert response to json
         return gson.toJson(serveRes, ServerQueryResponse.class);
     }

     private Object servePlan(ArrayList<String> selected, boolean miles, String optimization) {
         h.setMiles(miles);
         h.setOptimization(optimization);

         h.finalLocationsFromWeb(selected);
         //System.out.println("after search database");
         ArrayList<Distance> trip = h.getShortestItinerary();
         System.out.println("Trip: " + trip);
         // Create object with svg file path and array
         ArrayList<gMap> content = h.drawSVG();
         ServerPlanResponse serveRes = new ServerPlanResponse(trip, 120, 100, content);

         Gson gson = new Gson();
         //Convert response to json
         return gson.toJson(serveRes, ServerPlanResponse.class);
     }

     private Object serveUpload(ArrayList<String> locations, boolean miles, String optimization){
         System.out.println("Serving Upload: " + locations);

         String queryString = "select * from airports where ";
         for(int i = 0; i < locations.size(); ++i){
             if (i == locations.size() - 1) {
                  queryString += "airports.code = '" + locations.get(i) + "';";
              } else {
                  queryString += "airports.code = '" + locations.get(i) + "' or ";
              }
         }


         h.setMiles(miles);
         h.setOptimization(optimization);
         h.clearFinalLocations();
         h.searchDatabase(this.user, this.password, queryString, true);
         ArrayList<Location> trip = h.getSearchedLocations();

         ServerQueryResponse serveRes = new ServerQueryResponse(trip);

         Gson gson = new Gson();
         return gson.toJson(serveRes, ServerQueryResponse.class);
     }

     private Object download(Request rec, Response res){
         JsonParser parser = new JsonParser();
         JsonElement elm = parser.parse(rec.body());
         Gson gson = new Gson();
         ServerRequest serveRec = gson.fromJson(elm, ServerRequest.class);
         setHeadersFile(res);
         if(serveRec.getRequest().equals("saveKML")){
             ArrayList<String> allCoordinates = serveRec.getDescription();
             int half = allCoordinates.size() / 2;
             String[] lats = new String[half];
             String[] lons = new String[half];
             String[] names = new String[half];
             for(int i = 0; i < half; ++i){
                 lats[i] = allCoordinates.get(i);
             }
             int index = 0;
             for(int i = half; i < allCoordinates.size(); ++i){
                 lons[index] = allCoordinates.get(i);
                 ++index;
             }
             for(int i = 0; i < names.length; ++i){
                 names[i] = serveRec.getNames().get(i);
             }

             writeKml(res, lats, lons, names);
         }
         else{
             writeFile(res, serveRec.getDescription());
         }
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
         ServerRequest serveRec = gson.fromJson(elm, ServerRequest.class);

         // The object generated by the frontend should match whatever class you are reading into.
         // Notice how DataClass has name and ID
         // and how the frontend is generating an object with name and ID.
         System.out.println("Got \"" + serveRec.toString() + "\" from server.");
         String u0 = serveRec.getUnit();
         String o0 = serveRec.getOptSelection();



         //checks if user wants km or nah
         if(u0.equals("km")){
             miles = false;
         }

         // Because both possible requests from the client have the same format,
         // we can check the "type" of request we've received: either "query" or "svg" or "unit"
         if (serveRec.getRequest().equals("query")) {
            return serveQuery(serveRec.getDescription().get(0), miles, o0);
         // see if the user is looking for the map:
        }
         else if(serveRec.getRequest().equals("upload")){

             return serveUpload(serveRec.getDescription(),miles, o0);

        }
        else if(serveRec.getRequest().equals("plan")) {
            return servePlan(serveRec.getDescription(), miles, o0);
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
              fileWriter.println("{ \"title\" : \"The Coolest Trip\",\n"
                      + "  \"destinations\" : [");
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

      private void writeKml(Response res, String[] lats, String[] lons, String[] names){

          int latIndex = 0;
          int lonInndex = 0;
          double[] intLat = new double[lats.length];
          double[] intLon = new double[lons.length];
          for(int i = 0; i < lats.length; ++i){
              intLat[i] = Double.parseDouble(lats[i]);
              intLon[i] = Double.parseDouble(lons[i]);
          }

          try{
              PrintWriter fileWriter = new PrintWriter(res.raw().getOutputStream());

              fileWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n"
                    + "<Document>");

              for(int i = 0; i < intLon.length; ++i){
                  if(names[i].contains("&")){
                      int spec = names[i].indexOf("&");
                      names[i] = names[i].substring(0,spec) + "and" + names[i].substring(spec+1);
                  }
                  fileWriter.println("\t<Placemark> \n \t\t <name>" + names[i]
                                    + "</name>\n\t\t <Point>");
                  fileWriter.println("\t\t\t<coordinates>" + intLon[i] + ","
                                    + intLat[i] + ",0</coordinates>");
                  fileWriter.println("\t\t</Point>\n\t</Placemark>");
              }

              fileWriter.print("</Document>\n</kml>");

              fileWriter.flush();
              fileWriter.close();

          } catch(IOException e){
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
