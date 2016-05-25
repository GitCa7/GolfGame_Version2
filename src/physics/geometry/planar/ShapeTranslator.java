package physics.geometry.planar;

import com.badlogic.gdx.math.Vector3;
import physics.geometry.linear.*;
import physics.components.Position;

import physics.generic.ForEach;

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
    public ShapeTranslator(Shape shape, Vector3 position)
    {
        super(
        		new ForEach<Vector3, VertexTranslator>(new VertexTranslateOperation(position)).operate(shape.getVertices()),
        		new ForEach<Vector3, LineTranslator>(new LineTranslateOperation(position)).operate());
    }

    private Vector3 mPosition;
}
