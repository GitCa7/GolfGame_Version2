package GameRun;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Entities.Camera;
import Entities.Light;
import Entities.gameEntity;
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
import framework.entities.Player;

public class Test2 {
	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		TextMaster.init(loader);

		FontType font = new FontType(loader.loadFontTextureAtlas("segoe"), new File("res/fonts/segoe.fnt"));
		GUIText text = new GUIText("This is a test text!", 1, font, new Vector2f(0.5f, 0.5f), 0.5f, true);
		text.setColour(1, 0, 0);

		// *********TERRAIN TEXTURE STUFF**********

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		// *****************************************

		TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocks", loader), new ModelTexture(loader.loadTexture("rocks")));

		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);

		TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader), fernTextureAtlas);

		TexturedModel bobble = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader), new ModelTexture(loader.loadTexture("pine")));
		bobble.getTexture().setHasTransparency(true);

		fern.getTexture().setHasTransparency(true);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);

		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);

		List<gameEntity> entities = new ArrayList<gameEntity>();
		List<gameEntity> normalMapEntities = new ArrayList<gameEntity>();

		// ******************NORMAL MAP MODELS************************

		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);

		MasterRenderer renderer = new MasterRenderer(loader);

		RawModel bunnyModel = OBJLoader.loadObjModel("player", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("playerTexture")));


		// **********Water Renderer Set-up************************

		WaterTile water = new WaterTile(75, -75, 0);
		waters.add(water);

		// ****************Game Loop Below*********************

		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			picker.update();
			entity.increaseRotation(0, 1, 0);
			entity2.increaseRotation(0, 1, 0);
			entity3.increaseRotation(0, 1, 0);
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			// render reflection teture
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1));
			camera.getPosition().y += distance;
			camera.invertPitch();

			// render refraction texture
			buffers.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));

			// render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
			waterRenderer.render(waters, camera, sun);
			guiRenderer.render(guiTextures);
			TextMaster.render();

			DisplayManager.updateDisplay();
		}

		// *********Clean Up Below**************

		TextMaster.cleanUp();
		buffers.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

}
}
