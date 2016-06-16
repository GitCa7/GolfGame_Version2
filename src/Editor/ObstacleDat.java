package Editor;

import com.badlogic.gdx.math.Vector3;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Asus on 16-6-2016.
 */
public class ObstacleDat {
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

    public
}
