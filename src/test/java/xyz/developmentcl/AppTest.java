package xyz.developmentcl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.bukkit.Location;
import org.junit.Test;

import xyz.developmentcl.game_functions.GameFunctions;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void isPointOnSpace()
    {
        List<List<Integer>> coordinates = List.of(List.of(43, 53, -3), List.of(63, 73, 17));
        Location location = new Location(null, 53, 63, 7);

        if (GameFunctions.isPointInRectangularPrismCoordinates(coordinates, location)) {
            assertTrue( true );
        }
        else {
            assertTrue( false );
        }
    }

    @Test
    public void pointIsNotOnSpace1()
    {
        List<List<Integer>> coordinates = List.of(List.of(43, 53, -3), List.of(63, 73, 17));
        Location location = new Location(null, 33, 63, 10);

        if (!GameFunctions.isPointInRectangularPrismCoordinates(coordinates, location)) {
            assertTrue( true );
        }
        else {
            assertTrue( false );
        }
    }

    @Test
    public void pointIsNotOnSpace2()
    {
        List<List<Integer>> coordinates = List.of(List.of(43, 53, -3), List.of(63, 73, 17));
        Location location = new Location(null, 50, 63, 19);

        if (!GameFunctions.isPointInRectangularPrismCoordinates(coordinates, location)) {
            assertTrue( true );
        }
        else {
            assertTrue( false );
        }
    }
}
