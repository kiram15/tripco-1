package edu.csu2017fa314.T02.Server;


import java.util.ArrayList;

/**
 * Created by sswensen on 10/1/17.
 * Edited for use by emcintos 10/14/17.
 */


 public class ServerRequest {
     private String request = "";
     private ArrayList<String> description;
     private String unit = "";
     private String optSelection = "";

     public ServerRequest(String request, ArrayList<String> description, String unit, String optSelection) {
         this.request = request;
         this.description = description;
         this.unit = unit;
         this.optSelection = optSelection;
     }

     public String getRequest() {
         return this.request;
     }

     public void setQuery(String request) {
         this.request = request;
     }

     public ArrayList<String> getDescription() {
         return description;
     }

     public String getUnit(){
         return unit;
     }

     public String getOptSelection(){
         return this.optSelection;
     }

     public void setDescription(ArrayList<String> description) {
         this.description = new ArrayList<String>();
         this.description.addAll(description);
     }

     @Override
     public String toString() {
         return "Request{" +
                 "request='" + request + '\'' +
                 ", description='" + description + '\'' +
                 ", unit='" + unit + '\'' +
                 ", optSelection='" + optSelection + '\'' +
                 '}';
     }
 }
