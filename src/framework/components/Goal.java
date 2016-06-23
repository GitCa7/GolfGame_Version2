package framework.components;

import physics.geometry.spatial.SolidTranslator;

/**
 * Class modeling a spatial goal given by a solid
 * @author martin
 */
public class Goal implements Component
{

    public Goal(SolidTranslator goalSpace)
    {
        mGoalSpace = goalSpace;
    }

    /**
     * @return a shallow copy, as the solid translator stored shall be immutable.
     */
    public Goal clone()
    {
        return new Goal(mGoalSpace);
    }

    public SolidTranslator mGoalSpace;
}
