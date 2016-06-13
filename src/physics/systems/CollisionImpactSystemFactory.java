package physics.systems;
import framework.EntitySystemFactory;
import physics.collision.CollisionRepository;

/** Creates a CollisionImpactSystemFactory
 * Created by marcel on 21.05.2016.
 */

public class CollisionImpactSystemFactory extends EntitySystemFactory
{


    public CollisionImpactSystemFactory()
    {
        mRepoUsed = null;
    }

    @Override
    public CollisionImpactSystem produce()
    {
        if (mRepoUsed == null)
            throw new IllegalStateException("no collision repository set, cannot produce collision impact system");
        CollisionImpactSystem cs = new CollisionImpactSystem();
        cs.setRepository(mRepoUsed);
        return cs;
    }

    /**
     * sets the collision repository to use for produced systems
     * @param repoUsed repository used
     */
    public void setRepository(CollisionRepository repoUsed) { mRepoUsed = repoUsed; }

    private CollisionRepository mRepoUsed;
}
