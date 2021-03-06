package framework.main;

import Editor.Editor;
import GamePackage.GameLoader;
import GamePackage.GameVisual;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector3;
import framework.entities.Ball;
import framework.entities.Player;
import framework.systems.GoalSystem;
import framework.systems.TurnSystem;
import physics.systems.CollisionDetectionSystem;
import physics.systems.FrictionSystem;
import physics.systems.Movement;
import physics.systems.NonPenetrationSystem;
import testing.system.framework.MockMainMenu;
import physics.components.Position;
import physics.components.Velocity;
import java.io.File;
import java.io.IOException;

/**
 * Class running the game.QQ
 * @author martin
 */
public class Main
{

    public static void main(String[] args)
    {


        Movement.DEBUG= true;
        //NonPenetrationSystem.DEBUG = true;
        //CollisionImpactSystem.DEBUG = true;
        //CollisionDetectionSystem.DEBUG = true;
        Game.DEBUG = true;
        //GoalSystem.DEBUG = true;
        //FrictionSystem.DEBUG = true;

       // TurnSystem.DEBUG = true;

        Main main = new Main();
        try {
            new MockMainMenu(main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final float DELTA_TIME = .02f;
    public static final int FRAMES = (int) (4 / (DELTA_TIME ));

    public static String SAVE_FILE = "C:\\Users\\Asus\\Documents\\UNI\\GolfGame_Version2\\botResults.csv";


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


        boolean botThrowException = false;

        while (mGame.isActive() && !botThrowException)
        {

            do
            {
                controlFPS.startFrame();
                try {
                    mGame.tick(DELTA_TIME);
                }
                catch (NullPointerException npe)
                {
                    botThrowException = true;
                    npe.printStackTrace();
                }
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

        if (mLoader.mBobs.getLog() != null)
        {
            try
            {
                mLoader.mBobs.getLog().exportLog(new File(SAVE_FILE), BotObserver.BOT_TIME, BotObserver.BOT_REMAINING_MOVES);
            }
            catch (Exception e) {e.printStackTrace();}
        }
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
