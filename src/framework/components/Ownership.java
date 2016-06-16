package framework.components;

import com.badlogic.ashley.core.Entity;
import physics.components.Component;

/**
 * Component class modelling relation of an owner entity and an owned entity to which this component
 * is attached.
 * @author martin
 */
public class Ownership implements Component
{

    /**
     * parametric constructor
     * @param owner owner entity
     */
    public Ownership(Entity owner)
    {
        mOwner = owner;
    }

    /**
     * @return a new ownership component whose owner is set to null. Needs to be set manually.
     */
    public Ownership clone()
    {
        return new Ownership(null);
    }


    public Entity mOwner;
}
