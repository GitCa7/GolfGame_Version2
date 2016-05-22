package physics.components;

/**
 * component class storing the resulting force applied to an object
 * @autor martin
 */
import com.badlogic.gdx.math.*;

public class Force extends Vector3 implements Component
{
	public Force clone()
	{
		Force f = new Force();
		f.set (this);
		return f;
	}
}
