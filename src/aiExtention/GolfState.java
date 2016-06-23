package aiExtention;


import com.badlogic.gdx.math.Vector3;
import framework.simulation.GameState;
import framework.simulation.SimulatedGame;
import framework.entities.Ball;
import searchTree.SearchState;

public class GolfState extends SearchState {
	/** contains the state of all mutable entities */
	private GameState mSimulationState;
	/** contains all immutable entities and game logic (i.e. systems */
	private SimulatedGame mSimulationEngine;

	/**
	 * parametric constructor
	 * @param simulation a simulated game instance
	 * @param simulationState an instance of the state used with the simulated game
     */
	public GolfState(SimulatedGame simulation, GameState simulationState)
	{
		mSimulationEngine = simulation;
		mSimulationState = simulationState;
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
