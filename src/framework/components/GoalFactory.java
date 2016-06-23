package framework.components;

import com.badlogic.gdx.math.Vector3;
import physics.geometry.spatial.Solid;
import physics.geometry.spatial.SolidTranslator;

/**
 * Class creating goal instances
 */
public class GoalFactory implements ComponentFactory
{


    public Component produce()
    {
        return new Goal(mGoalSpace);
    }

    /**
     * sets the space delimiting the goal to s translated by pos
     * @param s a given solid
     * @param pos a given position
     */
    public void setGoal(Solid s, Vector3 pos)
    {
        mGoalSpace = new SolidTranslator(s, pos);
    }



    private SolidTranslator mGoalSpace;
}
