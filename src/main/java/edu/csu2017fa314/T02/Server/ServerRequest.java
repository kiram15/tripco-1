package edu.csu2017fa314.T02.Server;

/**
 * Created by sswensen on 10/1/17.
 * Edited for use by emcintos 10/14/17.
 */

 public class ServerRequest {
     private String request = "";
     private String description = "";
     private String unit = "";

     public ServerRequest(String request, String description, String unit) {
         this.request = request;
         this.description = description;
         this.unit = unit;
     }

     public String getRequest() {
         return this.request;
     }

     public void setQuery(String request) {
         this.request = request;
     }

     public String getDescription() {
         return description;
     }

     public String getUnit(){
         return unit;
     }

     public void setDescription(String description) {
         this.description = description;
     }

     @Override
     public String toString() {
         return "Request{" +
                 "request='" + request + '\'' +
                 ", description='" + description + '\'' +
                 ", unit='" + unit + '\'' +
                 '}';
     }
 }
