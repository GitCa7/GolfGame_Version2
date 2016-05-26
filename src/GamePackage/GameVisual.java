package GamePackage;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.FollowCamera;
import Entities.GolfBall;
import Entities.Light;
import Entities.gameEntity;
import GameRun.MousePicker2;
import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.DisplayManager;
import RenderComponents.Loader;
import RenderComponents.MasterRenderer;
import RenderComponents.OBJLoader;
import TerrainComponents.PointNode;
import TerrainComponents.Terrain;

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
	Camera cam;
	Light light;
	
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

	
	public void setTerrain(Terrain terra)	{
		terrains.add(terra);
		setUpEntities();
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
	
	
	public void setUpScene()	{
		cam = new Camera(new Vector3f(4,20,-422));
		followCam = new FollowCamera(golfBalls.get(0));   
		light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
	}
}
