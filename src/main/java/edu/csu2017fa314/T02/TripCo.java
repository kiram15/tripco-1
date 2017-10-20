package edu.csu2017fa314.T02;
import edu.csu2017fa314.T02.Model.Distance;
import edu.csu2017fa314.T02.Model.Hub;
import edu.csu2017fa314.T02.Model.Location;
import edu.csu2017fa314.T02.Server.Server;
import java.util.ArrayList;
import java.io.IOException;

public class TripCo {

   private String name = "";

   public String getName() {
      return name;
   }

   public String getMessage() {
      if (name == "") {
         return "Hello! ";
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
      Hub h = new Hub();

      //serve!
      Server s = new Server(args[0], args[1], h);
      s.serve();

   }
}