package Entities;

import org.lwjgl.util.vector.Vector3f;

import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.Loader;
import RenderComponents.OBJLoader;

import java.io.Serializable;

public class GolfBall extends gameEntity implements Serializable {
	
	private static  final Loader loader = new Loader();
	private static  final RawModel model = OBJLoader.loadObjModel("golfBallblend", loader);
	private static  final TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("golfBallTexNew")));

	public GolfBall(Vector3f position, float scale)	{
		super(staticModel, position, 0, 0, 0, scale);
	}

}
