package edu.csu2017fa314.T02.Model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.io.File;


public class TestModel
{
    private Model m;
    private Hub h;

    @Before
    public void setUp() throws Exception
    {
        m = new Model();
        h = new Hub();
    }

    @Test
    public void testGetNumbers()
    {
        assertArrayEquals(m.getNumbers(), new int[] {0, 1, 2, 3, 4, 5});
    }

    @Test
    public void testDiagonalGCD(){
        assertEquals(466, h.greatCirDist(37, -102, 41, -109));
        assertEquals(466, h.greatCirDist(41, -109, 37, -102));
    }

    @Test
    public void testSameLonGCD(){
        assertEquals(276, h.greatCirDist(37, -102, 41, -102));
    }

    @Test
    public void testSameLatGCD(){
        assertEquals(386, h.greatCirDist(37, -102, 37, -109));
    }

    @Test
    public void testSameLocationGCD(){
        assertEquals(0, h.greatCirDist(37, -102, 37, -102));
    }

    @Test
    public void testReverseGCD(){
        assertEquals(h.greatCirDist(37, -102, 41, -109), h.greatCirDist(41, -109, 37, -102));
    }

}
