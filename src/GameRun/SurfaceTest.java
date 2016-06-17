package GameRun;

import java.util.ArrayList;
import java.util.Random;


import Editor.Course;
import Editor.CourseLoader;
import org.lwjgl.input.Keyboard;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.badlogic.gdx.math.Vector3;

import Editor.Course;
import Editor.CourseLoader;
import Entities.*;
import LogicAndExtras.MousePicker;
import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.DisplayManager;
import RenderComponents.Loader;
import RenderComponents.MasterRenderer;
import RenderComponents.OBJLoader;
import TerrainComponents.Terrain;
import TerrainComponents.TerrainData;

public class SurfaceTest {
	
	//For rendering and displaying the Scene
			MasterRenderer renderer;
			
			//For loading Object Data
			Loader loader;
			
			//The Scene
			ArrayList<GolfBall> golfBalls;
			ArrayList<gameEntity> entities;
			ArrayList<gameEntity> surrondings;
			ArrayList<Terrain>terrains;
			FollowCamera followCam;
			freeCamera cam;
			Light light;
			boolean useFollow, targetingState;
			Arrow directionArrow;
			final static float targetYawAmount = 0.1f;
			
			
			
			public SurfaceTest()	{
				DisplayManager.createDisplay();
				loader = new Loader();
				renderer = new MasterRenderer(loader);
				golfBalls = new ArrayList<GolfBall>();
				entities = new ArrayList<gameEntity>();
				surrondings = new ArrayList<gameEntity>();
				terrains = new ArrayList<Terrain>();
				
				
				setUpTerrain();
				setUpEntities();
				setUpScene();
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
				
				GolfBall golfball = new GolfBall(new Vector3f(-500,terrains.get(0).getHeightSimple(-500, -460),-460), 2,false);
				//cam.setPosition(new Vector3f(4,20,-422));
				//System.out.println("ID: " + golfball.getModel().getRawModel().getID());
				golfBalls.add(golfball);
				//System.out.println("ID: " + entities.get(0));
				
				
				directionArrow = new Arrow(golfball);
				targetingState = false;
				surrondings.add(directionArrow);
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
		    	Course course = CourseLoader.loadCourse("test");
		    	TerrainData terraDat = course.getTerrain();
		    	System.out.println("Terrain is flat: " + terraDat.isFlat());
		    	Terrain terra = new Terrain(terraDat);
			   	terrains.add(terra);
			  
		    	
		    	
		    }
		    
		   public void setUpScene()	{
			   useFollow = true;
			   cam = new freeCamera(new Vector3f(4,20,-422));
			   followCam = new FollowCamera(golfBalls.get(0));
			  
			   light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		   }
		   
		   
		    
		   public void startGame()	{
			   //displayAllEntites();
			   
			   while(!Display.isCloseRequested()){
				   if(useFollow == false)	{
					   if(targetingState == false)
						   cam.move();
					}
					else	{
						if(targetingState == false)
							followCam.move();
						
					}
				   
				   
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
		           
		           
		           
		           for(Terrain terra : terrains)	{
		        	   renderer.processTerrain(terra);
		           }
		           
		           
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
		           
		            if(Keyboard.isKeyDown(Keyboard.KEY_RETURN) && !targetingState)	{
		            	targetingState = true;
		            	System.out.println("Targeting State entered");
		            }
		            if(Keyboard.isKeyDown(Keyboard.KEY_MINUS) && targetingState)	{
		            	directionArrow.setRotY(-(followCam.getYaw() - 180));
		            	targetingState = false;
		            	directionArrow.setRotX(0);
		            	directionArrow.setRotZ(0);
		            	System.out.println("Returning to normal targeting");
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
