package testing.unit.framework;

import framework.logging.AverageLog;
import framework.logging.IntervalLogger;
import framework.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * created 22.06.16
 *
 * @author martin
 */
public class AverageLoggingTest
{

    public static <T> T[] toArray(T ... elems)
    {
        return elems;
    }

    public static void main(String[] args)
    {
        class Counter implements Generator
        {
            @Override
            public String generate()
            {
                ++mCount;
                return Integer.toString(mCount);
            }

            private int mCount = 0;
        }

        class RandomNumbers implements Generator
        {

            @Override
            public String generate()
            {
                return Double.toString(mGen.nextDouble());
            }

            private Random mGen = new Random();
        }

        class RandomLetters implements Generator
        {

            public String generate()
            {
                return Character.toString(mAlphabet.charAt(mGen.nextInt(mAlphabet.length())));
            }

            private String mAlphabet = "abcdefghijklmnopqrstuvwxyz";
            private Random mGen = new Random();
        }

        int sectionSize = 10;
        int size = 100;
        String oFile = "/home/martin/Schreibtisch/averageLoggerTest.csv";

        AverageLoggingTest test = new AverageLoggingTest(sectionSize);
        test.runTest(toArray("value", "number", "letters"), toArray(new Counter(), new RandomNumbers(), new RandomLetters()), size);
        test.export(oFile);
    }

    public interface Generator
    {
        public String generate();
    }

    public AverageLoggingTest(int interval)
    {
        mAverager = new AverageLog("value");
        mLogger = new IntervalLogger(interval, mAverager);
    }

    public void runTest(String[] names, Generator[] generators, int size)
    {
        for (int cSection = 0; cSection < size; ++cSection)
        {
            for (int cItem = 0; cItem < names.length; ++cItem)
                mLogger.addItem(names[cItem], generators[cItem].generate());
            mLogger.closeSection();
        }
    }

    public void export(String oFile)
    {
        File out = new File(oFile);
        try
        {
            mAverager.getAverageLog().exportLog(out, "value");
        }
        catch (IOException ioe)
        {
            System.out.println ("could not export test");
            ioe.printStackTrace();
        }
    }

    private Logger mLogger;
    private AverageLog mAverager;
}
