package framework.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Logger operation saving the log contents in a file for every interval
 * created 22.06.16
 *
 * @author martin
 */
public class ChunkSaveOperation extends IntervalLoggerOperation
{

    public ChunkSaveOperation(File outputFile, String... exportNames) throws FileNotFoundException
    {
        mWriter = new PrintWriter(outputFile);
        mExportNames = exportNames;
    }

    @Override
    public void perform(Logger logger)
    {
        if (!getLockState())
        {
            setLock(true);
            try
            {
                logger.exportLog(mWriter, mExportNames);
            } catch (IOException ioe)
            {
                System.out.println("could not export");
                ioe.printStackTrace();
            }

            setLock(false);
        }
    }

    public void close()
    {
        mWriter.close();
    }

    private PrintWriter mWriter;
    private String[] mExportNames;
}
