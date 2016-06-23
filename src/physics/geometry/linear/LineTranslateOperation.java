package physics.geometry.linear;

import physics.generic.pool.Operation;
import com.badlogic.gdx.math.*;



public class LineTranslateOperation implements Operation<Line, LineTranslator>
{
	
	public LineTranslateOperation(Vector3 translate)
	{
		mTranslate = translate;
	}
	
	
	public LineTranslator operate(Line translateLine)
	{
		return new LineTranslator(translateLine, mTranslate);
	}
	
	private Vector3 mTranslate;
}
