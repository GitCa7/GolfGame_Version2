package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.geometry.planar.Shape;
import physics.components.Position;

/**
 * Created by Alexander on 25.05.2016.
 */
public class SolidTranslator extends Solid {
    /**
     * parametric constructor
     *
     * @param vertices vertices of solid
     * @param sides
     */
    public SolidTranslator(Solid solid, Position position) {
        super (
        		new ForAll<Vector3, VertexTranslator>(new VertexTranslateOperation(position)).operate(),
        		new ForAll<Vector3, ShapeTranslator>(new ShapeTranslateOperation(position)).operate()
        		);

        mPosition = position;
    }

    @Override
    public boolean isWithin(Vector3 p) {
        return false;
    }


    Position mPosition;
}
