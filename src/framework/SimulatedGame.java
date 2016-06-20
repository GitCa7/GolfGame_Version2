package framework;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;
import framework.constants.Families;
import framework.internal.components.Busy;
import physics.components.Position;
import physics.components.Velocity;
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
        mGlobalState = engine.getEntitiesFor(framework.constants.Families.GLOBAL_STATE).get(0);

        setEntities(engine);
        //dry refresh update
        mEngine.update(1);
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
        Busy busy = framework.constants.CompoMappers.BUSY.get(mGlobalState);

        Position ballPos = CompoMappers.POSITION.get(mBall.mEntity);
        Velocity ballVel = CompoMappers.VELOCITY.get(mBall.mEntity);


        f.add(force);
        //loop while busy
        do
        {
            mEngine.update(dTime);
            System.out.print("ball s " + ballPos);
            System.out.print(" v " + ballVel);
            System.out.print(" busy " + busy.mBusy);
            System.out.println();
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
        {
            mEngine.addEntity(moving);
            if (Families.GLOBAL_STATE.matches(moving))
                mGlobalState = moving;
        }

        mBall = state.getPlayerBall();
    }

    /**
     * Clones all moving entities. Adds all entities which are not TURN_TAKING
     * to the engine
     * @param original
     */
    private void setEntities(Engine original)
    {
        for (Entity immutable : original.getEntitiesFor(Families.NON_MODIFIABLE))
            mEngine.addEntity(immutable);

        GameState state = new GameState(original.getEntities(), mBall).clone();
        for (Entity cloned : state.getMutableEntities())
        {
            mEngine.addEntity(cloned);
            if (Families.GLOBAL_STATE.matches(cloned))
                mGlobalState = cloned;
        }

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
            mEngine.addEntityListener(system.getNewEntitiesListener());
            mEngine.addSystem(system);

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

        ImmutableArray<Entity> remove = mEngine.getEntitiesFor(Families.MODIFIABLE);

        for (int cRemove = remove.size() - 1; cRemove >= 0; --cRemove)
        {
            mEngine.removeEntity(remove.get(cRemove));
        }
    }

    private Engine mEngine;
    private Ball mBall;
    private Entity mGlobalState;
}
