package Entities;

import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.Loader;
import RenderComponents.OBJLoader;
import com.badlogic.gdx.math.Vector3;


public class Ball extends gameEntity	{
	
	private static final Loader loader = new Loader();
	private static final RawModel model = OBJLoader.loadObjModel("golfBallblend", loader);
	private static final TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("golfBallTexNew")));
	
	/**
	 * constructs ball having:
	 * a mass,
	 * a velocity, 
	 * a spin, 
	 * a force, 
	 * a gravity object
	 * note: the position needs to be set manually by adding a position component to this ball
	 */

	public Ball()
	{
		//Translate the physics.components into Coordinates
		super(staticModel, new Vector3(4, 20, -455), 0, 0, 0, 3);

	}

	/**
	 * @param radius a radius
	 * @param density a density constant
	 * @return the mass of a sphere of radius, density
	 */

	public static float mass (float radius, float density)
	{
		return (float) (4 * Math.PI * Math.pow (radius, 3) * density / 4);
	}

	

}
