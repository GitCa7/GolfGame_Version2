package physics.geometry.linear;
import com.badlogic.gdx.math.Vector3;
import physics.components.Position;


/**
 * Created by Alexander on 25.05.2016.
 * Translates Vertices to a new Position
 */
public class VertexTranslator extends Vector3 {

    public VertexTranslator(Vector3 vector, Position position){
        super(vector);
        this.add(position);
    }

}
