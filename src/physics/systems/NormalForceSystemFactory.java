package physics.systems;

import framework.EntitySystemFactory;
import physics.collision.CollisionRepository;

/**
 * Class producing normal force systems.
 * @author martin
 */
public class NormalForceSystemFactory extends EntitySystemFactory
{


    public NormalForceSystemFactory()
    {
        mRepo = null;
    }

    /**
     * Produces a new instance of normal force system having access to the collision
     * repository currently stored
     * @throws IllegalStateException if there is no collision repository set
     * @return
     */
    public NormalForceSystem produce()
    {
        if (mRepo == null)
            throw new IllegalStateException("no collision repository set, cannot produce a normal force system relying on it");
        NormalForceSystem newSystem = new NormalForceSystem();
        newSystem.setRepository(mRepo);
        return newSystem;
    }

    /**
     * sets the collision repository shared (see documentation of normal force system)
     * @param repo the shared collision repository
     */
    public void setRepository(CollisionRepository repo) { mRepo = repo; }

    private CollisionRepository mRepo;
}
