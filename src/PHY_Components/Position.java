package PHY_Components;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.math.*;

/**
 * class storing a position
 * @author martin
 */
public class Position extends Vector3 implements Component
{
	public Position (float x, float y, float z)
	{
		super (x, y, z);
	}
}