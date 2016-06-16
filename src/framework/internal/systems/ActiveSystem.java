package framework.internal.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import framework.constants.CompoMappers;
import framework.constants.Families;
import framework.internal.components.Active;
import framework.EntitySystem;

import java.util.HashSet;

/**
 *  searches for turn taking entities
 *  @author martin
 */
public class ActiveSystem extends EntitySystem
{

    public ActiveSystem()
    {
        mTurnTaking = new HashSet<>();
    }

    public ActiveSystem clone()
    {
        return new ActiveSystem();
    }

    public void addedToEngine (Engine e)
    {
        for (Entity add : e.getEntitiesFor(Families.GLOBAL_STATE))
            entities().add (add);

        for (Entity add : e.getEntitiesFor(Families.TURN_TAKING))
            mTurnTaking.add (add);
    }

    @Override
    public void addEntity(Entity e)
    {
        if (Families.GLOBAL_STATE.matches(e))
            entities().add(e);
        if (Families.TURN_TAKING.matches(e))
            mTurnTaking.add(e);
    }


    public void removeEntity(Entity e)
    {
        if (Families.GLOBAL_STATE.matches(e))
            entities().remove(e);
        if (Families.TURN_TAKING.matches(e))
            mTurnTaking.remove(e);
    }

    /**
     * sets active components to false if there are no entities left taking turns
     * @param dTime
     */
    public void update (float dTime)
    {
        for (Entity global : entities())
        {
            if (mTurnTaking.size() == 0)
            {
                Active active = CompoMappers.ACTIVE.get(global);
                active.mActive = false;
            }
        }
    }

    private  HashSet<Entity> mTurnTaking;
}
