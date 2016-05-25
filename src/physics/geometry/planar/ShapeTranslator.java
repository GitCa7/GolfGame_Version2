package physics.geometry.planar;

import com.badlogic.gdx.math.Vector3;
import physics.geometry.linear.Line;
import physics.components.Position;

import physics.generic.ForAll;

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
    public ShapeTranslator(Shape shape, Position position) 
    {
        super(
        		new ForAll<Vector3, VertexTranslator>(new VertexTranslateOperation(position)).operate(), 
        		new ForAll<Vector3, LineTranslator>(new LineTranslateOperation(position)).operate()
        		);
    }

    Position mPosition;
}
