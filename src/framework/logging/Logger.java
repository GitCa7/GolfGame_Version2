package framework.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Queue;

/**
 * Generic logger class storing sections of information.
 * Each section contains items. One item consists of a item name as key
 * and an item description as a value.
 * created 22.06.16
 *
 * @author martin
 */
public class Logger implements Cloneable
{

    /**
     * Opens an initial section
     */
    public Logger()
    {
        mLog = new ArrayList<>();
        mCurrentSection = new HashMap<>();
    }

    /**
     * @return a clone containing all saved sections
     */
    public Logger clone()
    {
        Logger newLogger = new Logger();
        newLogger.mLog = this.mLog;
        return newLogger;
    }

    /**
     * @param name a name used in the log
     * @return an array list storing all occurences of a description matching name. has the
     * same size as the log
     */
    public ArrayList<String> getDescriptions(String name)
    {
        ArrayList<String> descriptions = new ArrayList<>();
        for (HashMap<String, String> section : mLog)
            descriptions.add(section.get(name));
        return descriptions;
    }

    /**
     * @return the number of sections in the log
     */
    public int getLogSize() { return mLog.size(); }

    /**
     * add a new item to the current section
     * @param name the item's name
     * @param description the description mapped to name
     */
    public void addItem(String name, String description)
    {
        mCurrentSection.put(name, description);
    }

    /**
     * closes and saves a section and starts a new one
     */
    public void closeSection()
    {
        mLog.add(mCurrentSection);
        mCurrentSection = new HashMap<>();
    }

    /**
     * closes a section and starts a new one, discarding the current one
     */
    public void discardSection()
    {
        mCurrentSection = new HashMap<>();
    }

    /**
     * clears the contents of the log
     */
    public void clearLog()
    {
        mLog.clear();
    }

    /**
     * instantiates a new log resource
     */
    public void newLog()
    {
        mLog = new ArrayList<>();
    }

    /**
     * writes log to a .csv file. If the file does not exist, a new one will be created
     * @param f the file to write to
     * @param names the names to export
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void exportLog(File f, String ... names) throws IOException, FileNotFoundException
    {
        PrintWriter writer = getWriter(f);

        setUpFile(writer, names);
        exportLog(writer, names);

        writer.close();
    }

    public void exportLog(PrintWriter writer, String ... names) throws IOException
    {
        for (int cSection = 0; cSection < mLog.size(); ++cSection)
        {
            HashMap<String, String> section = mLog.get(cSection);

            writer.print(cSection + ",");

            String[] description = new String[names.length];
            for (int cItem = 0; cItem < names.length; ++cItem)
                description[cItem] = section.get(names[cItem]);
            writeLine(writer, description);
        }
    }

    public void setUpFile(PrintWriter writer, String ... names) throws IOException
    {
        writer.write("section,");
        writeLine(writer, names);
    }

    private PrintWriter getWriter(File f) throws IOException, FileNotFoundException
    {
        if (!f.exists())
            f.createNewFile();
        return new PrintWriter(f);
    }

    private void writeLine(PrintWriter writer, String[] output)
    {
        for(String out : output)
            writer.print(out + ",");
        writer.println();
    }

    private ArrayList<HashMap<String, String>> mLog;
    private HashMap<String, String> mCurrentSection;
}
