package framework;

import Editor.Course;
import Editor.CourseLoader;
import Editor.Editor;
import GamePackage.GameLoader;
import GamePackage.GameVisual;
import com.badlogic.gdx.math.Vector3;
import framework.constants.CompoMappers;
import framework.entities.Player;
import framework.testing.MockLoader;
import framework.testing.MockMainMenu;
import physics.components.Position;
import physics.components.Velocity;
import physics.entities.Ball;

import java.io.IOException;

/**
 * Class running the game.
 * @author martin
 */
public class Main
{

    public static void main(String[] args)
    {
        Main main = new Main();
        try {
            new MockMainMenu(main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final float DELTA_TIME = 1;



    /**
     * launches the editor, permitting to create a course file
     */
    public void launchEditor()
    {
        Editor edit = new Editor();
        //show the editor
        //do more editor stuff
    }

    /**
     * loads course from file
     * Postcondition: the course is set
     * @param fileName name of course to load from file
     */
    public void loadGame(String fileName)
    {
       GameLoader load = new GameLoader();
        mGame = load.loadConfig(fileName);
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

        Player active = mGame.getCurrentPlayers().get(0);
        mGame.hit(active, defaultHit);

        while (mGame.isActive())
        {

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

    private Game mGame;
    private GameVisual mVisual;
}
