package framework.logging;

import java.util.ArrayList;

/**
 * created 22.06.16
 *
 * @author martin
 */
public class AverageLog extends IntervalLoggerOperation
{

    /**
     * @param logNames names for which to average the item's description which shall be a number
     */
    public AverageLog(String ... logNames)
    {
        mAverageLog = new Logger();
        mAverageNames = logNames;
    }

    public Logger getAverageLog()
    {
        return mAverageLog;
    }

    @Override
    public void perform(Logger logger)
    {
        if (!getLockState())
        {
            setLock(true);

            for (String name : mAverageNames)
            {
                double average = getAverage(logger.getDescriptions(name));
                mAverageLog.addItem(name, Double.toString(average));
            }
            mAverageLog.closeSection();

            setLock(false);
        }
    }


    private double getAverage(ArrayList<String> descriptionValues)
    {
        double average = 0;
        for(String value : descriptionValues)
            average += Double.parseDouble(value);
        return average / descriptionValues.size();
    }


    private Logger mAverageLog;
    private String[] mAverageNames;
}
