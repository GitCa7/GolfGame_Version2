package GameRun;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Entities.Ball;
import Entities.Camera;
import Entities.Light;
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
		
		
		
		public HeightMapSurfaceTest()	{
			DisplayManager.createDisplay();
			loader = new Loader();
			renderer = new MasterRenderer(loader);
			entities = new ArrayList<gameEntity>();
			surrondings = new ArrayList<gameEntity>();
			terrains = new ArrayList<Terrain>();
			cam = new Camera();
			
			
			
			createSurrondings();
			setUpTerrain();
			setUpScene();
			//setUpEntities();
			
			//mousePick = new MousePicker(cam, renderer.getProjectionMatrix(), terrains.get(0));
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
		public void setUpEntities()	{
			gameEntity golfBall = new Ball(0, 0, null);
			physics.entities.add(golfBall);
			
		}
		*/
	   
	    public void setUpTerrain(){
	    	Terrain terrain = new Terrain(0,0,"heightmap");
	        Terrain terrain2 = new Terrain(1,0,"heightmap");
	        terrains.add(terrain);
	        terrains.add(terrain2);
	    }
	    
	   public void setUpScene()	{
		   cam.setPosition(new Vector3f(4,20,-422));
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
	           
	           
	           //mousePick.update();
	           //mousePick2.update();
	           /*
	           Vector3f Vec = mousePick.getCurrentTerrainPoint();
	           Vector3f Vec2 = mousePick2.getCurrentTerrainPoint();
	           if(mousePick.getCurrentTerrainPoint() != null)	{
	        	   System.out.println(Vec.x + "\t|\t" + Vec.y + "\t|\t" + Vec.z);
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
