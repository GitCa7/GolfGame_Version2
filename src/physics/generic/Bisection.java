package physics.generic;

/**
 * Template method for bisection method for a generic type T.
 * Searches for an object within an iterval delimited by a starting point and an end point
 * that is close to a crossover.
 * created 11.06.16
 *
 * @author martin
 */
public abstract class Bisection<T> implements Runnable
{
    /**
     * @param intervalStart left endpoint of interval
     * @param intervalEnd right endpoint of interval
     */
    public Bisection(T intervalStart, T intervalEnd)
    {
        mIntervalStart = intervalStart;
        mIntervalEnd = intervalEnd;
        mSolution = null;
    }

    /**
     * @throws IllegalStateException if the solver did not terminate yet
     * @return the solution found
     */
    public T getSolution()
    {
        if (mSolution == null)
            throw new IllegalStateException("the bisection solver did not run, cannot return a solution");
        return mSolution;
    }

    /**
     * @param subIntervalStart left endpoint of the current interval
     * @param subIntervalEnd right endpoint of the current interval
     * @return a point in the middle of [subIntervalStart, subIntervalEnd]
     */
    public abstract T pickMiddle(T subIntervalStart, T subIntervalEnd);

    /**
     * @param point point in interval to evaluate
     * @return -1 if point is below the value of the crossover,
     * 1 if the point is above the value of the crossover or
     * 0 if the point matches the value of the crossover exactly.
     */
    public abstract int compare(T point);

    /**
     * @param subIntervalStart left endpoint of the current interval
     * @param subIntervalEnd right endpoint of the current interval
     * @return true if the interval is sufficiently small such that its middle is considered
     * the solution. The exact solution should always be accepted.
     */
    public abstract boolean accept(T subIntervalStart, T subIntervalEnd);

    /**
     * @return if the solution is set
     */
    public boolean hasSolution() { return mSolution != null; }

    /**
     * runs the solver and sets the solution upon termination
     */
    public void run()
    {
        T left = mIntervalStart, right = mIntervalEnd;
        int leftValue = compare (mIntervalStart);
        int rightValue = compare (mIntervalEnd);

        while (!accept(left, right))
        {
            T middle = pickMiddle(left, right);
            int middleValue = compare(middle);

            if (middleValue == leftValue)
                left = middle;
            else if (middleValue == rightValue)
                right = middle;
        }

        mSolution = pickMiddle(left, right);
    }

    private T mIntervalStart, mIntervalEnd;
    private T mSolution;
}
