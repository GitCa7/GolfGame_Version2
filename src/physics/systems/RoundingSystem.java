package physics.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import framework.EntitySystem;
import physics.components.Position;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.constants.GlobalObjects;

/**
 * created 20.06.16
 *
 * @author martin
 */
public class RoundingSystem extends EntitySystem
{

    @Override
    public EntitySystem clone()
    {
        RoundingSystem newSystem = new RoundingSystem();
        newSystem.setPriority(priority);
        return newSystem;
    }

    /**
     * Rounds position component of all moving entities
     * @param dTime time delta
     */
    public void update(float dTime)
    {
        for (Entity update : entities())
        {
            Position position = CompoMappers.POSITION.get(update);
            position.x = (float) GlobalObjects.ROUND.roundDigits(position.x);
            position.y = (float) GlobalObjects.ROUND.roundDigits(position.y);
            position.z = (float) GlobalObjects.ROUND.roundDigits(position.z);
        }
    }

    public void addedToEngine(Engine engine)
    {
        for (Entity add : engine.getEntities())
        {
            if (Families.MOVING.matches(add))
                entities().add(add);
        }
    }

    @Override
    public void addEntity(Entity e)
    {
        if (Families.MOVING.matches(e))
            entities().add(e);
    }

    @Override
    public void removeEntity(Entity e)
    {
        if (Families.MOVING.matches(e))
            entities().remove(e);
    }
}
