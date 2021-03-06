package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import RenderComponents.DisplayManager;

public class freeCamera extends Camera {
	private static final float RUN_SPEED = 40;
	private static final float TURN_SPEED = 160;
	private static final float PITCH_CHANGE = 2f;
	private static final float YAW_CHANGE = 2f;
	
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 10;
	
	
	private Vector3f position;
	
	
	
	private float pitch = 20;
	private float yaw = 0;
	private float roll;

	private float movemntSpeed = 2f;
	
	public freeCamera(){
		position = new Vector3f(0,0,0);
		
	}

	public freeCamera(Vector3f position){
		this.position = position;
	}


	
	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}

		

	

	}
	

	public void move()	{
		checkInputs();
		float distance = currentSpeed * DisplayManager.getTimeDelat();
		
		
		float dx = (float) (distance * Math.sin(Math.toRadians(-yaw)));
		float dz = (float) (distance * Math.cos(Math.toRadians(-yaw)));
		//upwardsSpeed += 0.2f * DisplayManager.getTimeDelat();
		//setPosition(new Vector3f(dx, upwardsSpeed, dz));

		position.x -= dx;
		position.z -= dz;
		
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			yaw += YAW_CHANGE;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			yaw -= YAW_CHANGE;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			pitch -= PITCH_CHANGE;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			pitch += PITCH_CHANGE;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			position.y+= 0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			position.y-= 0.2f;;
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setYaw(float val) {
		yaw = val;
	}
	
	public void invertPitch() {
		this.pitch = -pitch;
	}
	
	public void setPitch(float val) {
		pitch = val;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	
}
