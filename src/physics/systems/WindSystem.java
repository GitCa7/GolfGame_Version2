package physics.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.EntitySystem;
import physics.components.Force;
import physics.components.Wind;
import physics.constants.CompoMappers;
import physics.constants.Families;

import java.util.Random;

/**
 * Entity system class simulating wind.
 * Chooses based on uniform distribution whether to apply wind.
 * Chooses wind intensity based on normal distribution.
 * If wind occurs the wind's directed intensity is added to the total force.
 * created 21.06.16
 *
 * @author martin
 */
public class WindSystem extends EntitySystem
{

    @Override
    public EntitySystem clone()
    {
        return new WindSystem();
    }

    public void addedToEngine (Engine e)
    {
        for (Entity inEngine : e.getEntities())
        {
            if (Families.WIND_AFFECTED.matches(inEngine))
                entities().add(inEngine);
        }
    }

    @Override
    public void addEntity(Entity e)
    {
        if (Families.WIND_AFFECTED.matches(e))
            entities().add(e);
    }

    @Override
    public void removeEntity(Entity e)
    {
        if (Families.WIND_AFFECTED.matches(e))
            entities().remove(e);
    }

    public void update(float dTime)
    {
        for (Entity update : entities())
        {
            Wind wind = CompoMappers.WIND.get(update);
            //initialize wind
            if (wind.mDurationCounter == 0)
                initializeWind(wind);

            if (wind.mDurationCounter > 0)
                applyWind(wind, CompoMappers.FORCE.get(update));
        }
    }

    public void applyWind(Wind wind, Force force)
    {
        force.add (wind);
        --wind.mDurationCounter;
    }

    public Vector3 randomFloatVector(Random generator)
    {
        Vector3 v = new Vector3(generator.nextFloat(), generator.nextFloat(), generator.nextFloat());
        v.setLength(1);
        return v;
    }

    public void initializeWind(Wind wind)
    {
        if (wind.mGen.nextDouble() <= wind.mFrequency)
        {
            wind.mDurationCounter = wind.mMinDuration + wind.mGen.nextInt(wind.mMaxDuration - wind.mMinDuration);
            float windIntensity = (float) (wind.mMinMagnitude + (wind.mMaxMagnitude - wind.mMinMagnitude) * wind.mGen.nextGaussian());
            wind.set(randomFloatVector(wind.mGen).setLength(windIntensity));
        }
    }
}
