package TerrainComponents;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import RenderComponents.Loader;

public class proxyTerrain {
	
	private float SIZE;
    private static final int triNumBorder = 43520;
    private static final float MAX_HEIGHT = 80;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
    
	private Loader loader;
	private boolean heightMapUse;
	private int VERTEX_COUNT;
	    
	private boolean useHeightMap;	
	private float[] vertices;
	private float[] normals;
	float[] textureCoords;
	 
	int[] indices;
	
	private float x,z;
	
	
	public ArrayList<PointNode> leafs;
	
	public proxyTerrain(int gridX, int gridZ, String heightMapPath)	{
		
		SIZE = 1000;
		this.x = gridX * SIZE;
        this.z = gridZ * SIZE;        
        heightMapUse = true;
        leafs = new ArrayList<PointNode>();
        loader = new Loader();
        generateTerrain(loader, heightMapPath);
		
	}
	
	public proxyTerrain(int gridX, int gridZ)	{
		
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
	
	private float getHeight(int x, int z, BufferedImage image)	{
    	if(x < 0 || x > image.getHeight() || z < 0 || z > image.getHeight())	{
    		return 0;
    	}
    	
    	//System.out.println("X: " + x + " | Z: " + z);
    	if(x >= 256)
    		x = 255;
    	
    	if(z >= 256)
    		z = 255;
    	
    	float height = image.getRGB(x, z);
    	height += MAX_PIXEL_COLOR/2f;
    	height /= MAX_PIXEL_COLOR/2f;
    	
    	height *= MAX_HEIGHT;
    	return height;
    }
	
	public float getHeightSimple(float x, float z)	{
    	for(int i = 0; i < vertices.length; i+= 3)	{
    		if(vertices[i] == x && vertices[i+2] == z){
    			return vertices[i+1];
    		}
    	}
    	return 0;
    }
	
	public float[] getVertices()	{
		return vertices;
	}
	
	public int[] getIndices()	{
		return indices;
	}
	
	public float[] getNormals()	{
		return normals;
	}
	
	public float[] getTextureCoords()	{
		return textureCoords;
	}
	
	public float getSize()	{
		return SIZE;
	}
	
	public float getX()	{
		return x;
	}

	public float getZ()	{
		return z;
	}
	
	public ArrayList<PointNode> getLeafs()	{
		return leafs;
	}
}
