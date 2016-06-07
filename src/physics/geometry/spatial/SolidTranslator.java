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
 * Class translating a solid (relative to the origin).
 * This class is meant to be immutable, just as a solid is.
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
		ForEach<Vector3, VertexTranslator> fe = new ForEach<>(operation);
		return fe.operate (new VertexTranslator[vertices.length], vertices);
	}
	
	/**
     * parametric constructor
     *
     *
     */
    public SolidTranslator(Solid solid, Vector3 position) {
        super ( translateVertices(solid.getVertices(), position), translateShapes(solid.getSides(), position));

        mPosition = position;
    }

    /**
     * @return the offset position by which the solid is translated
     */
	public Vector3 getPosition() { return mPosition; }

    @Override
    public boolean isWithin(Vector3 p) {
        return false;
    }

    private Vector3 mPosition;
}
