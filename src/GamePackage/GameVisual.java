package GamePackage;

import java.util.ArrayList;

import Entities.Camera;
import Entities.FollowCamera;
import Entities.GolfBall;
import Entities.Light;
import Entities.gameEntity;
import RenderComponents.DisplayManager;
import RenderComponents.Loader;
import RenderComponents.MasterRenderer;
import TerrainComponents.Terrain;

public class GameVisual {
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
	
	public GameVisual()	{
		DisplayManager.createDisplay();
		loader = new Loader();
		renderer = new MasterRenderer(loader);
		
	}

}
