package GameRun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Entities.FollowCamera;
import Entities.GolfBall;
import Entities.Light;
import Entities.gameEntity;
import GUIs.GUITexture;
import LogicAndExtras.MousePicker;
import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.DisplayManager;
import RenderComponents.GuiRenderer;
import RenderComponents.Loader;
import RenderComponents.MasterRenderer;
import RenderComponents.OBJLoader;
import TerrainComponents.Terrain;

public class TestWithGUI {
	
	MasterRenderer renderer;
	GuiRenderer guiRenderer;
			
			//For loading Object Data
			Loader loader;
			
			//The Scene
			ArrayList<GolfBall> golfBalls;
			ArrayList<gameEntity> entities;
			ArrayList<gameEntity> surrondings;
			List<GUITexture> GuiElements;
			Terrain[][] terrains;
			FollowCamera cam;
			Light light;
			MousePicker mousePick, mousePick2;
			
			
			
			public TestWithGUI()	{
				DisplayManager.createDisplay();
				loader = new Loader();
				renderer = new MasterRenderer(loader);
				guiRenderer = new GuiRenderer(loader);
				golfBalls = new ArrayList<GolfBall>();
				entities = new ArrayList<gameEntity>();
				surrondings = new ArrayList<gameEntity>();
				terrains = new Terrain[2][2];
				GuiElements = new ArrayList<GUITexture>();
				
				setUpEntities();
				setUpTerrain();
				setUpScene();
				setUpGuis();
				createSurrondings();
				
				
				
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
				
				GolfBall golfball = new GolfBall(new Vector3f(4,20,-475), 2);
				//cam.setPosition(new Vector3f(4,20,-422));
				//System.out.println("ID: " + golfball.getModel().getRawModel().getID());
				golfBalls.add(golfball);
				//System.out.println("ID: " + entities.get(0));
			}
			
			public void setUpGuis()	{
				
				GUITexture forceBar = new GUITexture(loader.loadTexture("golf-club"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
				//cam.setPosition(new Vector3f(4,20,-422));
				//System.out.println("ID: " + golfball.getModel().getRawModel().getID());
				GuiElements.add(forceBar);
				//System.out.println("ID: " + entities.get(0));
			}
			
			public void displayAllEntites()	{
				System.out.println("Size Entities:" + entities.size());
				System.out.println("Size Surronding:" + surrondings.size());
				
				for(gameEntity ent: entities)	{
					System.out.print("GameEntity: ");
					int ID = ent.getModel().getRawModel().getID();
					System.out.println();
				}
				System.out.println();
				
				for(gameEntity surronding: surrondings)	{
					System.out.print("surronding: ");
					int ID = surronding.getModel().getRawModel().getID();
					System.out.println();
				}
				
				
			}
		
			
		   
		    public void setUpTerrain(){
		    	
		    		
		    	for(int i = 0; i < terrains.length; i++)	{
		    		for(int j = 0; j < terrains[0].length; j++)	{
			    		terrains[i][j] = new Terrain(i,j, "heightmap");
			    	}
		    	}
		    	
		    	//terrains[0][0].printAllTris();
		    	//terrains[0][0].leafs.get(0).printCoord();
		    	//terrains[0][0].leafs.get(128).printCoord();
		    	//terrains[0][0].leafs.get(1).printCoord();
		    	//terrains[0][0].getAllTetrahedons();
		    }
		    
		   public void setUpScene()	{
			   cam = new FollowCamera(golfBalls.get(0));
			   //cam.setPosition(new Vector3f(4,20,-422));
			   //cam.setPitch(25);
			   
			   light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		   }
		    
		   public void startGame()	{
			   //displayAllEntites();
			   
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
		           
		           for(GolfBall ball:golfBalls)	{
		        	   
		        	   renderer.processEntity(ball);
		           }
		           
		           for(int i = 0; i < terrains.length; i++)	{
			    		for(int j = 0; j < terrains[0].length; j++)	{
			    			renderer.processTerrain(terrains[i][j]);
				    	}
			    	}
		           
		           
		        	guiRenderer.render(GuiElements);
		           
		           
		           
		           renderer.render(light, cam);
		           DisplayManager.updateDisplay();
		       }

		       renderer.cleanUp();
		       loader.cleanUp();
		       DisplayManager.closeDisplay();
		   }
		    
		   
		   public static void main(String[] args)	{
			   TestWithGUI test = new TestWithGUI();
		   }
		   

}
