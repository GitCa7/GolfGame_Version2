package framework;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.math.Vector3;
import framework.internal.components.Busy;
import physics.systems.RepositoryEntitySystem;
import physics.collision.CollisionRepository;
import physics.components.Force;
import physics.constants.CompoMappers;
import physics.entities.Ball;

/**
 * Class cloning a game based on its engine.
 * Imposes the following restrictions: there may only be one implicit player,
 * disposing of one ball.
 * Optimized to be cloned multiple times after initial instantiation. The current state of the
 * simulated game can be exported to a GameState object. A state of the simulated game can be set
 * using a GameState object.
 *
 * created 16.06.16
 *
 * @author martin
 */
public class SimulatedGame
{
    /**
     * construct a simulated game from an engine
     * @param engine engine whose modifiable entities to clone and whose systems to clone
     */
    public SimulatedGame (Engine engine, Ball playerBall)
    {
        mEngine = new Engine();
        mBall = playerBall;
        GlobalState = engine.getEntitiesFor(framework.constants.Families.GLOBAL_STATE).get(0);

        setEntities(engine);
        setSystems(engine);
    }

    /**
     * @return a game state containing cloned entities
     */
    public GameState getGameState()
    {
        return new GameState(mEngine.getEntities(), mBall).clone();
    }

    /**
     * @return the position of the ball
     */
    public Vector3 getBallPosition()
    {
        return CompoMappers.POSITION.get(mBall.mEntity);
    }

    /**
     * @return the position of the ball's target
     */
    public Vector3 getTargetPosition()
    {
        return framework.constants.CompoMappers.GOAL.get(mBall.mEntity).mGoalSpace.getPosition();
    }


    /**
     * Applies force to the ball and advances the engine until the
     * ball no longer moves.
     * @param force the force to apply
     * @param dTime time delta from one step to the next
     */
    public void play(Vector3 force, float dTime)
    {
        Force f = CompoMappers.FORCE.get(mBall.mEntity);
        Busy busy = framework.constants.CompoMappers.BUSY.get(GlobalState);

        f.add(force);
        //loop while busy
        do
        {
            mEngine.update(dTime);
        }while(busy.mBusy);
    }

    /**
     * sets the state of the simulated game to the state stored by state
     * @param state the state of a simulation
     */
    public void setGameState(GameState state)
    {
        removeModifiableEntities();
        for (Entity moving : state.getMutableEntities())
            mEngine.addEntity(moving);
    }

    /**
     * Clones all moving entities. Adds all entities which are not TURN_TAKING
     * to the engine
     * @param original
     */
    private void setEntities(Engine original)
    {
        GameState state = new GameState(original.getEntities(), mBall);
        for (Entity cloned : state.clone().getMutableEntities())
            mEngine.addEntity(cloned);

        mBall = state.getPlayerBall();

    }

    /**
     * Clones and adds all systems from the original engine to this engine and
     * adds entity listeners to the engine for each of them.
     * @param original original engine
     */
    private void setSystems(Engine original)
    {
        CollisionRepository repo = new CollisionRepository();

        for (com.badlogic.ashley.core.EntitySystem entitySystem : original.getSystems())
        {
            EntitySystem system = ((EntitySystem) entitySystem).clone();
            mEngine.addSystem(system);
            mEngine.addEntityListener(system.getNewEntitiesListener());

            //@TODO ugly RTTI, FIX!!!
            if (RepositoryEntitySystem.class.isAssignableFrom(system.getClass()))
                ((RepositoryEntitySystem) system).setRepository(repo);
        }
    }

    /**
     * removes all moving entities from the engine
     */
    private void removeModifiableEntities()
    {
        for (Entity remove : mEngine.getEntitiesFor(framework.constants.Families.MODIFIABLE))
            mEngine.removeEntity(remove);
    }

    private Engine mEngine;
    private Ball mBall;
    private Entity GlobalState;
}
