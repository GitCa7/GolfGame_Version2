package GameRun;

import Entities.Ball;
import Entities.Camera;
import Entities.Light;
import Entities.Obstacle;
import Entities.entity;
import Entities.gameEntity;
import LogicAndExtras.MousePicker;
import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.DisplayManager;
import RenderComponents.Loader;
import RenderComponents.MasterRenderer;
import RenderComponents.OBJLoader;
import TerrainComponents.Terrain;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import physics.components.Force;
import physics.components.PositionBridge;
import physics.components.Velocity;
import physics.systems.ForceApply;
import physics.systems.Movement;

import java.util.ArrayList;
import java.util.Random;




public class HeightMapSurfaceTest {

	//For rendering and displaying the Scene
		MasterRenderer renderer;
		
		//For loading Object Data
		Loader loader;
		
		//The Scene
		ArrayList<gameEntity> entities;
		ArrayList<gameEntity> surrondings;
		ArrayList<Terrain> terrains;
		Camera cam;
		Light light;
		MousePicker mousePick, mousePick2;
		
		ArrayList<Entity> physicsEntities;
		Engine modelEngine;
		
		public HeightMapSurfaceTest()	{
			DisplayManager.createDisplay();
			loader = new Loader();
			renderer = new MasterRenderer(loader);
			entities = new ArrayList<gameEntity>();
			surrondings = new ArrayList<gameEntity>();
			terrains = new ArrayList<Terrain>();
			cam = new Camera();
			
			physicsEntities = new ArrayList<Entity>();
			modelEngine = new Engine();
			
			createSurrondings();
			setUpTerrain();
			setUpScene();
			//setUpEntities();
			
			mousePick = new MousePicker(cam, renderer.getProjectionMatrix(), terrains.get(0));
			//mousePick2 = new MousePicker(cam, renderer.getProjectionMatrix(), terrains.get(1));
			
			startGame();
			
			
		}



		
		public void createSurrondings()	{
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
	        for(int i = 0; i< 100; i++)	{
	        	
	        	surrondings.add(new gameEntity(grassTextModel, new Vector3f(ran.nextFloat() * 800 - 400, 3, ran.nextFloat() * -600), 180, 0, 0, 3));
	        	surrondings.add(new gameEntity(fernTextModel, new Vector3f(ran.nextFloat() * 800 - 400, 0, ran.nextFloat() * -600), 0, 0, 0, 3));
	        }
		}
		
		/*
<<<<<<< HEAD
		
		public void setUpEntities()	{
			
			gameEntity cube = new Obstacle(new Vector3f(4,20,-455), 3);
			entities.add(cube);
			
		}
		
=======
*//*
		public void setUpEntities ()
		{
			gameEntity golfBall = new Ball();
			PositionBridge pBrid = new PositionBridge (golfBall);
			pBrid.set (4, 20, -455);

			Entity e = new Entity();
			e.add (pBrid);
			e.add (new Velocity());
			e.add (new Force());

			entities.add (golfBall);
		}

		public void setUpEngine()
		{
			for (Entity e : physicsEntities)
				modelEngine.addEntity (e);

			Movement moving = new Movement();
			ForceApply fApplying = new ForceApply();

			modelEngine.addSystem (moving);
			modelEngine.addSystem (fApplying);
		}
>>>>>>> origin/master
	   */
	    public void setUpTerrain(){
	    	Terrain terrain = new Terrain(0,0);
	        //Terrain terrain2 = new Terrain(1,0,"heightmap");
	        terrains.add(terrain);
	        //terrains.add(terrain2);
	    }
	    
	   public void setUpScene()	{
		   cam.setPosition(new Vector3f(-400,400,400));
		   cam.setPitch(25);
		   
		   light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
	   }
	    
	   public void startGame()	{
		   while(!Display.isCloseRequested()){
	           cam.move();
	           
	           
	           for(gameEntity plant:surrondings)	{
	        	   renderer.processEntity(plant);
	           }
	           
	           for(gameEntity gameEntity:entities)	{
	        	   renderer.processEntity(gameEntity);
	           }
	           
	           for(Terrain terrain:terrains)	{
	        	   renderer.processTerrain(terrain);
	           }


			   modelEngine.update(1);
	           
	           //mousePick.update();
	           //mousePick2.update();

	           /*
	           mousePick.update();
	           //mousePick2.update();
	           
	           Vector3f Vec = mousePick.getCurrentTerrainPoint();
	           //Vector3f Vec2 = mousePick2.getCurrentTerrainPoint();
	           if(mousePick.getCurrentTerrainPoint() != null)	{
	        	   //System.out.println(Vec.x + "\t|\t" + Vec.y + "\t|\t" + Vec.z);
	           }
	           
	           if(mousePick2.getCurrentTerrainPoint() != null)	{
	        	   System.out.println(Vec2.x + "\t|\t" + Vec2.y + "\t|\t" + Vec2.z);
	           }
	           */
	           renderer.render(light, cam);
	           DisplayManager.updateDisplay();
	       }

	       renderer.cleanUp();
	       loader.cleanUp();
	       DisplayManager.closeDisplay();
	   }
	    
	   
	   
	   public static void main(String[] args)	{
		  HeightMapSurfaceTest Test = new HeightMapSurfaceTest();
	   }
	
}
