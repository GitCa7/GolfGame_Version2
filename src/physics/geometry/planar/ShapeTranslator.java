package physics.geometry.planar;

import com.badlogic.gdx.math.Vector3;
import physics.geometry.linear.Line;
import physics.components.Position;

/**
 * Created by Alexander on 25.05.2016.
 */
public class ShapeTranslator extends Shape {
    /**
     * parametric constructor
     *
     * @param shape array of vertices of shape
     * @param position
     */
    public ShapeTranslator(Shape shape, Position position) {
        super(shape.getVertices(), shape.getBorder());
    }


        /*for(Vector3 v1: vertices)
            v1.add(position);
        for (Line b1: border) {
            b1.getEnd().add(position);
            b1.getStart().add(position);
        }
    }

    Position mPosition;
}
