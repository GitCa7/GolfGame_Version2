package GamePackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Entities.*;
import GUIs.GUITexture;
import TerrainComponents.TerrainGeometryCalc;
import framework.testing.HumanObserver;
import physics.components.Position;

import physics.constants.CompoMappers;
import physics.constants.Families;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;


import LogicAndExtras.MousePicker;
import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;

import RenderComponents.DisplayManager;
import RenderComponents.GuiRenderer;
import RenderComponents.Loader;
import RenderComponents.MasterRenderer;
import RenderComponents.OBJLoader;
import TerrainComponents.PointNode;
import TerrainComponents.Terrain;
import TerrainComponents.TerrainData;



public class GameVisual {
	MasterRenderer renderer;
	GuiRenderer guiRenderer;
	
	//For loading Object Data
	Loader loader;
	
	//The Scene
	ArrayList<GolfBall> golfBalls;
	ArrayList<gameEntity> entities;
	ArrayList<gameEntity> surrondings;
	List<GUITexture> GuiElements;
	ArrayList<Terrain> terrains;
	
	public Arrow directionArrow;
	
	FollowCamera followCam;
	Arrow targetDestination;
	Camera cam;
	Light light;
	float currentForce;
	float currentBall;
	Vector3f hitDirection;
	boolean forcePresent, forceChangeAccept;
	boolean targetingState;
	float timepassed;
	TerrainGeometryCalc calc = new TerrainGeometryCalc();
	Hole hole;
	
	private static final float forceLvlMax = 3;
	private static final float power = 250;
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
		currentForce = 0;
		hitDirection = new Vector3f(-1, 0, -1);
		forcePresent = false;
		forceChangeAccept = true;
		timepassed = 0;
		directionArrow = new Arrow(new Vector3f(-500,20,-460), 1.5f);
		GuiElements = new ArrayList<GUITexture>();
		guiRenderer = new GuiRenderer(loader);
		
		targetingState = false;
		
		
		
