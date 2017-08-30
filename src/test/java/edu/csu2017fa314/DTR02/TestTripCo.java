package edu.csu2017fa314.DTR02;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestTripCo {

   private TripCo t;

   @Before
   public void setUp() throws Exception
   {
      t = new TripCo();
   }

   @Test
   public void testHelloEmpty()
   {
      assertEquals(t.getName(),"");
      assertEquals(t.getMessage(),"Hello!");
   }

   @Test
   public void testHelloWorld()
   {
      t.setName("World");
      assertEquals(t.getName(),"World");
      assertEquals(t.getMessage(),"Hello World!");
   }
}
