package physics.components;

import Entities.gameEntity;
import org.lwjgl.util.vector.Vector3f;

/**
 * @autor martin
 * created 19.05.2016
 */
public class PositionBridge extends Position
{
	public PositionBridge (gameEntity gem)
	{
		super (0, 0, 0);
		Vector3f a = new Vector3f(this.x,this.y,this.z);
		gem.setPosition (a);
	}

}
