package physics.components;

import com.badlogic.gdx.math.*;
import org.lwjgl.util.vector.Vector3f;

/**
 * class storing a position
 * @author martin
 */
public class Position extends Vector3 implements Component
{
	public Position (){}
	public Position (float x, float y, float z)
	{
		super (x, y, z);
	}

	public Position clone()
	{
		Position p = new Position (this.x, this.y, this.z);
		return p;
	}
}