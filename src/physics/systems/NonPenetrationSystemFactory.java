package physics.systems;

import framework.systems.EntitySystemFactory;
import physics.collision.structure.CollisionRepository;

/**
 * Class producing non penetration systems
 * created 12.06.16
 *
 * @author martin
 */
public class NonPenetrationSystemFactory extends EntitySystemFactory
{

    public NonPenetrationSystemFactory()
    {
        mRepo = null;
    }

    /**
     * @throws IllegalStateException if the collision repository is not set
     * @return a new non penetration system holding a reference to the repository set
     */
    public NonPenetrationSystem produce()
    {
        if (mRepo == null)
            throw new IllegalStateException("Collision repository not set, non penetration system cannot be initialized");
        NonPenetrationSystem newSystem = new NonPenetrationSystem();
        newSystem.setRepository(mRepo);
        initSystem(newSystem);
        return newSystem;
    }

    /**
     * set the collision repository needed to initialize non penetration system
     * @param repo collision repository shared with at least one sytem updating its contents
     */
    public void setRepository(CollisionRepository repo) { mRepo = repo; }


    private CollisionRepository mRepo;
}
