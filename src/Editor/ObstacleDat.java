package Editor;

import Entities.Obstacle;
import com.badlogic.gdx.math.Vector3;
import org.lwjgl.util.vector.Vector3f;

import java.io.Serializable;

/**
 * Created by Asus on 16-6-2016.
 */
public class ObstacleDat implements Serializable {
    private float scale;
    private float rotY;
    private Vector3 pos;

    public ObstacleDat(float scale, float rotY, Vector3 pos){
        this.scale = scale;
        this.rotY = rotY;
        this.pos = pos;
    }

    public float getScale(){
        return scale;
    }

    public float getRotY() {
        return rotY;
    }

    public Vector3 getPos() {
        return pos;
    }
    public Obstacle toObstacle(){
        return new Obstacle(new Vector3f(pos.x,pos.y,pos.z),scale);
    }
}
