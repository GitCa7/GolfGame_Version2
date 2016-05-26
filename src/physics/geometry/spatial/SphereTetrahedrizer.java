package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.generic.Parameter;

import java.util.ArrayList;

/**
 * generates tetrahedra from a sphere by cutting the sphere into layers and connecting
 * the center to a triangle approximating the surface in a "zig-zag" fashion.
 * @author martin
 */
public class SphereTetrahedrizer
{
    /**
     * parametric constructor
     * @param center center of shpere
     * @param radius radius of shpere
     */
    public SphereTetrahedrizer(Vector3 center, float radius)
    {
        mCenter = center;
        mRadius = radius;
    }

    /**
     * @param halfVerticalSteps the number of steps from the pole to equator
     * @param horizontalSteps the number of points generated at the equator
     * @return list of tetrahedra approximating the ball. for each tetrahedron, one vertex is the center
     */
    public ArrayList<Tetrahedron> tetrahedrize(int halfVerticalSteps, int horizontalSteps)
    {
        if (horizontalSteps - halfVerticalSteps + 1 < 3)
            throw new IllegalArgumentException("too few horizontal steps, won't give tetrahedron at topmost layer");

        ArrayList<Tetrahedron> tetrahedra = new ArrayList<>();

        ArrayList<Vector3> northPole = null, northEquator = null;
        ArrayList<Vector3> southPole = null, southEquator = null;

        for (int nVertical = halfVerticalSteps - 1; nVertical >= 0; --nVertical)
        {
            double verticalAngle = nVertical * Math.PI / halfVerticalSteps;
            Vector3 northCenter = new Vector3 (mCenter.x, mCenter.y, (float) (mCenter.z - mRadius * Math.sin(verticalAngle)));
            Vector3 southCenter = new Vector3 (mCenter.x, mCenter.y, (float) (mCenter.z + mRadius * Math.sin(verticalAngle)));

            double circleRadius = mRadius * Math.cos (verticalAngle);

            boolean uneven = (nVertical % 2) == 1;

            northEquator = generateHorizontalPoints (northCenter, circleRadius, horizontalSteps, uneven);
            southEquator = generateHorizontalPoints (southCenter, circleRadius, horizontalSteps, uneven);

            tetrahedra.addAll (generateTetrahedra (northPole, northEquator, Half.NORTH));
            tetrahedra.addAll (generateTetrahedra (southPole, southEquator, Half.SOUTH));

            northPole = northEquator;
            southPole = southEquator;

        }

        return tetrahedra;
    }


    private enum Half {NORTH, SOUTH};

    /**
     *
     * @param poleList list of points closer to respective pole
     * @param equatorList list of points closer to equator
     * @return list of tetrahedra generated from both lists
     */
    private ArrayList<Tetrahedron> generateTetrahedra (ArrayList<Vector3> poleList, ArrayList<Vector3> equatorList, Half half)
    {
        assert (poleList == null || poleList.size() - equatorList.size() == 0);

        ArrayList<Tetrahedron> tetrahedra = new ArrayList<>();

        if (poleList == null)
        {
            Vector3 pole = mCenter.cpy();
            if (half == Half.NORTH)
                pole = new Vector3 (mCenter.x, mCenter.y, mCenter.z - mRadius);
            else
                pole = new Vector3 (mCenter.x, mCenter.y, mCenter.z + mRadius);

            for (int cPoints = 0; cPoints < equatorList.size(); ++cPoints)
            {
                Parameter<Tetrahedron> tp = new TetrahedronParameter(mCenter, pole, equatorList.get(cPoints), equatorList.get ((cPoints + 1) % equatorList.size()));
                tetrahedra.add (TetrahedronPool.getInstance().getInstance(tp));
            }

        }
        else
        {
            for (int cPoints = 0; cPoints < equatorList.size(); ++cPoints)
            {
                Vector3 currEquator, currPole, nextEquator, nextPole;
                currEquator = equatorList.get(cPoints);
                currPole = poleList.get(cPoints);
                nextEquator = equatorList.get ((cPoints + 1) % equatorList.size());
                nextPole = poleList.get ((cPoints + 1) % poleList.size());

                TetrahedronParameter tp1 = new TetrahedronParameter (mCenter, currEquator, nextEquator, currPole);
                TetrahedronParameter tp2 = new TetrahedronParameter (mCenter, nextEquator, currPole, nextPole);
                tetrahedra.add (TetrahedronPool.getInstance().getInstance(tp1));
                tetrahedra.add (TetrahedronPool.getInstance().getInstance(tp2));
            }
        }

        return tetrahedra;
    }

    /**
     * @param circleCenter center of circle for which to generate points
     * @param circleRadius radius of circle on which to generate points
     * @param numberOfPoints the number of points for this horizontal circle
     * @param uneven if true, the points will be generated with an offset of pi / numberOfPoints
     * @return a list of equally spaced points on a circle parallel to the x/y plane and specified
     * by the given center and radius
     */
    private ArrayList<Vector3> generateHorizontalPoints (Vector3 circleCenter, double circleRadius, int numberOfPoints, boolean uneven)
    {
        ArrayList<Vector3> points = new ArrayList<>();

        double angleOffset = uneven ? Math.PI / numberOfPoints : 0;

        for (int nPoint = 0; nPoint < numberOfPoints; ++nPoint)
        {
            double angle = angleOffset + 2*Math.PI / numberOfPoints;
            float x = (float) (circleRadius * Math.sin (angle));
            float y = (float) (circleRadius * Math.cos (angle));

            points.add (new Vector3 (circleCenter.x + x, circleCenter.y + y, circleCenter.z));
        }

        return points;
    }


    private Vector3 mCenter;
    private float mRadius;
}
