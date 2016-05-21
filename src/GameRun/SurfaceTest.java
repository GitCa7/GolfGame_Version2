package GameRun;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.GolfBall;
import Entities.Light;
import Entities.Obstacle;
import Entities.crate;
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

public class SurfaceTest {
	
	//For rendering and displaying the Scene
			MasterRenderer renderer;
			
			//For loading Object Data
			Loader loader;
			
			//The Scene
			ArrayList<gameEntity> entities;
			ArrayList<gameEntity> surrondings;
			Terrain[][] terrains;
			Camera cam;
			Light light;
			MousePicker mousePick, mousePick2;
			
			
			
			public SurfaceTest()	{
				DisplayManager.createDisplay();
				loader = new Loader();
				renderer = new MasterRenderer(loader);
				entities = new ArrayList<gameEntity>();
				surrondings = new ArrayList<gameEntity>();
				terrains = new Terrain[1][1];
				cam = new Camera();
				
				
				setUpScene();
				createSurrondings();
				setUpTerrain();
				
				setUpEntities();
				
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
			
			
			public void setUpEntities()	{
				
				gameEntity crate = new Obstacle(new Vector3f(4,20,-455), 3);
				gameEntity golfball = new GolfBall(new Vector3f(4,20,-475), 3);
				entities.add(crate);
				entities.add(golfball);
			}
			
		   
		    public void setUpTerrain(){
		    	
		    		
		    	for(int i = 0; i < terrains.length; i++)	{
		    		for(int j = 0; j < terrains[0].length; j++)	{
			    		terrains[i][j] = new Terrain(i,j);
			    	}
		    	}
		    	
		    	terrains[0][0].printAllTris();
		    	//terrains[0][0].leafs.get(0).printCoord();
		    	//terrains[0][0].leafs.get(128).printCoord();
		    	//terrains[0][0].leafs.get(1).printCoord();
		    }
		    
		   public void setUpScene()	{
			   cam.setPosition(new Vector3f(4,20,-422));
			   //cam.setPitch(25);
			   
			   light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		   }
		    
		   public void startGame()	{
			   while(!Display.isCloseRequested()){
		           cam.move();
		           
		           
		           for(gameEntity plant:surrondings)	{
		        	   renderer.processEntity(plant);
		           }
		           
		           for(gameEntity gameEntity:entities)	{
		        	   //gameEntity.increaseRotation(0, 1f, 0);
		        	   //gameEntity.increasePosition(0.1f, 0, 0);
		        	   renderer.processEntity(gameEntity);
		           }
		           
		           for(int i = 0; i < terrains.length; i++)	{
			    		for(int j = 0; j < terrains[0].length; j++)	{
			    			renderer.processTerrain(terrains[i][j]);
				    	}
			    	}
		           
		           
		           //mousePick.update();
		           //mousePick2.update();
		           
		           //Vector3f Vec = mousePick.getCurrentTerrainPoint();
		           //Vector3f Vec2 = mousePick2.getCurrentTerrainPoint();
		           /*
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
			   SurfaceTest test = new SurfaceTest();
		   }
		   

}
