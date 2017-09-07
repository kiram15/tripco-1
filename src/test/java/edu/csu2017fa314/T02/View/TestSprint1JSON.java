package edu.csu2017fa314.T02;
import edu.csu2017fa314.T02.Model.Distance;
import edu.csu2017fa314.T02.Model.Hub;
import edu.csu2017fa314.T02.Model.Location;
import java.util.ArrayList;

public class TestSprint1JSON {

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
      Hub h = new Hub();
      Distance d1 = new Distance("startID123", "endID456", 70000);
      Distance d2 = new Distance("startID789", "endID000", 23540000);
      Distance d3 = new Distance("startID_abc", "endID_def", 3000);
      ArrayList<Distance> test = new ArrayList<>();
      test.add(d1);
      test.add(d2);
      test.add(d3);
      h.writeJSON(test);
   }
}
