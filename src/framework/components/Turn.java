package framework.components;

/**
 * empty component to be added to player's whose turn it is
 * @author martin
 */
public class Turn implements Component
{
    /**
     * default constructor initializing the turn flag to false
     */
    public Turn()
    {
        mTurn = false;
    }

    @Override
    public Component clone()
    {
        Turn newTurn = new Turn();
        newTurn.mTurn = this.mTurn;
        return  newTurn;
    }


    public boolean mTurn;
}
