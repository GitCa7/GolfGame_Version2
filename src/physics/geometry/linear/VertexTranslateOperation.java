package physics.geometry.linear;

import physics.generic.Operation;
import physics.geometry.planar.Shape;
import physics.geometry.planar.ShapeTranslator;
import com.badlogic.gdx.math.*;


public class VertexTranslateOperation implements Operation<Vector3, VertexTranslator>
{
	
	public VertexTranslateOperation (Vector3 translate)
	{
		mTranslate = translate;
	}
	
	@Override
	public VertexTranslator operate(Vector3 v)
	{
		return new VertexTranslator(v, mTranslate);
	}
	
	private Vector3 mTranslate;
}
