package framework;

/**
 * created 21.06.16
 *
 * @author martin
 */
public class FPSController
{

    public FPSController(int frames)
    {
        mFrames = frames;
        mTimePerFrame = (int) (1000.0 / frames);
        mLastTime = -1;
    }

    public void startFrame()
    {
        if (mLastTime != -1)
            throw new IllegalStateException("frame was already started");
        mLastTime = System.currentTimeMillis();
    }

    public void endFrame()
    {
        long wait = mTimePerFrame - System.currentTimeMillis() + mLastTime;
        mLastTime = -1;
        if (wait > 0)
        {
            try
            {
                Thread.sleep(wait);
            } catch (Exception e)
            {
                System.out.println("cannot sleep");
            }
        }
    }

    private int mFrames;
    private int mTimePerFrame;
    private long mLastTime;
}
