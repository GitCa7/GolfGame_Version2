package Entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import ModelBuildComponents.TexturedModel;
import RenderComponents.Loader;
import RenderComponents.OBJLoader;

public class Arrow extends gameEntity {
	
	private static final Loader loader = new Loader();
	private static final RawModel model = OBJLoader.loadObjModel("/Arrow/Arrow", loader);
	private static final TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("/Arrow/Arrow_Text")));
	
	private final float maxDist = 50;
	

	public Arrow(Vector3f position, float scale) {
		super(staticModel, position, 90, 0, 0, scale);
		// TODO Auto-generated constructor stub
	}
	
	public Arrow(GolfBall golfB) {
		super(staticModel, golfB.getPosition(), 90, 0, 0, 1);
		Vector3f newPos = new Vector3f(golfB.getPosition().x, golfB.getPosition().y + 1, golfB.getPosition().z);
		super.setPosition(newPos);
		// TODO Auto-generated constructor stub
	}
	
	public void setAsTarget(Vector3f tarPos, gameEntity Ball)	{
		if(tarPos == null)	{
			tarPos = new Vector3f();
		}
		Vector3f ballPos = Ball.getPosition();
		Vector3f distance = new Vector3f(tarPos.x - ballPos.x, 0, tarPos.z - ballPos.z);
		System.out.println("Distance: " + distance);
		
		Vector3f newPos = new Vector3f(ballPos.x + distance.x, 1, ballPos.z + distance.z);
		System.out.println("New Position: " + newPos);
		if(distance.length() < maxDist)	{
			super.setPosition(newPos);
		}
		else	{
			super.setPosition(ballPos);
		}
	}
	
	public void setPosition(GolfBall golfB)	{
		Vector3f pos = golfB.getPosition();
		super.setPosition(new Vector3f(pos.x, pos.y + 2, pos.z));
	}
	
}
