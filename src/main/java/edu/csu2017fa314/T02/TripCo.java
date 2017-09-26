package edu.csu2017fa314.T02;
import edu.csu2017fa314.T02.Model.Distance;
import edu.csu2017fa314.T02.Model.Hub;
import edu.csu2017fa314.T02.Model.Location;
import java.util.ArrayList;

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

//      System.out.println("Testing Distance .equals method");
//      Distance d0 = new Distance(new Location("Boulder", 25.0, 50.75, null), new Location("Denver", 100.0, 80.0, null), 75);
//      Distance d1 = new Distance(new Location("Boulder", 25.0, 50.75, null), new Location("Fort Collins", 100.0, 80.0, null), -5);
//
//      System.out.println("Call to .equals: " + d0.equals(d1));

      //make a hub object
      Hub h = new Hub();

//      //call the method to read the csv file from command line/compute distances
//      //method will return the array list to be passed to the writeJSON method
      ArrayList<Distance> distances = h.readFile(args[0]);
//
//      //call method to write the JSON file
      h.writeJSON(distances);
      //will now have a JSON file in root directory called Itinerary.json that can be uploaded to the web app
   }
}
