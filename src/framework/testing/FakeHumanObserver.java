package framework.testing;

import Entities.FollowCamera;
import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.PlayerObserver;
import framework.entities.Player;

import java.util.Scanner;

/**
 * created 16.06.16
 *
 * @author martin
 */
public class FakeHumanObserver extends PlayerObserver
{
    private FollowCamera cam;

    public void setCam(FollowCamera cam){
        this.cam = cam;
    }
    @Override
    public Vector3 getForce(Game state)
    {
        return cam.inputLoop();
    }


    private float askForValue(String type, Scanner input)
    {
        System.out.println("enter the " + type + " coordinate");
        return input.nextFloat();
    }
}
