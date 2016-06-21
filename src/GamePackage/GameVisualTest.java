package GamePackage;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.badlogic.gdx.math.Vector3;

import Entities.FollowCamera;
import Entities.InputObserver;
import Entities.gameEntity;
import RenderComponents.DisplayManager;

public class GameVisualTest extends GameVisual {
	
	
	public GameVisualTest()	{
		super();
	}
	
	
	public void updateDisplay()	{
		
		updateObjects();
		forceLevelCheck();

		if(forceChangeAccept == false && timepassed >= 1)	{
			forceChangeAccept = true;
			timepassed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RETURN) && currentForce != 0)	{
			//System.out.println("Force now present");
			forcePresent = true;
		}
		
		

        for(gameEntity ball:golfBalls)	{
     	   renderer.processEntity(ball);
     	   //System.out.println("Ball Position: " + ball.getPosition());
        }
		for(gameEntity ob:entities)	{
			renderer.processEntity(ob);
			//System.out.println("Ball Position: " + ob.getPosition());
		}

     	   renderer.processTerrain(terrains.get(0));
        
        if(useFollow == false)	{
     	   renderer.render(light, cam);
        }
        else	{
     	   renderer.render(light, followCam);
			renderer.processEntity(directionArrow);
			directionArrow.setRotY(-(followCam.getYaw() - 180));
			//System.out.println("Angle: " + directionArrow.getRotY());
			Vector3 dir = new Vector3(0,0,1);
			dir.rotate(directionArrow.getRotY(),0,1,0);
			//System.out.println("Vector: " + dir.toString());
        }


        if(Keyboard.isKeyDown(Keyboard.KEY_TAB))	{
     	   if(useFollow == false)
     		   useFollow = true;
     	   else
     		   useFollow = false;
        }
        if(forceChangeAccept == false)
        	timepassed += DisplayManager.getTimeDelat();
        
        DisplayManager.updateDisplay();
	}
	
}
