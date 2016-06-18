package physics.systems;

import framework.EntitySystem;
import framework.EntitySystemFactory;
import physics.collision.CollisionRepository;

/**
 * class producing friction systems
 * @author martin
 */
public class FrictionSystemFactory extends EntitySystemFactory
{

    public FrictionSystemFactory()
    {
        mRepo = null;
    }

    @Override
    /**
     * @throws IllegalStateException if no collision repository was set
     * @return a new friction system
     */
    public EntitySystem produce()
    {
        if (mRepo == null)
            throw new IllegalStateException("cannot initialize friction system, collision repository is not set");
        FrictionSystem f = new FrictionSystem();
        f.setRepository(mRepo);
        initSystem(f);
        return f;
    }

    /**
     * Set the collision repository shared with another object updating its information.
     * This repository will be set for every system produced.
     * @param repo the repository
     */
    public void setRepository(CollisionRepository repo)
    {
        mRepo = repo;
    }


    private CollisionRepository mRepo;
}
