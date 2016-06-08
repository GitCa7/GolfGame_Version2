package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;
import physics.generic.ForEach;
import physics.geometry.linear.VertexTranslateOperation;
import physics.geometry.linear.VertexTranslator;
import physics.geometry.planar.Shape;
import physics.geometry.planar.ShapeTranslator;
import physics.geometry.planar.ShapeTranslateOperation;

/**
 * Class translating a solid (relative to the origin).
 * Created by Alexander on 25.05.2016.
 * @author Alexander
 * @author martin
 */
public class SolidTranslator
{
	/**
     * parametric constructor
     * sets the solid and the vector by which to translate all its vertices
	 * @param solid solid to translate
	 * @param position position by which to translate
     */
    public SolidTranslator(Solid solid, Vector3 position)
	{
		mTranslated = solid;
        mPosition = position;
    }


	/**
	 * computes the translated vertices
	 * @return the translated vertices
     */
	public Vector3[] getVertices()
	{
		Vector3[] vertices = mTranslated.getVertices();
		VertexTranslateOperation translate = new VertexTranslateOperation(mPosition);
		ForEach<Vector3, VertexTranslator> allVertices = new ForEach<>(translate);
		return allVertices.operate(new VertexTranslator[vertices.length], vertices);
	}

	/**
	 * computes the translated sides
	 * @return the translated sides
     */
	public Shape[] getSides()
	{
		Shape[] sides = mTranslated.getSides();
		ShapeTranslateOperation translate = new ShapeTranslateOperation(mPosition);
		ForEach<Shape, ShapeTranslator> allSides = new ForEach<>(translate);
		return allSides.operate(new ShapeTranslator[sides.length], sides);
	}

    /**
     * @return the offset position by which the solid is translated
     */
	public Vector3 getPosition() { return mPosition; }

	/**
	 * @param p a point given by a vector
	 * @return true if p is within the translated solid
     */
    public boolean isWithin(Vector3 p)
	{
        Vector3 relPosition = p.cpy().sub(mPosition);
		return mTranslated.isWithin(relPosition);
    }

	/**
	 * sets the translation vector to transPos
	 * @param transPos the new translation vector
     */
	public void setPosition(Vector3 transPos)
	{
		mPosition = transPos;
	}

	/**
	 * adds add to the translation vector
	 * @param add vector to add
     */
	public void addPosition(Vector3 add)
	{
		mPosition.add(add);
	}

	private Solid mTranslated;
    private Vector3 mPosition;
}
