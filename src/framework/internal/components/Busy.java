package framework.internal.components;

import framework.components.Component;

/**
 * component to be added to internal game state entity. Indicates whether the simulation is currently
 * unstable/changing (=> busy) or stable (=> not  busy)
 */
public class Busy implements Component
{
    /**
     * default constructor initializes busy flag to false
     */
    public Busy()
    {
        mBusy = false;
    }

    @Override
    public Component clone()
    {
        Busy newBusy = new Busy();
        newBusy.mBusy = this.mBusy;
        return newBusy;
    }

    public boolean mBusy;
}
