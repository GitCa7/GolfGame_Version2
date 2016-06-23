package physics.geometry.planar;
import com.badlogic.gdx.math.*;
import physics.generic.pool.Operation;



public class ShapeTranslateOperation implements Operation<Shape, ShapeTranslator>
{
	
	public ShapeTranslateOperation(Vector3 translate)
	{
		mTranslate = translate;
	}
	
	
	public ShapeTranslator operate(Shape translateShape)
	{
		return new ShapeTranslator (translateShape, mTranslate);
	}
	
	private Vector3 mTranslate;
}
