package aiExtention;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.GameState;
import framework.SimulatedGame;
import physics.components.Force;
import physics.components.GravityForce;
import physics.components.Position;
import physics.components.Velocity;
import physics.entities.Ball;
import searchTree.SearchState;

public class GolfState extends SearchState {
	/** contains the state of all mutable entities */
	private GameState mSimulationState;
	/** contains all immutable entities and game logic (i.e. systems */
	private SimulatedGame mSimulationEngine;


	public GolfState(SimulatedGame simulation, GameState mSimulationState)
	{
		mSimulationEngine = simulation;
		mSimulationState = mSimulationState;
	}

	/**
	 * @return the reference to the simulated game after setting the state of stored. Any modification occuring to the simulation
	 * will also affect this state.
     */
	public SimulatedGame getSimulation()
	{
		mSimulationEngine.setGameState(mSimulationState);
		return mSimulationEngine;
	}

	/**
	 * @return the ai's ball's position
     */
	public Vector3 getPosition() {
		return mSimulationState.getBallPosition();
	}

	/**
	 * @return the target of the ai's ball
     */
	public Vector3 getTargetPosition() { return mSimulationState.getTargetPosition(); }

	/**
	 * @return the distance from the ai's ball position to the target (i.e. target - ballPos)
     */
	public Vector3 getDistanceToTarget() { return mSimulationState.getTargetPosition().cpy().sub(mSimulationState.getBallPosition()); }

	/**
	 * @return the ai's ball
     */
	public Ball getBall() {
		return mSimulationState.getPlayerBall();
	}

	/**
	 * @return a partial deep copy of this state
     */
	public GolfState cloneState()
	{
		return new GolfState(mSimulationEngine, mSimulationState.clone());
	}

}
