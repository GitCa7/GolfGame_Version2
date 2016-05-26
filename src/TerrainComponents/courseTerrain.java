package TerrainComponents;

import java.util.ArrayList;

import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import RenderComponents.Loader;

public class courseTerrain extends Terrain{
	private static final Loader loader = new Loader();
	private ModelTexture modelTex = new ModelTexture(loader.loadTexture("grass_surf"));
	private RawModel model;
	
	private float[] vertices;
    private float[] normals;
    float[] textureCoords;
    int[] indices;
    
    public ArrayList<PointNode> leafs;
    
}
