
package physics.testing;

import com.badlogic.gdx.math.Vector3;
import framework.ComponentBundle;
import framework.Game;
import framework.GameConfigurator;
import framework.Player;
import physics.components.ComponentFactory;
import physics.constants.CompoMappers;
import physics.entities.Ball;
import physics.entities.EntityFactory;
import physics.entities.Hole;
import physics.systems.EntitySystemFactory;

/**
 * demonstration: how to use the game engine
 * @author martin
 * created 19.05.2016
 */
public class GameCreationDemo
{
	public static void main (String[] args)
	{
		// instantiate component bundles to hold pairing of physics.components and (possibly null) physics.systems
		ComponentBundle position = position();
		ComponentBundle velocity = velocity();
		ComponentBundle force = force();

		//construct game configurator
		GameConfigurator config = new GameConfigurator();
		//obtain entity factory for physics.entities moving and affected by forces
		EntityFactory basicMovingEntities = config.entityFactory();
		basicMovingEntities.addComponent (position, velocity, force);

		//add physics.entities to game configuration/engine
		config.addEntities (basicMovingEntities, 2);
		//modify factories stored in component bundles
		//add physics.entities using entity factory

		//add balls
		Ball b1 = new Ball (basicMovingEntities.produce());
		//modify factories again to (i.e.) change position of 2nd ball
		Ball b2 = new Ball (basicMovingEntities.produce());

		Player p1 = new Player ("p1");
		Player p2 = new Player ("p2");

		config.addBall (p1, b1);
		config.addBall (p2, b2);

		//set hole
		EntityFactory holeEntityFax = config.entityFactory();
		holeEntityFax.addComponent (position);
		Hole h = new Hole (holeEntityFax.produce());
		config.setHole (h);

		//get game after setup is done
		Game game = config.game();

		//hitting the ball of the current player
		//will make the game busy, so no hit can be executed while the ball is still moving
		//after it's the next player's turn
		game.hit (new Vector3(1, 1, 0));

		//check whether the game is busy
		game.isBusy();

		//getting ball of player 1
		game.getBall (p1);

		Vector3 pos = CompoMappers.POSITION.get (game.getBall (p1));

		//@TODO enable cloning of games

	}


	public static ComponentBundle position()
	{
		ComponentFactory compFax = null;
		return new ComponentBundle (compFax, null);
	}


	public static ComponentBundle velocity()
	{
		ComponentFactory compFax = null;
		EntitySystemFactory entFax = null;
		return new ComponentBundle (compFax, entFax);
	}

	public static ComponentBundle force()
	{
		ComponentFactory compFax = null;
		EntitySystemFactory entFax = null;
		return new ComponentBundle (compFax, entFax);
	}


}
