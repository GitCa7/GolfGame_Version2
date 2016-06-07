package framework.components;

import com.badlogic.ashley.core.Entity;
import physics.components.ComponentFactory;

/**
 * Factory class producing ownership components
 * @author martin
 */
public class OwnershipFactory implements ComponentFactory
{

    public Ownership produce()
    {
        return new Ownership(mOwner);
    }

    public void setOwner(Entity owner)
    {
        mOwner = owner;
    }



    private Entity mOwner;
}
