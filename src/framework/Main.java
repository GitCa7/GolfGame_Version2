package framework;

import Editor.Course;
import Editor.Editor;
import GamePackage.GameVisual;
import com.badlogic.gdx.math.Vector3;
import framework.constants.CompoMappers;
import framework.entities.Player;
import framework.testing.MockLoader;
import framework.testing.MockMainMenu;
import physics.components.Position;
import physics.components.Velocity;
import physics.entities.Ball;

/**
 * Class running the game.
 * @author martin
 */
public class Main
{

    public static void main(String[] args)
    {
        Main main = new Main();
        new MockMainMenu(main);
    }

    public static final float DELTA_TIME = 1;


    public Main()
    {
        mCourse = null;
    }

    /**
     * launches the editor, permitting to create a course file
     */
    public void launchEditor()
    {
        //Editor edit = new Editor();
        //show the editor
        //do more editor stuff
    }

    /**
     * loads course from file
     * Postcondition: the course is set
     * @param fileName name of course to load from file
     */
    public void loadCourse(String fileName)
    {
        throw new UnsupportedOperationException("loading a course is not implemented");
    }

    /**
     * Initializes a game instance.
     * Precondition: a course is set
     * Postcondition: the game is initialized and set
     */
    public void initGame()
    {
        MockLoader loader = new MockLoader();
        mGame = loader.load();
    }

    /**
     * Initializes the visualization instance.
     * Precondition: a course is set
     * Postcondition: the visualization is initialized and set
     */
    public void initGraphics()
    {
        throw new UnsupportedOperationException("graphics not supported yet");
    }

    /**
     * runs the game loop.
     * Precondition: the game and the graphics are set.
     */
    public void run()
    {
        Vector3 defaultHit = new Vector3(50, 25, 0);

        while (mGame.isActive())
        {
            System.out.println ("into loop");
            Player active = mGame.getCurrentPlayers().get(0);
            mGame.hit(active, defaultHit);
            do
            {
                mGame.tick(DELTA_TIME);
                printCurrentBall();
                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception e) {}

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

    private Course mCourse;
    private Game mGame;
    private GameVisual mVisual;
}
