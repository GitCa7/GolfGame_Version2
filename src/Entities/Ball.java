package Entities;

import com.badlogic.ashley.core.Entity;

import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import PHY_Components.*;
import RenderComponents.Loader;
import RenderComponents.OBJLoader;



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
	 * @param radius radius of ball
	 * @param density density of ball's material
	 * @param gBuild gravity builder object \n
	 * note: the position needs to be set manually by adding a position component to this ball
	 */
	public Ball (float radius, float density, GravityForce.Builder gBuild)
	{
		//Translate the physics.components into Coordinates
		super(staticModel, position, 0, 0, 0, scale);
		
		
		
		float mass = mass (radius, density);
		add (new Mass (mass));
		add (new Velocity());
		add (new Spin());
		add (new Force());
		add (gBuild.get (mass));
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
