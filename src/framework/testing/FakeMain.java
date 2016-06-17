package framework.testing;

import Editor.Editor;
import Entities.gameEntity;
import GamePackage.GameLoader;
import GamePackage.GameVisual;
import TerrainComponents.TerrainData;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.GameConfigurator;
import framework.entities.Player;
import org.lwjgl.util.vector.Vector3f;
import physics.components.Position;
import physics.components.Velocity;
import physics.entities.Ball;
import physics.geometry.planar.Triangle;
import physics.geometry.planar.TriangleBuilder;
import physics.geometry.spatial.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class running the game.
 * @author martin
 */
public class FakeMain
{

    public static void main(String[] args)
    {
        FakeMain fm = new FakeMain();
        fm.initGame();
        fm.initGraphics();

        fm.run();
        fm.close();
    }

    public static final float DELTA_TIME = 1;



    /**
     * launches the editor, permitting to create a course file
     */
    public void launchEditor()
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new Editor(), config);
    }


    public void initGame()
    {
        GameConfigurator config = new GameConfigurator();

        //terrain from huge triangle
        float largeValue = 100000;
        Vector3 v1 = new Vector3(-largeValue, 0, 0);
        Vector3 v2 = new Vector3(largeValue, 0, -largeValue);
        Vector3 v3 = new Vector3(largeValue, 0, largeValue);
        ArrayList<Triangle> terrainMesh = new ArrayList<>();
        terrainMesh.add(new TriangleBuilder(v1, v2, v3).build());
        config.setTerrain(terrainMesh);

        //add a box obstacle
        Vector3 boxPos = new Vector3(50, 0, 20);
        BoxParameter bp = new BoxParameter(new Vector3(25, 0, 25), new Vector3(25, 0, -25), new Vector3(0, 20, 0));
        ArrayList<SolidTranslator> boxBodyList = new ArrayList<>();
        boxBodyList.add(new SolidTranslator(BoxPool.getInstance().getInstance(bp), boxPos));
        config.addObstacle(boxBodyList);

        //add a tetrahedron obstacle
        Vector3 tetPos = new Vector3(75, 0, -40);
        TetrahedronParameter tp = new TetrahedronParameter(new Vector3(15, 0, 0), new Vector3(0, 0, 15), new Vector3(8, 8, 10));
        ArrayList<SolidTranslator> tetBodyList = new ArrayList<>();
        tetBodyList.add(new SolidTranslator(TetrahedronPool.getInstance().getInstance(tp), tetPos));
        config.addObstacle(tetBodyList);

        //set the hole
        Vector3 holePosition = new Vector3(100, 0, 0);
        float holeSize = 20;
        config.setHole(holePosition, holeSize);

        float ballRadius = 3, ballMass = 5;
        Vector3 initBallPos = new Vector3();
        //config.addHumanAndBall("martin", ballRadius, ballMass, initBallPos);
        //comment above and uncomment below for bot
        config.addBotAndBall("bot", ballRadius, ballMass, initBallPos);
        mGame = config.game();
    }

    /**
     * Initializes the visualization instance.
     * Precondition: a course is set
     * Postcondition: the visualization is initialized and set
     */
    public void initGraphics()
    {
        mVisual = new GameVisual();
        ArrayList<Vector3f> balls = new ArrayList<>();
        balls.add(new Vector3f());
        mVisual.setBalls(balls);
        mVisual.setTerrain(new TerrainData());

        ArrayList<gameEntity> entities = new ArrayList<>();

        mVisual.setEntities(entities);

        mVisual.setEngine(mGame.getEnigine());
    }

    /**
     * runs the game loop.
     * Precondition: the game and the graphics are set.
     */
    public void run()
    {
        float dt = 1;

        while (mGame.isActive())
        {
            do
            {
                if (mGame.isBusy())
                    printCurrentBall();

                mGame.tick(dt);

                //   mVisual.updateDisplay();

                try
                {
                    Thread.sleep(100);
                } catch (Exception e)
                {
                }
            } while (mGame.isBusy());
        }

    }


    private void printCurrentBall()
    {
        Ball ball = mGame.getBall(mGame.getCurrentPlayers().get(0));
        Position p = physics.constants.CompoMappers.POSITION.get(ball.mEntity);
        Velocity v = physics.constants.CompoMappers.VELOCITY.get(ball.mEntity);
        System.out.println ("current ball at " + p + " moving at " + v);
    }

    private void close()
    {
        mVisual.endDisplay();
    }

    private GameLoader mLoader;
    private Game mGame;
    private GameVisual mVisual;
}
