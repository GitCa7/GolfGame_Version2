package ModelBuildComponents;


import java.io.Serializable;

public class TexturedModel implements Serializable {
	
	private RawModel rawModel;
	private ModelTexture texture;
	

	public TexturedModel(RawModel model, ModelTexture tex)		{
		rawModel = model;
		texture = tex;
	}


	public RawModel getRawModel() {
		return rawModel;
	}


	public ModelTexture getTexture() {
		return texture;
	}
	
	
	
}
