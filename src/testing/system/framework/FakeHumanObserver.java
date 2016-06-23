package testing.system.framework;

import com.badlogic.gdx.math.Vector3;
import framework.main.Game;
import framework.main.PlayerObserver;

import java.util.Scanner;

/**
 * created 16.06.16
 *
 * @author martin
 */
public class FakeHumanObserver extends PlayerObserver
{
    @Override
    public Vector3 getForce(Game state)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("enter the force you want to apply");
        float x = askForValue("x", input);
        float y = askForValue("y", input);
        float z = askForValue("z", input);
        return new Vector3(x, y, z);
    }


    private float askForValue(String type, Scanner input)
    {
        System.out.println("enter the " + type + " coordinate");
        return input.nextFloat();
    }
}
