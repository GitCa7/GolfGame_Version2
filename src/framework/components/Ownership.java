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

    public Ownership clone()
    {
        throw new UnsupportedOperationException("cloneing ownership not yet supported");
    }


    public Entity mOwner;
}
