package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FollowCamera extends Camera{

	private float distanceFromPlayer = 100f;
	private float angleAroundPlayer = 0;
	private gameEntity player;
	private Vector3f pos = new Vector3f(0,0,0);
		
	
	public FollowCamera(GolfBall playerObject)	{
		this.player = playerObject;
		super.setPosition(pos);
	}
	
	public void move(){
		calculateZoom();
		//calculatePitch();
		//calculateAngleAroundPlayer();
		calcPitchAngle();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		super.setYaw(180 - (player.getRotY() + angleAroundPlayer));
		super.setYaw(super.getYaw() % 360);
		super.setPosition(pos);
	}
	

	private void calculateZoom()	{
		
		float zoomLevel = Mouse.getDWheel() * 0.03f;
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
	
	public void getAngleAroundPlayer()	{
		System.out.println(super.getYaw());
	}
	
	public Vector2f getDirection()	{
		Vector3f center = player.getPosition();
		Vector2f Direction = new Vector2f(center.x - pos.x, center.z - pos.z);
		if(Direction.length()  > 1)
			Direction.normalise();
		System.out.println("Direction: " + Direction);
		return Direction;
	}
	
	/*
	private void calculatePitch()	{
		if(Mouse.isButtonDown(1))	{
			float pitchChange = Mouse.getDY() * 0.2f;
			super.setPitch(super.getPitch() - pitchChange);
			if(super.getPitch() < 0)	{
				super.setPitch(0);
			}
			else if	(super.getPitch() > 90){
				super.setPitch(90);
			}
		}
	}
	
	private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(0))	{
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	*/
	private float calculateHorizontalDistance()	{
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(super.getPitch() + 4)));
	}
	
	private float calculateVerticalDistance()	{
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(super.getPitch() + 4)));
	}
	
	
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance)	{
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		//System.out.println(player.getPosition().x  + " " +  player.getPosition().y  + " " + player.getPosition().z);
		
		pos.x = player.getPosition().x - offsetX;
		pos.z = player.getPosition().z - offsetZ;
		pos.y = player.getPosition().y + verticalDistance + 4;
		
	}
}
