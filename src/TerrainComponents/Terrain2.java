package TerrainComponents;

import java.util.ArrayList;

import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import RenderComponents.Loader;

public class Terrain2 {
	private Loader loader = new Loader();
	private ModelTexture modelTex = new ModelTexture(loader.loadTexture("grass_surf"));
	private RawModel model;

	private static final float SIZE = 1000;
    private static final int triNumBorder = 43520;
    private static final float MAX_HEIGHT = 80;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
    
    private float x;
    private float z;
    
    
    private boolean heightMapUse;
    private int VERTEX_COUNT;
    
    private boolean useHeightMap;
    private float[] vertices;
    private float[] normals;
    float[] textureCoords;
    int[] indices;
    
    public ArrayList<PointNode> leafs;
    
    public Terrain2(float posX, float zPos, float xSize, float zSize, float stepSize)	{
    	
    }
    

}
