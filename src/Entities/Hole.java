package Entities;

import org.lwjgl.util.vector.Vector3f;

import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.Loader;
import RenderComponents.OBJLoader;

public class Hole extends gameEntity {

	private static final Loader loader = new Loader();
	private static final RawModel model = OBJLoader.loadObjModel("/Hole/Hole", loader);
	private static final TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("/Hole/holeTexture")));
	
	public Hole(Vector3f position, float scale)	{
		super(staticModel, position, 0, 0, 0, scale);
	}
}
