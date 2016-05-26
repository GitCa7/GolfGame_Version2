package GameRun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.badlogic.gdx.math.Vector3;

import Entities.Camera;
import Entities.FollowCamera;
import Entities.FollowCamera;
import Entities.GolfBall;
import Entities.Hole;
import Entities.Light;
import Entities.Obstacle;
import Entities.crate;
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

public class SurfaceTest {
	
	//For rendering and displaying the Scene
			MasterRenderer renderer;
			GuiRenderer guiRender;
			
			//For loading Object Data
			Loader loader;
			
			//The Scene
			ArrayList<GolfBall> golfBalls;
			ArrayList<gameEntity> entities;
			ArrayList<gameEntity> surrondings;
			
			ArrayList<GUITexture> guis;
			
			Terrain[][] terrains;
			FollowCamera followCam;
			Camera cam;
			Light light;
			
			MousePicker2 mousePick;
			
			
			private boolean useFollow;
			
			public SurfaceTest()	{
				DisplayManager.createDisplay();
				loader = new Loader();
				renderer = new MasterRenderer(loader);
				guiRender = new GuiRenderer(loader);
				
				golfBalls = new ArrayList<GolfBall>();
				entities = new ArrayList<gameEntity>();
				surrondings = new ArrayList<gameEntity>();
				terrains = new Terrain[2][2];
				guis = new ArrayList<GUITexture>();
				
				useFollow = false;
				setUpEntities();
				setUpTerrain();
				setUpGuis();
				setUpScene();
				
				createSurrondings();
				
				
				//mousePick = new MousePicker2(cam, renderer.getProjectionMatrix(), terrains);
				
				startGame();
				
				
			}
			
			
			public void setUpGuis()	{
				GUITexture forceBar = new GUITexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
				guis.add(forceBar);
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
		        float x,z;
		        for(int i = 0; i < terrains.length;i++)	{
		        	for(int j = 0; j < terrains[0].length;j++)	{
		        		System.out.println("For Terrain: " + terrains[i][j]);
		        		System.out.println("Infos: X\t" + terrains[i][j].x_start + "\t " + terrains[i][j].x_end + "\n\t\t: " + terrains[i][j].z_start + "\t " + terrains[i][j].z_end + "\n");
		        		
				        for(int k = 0; k < 50; k++)	{
				        	x = ran.nextFloat() * ((terrains[i][j].x_end - terrains[i][j].x_start) * i);
				        	z = ran.nextFloat() * ((terrains[i][j].z_end - terrains[i][j].z_start) * j);
				        	surrondings.add(new gameEntity(grassTextModel, new Vector3f(x, terrains[i][j].getHeightSimple(x, z) + 3, z), 180, 0, 0, 3));
				        	x = ran.nextFloat() * (terrains[i][j].x_end - terrains[i][j].x_start);
				        	z = ran.nextFloat() * (terrains[i][j].z_end - terrains[i][j].z_start);
				        	surrondings.add(new gameEntity(fernTextModel, new Vector3f(x, terrains[i][j].getHeightSimple(x, z), z), 0, 0, 0, 3));
				        } 
			        }
		        }
			}
			
			
			public void setUpEntities()	{
				
				GolfBall golfball = new GolfBall(new Vector3f(4,5,-475), 1);
				//cam.setPosition(new Vector3f(4,20,-422));
				//System.out.println("ID: " + golfball.getModel().getRawModel().getID());
				golfBalls.add(golfball);
				//System.out.println("ID: " + entities.get(0));
				
				gameEntity crate = new crate(new Vector3f(4,20,-490), 2);
				gameEntity crate2 = new crate(new Vector3f(4,20,-500), 20);
				gameEntity crate3 = new crate(new Vector3f(4,20,-510), 20);
				
				entities.add(crate);
				entities.add(crate2);
				entities.add(crate3);
				
				gameEntity hole = new Hole(new Vector3f(4,1,-475), 5);
				entities.add(hole);
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
		    	

		    	
		    }
		    
		   public void setUpScene()	{
			   cam = new Camera(new Vector3f(4,20,-422));
			   followCam = new FollowCamera(golfBalls.get(0));
			   
			   
			   light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		   }
		    
		   public void startGame()	{
			   //displayAllEntites();
			   
			   Vector3f direction, intersect;
			   
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
		           
		           
		           guiRender.render(guis);
		           
		           
		           
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
			   guiRender.cleanUp();
		       renderer.cleanUp();
		       loader.cleanUp();
		       DisplayManager.closeDisplay();
		   }
		    
		   
		   public static void main(String[] args)	{
			   SurfaceTest test = new SurfaceTest();
		   }
		   

}
