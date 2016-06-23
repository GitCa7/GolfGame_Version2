package physics.systems;

import framework.systems.EntitySystemFactory;
import physics.collision.detection.CollisionDetector;
import physics.collision.structure.CollisionRepository;

/** Creates CollisionDetectionSystemFactory
 * Created by marcel on 21.05.2016.
 */

public class CollisionDetectionSystemFactory extends EntitySystemFactory
{

    public CollisionDetectionSystemFactory()
    {
        mRepoUsed = null;
    }

    @Override
    public CollisionDetectionSystem produce()
    {
        if (mRepoUsed == null)
            throw new IllegalStateException("no collision repository set, cannot produce collision detection system");

        if (mDetector == null)
            throw new IllegalStateException("no collision detector set, cannot produce collision detection system");

        CollisionDetectionSystem newSystem = new CollisionDetectionSystem(mDetector.clone());
        newSystem.setRepository(mRepoUsed);
        initSystem(newSystem);
        return newSystem;
    }

    /**
     * sets the collision repository to use for produced systems
     * @param repoUsed repository used
     */
    public void setRepository(CollisionRepository repoUsed) { mRepoUsed = repoUsed; }

    /**
     * sets the collision detector to use for the produced systems
     * @param detector
     */
    public void setCollisionDetector (CollisionDetector detector) { mDetector = detector; }

    private CollisionRepository mRepoUsed;
    private CollisionDetector mDetector;
}

