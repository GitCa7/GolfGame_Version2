package physics.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.systems.EntitySystem;
import physics.collision.structure.ColliderEntity;
import physics.collision.structure.ColliderPair;
import physics.collision.structure.CollisionRepository;
import physics.components.Body;
import physics.components.Position;
import physics.constants.CompoMappers;
import physics.constants.GlobalObjects;
import physics.geometry.spatial.SolidBisection;
import physics.geometry.spatial.SolidTranslator;

import java.util.ArrayList;

/**
 * System ensuring no moving entity penetrates any other entity
 * @author martin
 */
public class NonPenetrationSystem extends EntitySystem implements RepositoryEntitySystem
{

    public static boolean DEBUG = false;

    public NonPenetrationSystem()
    {
        mRepo = null;
    }


    public NonPenetrationSystem clone()
    {
        NonPenetrationSystem newSystem = new NonPenetrationSystem();
        newSystem.setPriority(priority);
        return newSystem;
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
        if (adjustPosition.hasSolution())
            return adjustPosition.getSolution();
        else
            return activePos;
    }

    private void setEntityPosition(Entity e, Vector3 newPosition)
    {
        Position p = CompoMappers.POSITION.get(e);
        Body b = CompoMappers.BODY.get(e);


        if (DEBUG)
            System.out.println ("correcting position from " + p + " to " + newPosition);

        GlobalObjects.ROUND.roundDigits(newPosition.x);
        GlobalObjects.ROUND.roundDigits(newPosition.y);
        GlobalObjects.ROUND.roundDigits(newPosition.z);
        p.set(newPosition);
        b.setPosition(newPosition);
    }


    private CollisionRepository mRepo;
}
