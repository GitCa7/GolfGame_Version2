package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.constants.PhysicsCoefficients;
import physics.generic.Bisection;

/**
 * Class adjusting the position of a dynamic solid intersecting with a static solid
 * such that the intersection no longer occurs. Therefore, the dynamic solid will be moved
 * along a line segment joining two positions, given that no collision occurs at one position
 * and a collision occurs at the other.
 * created 11.06.16
 *
 * @author martin
 */
public class SolidBisection extends Bisection<Vector3>
{

    public static final double DEFAULT_EPSILON = Math.pow (10, -PhysicsCoefficients.ARITHMETIC_TOLERANCE);

    /**
     * parametric constructor
     * @param leftEnd one end of line segment matching condition (=>doc)
     * @param rightEnd other end of line segment matching condition (=>doc)
     * @param dynamicSolid the solid whose position to adjust
     * @param staticSolid the solid against which to check for collisions
     */
    public SolidBisection(Vector3 leftEnd, Vector3 rightEnd, SolidTranslator dynamicSolid, SolidTranslator staticSolid)
    {
        super (leftEnd, rightEnd);
        mDynamic = dynamicSolid;
        mStatic = staticSolid;
        mCurrLeft = null;
        mCurrRight = null;
        mLastComparison = -1;
        mEpsilon = DEFAULT_EPSILON;
    }

    @Override
    /**
     * @return picks the point in the middle of on the line segment
     * joining subIntervalStart and subIntervalEnd
     */
    public Vector3 pickMiddle(Vector3 subIntervalStart, Vector3 subIntervalEnd)
    {
        mCurrLeft = subIntervalStart;
        mCurrRight = subIntervalEnd;
        //m = .5 * (s + e)
        Vector3 middle = subIntervalStart.cpy().add(subIntervalEnd);
        return middle.scl(.5f);
    }

    @Override
    /**
     * @return -1 if a collision occurs at point, otherwise returns 1
     */
    public int compare(Vector3 point)
    {
        SolidIntersection test = new SolidIntersection(mDynamic, mStatic);
        test.checkForIntersection();
        if (test.doIntersect())
            return -1;
        return 1;
    }

    @Override
    /**
     * @param point the midpoint of the current interval
     * @return true if the current interval's width is small enough and no collision
     * occurs at point
     */
    public boolean accept(Vector3 point)
    {
        if (mCurrLeft == null || mCurrRight == null)
            return false;
        return (mCurrLeft.dst(mCurrRight) < mEpsilon && mLastComparison == 1);
    }

    /**
     * sets the epsilon value, indicating the upper bound on the accepted interval width
     * @param epsilon epsilon value
     */
    public void setEpsilon(double epsilon) { mEpsilon = epsilon; }


    private SolidTranslator mDynamic, mStatic;
    private Vector3 mCurrLeft, mCurrRight;
    private int mLastComparison;
    private double mEpsilon;
}
