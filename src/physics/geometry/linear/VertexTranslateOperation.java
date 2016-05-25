package physics.geometry.linear;

import physics.generic.Operation;
import physics.geometry.planar.Shape;
import physics.geometry.planar.ShapeTranslator;

public class VertexTranslateOperation implements Operation<Shape, ShapeTranslator>
{
	
	public VertexTranslateOperation (Vector3 translate)
	{
		mTranslate = translate;
	}
	
	@Override
	public VertexTranslator operate(Shape s) 
	{
		return new VertexTranslator(s, mTranslate);
	}
	
	private Vector3 mTranslate;
}
