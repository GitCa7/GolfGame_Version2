package Entities;

import GamePackage.GameVisual;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FollowCamera extends Camera implements InputObserver{

	private float distanceFromPlayer = 100f;
	private float angleAroundPlayer = 0;
	private gameEntity player;
	private Vector3f pos = new Vector3f(0,0,0);

		
	private GameVisual parent;


	public FollowCamera(GolfBall playerObject,GameVisual parent)	{

		this.player = playerObject;
		this.parent = parent;
		super.setPosition(pos);
	}
	
	public FollowCamera(GolfBall playerObject)	{

		this.player = playerObject;
		this.parent = null;
		super.setPosition(pos);
	}
	
	public FollowCamera()	{
		this.player = null;
		super.setPosition(pos);
	}
	
	public void setPlayer(gameEntity ent)	{
		player = ent;
	}
	
	public void move(){
		if(player != null)	{
			calculateZoom();
			calcPitchAngle();
			float horizontalDistance = calculateHorizontalDistance();
			float verticalDistance = calculateVerticalDistance();
			calculateCameraPosition(horizontalDistance, verticalDistance);
			super.setYaw(180 - (player.getRotY() + angleAroundPlayer));
			super.setYaw(super.getYaw() % 360);
			super.setPosition(pos);

		}
	}
	

	private void calculateZoom()	{
		
		float zoomLevel = Mouse.getDWheel() * 0.03f;
		distanceFromPlayer -= zoomLevel;
		if(distanceFromPlayer < 5) {
			distanceFromPlayer = 5;
		}
		
	}
	
	private void calculateZoom(float zoomLevel)	{
		
		distanceFromPlayer -= zoomLevel;
		if(distanceFromPlayer < 5) {
			distanceFromPlayer = 5;
		}
		
	}
	
	private void calcPitchAngle()	{
		if(Mouse.isButtonDown(1))	{
			float pitchChange = Mouse.getDY() * 0.2f;
			super.setPitch(super.getPitch() - pitchChange);
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
			if(super.getPitch() < 0)	{
				super.setPitch(0);
			}
			else if	(super.getPitch() > 90){
				super.setPitch(90);
			}
		}
	}
	
	
	
	public void calcPitchAngle(float deltaX, float deltaY)	{
		float pitchChange = deltaY * 0.2f;
		super.setPitch(super.getPitch() - pitchChange);
		
		float angleChange = deltaX * 0.3f;
		angleAroundPlayer -= angleChange;
		
		if(super.getPitch() < 0)	{
			super.setPitch(0);
		}
		else if	(super.getPitch() > 90){
			super.setPitch(90);
		}
	}
	
	public void getAngleAroundPlayer()	{
		System.out.println(super.getYaw());
	}
	
	public Vector2f getDirection()	{
		if(player != null)	{
			Vector3f center = player.getPosition();
			Vector2f Direction = new Vector2f(center.x - pos.x, center.z - pos.z);
			if(Direction.length()  > 1)
				Direction.normalise();
			System.out.println("Direction: " + Direction);
			return Direction;
		}
		else	{
			return null;
		}
	}
	
	private float calculateHorizontalDistance()	{
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(super.getPitch() + 4)));
	}
	
	private float calculateVerticalDistance()	{
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(super.getPitch() + 4)));
	}
	
	
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance)	{
		if(player != null)	{
			float theta = player.getRotY() + angleAroundPlayer;
			float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
			float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
			//System.out.println(player.getPosition().x  + " " +  player.getPosition().y  + " " + player.getPosition().z);
			
			pos.x = player.getPosition().x - offsetX;
			pos.z = player.getPosition().z - offsetZ;
			pos.y = player.getPosition().y + verticalDistance + 4;
		}
	}
	
	public void update(float deltaX, float deltaY, float deltaZ)	{
		
		if(player != null)	{
			//DeltaZ will be for followCamera the curent Value of the mouseWheel
			float mouseWheel = deltaZ * 0.3f;
			calculateZoom(deltaZ);
			
			calcPitchAngle(deltaX,deltaY);
			
			float horizontalDistance = calculateHorizontalDistance();
			float verticalDistance = calculateVerticalDistance();
			calculateCameraPosition(horizontalDistance, verticalDistance);
			super.setYaw(180 - (player.getRotY() + angleAroundPlayer));
			super.setYaw(super.getYaw() % 360);
			super.setPosition(pos);
		}
	}

	public Vector3 inputLoop(){
		boolean up = true;
		float curForce = 500;
		float applForce;
		while(!Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			//System.out.println(curForce);
			/*
			if(up){
				curForce += 10;
			}else{
				curForce -= 10;
			}
			if (up&&curForce>2000){
				up = false;
			}
			if(!up&&curForce<500){
				up = true;
			}
			*/
			parent.directionArrow.setScale(curForce/300);
			parent.updateDisplay();
			parent.updateForceGui(up);
			
		}
		Vector3 dir = new Vector3(0,0,1);
		dir.rotate(parent.directionArrow.getRotY(),0,1,0);
		applForce = parent.getForce() * curForce;
		return new Vector3(dir.x*applForce,applForce/5,dir.z*applForce);
	}
	
	
	
	
}
