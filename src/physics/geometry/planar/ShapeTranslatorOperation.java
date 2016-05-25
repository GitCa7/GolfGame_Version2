package physics.geometry.planar;

public class ShapeTranslatorOperation implements Operation<Shape, ShapeTranslator>
{
	
	public ShapeTranslatorOperation (Vector3 translate)
	{
		mTranslate = translate;
	}
	
	
	public ShapeTranslator operate(Shape translateShape)
	{
		return new ShapeTranslator (translateShape, mTranslate);
	}
	
	private Vector3 mTranslate;
}
