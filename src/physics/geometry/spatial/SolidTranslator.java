package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.generic.ForEach;
import physics.geometry.linear.VertexTranslateOperation;
import physics.geometry.linear.VertexTranslator;
import physics.geometry.planar.Shape;
import physics.components.Position;
import physics.geometry.planar.ShapeTranslator;
import physics.geometry.planar.ShapeTranslatorOperation;

/**
 * Created by Alexander on 25.05.2016.
 */
public class SolidTranslator extends Solid {
    /**
     * parametric constructor
     *
     *
     */
    public SolidTranslator(Solid solid, Position position) {
        super (
        		new ForEach<Vector3, VertexTranslator>(new VertexTranslateOperation(position)).operate(),
        		new ForEach<Vector3, ShapeTranslator>(new ShapeTranslatorOperation(position)).operate()
        		);

        mPosition = position;
    }

    @Override
    public boolean isWithin(Vector3 p) {
        return false;
    }


    Position mPosition;
}
