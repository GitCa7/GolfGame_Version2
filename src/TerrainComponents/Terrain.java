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

<<<<<<< HEAD
	private float SIZE;
=======
	private float SIZE = 1000;
>>>>>>> 31dd2bf2d6c4b2a4e78e23e369e20961b35c40d0
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
    
<<<<<<< HEAD
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
=======
	
	public Terrain(int gridX, int gridZ, String heightMapPath) {
		this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        xStart = gridX;
        zStart = gridZ;
        
        heightMapUse = true;
        leafs = new ArrayList<PointNode>();
        generateTerrain(loader, heightMapPath);
	}
	
	public Terrain(int gridX, int gridZ, float size){
		SIZE = size;
		this.x = gridX * SIZE;
	    this.z = gridZ * SIZE;
	    heightMapUse = false;
	    leafs = new ArrayList<PointNode>();
	    generateTerrain(loader, null);
	}
    
	public void addNode(float x, float y, float z)	{
    	PointNode newNode = new PointNode(x,y,z);
    	leafs.add(newNode);
    }
     
     
    public float getX() {
        return x;
    }
 
 
 
    public float getZ() {
        return z;
    }
 
    private Vector3f calculateNormal(int x, int z, BufferedImage image)		{
    	
    	if(x < 0 || x > image.getHeight() || z < 0 || z > image.getHeight())	{
    		return null;
    	}
    	
    	float heightL = getHeight(x-1, z, image);
    	float heightR = getHeight(x+1, z, image);
    	float heightD = getHeight(x, z-1, image);
    	float heightU = getHeight(x, z+1, image);

    	Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD - heightU);
    	normal.normalise();
    	return normal;
    }
    
    
    
    private void calculateNormal2()	{
    	Vector3f p1,p2,p3,v1,v2,normal;
    	Vector3f tmpCoords;
        
        for(int i = 0; i < indices.length - 3; i += 3)	{
        	tmpCoords = leafs.get(indices[i]).getCoordinates();
        	p1 = new Vector3f(tmpCoords.x, tmpCoords.y, tmpCoords.z);
        	
        	tmpCoords = leafs.get(indices[i+1]).getCoordinates();
        	p2 = new Vector3f(tmpCoords.x, tmpCoords.y, tmpCoords.z);
        	
        	tmpCoords = leafs.get(indices[i+2]).getCoordinates();
        	p3 = new Vector3f(tmpCoords.x, tmpCoords.y, tmpCoords.z);
        	
        	//(p2.X - p1.X, p2.Y - p1.Y, p2.Z - p1.Z)
        	v1 = new Vector3f(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
        	v2 = new Vector3f(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z);
        	
        	normal = new Vector3f();
        	//normal = v1.crs(v2);
        	Vector3f.cross(v1, v2, normal);
        	normal.normalise();
        	
        	//for vertice 0
        	leafs.get(indices[i]).setNormal(normal);
        	leafs.get(indices[i+1]).setNormal(normal);
        	leafs.get(indices[i]+2).setNormal(normal);
        	/*
        	normals[i] = normal.x;
        	normals[i+1] = normal.y;
        	normals[i+2] = normal.z;
        	*/
        }
    }
 
    
    public RawModel getModel() {
        return model;
    }
 
 
 
    public ModelTexture getTexture() {
        return modelTex;
    }
    
    private void updateVertices()	{
    	int offset = 0;
    	for(PointNode leaf: leafs)	{
    		vertices[offset] = leaf.getCoordinates().x;
    		vertices[offset+1] = leaf.getCoordinates().y;
    		vertices[offset+2] = leaf.getCoordinates().z;
    	}
    }
 
    private void updateNormals()	{
    	int offset = 0;
    	for(PointNode leaf: leafs)	{
    		vertices[offset] = leaf.getCoordinates().x;
    		vertices[offset+1] = leaf.getCoordinates().y;
    		vertices[offset+2] = leaf.getCoordinates().z;
    	}
    }


	public void changeHeight(float xStart, float xLim, float zStart, float zLim, float amount)	{
		//int number = 0;
		for(int i = 0; i < vertices.length; i+=3)	{
			if(vertices[i] < xStart && vertices[i] > xLim && vertices[i+2] < zStart && vertices[i+2] > zLim)	{
				if(vertices[i+1] < 30&&vertices[i+1]>-30)
					vertices[i+1] += amount;

			}
		}

		model = loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public void changeHeightNB(float xStart, float xLim, float zStart, float zLim, float amount)	{
		//int number = 0;
		for(int i = 0; i < vertices.length; i+=3)	{
			if(vertices[i] < xStart && vertices[i] > xLim && vertices[i+2] < zStart && vertices[i+2] > zLim)	{
				if(vertices[i+1] < 80)
					vertices[i+1] += amount;

			}
		}

		model = loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public float getHeightDif(float x, float z)	{
		Vector3 real = new Vector3(x,0,z);
		Vector3 res = new Vector3(vertices[0],0,vertices[2]);
		for(int i = 3; i < vertices.length; i+= 3){
			Vector3 tmp = new Vector3(vertices[i],0,vertices[i+2]);
			if(tmp.dst2(real)<res.dst2(real)){
				res = tmp;
			}
		}

		for(int i = 0; i < vertices.length; i+= 3)	{
			if(vertices[i] == res.x && vertices[i+2] == res.z){
				return vertices[i+1];
			}
		}
		return 0;

	}


    public void printVert()		{
		if(vertices != null)	{
			//for(int i = 0; i < vertices.length - 3; i += 3)	{
			for(int i = 0; i < 18; i += 3)	{
					System.out.println("X: " + vertices[i] + "\tY: " + vertices[i+1] + "\tZ: " + vertices[i+2]);
			}
		}
		System.out.println("\n");
	}
    
    public void printGraph()		{
		if(leafs.size() != 0)	{
			for(int i = 0; i < 6; i++)	{
				System.out.print(i + ": ");
				leafs.get(i).printCoord();
			}
		}
		System.out.println("\n");
	}
    
    public void raiseInRange(float xStart, float xLimit, float zStart, float zLimit, float amount)	{
		
		Vector3f tempCoord;
		int number = 0;
		PointNode temp;
		
		for(int i = 0; i < leafs.size(); i++)	{
			tempCoord = leafs.get(i).getCoordinates();
			if(tempCoord.x >= xStart &&  tempCoord.x <= xLimit && -tempCoord.z >= zStart &&  -tempCoord.z <= zLimit)	{
				number++;
				temp = leafs.get(i);
				temp.changeHeight(amount);
				leafs.set(i, temp);
			}
		}
		
		System.out.println(number + " points were in the given range");
		updateVertices();
	}
    
    
    public void generateTerrain(Loader loader, String heightMap){
        
    	VERTEX_COUNT = 0;
    	
    	BufferedImage image = null;
    	if(heightMapUse == true)	{
    		
	    	try {
				image = ImageIO.read(new File("res/" + heightMap +".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	VERTEX_COUNT = image.getHeight();
    	}
    	
    	
    	if(VERTEX_COUNT == 0)	{
    		VERTEX_COUNT = 128;
    	}
    		
    	int count = VERTEX_COUNT * VERTEX_COUNT;
        vertices = new float[count * 3];
        normals = new float[count * 3];
        textureCoords = new float[count*2];
        indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT*1)];
        int vertexPointer = 0;
        int leafCount = 0;
        Vector3f normal;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = -((float)j)/((float)VERTEX_COUNT - 1) * SIZE;
                
                if(heightMapUse == true)	{
                	vertices[vertexPointer*3+1] = getHeight(j, i, image);
                }
                else	{
                	vertices[vertexPointer*3+1] = 1;
                }
                
                vertices[vertexPointer*3+2] = -(float)i/((float)VERTEX_COUNT - 1) * SIZE;
                
                
                if(i == VERTEX_COUNT - 1)	{
                	xEnd = -(float)j/((float)VERTEX_COUNT - 1) * SIZE;
                }
                
                addNode(vertices[vertexPointer*3], vertices[vertexPointer*3+1], vertices[vertexPointer*3+2]);
                
                
                
                
                normal = new Vector3f();
                if(heightMapUse)	{
	                normal = calculateNormal(j,i,image);
	
	                normals[vertexPointer*3] = normal.x;
	                normals[vertexPointer*3+1] = normal.y;
	                normals[vertexPointer*3+2] = normal.z;
                }
                else	{
                	normal = new Vector3f(0,1,0);
                	normals[vertexPointer*3] = 0;
	                normals[vertexPointer*3+1] = 1;
	                normals[vertexPointer*3+2] = 0;
                }
                leafs.get(leafCount).setNormal(normal);
                
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        
        model = loader.loadToVAO(vertices, textureCoords, normals, indices);
>>>>>>> 31dd2bf2d6c4b2a4e78e23e369e20961b35c40d0
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
