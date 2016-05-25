package physics.geometry.planar;
import com.badlogic.gdx.math.*;
import physics.generic.Operation;



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
