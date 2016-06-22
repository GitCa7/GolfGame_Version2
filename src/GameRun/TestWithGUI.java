package GameRun;

import java.io.File;
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
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;

public class TestWithGUI {

	public static boolean DEBUG = false;

	MasterRenderer renderer;
	GuiRenderer guiRenderer;

	// For loading Object Data
	Loader loader;

	// The Scene
	ArrayList<GolfBall> golfBalls;
	ArrayList<gameEntity> entities;
	ArrayList<gameEntity> surrondings;
	List<GUITexture> GuiElements;
	Terrain[][] terrains;
	FollowCamera cam;
	Light light;
	MousePicker mousePick, mousePick2;
	private boolean increase, forceincrease;
	
	final Vector2f max = new Vector2f(0.15f, 0.225f);

	public TestWithGUI() {


		DisplayManager.createDisplay();
		loader = new Loader();
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		golfBalls = new ArrayList<GolfBall>();
		entities = new ArrayList<gameEntity>();
		surrondings = new ArrayList<gameEntity>();
		terrains = new Terrain[2][2];
		GuiElements = new ArrayList<GUITexture>();
		increase = false;
		forceincrease = false;
		setUpEntities();
		setUpTerrain();
		setUpScene();
		setUpGuis();
		createSurrondings();

		TextMaster.init(loader);
		// mousePick = new MousePicker(cam, renderer.getProjectionMatrix(),
		// terrains.get(0));
		// mousePick2 = new MousePicker(cam, renderer.getProjectionMatrix(),
		// terrains.get(1));

		startGame();

	}

	public void createSurrondings() {
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
		for (int i = 0; i < 100; i++) {

			surrondings.add(new gameEntity(grassTextModel,
					new Vector3f(ran.nextFloat() * 800 - 400, 3, ran.nextFloat() * -600), 180, 0, 0, 3));
			surrondings.add(new gameEntity(fernTextModel,
					new Vector3f(ran.nextFloat() * 800 - 400, 0, ran.nextFloat() * -600), 0, 0, 0, 3));
		}
	}

	public void setUpEntities() {

		GolfBall golfball = new GolfBall(new Vector3f(4, 20, -475), 2, false);
		// cam.setPosition(new Vector3f(4,20,-422));
		// System.out.println("ID: " +
		// golfball.getModel().getRawModel().getID());
		golfBalls.add(golfball);
		// System.out.println("ID: " + entities.get(0));
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
	
	private void updateForceGui()	{
		Vector2f currScale = GuiElements.get(1).getScale();
		Vector2f max = GuiElements.get(1).getOriginal();
		Vector2f min = new Vector2f(0.05f, 0.05f);
		
		if(forceincrease)	{
			if (DEBUG)
				System.out.println("ForceIncrease = true: \nCurrent Scale: " + currScale + "max: " + max);
			if(currScale.x >= max.x || currScale.y >= max.y)	{
				forceincrease = false;
			}
			GuiElements.get(1).reScale(1.01f);
		}
		else	{
			if (DEBUG)
				System.out.println("ForceIncrease = false: \nCurrent Scale: " + currScale + "min: " + min);
			if(currScale.x <= min.x || currScale.y <= min.y)	{
				forceincrease = true;
			}
			else	{
				GuiElements.get(1).reScale(0.99f);
			}
		}
	}
	/*
	public void updateGui() {
		
		Vector2f max = new Vector2f(0.75f,0.75f);
		Vector2f min = new Vector2f(-0.75f,-0.75f);
		Vector2f current = GuiElements.get(0).getPosition();
		if(increase)	{
			if(current.x >= max.x && current.y >= max.y)	{
				increase = false;
			}
			else	{
				GuiElements.get(0).setPosition(new Vector2f(current.x + 0.01f, current.y + 0.01f));
			}
		}
		else	{
			if(current.x <= min.x && current.y <= min.y)	{
				increase = true;
			}
			else	{
				GuiElements.get(0).setPosition(new Vector2f(current.x - 0.01f, current.y - 0.01f));
			}
		}
		//GuiElements.get(0).setPosition(position);
	}
	*/
	public void displayAllEntites() {
		if (DEBUG) {
			System.out.println("Size Entities:" + entities.size());
			System.out.println("Size Surronding:" + surrondings.size());
		}

		for (gameEntity ent : entities) {
			if (DEBUG)
				System.out.print("GameEntity: ");
			int ID = ent.getModel().getRawModel().getID();
			System.out.println();
		}
		System.out.println();

		for (gameEntity surronding : surrondings) {
			if (DEBUG)
				System.out.print("surronding: ");
			int ID = surronding.getModel().getRawModel().getID();
			System.out.println();
		}

	}

	public void setUpTerrain() {

		terrains[0][0] = new Terrain(0, 0, "heightmap");
		

		// terrains[0][0].printAllTris();
		// terrains[0][0].leafs.get(0).printCoord();
		// terrains[0][0].leafs.get(128).printCoord();
		// terrains[0][0].leafs.get(1).printCoord();
		// terrains[0][0].getAllTetrahedons();
	}

	public void setUpScene() {
		cam = new FollowCamera(golfBalls.get(0));
		// cam.setPosition(new Vector3f(4,20,-422));
		// cam.setPitch(25);

		light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));
	}

	public void startGame() {
		// displayAllEntites();

		FontType font = new FontType(loader.loadFontTextureAtlas("sans"), new File("res/GuiTextures/Fonts/sans.fnt"));
		int playerID = 0; 
		int strokes = 0;
		GUIText text = new GUIText("Player: " + playerID + " | Hits: "  + strokes , 2, font, new Vector2f(1000, 1000), 1f, false);
		text.setColour(0, 0, 0);
		
		while (!Display.isCloseRequested()) {
			cam.move();
			updateForceGui();
			

			for (gameEntity plant : surrondings) {
				renderer.processEntity(plant);
			}

			for (gameEntity gameEntity : entities) {
				// gameEntity.increaseRotation(0, 1f, 0);
				// gameEntity.increasePosition(0.1f, 0, 0);
				renderer.processEntity(gameEntity);
			}

			for (GolfBall ball : golfBalls) {

				renderer.processEntity(ball);
			}

			
			renderer.processTerrain(terrains[0][0]);


			

			renderer.render(light, cam);
			guiRenderer.render(GuiElements);
			TextMaster.render();
			DisplayManager.updateDisplay();
		}
		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static void main(String[] args) {
		TestWithGUI test = new TestWithGUI();
	}

}
