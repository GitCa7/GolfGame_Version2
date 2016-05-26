package TerrainComponents;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import com.badlogic.gdx.math.Vector3;

import ModelBuildComponents.ModelTexture;
import ModelBuildComponents.RawModel;
import RenderComponents.Loader;
import physics.geometry.linear.Line;
import physics.geometry.planar.Triangle;
import physics.geometry.planar.TriangleBuilder;
import physics.geometry.spatial.Tetrahedron;
import physics.geometry.spatial.TetrahedronBuilder;



public class Terrain {
	private static final Loader loader = new Loader();
	private ModelTexture modelTex = new ModelTexture(loader.loadTexture("grass_surf"));
	private RawModel model;

	private float SIZE;
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
    
    public float x_start, x_end, z_start, z_end;
    protected proxyTerrain prox;
    
    public Terrain(proxyTerrain prox) {
    	this.prox = prox;
    	vertices = prox.getVertices();
    	indices = prox.getIndices();
    	normals = prox.getNormals();
    	leafs = prox.getLeafs();
    	textureCoords = prox.getTextureCoords();
    	
    	model = loader.loadToVAO(vertices, textureCoords, normals, indices);
    }
    
    public Terrain(int posX, int posY, String heightMapPath) {
    	
    	prox = new proxyTerrain(posY, posY, heightMapPath);
    	vertices = prox.getVertices();
    	indices = prox.getIndices();
    	normals = prox.getNormals();
    	textureCoords = prox.getTextureCoords();
    	leafs = prox.getLeafs();
    	
    	System.out.println("Length of indices: " + indices.length);
    	
    	model = loader.loadToVAO(vertices, textureCoords, normals, indices);
    }
    
    public Terrain(int posX, int posY) {
    	proxyTerrain prox = new proxyTerrain(posY, posY);
    	vertices = prox.getVertices();
    	indices = prox.getIndices();
    	normals = prox.getNormals();
    	leafs = prox.getLeafs();
    	textureCoords = prox.getTextureCoords();
    	
    	model = loader.loadToVAO(vertices, textureCoords, normals, indices);
    }
	
	
    public float getHeightSimple(float x, float z)	{
    	float height = prox.getHeightSimple(x, z);
    	return height;
    }
    
    public RawModel getModel() {
        return model;
    }
    
    public ModelTexture getTexture() {
        return modelTex;
    }
    
    public float getX()	{
    	return prox.getX();
    }
    
    public float getZ()	{
    	return prox.getZ();
    }
    
}
