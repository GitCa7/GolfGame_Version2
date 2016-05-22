package physics.systems;
import com.badlogic.gdx.math.*;
import physics.collision.*;
/**
 * Created by Alexander on 22.05.2016.
 */
public class NormalForceSystem
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
        //given a force g pushing on surface, normal unit vector nu of this surface
        // = g * nu * nu * (-1)
        return /*N*/new Vector3();
    }
}