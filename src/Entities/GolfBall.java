package Entities;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
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
	private boolean bot;

	public GolfBall(Vector3f position, float scale,boolean bot)	{
		super(staticModel, position, 0, 0, 0, scale);
		this.bot = bot;
	}

	@Override
	public BoundingBox boundingBox(){
		BoundingBox tmp = new BoundingBox(new Vector3(position.x-scale,position.y-scale,position.z-scale),new Vector3(position.x+scale,position.y+scale,position.z+scale));
		return tmp;
	}

	public boolean isBot(){
		return  bot;
	}
}
