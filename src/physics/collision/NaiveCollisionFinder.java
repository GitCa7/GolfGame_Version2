package physics.collision;

import physics.components.Body;
import physics.geometry.spatial.SolidIntersection;
import physics.geometry.spatial.SolidTranslator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class searching all pairs of possible collisions by checking every solid of one element
 * against any other solid of the other element of the pair.
 * @author martin
 */
public class NaiveCollisionFinder extends NarrowCollisionFinder
{

    public NaiveCollisionFinder clone() { return new NaiveCollisionFinder(); }

    @Override
    public Collection<ColliderPair<ColliderEntity>> collisions()
    {
        ArrayList<ColliderPair<ColliderEntity>> colliding = new ArrayList<>();

        //for every active|all pair check for physics.collision
        for (ColliderPair<ColliderEntity> pair : getPossibleCollisions())
        {
            ColliderPair<ColliderBody> bodyCollision = checkBodies(pair.getFirst().getBody(), pair.getSecond().getBody());
            if (bodyCollision != null)
            {
                ColliderEntity ce1 = new ColliderEntity(pair.getFirst().getEntity(), bodyCollision.getFirst());
                ColliderEntity ce2 = new ColliderEntity(pair.getSecond().getEntity(), bodyCollision.getSecond());
                colliding.add(new ColliderPair<ColliderEntity>(ce1, ce2));
            }
        }

        return colliding;
    }

    /**
     * checks all solid in b1 and b2 against each other
     * @param b1 a body
     * @param b2 a body
     * @return a pair of collider bodies if there is a collision between b1 and b2, null otherwise
     */
    private ColliderPair<ColliderBody> checkBodies(Body b1, Body b2)
    {
        for (SolidTranslator s1 : b1)
        {
            for (SolidTranslator s2 : b2)
            {
                SolidIntersection checker = new SolidIntersection(s1, s2);
                checker.checkForIntersection();
                if (checker.doIntersect())
                {
                    ColliderPair<ColliderSolid> colliderSolid = checker.getSolidCollision();
                    ColliderBody cb1 = new ColliderBody(b1, colliderSolid.getFirst());
                    ColliderBody cb2 = new ColliderBody(b2, colliderSolid.getSecond());
                    return new ColliderPair<>(cb1, cb2);
                }
            }
        }
        return null;
    }
}
