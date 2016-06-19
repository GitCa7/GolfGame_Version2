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
        terraCalc = new TerrainGeometryCalc(null);
        //terraCalc.generateTerrain(vertices, normals, textureCoords, indices, leafs, 1000, heightMapID);
        tetrahedons = terraCalc.getAllTetrahedons(this);
        this.SIZE = SIZE;
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
        
    	System.out.println("Number of vertices: " + count);
    	
    	terraCalc = new TerrainGeometryCalc(null);
        terraCalc.generateTerrain(vertices, normals, textureCoords, indices, leafs, SIZE, null);
        System.out.println("Attributres before calculation: \n" + "Amount of vertices: " + "\t" + vertices.length + "\nAmount of indices: " + "\t" + indices.length + "\nAmount of normals: " + "\t" + normals.length + "\nAmount of Texture Coordinates: " + "\t" + textureCoords.length);
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
        //System.out.println("Attributres before calculation: \n" + "Amount of vertices: " + "\t" + vertices.length + "\nAmount of indices: " + "\t" + indices.length + "\nAmount of normals: " + "\t" + normals.length + "\nAmount of Texture Coordinates: " + "\t" + textureCoords.length);
        
        terraCalc = new TerrainGeometryCalc(heightMapID);
        if(terraCalc.getHeigtMapExcistence() == true)	{
        	this.vertices = terraCalc.updateVerticeAmount();
        	this.normals = terraCalc.updateNormalsAmount();
        	this.textureCoords = terraCalc.updateTextureCoordAmount();
        	this.indices = terraCalc.updateIndicesAmount();
        }
        
        terraCalc.generateTerrain(vertices, normals, textureCoords, indices, leafs, 1000, heightMapID);
        //System.out.println("Attributres after calculation: \n" + "Amount of vertices: " + "\t" + vertices.length + "\nAmount of indices: " + "\t" + indices.length + "\nAmount of normals: " + "\t" + normals.length + "\nAmount of Texture Coordinates: " + "\t" + textureCoords.length);

        tetrahedons = terraCalc.getAllTetrahedons(this);
    }
    
    public TerrainData(int newVertexCount, int newSIZE) throws Exception{

    	if(newVertexCount % 8 != 0 || newVertexCount < 0)	{
    		throw new Exception("Vertexcount has to be devisable by 8 and greater than 0");
    	}
    	
    	
    	int VERTEX_COUNT = newVertexCount;
    	if(newSIZE > 100)
    		SIZE = newSIZE;
    	else 
    		SIZE = 1000;
    	
    	int count = VERTEX_COUNT * VERTEX_COUNT;
    	
    	this.vertices = new float[count * 3];
    	this.normals = new float[count * 3];
    	this.textureCoords = new float[count*2];
    	this.indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT*1)];
        this.leafs = new ArrayList<PointNode>();
    	
    	terraCalc = new TerrainGeometryCalc(null);
        terraCalc.generateTerrain(vertices, normals, textureCoords, indices, leafs, SIZE, null);
    	System.out.println("VertexCount: " + VERTEX_COUNT + "\nCount: " + count);
    	System.out.println("Attributres before calculation: \n" + "Amount of vertices: " + "\t" + vertices.length + "\nAmount of indices: " + "\t" + indices.length + "\nAmount of normals: " + "\t" + normals.length + "\nAmount of Texture Coordinates: " + "\t" + textureCoords.length + "\nAmount of Triangles: " + "\t" + terraCalc.getAllTris(this).size());
        tetrahedons = terraCalc.getAllTetrahedons(this);
    }
    
    private void updateTetraList()	{
    	
    	for(int i = 0;  tetrahedons[i] != null && i < tetrahedons.length; i++)	{
    		tetraList.add(tetrahedons[i]);
    	}
    }

    public void printleafs(){
        for (int i=0;i<leafs.size();i=i+100){
            System.out.println(leafs.get(i).getCoordinates());
        }
    }

    public void printVerts(){
        for (int i=1;i<vertices.length-300;i=i+300){
            System.out.println(vertices[i]);
        }
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
    
    public boolean isFlat()	{
    	return terraCalc.terrainIsFlat(this);
    }
    
    
    public float getSIZE(){return SIZE;}


    
    public static void main(String[] args)	{
    	TerrainData terraDat = new TerrainData();
    	TerrainGeometryCalc calculate = new TerrainGeometryCalc();
    	System.out.println("Number of all Triangles: " + calculate.getAllTris(terraDat).size());
    }

}
