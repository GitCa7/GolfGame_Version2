package framework;

import Editor.Editor;
import GamePackage.GameLoader;
import GamePackage.GameVisual;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector3;
import framework.entities.Player;
import framework.systems.GoalSystem;
import framework.testing.MockMainMenu;
import physics.components.Position;
import physics.components.Velocity;
import physics.entities.Ball;
import physics.systems.CollisionDetectionSystem;
import physics.systems.CollisionImpactSystem;
import physics.systems.Movement;

import java.io.IOException;

/**
 * Class running the game.QQ
 * @author martin
 */
public class Main
{

    public static void main(String[] args)
    {

     //   Movement.DEBUG= true;
     //   CollisionImpactSystem.DEBUG = true;
     //   CollisionDetectionSystem.DEBUG = true;
        Game.DEBUG = true;
        GoalSystem.DEBUG = true;

        Main main = new Main();
        try {
            new MockMainMenu(main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final float DELTA_TIME = 0.05f;
    public static final int FRAMES = (int) (1.2 * 1000 * DELTA_TIME);



    /**
     * launches the editor, permitting to create a course file
     */
    public void launchEditor()
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Editor a = new Editor();
        LwjglApplication b =new LwjglApplication(a, config);

    }

    /**
     * loads course from file
     * Postcondition: the course is set
     * @param fileName name of course to load from file
     */
    public void loadGame(String fileName)
    {
        mLoader = new GameLoader();
        mGame = mLoader.loadConfig(fileName);
        initGraphics();
    }



    /**
     * Initializes the visualization instance.
     * Precondition: a course is set
     * Postcondition: the visualization is initialized and set
     */
    public void initGraphics()
    {
        mVisual = mLoader.loadVisual(mGame);

    }

    /**
     * runs the game loop.
     * Precondition: the game and the graphics are set.
     */
    public void run()
    {
        mVisual.startDisplay();
        Vector3 defaultHit = new Vector3(50, 25, 0);

        Player active = mGame.getCurrentPlayers().get(0);
        //mGame.hit(active, defaultHit);

        FPSController controlFPS = new FPSController(FRAMES);

        while (mGame.isActive())
        {

            do
            {
                controlFPS.startFrame();
                mGame.tick(DELTA_TIME);
                mVisual.updateDisplay();
                controlFPS.endFrame();
                /*printCurrentBall();
                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception e) {}*/

            } while (mGame.isBusy());

        }

        mVisual.endDisplay();
    }


    private void printCurrentBall()
    {
        Ball ball = mGame.getBall(mGame.getCurrentPlayers().get(0));
        Position p = physics.constants.CompoMappers.POSITION.get(ball.mEntity);
        Velocity v = physics.constants.CompoMappers.VELOCITY.get(ball.mEntity);
        System.out.println ("current ball at " + p + " moving at " + v);
    }

    private GameLoader mLoader;
    private Game mGame;
    private GameVisual mVisual;
}
