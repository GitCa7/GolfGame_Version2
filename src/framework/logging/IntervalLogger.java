package framework.logging;


import java.util.LinkedList;
import java.util.Queue;

/**
 * Logger class running a logger until it reaches a certain size.
 * Then performs a interval logger operation action in a separate thread.
 * created 22.06.16
 *
 * @author martin
 */
public class IntervalLogger extends Logger
{

    public class Processor implements Runnable
    {
        public void run()
        {
            while (!mOperationQueue.isEmpty())
                mOperation.perform(mOperationQueue.poll());
        }
    }

    public IntervalLogger(int logSize, IntervalLoggerOperation operation)
    {
        mLogSize = logSize;
        mOperation = operation;
        mOperationQueue = new LinkedList<>();
        mProcessor = new Thread(new Processor());
    }

    @Override
    public void closeSection()
    {
        if (mLogSize < getLogSize())
        {
            mOperationQueue.add(this.clone());
            this.newLog();
            if (!mProcessor.isAlive())
                mProcessor.run();
        }
        super.closeSection();
    }

    private int mLogSize;
    private IntervalLoggerOperation mOperation;
    private Queue<Logger> mOperationQueue;
    private Thread mProcessor;
}
