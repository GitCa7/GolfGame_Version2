package TerrainComponents;

import java.io.Serializable;
import java.util.ArrayList;

import physics.geometry.spatial.Tetrahedron;


public class TerrainData implements Serializable {
    private float[] vertices;
    private float[] normals;
    private float SIZE;
    
    Tetrahedron[] tetrahedons;
    public ArrayList<Tetrahedron> tetraList;
    float[] textureCoords;
    ArrayList<PointNode> leafs;
    int[] indices;
    private boolean hasHeightMap = false;
    String heightMapID;
    
    TerrainGeometryCalc terraCalc;

    public TerrainData(float[] vertices,float[] normals,float[] textureCoords,int[] indices,ArrayList<PointNode> leafs, float SIZE){
        this.leafs = leafs;
        this.textureCoords =textureCoords;
        this.indices = indices;
        this.vertices = vertices;
        this.normals = normals;
        terraCalc = new TerrainGeometryCalc();
        terraCalc.generateTerrain(vertices, normals, textureCoords, indices, leafs, 1000, heightMapID);
        tetrahedons = terraCalc.getAllTetrahedons(this);
    }

    public TerrainData(String heightMapID){
        hasHeightMap = true;
        this.heightMapID = heightMapID;
        int VERTEX_COUNT = 128;
    	SIZE = 1000;
    	int count = VERTEX_COUNT * VERTEX_COUNT;
    	this.vertices = new float[count * 3];
    	this.normals = new float[count * 3];
    	this.textureCoords = new float[count*2];
    	this.indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT*1)];
        this.leafs = new ArrayList<PointNode>();
        terraCalc = new TerrainGeometryCalc();
        terraCalc.generateTerrain(vertices, normals, textureCoords, indices, leafs, 1000, heightMapID);
        tetrahedons = terraCalc.getAllTetrahedons(this);
        updateTetraList();
    }
    
    private void updateTetraList()	{
    	for(int i = 0; i < tetrahedons.length; i++)	{
    		tetraList.add(tetrahedons[i]);
    	}
    }
    
    public TerrainData(){
    	int VERTEX_COUNT = 128;
    	SIZE = 1000;
    	int count = VERTEX_COUNT * VERTEX_COUNT;
    	this.vertices = new float[count * 3];
    	this.normals = new float[count * 3];
    	this.textureCoords = new float[count*2];
    	this.indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT*1)];
        this.leafs = new ArrayList<PointNode>();
    	
    	terraCalc = new TerrainGeometryCalc();
        terraCalc.generateTerrain(vertices, normals, textureCoords, indices, leafs, SIZE, null);
        tetrahedons = terraCalc.getAllTetrahedons(this);
    }
    
    public Tetrahedron[] getAllTetrahedon()	{
    	return tetrahedons;
    }

    public float[] getX() {
        return vertices;
    }
    
    public float[] getVertices() {
        return vertices;
    }

    public float[] getNormals() {
        return normals;
    }

    public boolean hasHeightMap() {
        return hasHeightMap;
    }

    public String getHeightMapID() {
        return heightMapID;
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }

    public ArrayList<PointNode> getLeafs() {
        return leafs;
    }

    public int[] getIndices() {
        return indices;
    }
}
