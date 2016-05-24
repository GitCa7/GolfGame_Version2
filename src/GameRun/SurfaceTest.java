package GameRun;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.badlogic.gdx.math.Vector3;

import Entities.Camera;
import Entities.FollowCamera;
import Entities.FollowCamera;
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
			ArrayList<GolfBall> golfBalls;
			ArrayList<gameEntity> entities;
			ArrayList<gameEntity> surrondings;
			Terrain[][] terrains;
			FollowCamera followCam;
			Camera cam;
			Light light;
			
			
			private boolean useFollow;
			
			public SurfaceTest()	{
				DisplayManager.createDisplay();
				loader = new Loader();
				renderer = new MasterRenderer(loader);
				golfBalls = new ArrayList<GolfBall>();
				entities = new ArrayList<gameEntity>();
				surrondings = new ArrayList<gameEntity>();
				terrains = new Terrain[2][2];
				
				useFollow = false;
				setUpEntities();
				setUpTerrain();
				setUpScene();
				createSurrondings();
				
				
				
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
		    	

		    	terrains[0][0].getAllTetrahedons();
		    }
		    
		   public void setUpScene()	{
			   cam = new Camera(new Vector3f(4,20,-422));
			   followCam = new FollowCamera(golfBalls.get(0));
			   
			   
			   light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		   }
		    
		   public void startGame()	{
			   //displayAllEntites();
			   
			   while(!Display.isCloseRequested()){
				   
				   if(useFollow == false)	{
					   cam.move();
				   }
				   else	{
					   followCam.move();
				   }
		           
		           
		           for(gameEntity plant:surrondings)	{
		        	   renderer.processEntity(plant);
		           }
		           
		           for(gameEntity gameEntity:entities)	{
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

		       renderer.cleanUp();
		       loader.cleanUp();
		       DisplayManager.closeDisplay();
		   }
		    
		   
		   public static void main(String[] args)	{
			   SurfaceTest test = new SurfaceTest();
		   }
		   

}
