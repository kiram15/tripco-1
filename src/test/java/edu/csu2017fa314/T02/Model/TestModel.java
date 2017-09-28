package edu.csu2017fa314.T02.Model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.io.File;


public class TestModel
{
    private Model m;
    private Hub h;
    //private Distance d;
    private Location L1, L2, L3, L4;

    @Before
    public void setUp() throws Exception
    {
        m = new Model();
        h = new Hub();
        L1 = new Location("test", 37, -102, null);
        L2 = new Location("test2", 41, -109, null);
    }

    @Test
    public void testGetNumbers()
    {
        assertArrayEquals(m.getNumbers(), new int[] {0, 1, 2, 3, 4, 5});
    }

    // ------------------- Test Great Circle Distance -------------------

    @Test
    public void testDiagonalGCD(){
        Distance d = new Distance(L1, L2);
        assertEquals(466, d.computeGCD(L1, L2));
        L1.setLat(41);
        L2.setLat(37);
        Distance d2 = new Distance(L1, L2);
        assertEquals(466, d.computeGCD(L1, L2));
    }

    @Test
    public void testSameLonGCD(){
        L2.setLat(41);
        L2.setLon(-102);
        L1.setLat(37);
        L1.setLon(-102);
        Distance d = new Distance(L1, L2);
        assertEquals(276, d.computeGCD(L1, L2));
    }

    @Test
    public void testSameLatGCD(){
        L1.setLat(37);
        L2.setLat(37);
        L1.setLon(-102);
        L2.setLon(-109);
        Distance d = new Distance(L1, L2);
        assertEquals(386, d.computeGCD(L1, L2));
    }

    @Test
    public void testSameLocationGCD(){
        Distance d = new Distance(L1, L1);
        assertEquals(0, d.computeGCD(L1, L1));
    }

    @Test
    public void testReverseGCD(){
        Distance d = new Distance(L1, L2);
        assertEquals(d.computeGCD(L1, L2), d.computeGCD(L2, L1));
    }

    // ------------------- Test Great Circle Distance -------------------

    @Test
    public void testDMSLatLon(){
        Hub h = new Hub();
        assertEquals(106.828678, h.latLonConvert("106°49'43.24\" W"), 0.01);
        assertEquals(70.009258, h.latLonConvert("70°0'33.33\" N"), 0.01);
        assertEquals(0.005556, h.latLonConvert("0°0'20\" E"), 0.01);
    }

    @Test
    public void testDMLatLon(){
        Hub h = new Hub();
        assertEquals(99.255556, h.latLonConvert("99°15.13' W"), 0.01);
        assertEquals(70, h.latLonConvert("70°0' S"), 0.01);
    }

    @Test
    public void testDLatLon(){
        Hub h = new Hub();
        assertEquals(99.25, h.latLonConvert("99.25°"), 0.01);
        assertEquals(-106.24, h.latLonConvert("-106.24"), 0.01);
    }
}
