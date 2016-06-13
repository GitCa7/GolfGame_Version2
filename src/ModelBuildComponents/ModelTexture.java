package ModelBuildComponents;

import java.io.Serializable;

public class ModelTexture implements Serializable {

	private transient int textureID;
	
	private transient float shineDamper = 1;
	private transient float reflectivity;
	
	
	private transient boolean hasTranspercy = false;
	private transient boolean useFakeLighting = false;
	
	

	public ModelTexture(int textureID)	{
		this.textureID = textureID;
	}
	
	
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}



	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}



	public void setHasTranspercy(boolean hasTranspercy) {
		this.hasTranspercy = hasTranspercy;
	}

	public boolean getHasTranspercy() {
		return hasTranspercy;
	}

	public int getID()	{
		return textureID;
	}
	
	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
}
