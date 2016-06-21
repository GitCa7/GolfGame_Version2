package GUIs;

import org.lwjgl.util.vector.Vector2f;

public class GUITexture {
	
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	
	private Vector2f orig;
	
	public GUITexture(int texture, Vector2f position, Vector2f scale)	{
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		orig = null;
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
	
	public void setScale(Vector2f newScale) {
		scale = newScale;
	}
	
	public void setOriginal(Vector2f orig)		{
		this.orig = orig;
	}
	
	public Vector2f getOriginal()		{
		return orig;
	}
	
	public void reScale(float value) {
		if(orig != null)	{
			scale = new Vector2f(scale.x * value, scale.y * value);
		}
	}
	
	public void setToOrig() {
		scale = orig;
	}
	
	public void setPosition(Vector2f position) {
		this.position = position;
	}

}
