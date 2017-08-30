package edu.csu2017fa314.DTR02.View;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestView
{
    private View v;

    @Before
    public void setUp() throws Exception 
    {
        v = new View();
    }

    @Test 
    public void testSetDistance() 
    {
        v.setTotalDistance(4);
        assertTrue(v.getTotalDistance() == 4);
        v.setTotalDistance(-4);
        assertTrue(v.getTotalDistance() == -4);
    }

}
