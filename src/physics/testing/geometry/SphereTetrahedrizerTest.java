package physics.testing.geometry;

import com.badlogic.gdx.math.Vector3;
import org.junit.Test;
import physics.constants.GlobalObjects;
import physics.geometry.spatial.SolidTranslator;
import physics.geometry.spatial.SphereTetrahedrizer;
import physics.geometry.spatial.Tetrahedron;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * created 14.06.16
 *
 * @author martin
 */
public class SphereTetrahedrizerTest
{

    public static final Vector3 CENTER = new Vector3();
    public static final float RADIUS = 10;

    /**
     * @param horizontalSlices an even number giving the number of horizontal steps
     * @param verticalSteps the number of vertical steps
     * @return true if the expected number of tetrahedra was produced and each tetrahedron
     * is within the sphere
     */
    public ArrayList<SolidTranslator> tetrahedrize (int horizontalSlices, int verticalSteps)
    {
        SphereTetrahedrizer tetrahedrizer = new SphereTetrahedrizer(CENTER, RADIUS);
        return tetrahedrizer.tetrahedrize(horizontalSlices / 2, verticalSteps);
    }

    /**
     * @param horizontalSlices an even number giving the number of horizontal steps
     * @param verticalSteps the number of vertical steps
     * @return true if the expected number of tetrahedra was produced
     */
    public boolean testAmountOfTetrahedra(int horizontalSlices, int verticalSteps)
    {
        int numberOfTetrahedraExpected = 2*verticalSteps*(horizontalSlices - 1);
        return tetrahedrize(horizontalSlices, verticalSteps).size() == numberOfTetrahedraExpected;
    }

    /**
    * @param horizontalSlices an even number giving the number of horizontal steps
    * @param verticalSteps the number of vertical steps
    * @return true if each tetrahedron is within the sphere
    */
    public boolean testAllWithinSphere(int horizontalSlices, int verticalSteps)
    {
        for (SolidTranslator tet : tetrahedrize(horizontalSlices, verticalSteps))
        {
            for (Vector3 vertex : tet.getVertices())
            {
                if (Math.sqrt(vertex.dot(vertex)) - GlobalObjects.ROUND.getEpsilon() > RADIUS)
                    return false;
            }
        }
        return true;
    }


    public void print(Collection<SolidTranslator> tets)
    {
        for (SolidTranslator tet : tets)
        {
            System.out.print (tet.getSolid().getClass() + " ");
            for (Vector3 vertex : tet.getVertices())
                System.out.print (vertex + ", ");
            System.out.println();
        }
    }


    @Test
    public void testPoles()
    {
        int horizontal = 2;
        int vertical = 4;
        System.out.println ("two halves");
        print (tetrahedrize(horizontal, vertical));
        assertTrue ("amount of tetrahedra needs to match formula", testAmountOfTetrahedra(horizontal, vertical));
        assertTrue ("all vertices of tetrahedra produced need to be within sphere", testAllWithinSphere(horizontal, vertical));
    }

    @Test
    public void test4()
    {
        int horizontal = 4;
        int vertical = 4;
        System.out.println ("four slices");
        print (tetrahedrize(horizontal, vertical));
        assertTrue ("amount of tetrahedra needs to match formula", testAmountOfTetrahedra(horizontal, vertical));
        assertTrue ("all vertices of tetrahedra produced need to be within sphere", testAllWithinSphere(horizontal, vertical));
    }

    @Test
    public void testInvalid()
    {
        int horizontal = 4;
        int vertical = 1;

        boolean exception = false;

        try
        {
            assertTrue(testAmountOfTetrahedra(horizontal, vertical));
            assertTrue(testAllWithinSphere(horizontal, vertical));
        }
        catch (Exception e) { exception = true; }

        assertTrue("single vertical step should not be allowed", exception);
    }
}
