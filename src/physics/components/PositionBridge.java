package physics.components;

import Entities.gameEntity;

/**
 * @autor martin
 * created 19.05.2016
 */
public class PositionBridge extends Position
{
	public PositionBridge (gameEntity gem)
	{
		super (0, 0, 0);
		gem.setPosition (this);
	}

}
