package TerrainComponents;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import com.badlogic.gdx.math.Vector3;

import ModelBuildComponents.RawModel;
import RenderComponents.Loader;
import physics.geometry.linear.Line;
import physics.geometry.planar.Triangle;
import physics.geometry.planar.TriangleBuilder;
import physics.geometry.spatial.Tetrahedron;
import physics.geometry.spatial.TetrahedronBuilder;

public class TerrainGeometryCalc {
	
	private static final Loader loader = new Loader();
	private static final int triNumBorder = 43520;
	private static final float MAX_HEIGHT = 80;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	
	public TerrainGeometryCalc()	{
		
	}
	
	public RawModel getTerrainFromData(TerrainData data)	{
		
		float[] vertices = data.getVertices();
		float[] normals = data.getNormals();
		int[] indices = data.getIndices();
		float[] textureCoords = data.getTextureCoords();
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, normals, indices);
		return model;
	}
	
	
	
	
	public void generateTerrain(float[] vertices,float[] normals,float[] textureCoords,int[] indices, ArrayList<PointNode> leafs, float SIZE, String heightMapPath)	{
		int VERTEX_COUNT = 0;
    	boolean heightMapUse = false;
    	BufferedImage image = null;
    	if(heightMapPath != null)	{
    		
	    	try {
				image = ImageIO.read(new File("res/" + heightMapPath +".png"));
				heightMapUse = true;
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
                
                
                PointNode tmp = new PointNode(vertices[vertexPointer*3], vertices[vertexPointer*3+1], vertices[vertexPointer*3+2]);
                
                leafs.add(tmp);
                
                
                
                
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
	
	public ArrayList<Triangle> getAllTris(TerrainData terraData)		{
		Vector3f start, middle, end;
    	Vector3 startTemp, middleTemp, endTemp;
    	Triangle tempTri;
    	TriangleBuilder build;
    	int[] indices = terraData.getIndices();
    	ArrayList<PointNode> leafs = terraData.getLeafs();
    	Triangle[] list = new Triangle[indices.length / 3];
    	
    	Vector3[] tempVecAray;
    	Line line1, line2, line3;
    	Line[] tempLineArray;
    	
    	int offset = 0;
    	for(int i = 0; i < list.length - 3; i+=3)	{
    		
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
    		
    		
    		build = new TriangleBuilder(tempVecAray);
    		tempTri = build.build();
    		list[offset] = tempTri;
    		offset++;
    	}
		ArrayList<Triangle> ret = new ArrayList<>();
		for(int i = 0; i < list.length; i++){
			ret.add(list[i]);
		}
    	return ret;
	}
	
	 public Tetrahedron[] getAllTetrahedons(TerrainData terraData)	{
	    	
	    	float newPointDist = 5f;
	    	
	    	//need to go thoruhall triangles of the terrain
	    	Triangle[] allTri = (Triangle[]) getAllTris(terraData).toArray();
	    	
	    	Vector3[] temp;
	    	Line[] tempLines;
	    	Vector3 a,b,c,dNew,eNew,fNew;
	    	
	    	TetrahedronBuilder TetraBuild;
	    	Tetrahedron[] TetraList = new Tetrahedron[triNumBorder * 3];
	    	
	    	int offset = 0;
	    	Vector3 lineA,lineB,lineC;
	    	
	    	
	    	for(int i = 0; i < triNumBorder; i++)	{
	    		
	    		//System.out.println("Number: " + i + "\t" + allTri[i]);
	    		
	    		temp = allTri[i].getVertices();
	    		tempLines = allTri[i].getLine();
				a = temp[0];
				b = temp[1];
				c = temp[2];
	    		
				
				lineA = tempLines[0].direction();
				lineB = tempLines[1].direction();
				lineC = tempLines[2].direction();
				
				
				dNew = new Vector3(a.x, a.y - newPointDist, a.z);
				eNew = new Vector3(b.x, b.y - newPointDist, b.z);
				fNew = new Vector3(c.x, c.y - newPointDist, c.z);
				//First Tetraeder ABCD
				//Second Tetraeder CBFD
				//Third Tetraeder BEFD

				TetraBuild = new TetrahedronBuilder(new Vector3[]{a,b,c,dNew});
	    		TetraList[offset] = TetraBuild.build();
	    		
	    		TetraBuild = new TetrahedronBuilder(new Vector3[]{c,b,fNew,dNew});
	    		TetraList[offset+1] = TetraBuild.build();
	    		
	    		TetraBuild = new TetrahedronBuilder(new Vector3[]{b,eNew,fNew,dNew});
	    		TetraList[offset+2] = TetraBuild.build();
	    		offset+=3;
	    	}
	    	
	    	return TetraList;
	    	
	    	
	    }

}
