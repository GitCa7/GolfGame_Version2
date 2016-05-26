package physics.geometry.planar;

import com.badlogic.gdx.math.Vector3;
import physics.geometry.linear.Line;
import physics.geometry.linear.LineTranslateOperation;
import physics.geometry.linear.VertexTranslateOperation;
import physics.components.Position;

import physics.generic.ForAll;

/**
 * Created by Alexander on 25.05.2016.
 */
public class ShapeTranslator extends Shape {
    
	
	public static Vector3[] translateVertices(Vector3[] vertices, Vector3 position)
	{
		VertexTranslateOperation operation = new VertexTranslateOperation(position)
		ForEach<Vector3, VertexTranslator> fa = new ForEach<>(operation);
		return fa.operate (new VertexTranslator[vertices.length], vertices);
	}
	
	
	public static Line[] translateLines(Line[] lines, Vector3 position)
	{
		LineTranslateOperation operation = new LineTranslateOperation(position);
		ForEach<Vector3, LineTranslator> fa = new ForAll<>(operation);
		return new fa.operate(new LineTranslator[lines.length], lines);
	}
	
	/**
     * parametric constructor
     *
     * @param shape array of vertices of shape
     * @param position
     */
    public ShapeTranslator(Shape shape, Position position) 
    {
    	super (translateVertices (shape.vertices()), translateLines(shape.getBorder()));
    }

    Position mPosition;
}
