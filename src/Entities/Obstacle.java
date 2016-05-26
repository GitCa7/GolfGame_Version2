package Entities;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.lwjgl.util.vector.Vector3f;

import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.Loader;
import RenderComponents.OBJLoader;

public class Obstacle extends gameEntity {
	
	private static final Loader loader = new Loader();
	private static final RawModel model = OBJLoader.loadObjModel("/newObstacles/cube", loader);
	private static final TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("sand2")));

	
	public Obstacle(Vector3f position,float scale) {
		super(staticModel, position, 0, 0, 0, scale);
		// TODO Auto-generated constructor stub
	}
	
	/*
	public static void main(String[] args)	{
		Obstacle cube = new Obstacle(new Vector3f(0,0,0), 4);
	}
	*/

	@Override
	public BoundingBox boundingBox(){
		BoundingBox tmp = new BoundingBox(new Vector3(position.x-scale,position.y-scale,position.z-scale),new Vector3(position.x+scale,position.y+scale,position.z+scale));
		System.out.println(tmp.toString());
		return tmp;
	}
	
	

}
