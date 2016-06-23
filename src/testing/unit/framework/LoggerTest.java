package testing.unit.framework;

import framework.logging.Logger;

import java.io.File;
import java.util.Random;

/**
 * created 22.06.16
 *
 * @author martin
 */
public class LoggerTest
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

        int size = 10;
        String oFile = "/home/martin/Schreibtisch/loggerTest.csv";

        LoggerTest test = new LoggerTest();
        test.run(toArray("count", "numbers", "letters"), toArray(new Counter(), new RandomNumbers(), new RandomLetters()), 10);
        test.export(oFile, toArray("letters", "numbers", "count"));
    }

    public interface Generator
    {
        public String generate();
    }

    public LoggerTest()
    {
        mLogger = new Logger();
    }


    public void run(String[] names, Generator[] generators, int size)
    {
        Random gen = new Random();

        for (int cSection = 0; cSection < size; ++cSection)
        {
            for (int cGenerator = 0; cGenerator < generators.length; ++cGenerator)
                mLogger.addItem(names[cGenerator], generators[cGenerator].generate());
            mLogger.closeSection();
        }
    }

    public void export (String fileName, String[] names)
    {
        File exportFile = new File(fileName);
        try
        {
            mLogger.exportLog(exportFile, names);
        }
        catch(Exception e)
        {
            System.out.println("something went wrong while writing");
            e.printStackTrace();
        }
    }


    private Logger mLogger;
}
