package Entities;

import org.lwjgl.util.vector.Vector3f;

import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.Loader;
import RenderComponents.OBJLoader;

public class crate extends gameEntity{

	private static final Loader loader = new Loader();
	private static final RawModel model = OBJLoader.loadObjModel("/newObstacles/ComplexCrate", loader);
	private static final TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("/newObstacles/crateTexture")));

	

	public crate(Vector3f position,float scale) {
		super(staticModel, position, 0, 0, 0, scale);
		// TODO Auto-generated constructor stub
	}

}
