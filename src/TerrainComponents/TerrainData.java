package TerrainComponents;

import java.util.ArrayList;

<<<<<<< HEAD
=======
/**
 * Created by Asus on 26-5-2016.
 */
>>>>>>> origin/master
public class TerrainData {
    private float[] vertices;
    private float[] normals;
    float[] textureCoords;
    ArrayList<PointNode> leafs;
    int[] indices;
    private boolean hasHeightMap = false;
    String heightMapID;

    public TerrainData(float[] vertices,float[] normals,float[] textureCoords,int[] indices,ArrayList<PointNode> leafs){
        this.leafs = leafs;
        this.textureCoords =textureCoords;
        this.indices = indices;
        this.vertices = vertices;
        this.normals = normals;
    }

    public TerrainData(String heightMapID){
        hasHeightMap = true;
        this.heightMapID = heightMapID;
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
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/master
