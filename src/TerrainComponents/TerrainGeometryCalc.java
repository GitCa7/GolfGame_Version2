package TerrainComponents;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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

public class TerrainGeometryCalc implements Serializable {

	private static final Loader loader = new Loader();
	private static final int triNumBorder = 43520;
	private static final int triNumBorder2 = 10837;
	private static final float MAX_HEIGHT = 80;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

	private String heightMapPath;
	private boolean heightMapUse;
	private BufferedImage heightMapImage;
	private int count, VERTEX_COUNT;

	public TerrainGeometryCalc() {
		heightMapImage = null;
		heightMapUse = false;

	}

	public TerrainGeometryCalc(String heightMapPath) {
		this.heightMapPath = heightMapPath;
		heightMapImage = null;
		heightMapUse = false;
		if (heightMapPath != null) {

			try {
				heightMapImage = ImageIO.read(new File("res/" + heightMapPath + ".png"));
				heightMapUse = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			VERTEX_COUNT = heightMapImage.getHeight();
		}

		if (VERTEX_COUNT == 0) {
			VERTEX_COUNT = 128;
		}
		count = (int) (VERTEX_COUNT * VERTEX_COUNT);
	}

	public RawModel getTerrainFromData(TerrainData data) {

		float[] vertices = data.getVertices();
		float[] normals = data.getNormals();
		int[] indices = data.getIndices();
		float[] textureCoords = data.getTextureCoords();

		RawModel model = loader.loadToVAO(vertices, textureCoords, normals, indices);
		return model;
	}

	
	public void generateTerrain(float[] vertices, float[] normals, float[] textureCoords, int[] indices, ArrayList<PointNode> leafs, float SIZE, String heightMapPath) {

		// int count = VERTEX_COUNT * VERTEX_COUNT;
		System.out.println("HeightmapUse: " + heightMapUse);
		if (!heightMapUse) {
			System.out.println("No Heightmap used");
			count = vertices.length / 3;
			VERTEX_COUNT = (int) Math.sqrt(count);
		}

		int vertexPointer = 0;
		int leafCount = 0;
		Vector3f normal;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {

				vertices[vertexPointer * 3] = -((float) j) / ((float) VERTEX_COUNT - 1) * SIZE;

				if (heightMapUse == true) {
					vertices[vertexPointer * 3 + 1] = getHeight(j, i, heightMapImage);
				} else {
					vertices[vertexPointer * 3 + 1] = 1;
				}

				vertices[vertexPointer * 3 + 2] = -(float) i / ((float) VERTEX_COUNT - 1) * SIZE;

				

				if(vertexPointer * 3 == 108)	{
					vertices[vertexPointer * 3+1] = 11; 
				}
				
				PointNode tmp = new PointNode(vertices[vertexPointer * 3], vertices[vertexPointer * 3 + 1],
						vertices[vertexPointer * 3 + 2]);
				leafs.add(tmp);
				
				normal = new Vector3f();
				if (heightMapUse) {
					normal = calculateNormal(j, i, heightMapImage);

					normals[vertexPointer * 3] = normal.x;
					normals[vertexPointer * 3 + 1] = normal.y;
					normals[vertexPointer * 3 + 2] = normal.z;
				} else {
					normal = new Vector3f(0, 1, 0);
					normals[vertexPointer * 3] = 0;
					normals[vertexPointer * 3 + 1] = 1;
					normals[vertexPointer * 3 + 2] = 0;
				}
				leafs.get(leafCount).setNormal(normal);

				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		
		
		
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
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
	
	public void printAllIndices(TerrainData terraDat)	{
		int[] indices = terraDat.getIndices();
		for(int i = 0; i < indices.length; i++)	{
			System.out.print(indices[i] + " ");
			if((i +1) % 3 == 0)	{
				System.out.print("\n");
			}
		}
	}

	private Vector3f calculateNormal(int x, int z, BufferedImage image) {

		if (x < 0 || x > image.getHeight() || z < 0 || z > image.getHeight()) {
			return null;
		}

		float heightL = getHeight(x - 1, z, image);
		float heightR = getHeight(x + 1, z, image);
		float heightD = getHeight(x, z - 1, image);
		float heightU = getHeight(x, z + 1, image);

		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	private float getHeight(int x, int z, BufferedImage image) {
		if (x < 0 || x > image.getHeight() || z < 0 || z > image.getHeight()) {
			return 0;
		}

		// System.out.println("X: " + x + " | Z: " + z);
		if (x >= 256)
			x = 255;

		if (z >= 256)
			z = 255;

		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;

		height *= MAX_HEIGHT;
		return height;

	}
	
	public void addSlopeInMiddle(float[] vertices)	{
		int middle = vertices.length / 3;
		vertices[middle] += 10;
		
	}
	
	public ArrayList<Triangle> getAllTris(TerrainData terraData) {
		Vector3f start, middle, end;
		Vector3 startTemp, middleTemp, endTemp;
		Triangle tempTri;
		TriangleBuilder build;
		int[] indices = terraData.getIndices();
		float[] vertices = terraData.getVertices();
		ArrayList<PointNode> leafs = terraData.getLeafs();
		Triangle[] list = new Triangle[indices.length / 3];

		Vector3[] tempVecAray;
		Line line1, line2, line3;
		Line[] tempLineArray;

		//System.out.println("Size of indices: " + indices.length);

		int startingPoint, middlePoint, endPoint;
		float startX, startY, startZ, middleX, middleY, middleZ, endX, endY, endZ;
		int endEncounter = 0;
		int offset = 0;
		for (int i = 0; i < indices.length; i += 3) {
			//System.out.println("Offset: " + offset + "\t i: " + i + "\tEnd: " + (list.length));
			if(indices[i] == indices[i+1] && indices[i+1] == indices[i+2])	{
				endEncounter = i;
				//System.out.println("EndCounter set to: " + endEncounter);
				break;
			}
			
    		
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
		//System.out.println("Starting with " + list.length + " Entries");

		ArrayList<Triangle> ret = new ArrayList<>();
		for (int i = 0; i < (endEncounter / 3) && i < list.length; i++) {
			ret.add(list[i]);
		}
		return ret;
	}

	public Tetrahedron[] getAllTetrahedons(TerrainData terraData) {

		float newPointDist = 5f;

		// need to go through all triangles of the terrain
		ArrayList<Triangle> tmp = getAllTris(terraData);

		Triangle[] allTri = new Triangle[tmp.size()];
		for (int i = 0; i < tmp.size(); i++) {
			allTri[i] = tmp.get(i);
		}

		Vector3[] temp;
		Line[] tempLines;
		Vector3 a, b, c, dNew, eNew, fNew;

		TetrahedronBuilder TetraBuild;
		Tetrahedron[] TetraList = new Tetrahedron[allTri.length * 3];

		int offset = 0;
		Vector3 lineA, lineB, lineC;

		for (int i = 0; i < allTri.length; i++) {

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
			// First Tetraeder ABCD
			// Second Tetraeder CBFD
			// Third Tetraeder BEFD

			// TetraBuild = new TetrahedronBuilder(new Vector3[]{a,b,c,dNew});
			TetraBuild = new TetrahedronBuilder(new Vector3[] { a, b, c });
			TetraList[offset] = TetraBuild.build();

			// TetraBuild = new TetrahedronBuilder(new
			// Vector3[]{c,b,fNew,dNew});
			TetraBuild = new TetrahedronBuilder(new Vector3[] { c, b, dNew });
			TetraList[offset + 1] = TetraBuild.build();

			// TetraBuild = new TetrahedronBuilder(new
			// Vector3[]{b,eNew,fNew,dNew});
			TetraBuild = new TetrahedronBuilder(new Vector3[] { b, fNew, dNew });
			TetraList[offset + 2] = TetraBuild.build();
			offset += 3;

		}

		return TetraList;

	}

	public boolean terrainIsFlat(TerrainData terraData) {

		PointNode start = terraData.getLeafs().get(0);
		float yTarget = start.getCoordinates().y;

		float yVal;
		for (PointNode tmp : terraData.getLeafs()) {
			yVal = tmp.getCoordinates().y;
			if (yVal != yTarget) {
				return false;
			}
		}
		return true;
	}
	
	
	
	
	
	

	public boolean getHeigtMapExcistence() {
		return heightMapUse;
	}

	public float[] updateVerticeAmount() {
		return new float[(int) (count * 3)];
	}

	public float[] updateNormalsAmount() {
		return new float[(int) (count * 3)];
	}

	public float[] updateTextureCoordAmount() {
		return new float[(int) (count * 2)];
	}

	public int[] updateIndicesAmount() {
		return new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
	}

}
