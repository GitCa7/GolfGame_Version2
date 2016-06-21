package physics.collision;

import physics.components.Body;

import java.util.ArrayList;

/**
 * Created by marcel on 21.06.2016.
 */
public class NaiveCollisionFinder extends CollisionFinder
{

    /**
     * @param bodies the bodies for which to determine the pairs which may be colliding
     */
    public NaiveCollisionFinder(ArrayList<Body> bodies)
    {
        super(bodies);
    }

    @Override
    public ArrayList<BodyPair> possibleCollisions()
    {
        return null;
    }
}
