package framework.logging;

/**
 * created 22.06.16
 *
 * @author martin
 */
public abstract class IntervalLoggerOperation
{

    public IntervalLoggerOperation()
    {
        mLock = false;
    }

    public boolean getLockState()
    {
        return mLock;
    }

    public abstract void perform(Logger logger);

    public void setLock(boolean flag)
    {
        mLock = flag;
    }

    private boolean mLock;
}
