package physics.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import physics.collision.ColliderEntity;
import physics.collision.ColliderPair;
import physics.collision.CollisionDetector;
import physics.collision.CollisionRepository;
import physics.components.Body;
import physics.components.Position;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.geometry.spatial.SolidBisection;
import physics.geometry.spatial.SolidTranslator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * System ensuring no moving entity penetrates any other entity
 * @author martin
 */
public class NonPenetrationSystem extends EntitySystem
{


    public NonPenetrationSystem()
    {
        mRepo = null;
    }

    public void setRepository(CollisionRepository repo)
    {
        mRepo = repo;
    }

    @Override
    public void addEntity(Entity e)
    {

    }

    @Override
    public void removeEntity(Entity e)
    {

    }

    /**
     * Iterates over all collisions. For each entity's body penetrating another entity's body
     * the position of the first will be set, such that it does no longer penetrate and the distance from the
     * penetrating body to the penetrated body is below a given tolerance.
     * @param dTime time between last and current update
     */
    public void update(float dTime)
    {
        if (mRepo == null)
            throw new IllegalStateException("no collision repository set, non penetration system cannot operate");
        ArrayList<ColliderPair<ColliderEntity>> collisions = mRepo.getColliderPairs();

        for (ColliderPair<ColliderEntity> pair : collisions)
        {
            if (pair.getFirst().isActive())
            {
                Vector3 nonP = computeNonPenetratingPosition(pair.getFirst(), pair.getSecond());
                setEntityPosition(pair.getFirst().getEntity(), nonP);
            }
            else if (pair.getSecond().isActive())
            {
                Vector3 nonP = computeNonPenetratingPosition(pair.getSecond(), pair.getFirst());
                setEntityPosition(pair.getSecond().getEntity(), nonP);
            }
        }

    }


    private Vector3 computeNonPenetratingPosition(ColliderEntity active, ColliderEntity passive)
    {
        Position activePos = CompoMappers.POSITION.get(active.getEntity());
        //find position between previous position and current position outside but close to the passive's body
        SolidTranslator dynamic = active.getCollidingSolid(), stat = passive.getCollidingSolid();
        SolidBisection adjustPosition = new SolidBisection(activePos, activePos.mPrevious, dynamic, stat);
        adjustPosition.run();
        return adjustPosition.getSolution();
    }

    private void setEntityPosition(Entity e, Vector3 newPosition)
    {
        Position p = CompoMappers.POSITION.get(e);
        Body b = CompoMappers.BODY.get(e);


        System.out.println ("correcting position from " + p + " to " + newPosition);

        p.set(newPosition);
        b.setPosition(newPosition);
    }



    private CollisionRepository mRepo;
}
