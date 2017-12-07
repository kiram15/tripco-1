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

   /**
    * returns the message global
    */
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

   /** The main method to the whole backend code.
   * Creates the server, and a hub object to execute
   * all actions required
   */
   public static void main(String[] args) {
      System.out.println("Welcome to TripCo");

      //make a hub object
      Hub hub = new Hub();

      //serve!
      Server server = new Server(args[0], args[1], hub);
      server.serve();

   }
}
