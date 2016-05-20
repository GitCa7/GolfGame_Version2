package Entities;

import ModelBuildComponents.TexturedModel;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import org.lwjgl.util.vector.Vector3f;


public class gameEntity extends Entity {

	private TexturedModel model;
	private Vector3 position;
	private float rotX, rotY, rotZ;
	private float scale;

	public gameEntity(TexturedModel model, Vector3 position, float rotX, float rotY, float rotZ,
			float scale) {
		this.model = model;
		//Translate the physics.components into Coordinates
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return new Vector3f (position.x, position.y, position.z);
	}

	public void setPosition (Vector3 position) {
		//Translate the physics.components into Coordinates
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
	
	
	
}