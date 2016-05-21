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
import PHY_Geometry.planar.Line;
import PHY_Geometry.planar.Triangle;
import PHY_Geometry.spatial.Tetrahedron;
import PHY_Geometry.spatial.TetrahedronBuilder;
import RenderComponents.Loader;



public class Terrain {
	private Loader loader = new Loader();
	private ModelTexture modelTex = new ModelTexture(loader.loadTexture("grass_surf"));
	private RawModel model;

	private static final float SIZE = 1000;
    
    private static final float MAX_HEIGHT = 80;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
    
    private float x;
    private float z;
    
    private float xStart, zStart, xEnd, zEnd;
    
    private boolean heightMapUse;
    private int VERTEX_COUNT;
    
    private boolean useHeightMap;
    private float[] vertices;
    private float[] normals;
    float[] textureCoords;
    int[] indices;
    
    public ArrayList<PointNode> leafs;
    
	
	public Terrain(int gridX, int gridZ, String heightMapPath) {
		this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        xStart = gridX;
        zStart = gridZ;
        
        heightMapUse = true;
        leafs = new ArrayList<PointNode>();
        generateTerrain(loader, heightMapPath);
	}
	
	public Terrain(int gridX, int gridZ){
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
    

    /*
    public void changeHeight(float xStart, float xLim, float zStart, float zLim, float amount)	{
    	int number = 0;
    	for(int i = 0; i < vertices.length; i+=3)	{
    		if(-vertices[i] > xStart && -vertices[i] > xLim && -vertices[i+2] > zStart && -vertices[i+2] > zLim)	{
    			if(vertices[i+1] < 30)
    				vertices[i+1] += amount;
    			
    		}
    	}
    	
    	model = loader.loadToVAO(vertices, textureCoords, normals, indices);
    }
 	*/
    
    
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
                vertices[vertexPointer*3] = -((float)j * 0.9f)/((float)VERTEX_COUNT - 1) * SIZE;
                
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
    }
    