		setUpGuis();
		surrondings.add(directionArrow);
		
		
		
	}
	
	
	
	
	public void setBalls(ArrayList<Vector3f> balls,ArrayList<HumanObserver>  obs){
		ArrayList<GolfBall> tmp = new ArrayList();
		for (int i =0;i<balls.size();i++){
			GolfBall ball = new GolfBall(new Vector3f(balls.get(i).x,balls.get(i).y+5,balls.get(i).z),5,false);
			FollowCamera tmp2 = new FollowCamera(ball,this);
			obs.get(i).setCam(tmp2);
			tmp.add(ball);
		}
		golfBalls = tmp;
	}
	public void setEntities(ArrayList<gameEntity> obs){
		entities = obs;
	}
	
	public void setTerrain(TerrainData terra)	{
		Terrain terraNew = new Terrain(terra);
		terrains.add(terraNew);
		
		//setUpEntities();
		createSurrondings();
		setUpScene();
	}
	
	public void setUpGuis() {

		GUITexture forceBar = new GUITexture(loader.loadTexture("/GuiTextures/force11"), new Vector2f(0.85f, -0.77f),
				new Vector2f(0.15f, 0.225f));
		// cam.setPosition(new Vector3f(4,20,-422));
		// System.out.println("ID: " +
		// golfball.getModel().getRawModel().getID());
		GuiElements.add(forceBar);
		
		GUITexture forceBar2 = new GUITexture(loader.loadTexture("/GuiTextures/force33"), new Vector2f(0.85f, -0.77f),
				new Vector2f(0.15f, 0.225f));
		GuiElements.add(forceBar2);
		GuiElements.get(1).setOriginal(new Vector2f(0.15f, 0.225f));
		GUITexture golfClub = new GUITexture(loader.loadTexture("/GuiTextures/golf-club2"), new Vector2f(0.6f, -0.9f),
				new Vector2f(0.15f, 0.225f));
		GuiElements.add(golfClub);
	}
	
	public void updateForceGui(boolean forceincrease)	{
		Vector2f currScale = GuiElements.get(1).getScale();
		Vector2f max = GuiElements.get(1).getOriginal();
		Vector2f min = new Vector2f(0.05f, 0.05f);
		
		if(forceincrease)	{
			System.out.println("ForceIncrease = true: \nCurrent Scale: " + currScale + "max: " + max);
			if(currScale.x >= max.x || currScale.y >= max.y)	{
				forceincrease = false;
			}
			GuiElements.get(1).reScale(1.01f);
		}
		else	{
			System.out.println("ForceIncrease = false: \nCurrent Scale: " + currScale + "min: " + min);
			if(currScale.x <= min.x || currScale.y <= min.y)	{
				forceincrease = true;
			}
			else	{
				GuiElements.get(1).reScale(0.99f);
			}
		}
	}
	
	public boolean counterIncrease(boolean forceincrease)	{
		Vector2f currScale = GuiElements.get(1).getScale();
		Vector2f max = GuiElements.get(1).getOriginal();
		Vector2f min = new Vector2f(0.02f, 0.02f);
		if(forceincrease)	{
			if(currScale.x >= max.x || currScale.y >= max.y)	{
				return false;
			}
			else 	{
				return true;
			}
		}
		else	{
			if(currScale.x <= min.x || currScale.y <= min.y)	{
				return true;
			}
			else 	{
				return false;
			}
		}
		
	}
	
	public float getForce()	{
		Vector2f original = GuiElements.get(1).getOriginal();
		Vector2f current = GuiElements.get(1).getScale();
		float origLength = original.length();
		float currLength = current.length();
		return currLength / origLength;
	}
	
	public void setUpEntities()	{
		Terrain tmpTerrain = terrains.get(0);
		int middle = tmpTerrain.leafs.size() / 2;
		PointNode centerNode = tmpTerrain.leafs.get(middle);
		Vector3f center = new Vector3f(centerNode.getCoordinates().x, centerNode.getCoordinates().y + 10, centerNode.getCoordinates().z);

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
			Vector3f fPos = new Vector3f(position.x, position.y + 2, position.z);
			golfBalls.get(pos).setPosition(fPos);
			directionArrow.setPosition(new Vector3f(fPos.x,fPos.y+5,fPos.z));
			pos++;
		}
	
		
	}
	public void setHole(Vector3f pos,float scale){
		hole = new Hole(pos,scale);
	}
	
	public void forceLevelCheck()	{
		if(currentForce > 3)	{
			currentForce = 0;
		}
		//System.out.println("CurrentForce: " + currentForce);
		if(Keyboard.isKeyDown(Keyboard.KEY_1) && currentForce < forceLvlMax  && forceChangeAccept)	{
			currentForce++;
			//System.out.println("CurrentForce set to: " + currentForce);
			forceChangeAccept = false;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_2) && currentForce > 0 && forceChangeAccept)	{
			currentForce--;
			//System.out.println("CurrentForce set to: " + currentForce);
			forceChangeAccept = false;
		}
	}
	
	public Vector3 deliverForce()	{
		Vector3 returnForce;
		Vector3 dir = new Vector3(0,0,1);
		dir.rotate(directionArrow.getRotY(),0,1,0);
		if(currentForce != 0)	{
			//System.out.println("NewForce = " + hitDirection + "(hitDirection) scaled by: " + currentForce + " * 400");
			
			Vector3f newForce = new Vector3f(dir.x * (currentForce * power), dir.y * (currentForce * power), dir.z * (currentForce * power));
			
			returnForce = new Vector3(newForce.x, newForce.y, newForce.z);
		}
		else	{
			returnForce = null;
		}
		//System.out.println(returnForce);
		currentForce = 0;
		return returnForce;
	}
	
	public boolean hasForce()	{
		return forcePresent;
	}
	
	public void setForcePresent(boolean value)	{
		forcePresent = value;
	}
	
	
	public void updateObstacles()	{
		//directionArrow = new Arrow(new Vector3f(0,0,0), 1);
		int pos = 0;
		crate Crate;
		for(Entity ent : gameEngine.getEntitiesFor(Families.COLLIDING))	{
			Vector3 position = CompoMappers.POSITION.get(ent);
			Vector3f fPos = new Vector3f(position.x, position.y, position.z);
			Crate = new crate(fPos, 1);
			surrondings.add(Crate);
		}
		
	}
	
	public void checkGolfBallAmount()	{
		if(golfBalls.size() != gameEngine.getEntities().size())	{
			GolfBall tmpBall;
			int pos = 0;
			for(Entity ent : gameEngine.getEntitiesFor(Families.MOVING))	{
				if(golfBalls.size() == 0 || golfBalls.get(pos) == null)	{
					Vector3 position = CompoMappers.POSITION.get(ent);
					Vector3f fPos = new Vector3f(position.x, position.y + 1, position.z);
					golfBalls.add(new GolfBall(fPos, 2,false));
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
		followCam = new FollowCamera(golfBalls.get(0),this);
		light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
	}
	
	public boolean stillDispalyed()	{
		return !Display.isCloseRequested();
	}
	
	
	
	public void startDisplay()	{
		mousePick = new MousePicker(followCam, renderer.getProjectionMatrix(), terrains.get(0));
		useFollow = true;
		System.out.println(calc.terrainIsFlat(terrains.get(0).toData()));
		updateObstacles();
		terrains.get(0).toData().printVerts();
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
		for(Entity a:gameEngine.getEntities()){
			if(a.getComponent(Position.class)!=null) {
				Vector3 pos = a.getComponent(Position.class);
				Obstacle tmp = new Obstacle(new Vector3f(pos.x, pos.y, pos.z), 0.5f);
				renderer.processEntity(tmp);
			}
		}

		for(gameEntity ob:entities)	{
			renderer.processEntity(ob);
			//System.out.println("Ball Position: " + ob.getPosition());
		}

     	   renderer.processTerrain(terrains.get(0));
        	renderer.processEntity(hole);
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
        
        guiRenderer.render(GuiElements);
        DisplayManager.updateDisplay();
	}
	
	public void endDisplay()	{
		renderer.cleanUp();
		guiRenderer.cleanUp();
	    loader.cleanUp();
	    DisplayManager.closeDisplay();
	}
	
	
	
	
	
}