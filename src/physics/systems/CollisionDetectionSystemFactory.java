package physics.systems;

import physics.collision.CollisionRepository;

/** Creates CollisionDetectionSystemFactory
 * Created by marcel on 21.05.2016.
 */

public class CollisionDetectionSystemFactory implements EntitySystemFactory
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

        CollisionDetectionSystem newSystem = new CollisionDetectionSystem();
        newSystem.setRepository(mRepoUsed);
        return newSystem;
    }

    /**
     * sets the collision repository to use for produced systems
     * @param repoUsed repository used
     */
    public void setRepository(CollisionRepository repoUsed) { mRepoUsed = repoUsed; }

    private CollisionRepository mRepoUsed;
}

