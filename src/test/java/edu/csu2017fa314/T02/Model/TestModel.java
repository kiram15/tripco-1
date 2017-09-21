package edu.csu2017fa314.T02.Model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestModel 
{
    private Model m;

    @Before
    public void setUp() throws Exception 
    {
        m = new Model();
    }

    @Test 
    public void testGetNumbers() 
    {
        assertArrayEquals(m.getNumbers(), new int[] {0, 1, 2, 3, 4, 5});
    }

    @Test
    public void testShortestTripFrom(){
        //Location l0 = new Location("abee", h.latLonConvert("39°38'07\" N"), h.latLonConvert("104°45'32\" W"));
        //Location l1 = new Location("burtar", h.latLonConvert("40°35'22\" N"), h.latLonConvert("105°03'48\" W"));
    }
}
