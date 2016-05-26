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
    
	
	public static Shape[] translateShapes(Shape[] shapes, Vector3 position)
	{
		ShapeTranslatorOperation operation = new ShapeTranslatorOperation(position);
		ForEach<Shape, ShapeTranslator> fa = new ForEach<>(operation);
		return fa.operate(new ShapeTranslator[shapes.length], shapes);
	}
	
	
	public static Vector3[] translateVertices(Vector3[] vertices, Vector3 position)
	{
		VertexTranslateOperation operation = new VertexTranslateOperation(position);
		ForEach<Vector3, Vector> fe = new ForEach<>(operation);
		return fe.operate (new VertexTranslator[vertices.length], vertices);
	}
	
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
