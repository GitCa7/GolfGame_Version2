package physics.systems;
import com.badlogic.gdx.math.*;
import physics.collision.*;


/**
 * Created by Alexander on 22.05.2016.
 */
        /*
        Collision detection => repository
        repository => collision impact system
        repository => normal force system
        */

public class CollisionImpactSystem
{
    public void update (float time)
    {
        //get collider pairs
        //for each collider pair
        //f = compute
        //apply f on active entity
    }

    private Vector3 compute (ColliderEntity active, ColliderEntity passive)
    {
        //given initial velocity v, normal unit vector nu
        //decompose v into u orthogonal to the plane and u parallel to the plane
        //u = v * nu * nu (orthogonal projection, use vector projector)
        //w = v - u
        //v' = f*w - r*u
        //F = m * (v' - v) / dt
        //return F;
        return new Vector3();
    }
}

