package physics.testing.geometry;

import com.badlogic.gdx.math.Vector3;
import org.junit.Test;
import physics.geometry.spatial.*;

import static org.junit.Assert.assertTrue;

/**
 * created 11.06.16
 *
 * @author martin
 */
public class SolidBisectionTest
{



    @Test
    public void basicTest()
    {
        //static
        //tip at 5, 5, 5
        BoxParameter b1p = new BoxParameter(new Vector3(5, 0, 0), new Vector3(0, 5, 0), new Vector3(0, 0, 5));
        Box b1 = BoxPool.getInstance().getInstance(b1p);

        //dynamic
        //tip at 13, -1, -3
        BoxParameter b2p = new BoxParameter(new Vector3 (2, 3, 2), new Vector3(1, -2, 2), new Vector3(10, -2, -7));
        Box b2 = BoxPool.getInstance().getInstance(b2p);

        SolidTranslator s1 = new SolidTranslator (b1, new Vector3());
        //intersection vertex at 2, 3, 3 => move out by -2, 0, 0
        SolidTranslator s2 = new SolidTranslator (b2, new Vector3 (-11, 4, 6));

        double eps = .1;
        Vector3 left = new Vector3(-15, 4, 6), right = new Vector3(-11, 4, 6);
        SolidBisection biSolid = new SolidBisection(left, right, s2, s1);
        biSolid.setEpsilon(eps);
        biSolid.run();

        Vector3 expect = new Vector3(-13, 4, 6);
        Vector3 obtained = biSolid.getSolution();

        System.out.println ("solution " + obtained);

        assertTrue (Math.abs(obtained.x - expect.x) < eps * 2);
    }
}
