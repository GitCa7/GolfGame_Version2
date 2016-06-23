package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.constants.GlobalObjects;
import physics.constants.PhysicsCoefficients;
import physics.generic.numerical.Bisection;

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

    public static final double DEFAULT_EPSILON = Math.pow (10, -PhysicsCoefficients.ARITHMETIC_PRECISION);
    public static final double DEFAULT_QUIT_EPSILON = Math.pow(10, -PhysicsCoefficients.MAXIMUM_ARITHMETIC_PRECISION);

    /**
     * parametric constructor
     * @param leftEnd one end of line segment matching condition (=>doc)
     * @param rightEnd other end of line segment matching condition (=>doc)
     * @param dynamicSolid the solid whose position to adjust. THIS OBJECT WILL BE MODIFIED!
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
        mQuitEpsilon = DEFAULT_QUIT_EPSILON;
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
        mDynamic.setPosition(point);

        SolidIntersection test = new SolidIntersection(mDynamic, mStatic);
        test.checkForIntersection();
        if (test.doIntersect())
            mLastComparison = -1;
        else
            mLastComparison = 1;
        return mLastComparison;
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

        boolean distanceRule = mCurrLeft.dst(mCurrRight) < mEpsilon;
        boolean comparisonRule = mLastComparison == 1;
        boolean strictDistanceRule = mCurrLeft.dst(mCurrRight) < mQuitEpsilon;
     //
        //   boolean equalityRule = GlobalObjects.ROUND.epsilonEquals(mCurrLeft.dst(mCurrRight), 0f);
                //mCurrLeft.epsilonEquals(mCurrRight, (float) GlobalObjects.ROUND.getEpsilon());

        return (strictDistanceRule || (distanceRule && comparisonRule));
    }

    /**
     * @param left the left end point
     * @param right the right end point
     * @return true if the distance between left, right is below the maximum precision set
     */
    public boolean terminate(Vector3 left, Vector3 right)
    {
        return mCurrLeft.dst(mCurrRight) < mQuitEpsilon;
    }

    /**
     * sets the epsilon value, indicating the upper bound on the accepted interval width
     * @param epsilon epsilon value
     */
    public void setEpsilon(double epsilon) { mEpsilon = epsilon; }


    private SolidTranslator mDynamic, mStatic;
    private Vector3 mCurrLeft, mCurrRight;
    private int mLastComparison;
    private double mEpsilon, mQuitEpsilon;
}
