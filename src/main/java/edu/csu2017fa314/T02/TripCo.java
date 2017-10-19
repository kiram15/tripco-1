package edu.csu2017fa314.T02;
import edu.csu2017fa314.T02.Model.Distance;
import edu.csu2017fa314.T02.Model.Hub;
import edu.csu2017fa314.T02.Model.Location;
import java.util.ArrayList;
import java.io.IOException;

public class TripCo {

   private String name = "";

   public String getName() {
      return name;
   }

   public String getMessage() {
      if (name == "") {
         return "Hello!";
      } else {
         return "Hello " + name + "!";
      }
   }

   public void setName(String name) {
      this.name = name;
   }

   public static void main(String[] args) {
      System.out.println("Welcome to TripCo");

      //make a hub object
      //Hub h = new Hub();

      //call the method to read the csv file from command line/compute distances
      //method will return the array list to be passed to the writeJSON method
      h.readFile(args[0]);

      ArrayList<Distance> distances = h.shortestTrip();


      //call method to write the JSON file
      h.writeJSON(distances);
      //will now have a JSON file in root directory called Itinerary.json that can be uploaded to the web app

      try {
         //test drawsvg method
         h.drawSVG(args[1]);
      } catch (IOException e){
         System.exit(0);
      }

   }
}
