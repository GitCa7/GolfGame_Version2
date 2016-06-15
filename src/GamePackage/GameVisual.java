package GamePackage;

import java.util.ArrayList;
import java.util.Random;

import framework.Game;
import physics.components.Body;
import physics.components.Force;
import physics.components.Friction;
import physics.components.Mass;
import physics.components.Position;
import physics.constants.CompoMappers;
import physics.constants.Families;
import physics.geometry.spatial.Box;
import physics.geometry.spatial.BoxBuilder;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import Entities.Arrow;
import Entities.Camera;
import Entities.FollowCamera;
import Entities.GolfBall;
import Entities.Light;
import Entities.gameEntity;
import GamePackage.PhysicsTranslator;
import LogicAndExtras.MousePicker;
import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;

import RenderComponents.DisplayManager;
import RenderComponents.Loader;
import RenderComponents.MasterRenderer;
import RenderComponents.OBJLoader;
import TerrainComponents.PointNode;
import TerrainComponents.Terrain;
import TerrainComponents.TerrainData;

public class GameVisual {
	MasterRenderer renderer;
	
	//For loading Object Data
	Loader loader;
	
	//The Scene
	ArrayList<GolfBall> golfBalls;
	ArrayList<gameEntity> entities;
	ArrayList<gameEntity> surrondings;
	ArrayList<Terrain> terrains;
	FollowCamera followCam;
	Arrow targetDestination;
	Camera cam;
	Light light;
	
	MousePicker mousePick;
	
	//The Physics
	private Engine gameEngine;
	
	
	private boolean useFollow;
	
	public GameVisual()	{
		DisplayManager.createDisplay();
		loader = new Loader();
		renderer = new MasterRenderer(loader);
		
		
		
		golfBalls = new ArrayList<GolfBall>();
		entities = new ArrayList<gameEntity>();
		surrondings = new ArrayList<gameEntity>();
		terrains = new ArrayList<Terrain>();
		
		
	}

	
	public void setTerrain(TerrainData terra)	{
		Terrain terraNew = new Terrain(terra);
		terrains.add(terraNew);
		
		//setUpEntities();
		createSurrondings();
		setUpScene();
	}
	
	public void setUpEntities()	{
		Terrain tmpTerrain = terrains.get(0);
		int middle = tmpTerrain.leafs.size() / 2;
		PointNode centerNode = tmpTerrain.leafs.get(middle);
		Vector3f center = new Vector3f(centerNode.getCoordinates().x, centerNode.getCoordinates().y + 10, centerNode.getCoordinates().z);
		
		GolfBall golfball = new GolfBall(new Vector3f(4,5,-475), 1);
		golfBalls.add(golfball);
		
		
	}
	
	public void createSurrondings()	{
		Terrain tmpTerrain = terrains.get(0);
		
		RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
        ModelTexture grassModelText = new ModelTexture(loader.loadTexture("grassTexture"));
        grassModelText.setHasTranspercy(true);
        grassModelText.setUseFakeLighting(true);
        
        RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
        ModelTexture fernModelText = new ModelTexture(loader.loadTexture("fern"));
        fernModelText.setHasTranspercy(true);
        fernModelText.setUseFakeLighting(true);
        
        TexturedModel grassTextModel = new TexturedModel(grassModel, grassModelText);
        TexturedModel fernTextModel = new TexturedModel(fernModel, fernModelText);
        
        Random ran = new Random();
        float x,z;

		for(int k = 0; k < 50; k++)	{
			x = ran.nextFloat() * ((tmpTerrain.x_end - tmpTerrain.x_start));
			z = ran.nextFloat() * ((tmpTerrain.z_end - tmpTerrain.z_start));
			surrondings.add(new gameEntity(grassTextModel, new Vector3f(x, tmpTerrain.getHeightSimple(x, z) + 3, z), 180, 0, 0, 3));
			x = ran.nextFloat() * (tmpTerrain.x_end - tmpTerrain.x_start);
			z = ran.nextFloat() * (tmpTerrain.z_end - tmpTerrain.z_start);
			surrondings.add(new gameEntity(fernTextModel, new Vector3f(x, tmpTerrain.getHeightSimple(x, z), z), 0, 0, 0, 3));
        }
	}
	
	public void setEngine(Engine newEngine)	{
		gameEngine = newEngine; 
		checkGolfBallAmount();
		targetDestination = new Arrow(golfBalls.get(0));
	}
	
	public void updateObjects()	{
		
		
		int pos = 0;
		for(Entity ent : gameEngine.getEntitiesFor(Families.MOVING))	{
			Vector3 position = CompoMappers.POSITION.get(ent);
			//System.out.println("Delta: " + position);
			Vector3f fPos = new Vector3f(position.x, position.y, position.z);
			golfBalls.get(pos).setPosition(fPos);
			pos++;
		}
		
	}
	
	public void checkGolfBallAmount()	{
		if(golfBalls.size() != gameEngine.getEntities().size())	{
			GolfBall tmpBall;
			int pos = 0;
			for(Entity ent : gameEngine.getEntitiesFor(Families.MOVING))	{
				if(golfBalls.size() == 0 || golfBalls.get(pos) == null)	{
					Vector3 position = CompoMappers.POSITION.get(ent);
					Vector3f fPos = new Vector3f(position.x, position.y, position.z);
					golfBalls.add(new GolfBall(fPos, 2));
				}
				pos++;
			}
			
		}
	}
	
	
	private void updateArrow()	{
		
		mousePick.update();
		if(useFollow == true)	{
			if(mousePick.getCurrentTerrainPoint() != null)	{
				targetDestination.setAsTarget(mousePick.getCurrentTerrainPoint(), golfBalls.get(0));
			}
			else	{
				targetDestination.setPosition(golfBalls.get(0));
			}
		}
	}
	
	public void setUpScene()	{
		cam = new Camera(new Vector3f(4,20,-422));
		followCam = new FollowCamera(golfBalls.get(0));
		light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
	}
	
	public boolean stillDispalyed()	{
		return !Display.isCloseRequested();
	}
	
	public void startDisplay()	{
		mousePick = new MousePicker(followCam, renderer.getProjectionMatrix(), terrains.get(0));
		useFollow = true;
	}
	
	public void updateDisplay()	{
		updateObjects();
		
		if(useFollow == false)	{
			cam.move();
		}
		else	{
			followCam.move();
		}

        for(gameEntity ball:golfBalls)	{
     	   renderer.processEntity(ball);
     	   //System.out.println("Ball Position: " + ball.getPosition());
        }

        for(Terrain terrain:terrains)	{
     	   renderer.processTerrain(terrain);
        }
        
        if(useFollow == false)	{
     	   renderer.render(light, cam);
        }
        else	{
     	   renderer.render(light, followCam);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_TAB))	{
     	   if(useFollow == false)
     		   useFollow = true;
     	   else
     		   useFollow = false;
        }
        
        DisplayManager.updateDisplay();
	}
	
	public void endDisplay()	{
		renderer.cleanUp();
	    loader.cleanUp();
	    DisplayManager.closeDisplay();
	}
	
	
	
	
	
}
