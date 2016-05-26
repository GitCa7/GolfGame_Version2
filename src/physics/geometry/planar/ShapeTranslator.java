package physics.geometry.planar;

import com.badlogic.gdx.math.Vector3;
import physics.generic.ForEach;
import physics.geometry.linear.*;
import physics.components.Position;


/**
 * Created by Alexander on 25.05.2016.
 */
public class ShapeTranslator extends Shape {
    
	
	public static Vector3[] translateVertices(Vector3[] vertices, Vector3 position)
	{
		VertexTranslateOperation operation = new VertexTranslateOperation(position);
		ForEach<Vector3, VertexTranslator> fa = new ForEach<>(operation);
		return fa.operate (new VertexTranslator[vertices.length], vertices);
	}
	
	
	public static Line[] translateLines(Line[] lines, Vector3 position)
	{
		LineTranslateOperation operation = new LineTranslateOperation(position);
		ForEach<Line, LineTranslator> fa = new ForEach<>(operation);
		return fa.operate(new LineTranslator[lines.length], lines);
	}
	
	/**
     * parametric constructor
     *
     * @param shape array of vertices of shape
     * @param position
     */
    public ShapeTranslator(Shape shape, Position position) 
    {
    	super (translateVertices (shape.getVertices(),position), translateLines(shape.getBorder(),position));
    }

    Position mPosition;
}