    public void displayBoundaries()	{
    	PointNode lowerLeft,UpperLeft,lowerRight,UpperRight;
    	
    	lowerLeft = leafs.get(0);
    	UpperLeft = leafs.get(0);
    	lowerRight = leafs.get(0);
    	UpperRight = leafs.get(0);
    	
    	
    	for(PointNode leaf: leafs)	{
    		//For upper left (most negative Value on x)
    		if(leaf.getCoordinates().x == 0 & leaf.getCoordinates().z  == 0)	{
    			UpperLeft = leaf;
    		}
    		//For lower left
    		if(leaf.getCoordinates().x == 0 & leaf.getCoordinates().z < lowerLeft.getCoordinates().z)	{
    			lowerLeft = leaf;
    		}
    		//For Upper Right
    		if(leaf.getCoordinates().x < UpperRight.getCoordinates().x & leaf.getCoordinates().z == 0)	{
    			UpperRight = leaf;
    		}
    		//For Lower Right
    		if(leaf.getCoordinates().x < lowerRight.getCoordinates().x & leaf.getCoordinates().z < lowerRight.getCoordinates().z)	{
    			lowerRight = leaf;
    		}
    	}
    	
    	System.out.println("UpperLeft: " + UpperLeft.getCoordinates().x + "\t | y: " + UpperLeft.getCoordinates().y + "\t | z: " + UpperLeft.getCoordinates().z);
    	System.out.println("lowerLeft: " + lowerLeft.getCoordinates().x + "\t | y: " + lowerLeft.getCoordinates().y + "\t | z: " + lowerLeft.getCoordinates().z);
    	System.out.println("UpperRight: " + UpperRight.getCoordinates().x + "\t | y: " + UpperRight.getCoordinates().y + "\t | z: " + UpperRight.getCoordinates().z);
    	System.out.println("lowerRight: " + lowerRight.getCoordinates().x + "\t | y: " + lowerRight.getCoordinates().y + "\t | z: " + lowerRight.getCoordinates().z);

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
    
    
    //What seems to be necessary is to convert all the triangles of the terrain into Tetrahedons
    //For that we´re gonna need to get all the trainagles that  the terrains composed of
    
    public void printAllTris()	{
    	
    	int offset = 1;
    	for(int i = 0; i < 15; i+=3)	{
    		System.out.println(offset + " Triangle: ");
    		
    		System.out.print("1:" + indices[i] + "\t");
    		leafs.get(indices[i]).printCoord();
    		System.out.print("2:" + indices[i+1] + "\t");
    		leafs.get(indices[i+1]).printCoord();
    		System.out.print("3:" + indices[i+2] + "\t");
    		leafs.get(indices[i+2]).printCoord();
    		/*
    		System.out.println("1:" + indices[i] + "\tX: " + vertices[indices[i]] + "\tY: " + vertices[indices[i]+1] + "\tZ: " + vertices[indices[i]+2]);
    		System.out.println("2: " + indices[i+1] + "\tX: " + vertices[indices[i+1]] + "\tY: " + vertices[indices[i+1]+1] + "\tZ: " + vertices[indices[i+1]+2]);
    		System.out.println("3: " + indices[i+2] + "\tX: " + vertices[indices[i+2]] + "\tY: " + vertices[indices[i+2]+1] + "\tZ: " + vertices[indices[i+2]+2]);
    		
    		*/
    		System.out.println();
    		offset++;
    	}
    	
    	
    	
    }
    
    public Vector3[] getVertasVec(){
    	Vector3[] list = new Vector3[vertices.length / 3];
    	int offset = 0;
    	for(PointNode leaf:leafs){
    		list[offset] = new Vector3(leaf.getCoordinates().x, leaf.getCoordinates().y, leaf.getCoordinates().z);
    		offset++;
    	}
    	return list;
    	
    }
    
    
    public Tetrahedron[] getAllTetrahedons()	{
    	
    	float newPointDist = 5f;
    	
    	//need to go thoruhall triangles of the terrain
    	Triangle[] allTri = getAllTris();
    	Vector3[] temp;
    	Vector3 a,b,c,dNew;
    	
    	TetrahedronBuilder TetraBuild;
    	Tetrahedron[] TetraList = new Tetrahedron[allTri.length];
    	
    	int offset = 0;
    	
    	
    	
    	for(int i = 0; i < allTri.length; i++)	{
    		
    		temp = allTri[i].getVertices();
			a = temp[0];
			b = temp[1];
			c = temp[2];
    		
    		if(i % 2 == 0)	{
    			//if even => point with smaller distance to origin is b
        		//There we need to create another point slightly below

    			//Now get the right point
    			if(a.dst(b) < a.dst(c))	{
    				// We now know that we need to create a point under b to create the Tetrahedon
    				dNew = new Vector3(b.x, (b.y - newPointDist) , b.z);
    			}
    			else	{
    				dNew = new Vector3(c.x, (c.y - newPointDist) , c.z);
    			}
    		}
    		else	{
    			//if uneven => point with greater distance to origin is b
        		//There we need to create another point slightly below

    			//Now get the right point
    			if(a.dst(b) > a.dst(c))	{
    				// We now know that we need to create a point under b to create the Tetrahedon
    				dNew = new Vector3(b.x, (b.y - newPointDist) , b.z);
    			}
    			else	{
    				dNew = new Vector3(c.x, (c.y - newPointDist) , c.z);
    			}
    		}
    		TetraBuild = new TetrahedronBuilder(new Vector3[]{a,b,c,dNew});
    		TetraList[offset] = TetraBuild.build();
    		offset++;
    	}
    	return TetraList;
    	
    }
    
   
    
    
    
    public Triangle[] getAllTris()	{
    	Vector3f start, middle, end;
    	Vector3 startTemp, middleTemp, endTemp;
    	Triangle tempTri;
    	Triangle[] list = new Triangle[indices.length / 3];
    	
    	Vector3[] tempVecAray;
    	Line line1, line2, line3;
    	Line[] tempLineArray;
    	
    	int offset = 0;
    	for(int i = 0; i < indices.length - 3; i+=3)	{
    		
    		start = leafs.get(indices[i]).getCoordinates();
    		startTemp = new Vector3(start.x, start.y, start.z);
    		
    		middle = leafs.get(indices[i+1]).getCoordinates();
    		middleTemp = new Vector3(middle.x, middle.y, middle.z);
    		
    		end = leafs.get(indices[i+2]).getCoordinates();
    		endTemp = new Vector3(end.x, end.y, end.z);
    		
    		tempVecAray = new Vector3[]{startTemp, middleTemp, endTemp};
    		line1 = new Line(startTemp, middleTemp);
    		line2 = new Line(middleTemp, endTemp);
    		line3 = new Line(endTemp, startTemp);
    		
    		tempLineArray = new Line[]{line1, line2, line3};
    		
    		tempTri = new Triangle(tempVecAray, tempLineArray);
    		list[offset] = tempTri;
    		offset++;
    	}
    	return list;
    }
    
    
   
    
}
