package physics.collision;

import java.util.ArrayList;
import java.util.Collection;

/**
 * created 22.06.16
 *
 * @author martin
 */
public abstract class NarrowCollisionFinder implements Cloneable
{
    public NarrowCollisionFinder()
    {
        mPossibleCollisions = new ArrayList<>();
    }

    public abstract  NarrowCollisionFinder clone();

    public abstract Collection<ColliderPair<ColliderEntity>> collisions();


    public Collection<ColliderPair<ColliderEntity>> getPossibleCollisions()
    {
        return mPossibleCollisions;
    }

    public void setCollidingEntities (Collection<EntityAndBody> all, Collection<EntityAndBody> moving)
    {
        mPossibleCollisions.clear();
        for (EntityAndBody move : moving)
        {
            ColliderEntity e1 = constructIncompleteCollider(move);

            for (EntityAndBody any : all)
            {
                if (!move.equals(any))
                {
                    ColliderEntity e2 = constructIncompleteCollider(any);
                    mPossibleCollisions.add(new ColliderPair<>(e1, e2));
                }
            }
        }
    }

    public void setPossibleCollisions(Collection<ColliderPair<ColliderEntity>> possibleCollisions)
    {
        mPossibleCollisions = possibleCollisions;
    }


    private ColliderEntity constructIncompleteCollider(EntityAndBody entity)
    {
        ColliderSolid colliderSolid = new ColliderSolid(null, null);
        ColliderBody colliderBody = new ColliderBody(entity.mBody, colliderSolid);
        return new ColliderEntity(entity.mEntity, colliderBody);
    }

    private Collection<ColliderPair<ColliderEntity>> mPossibleCollisions;
}
