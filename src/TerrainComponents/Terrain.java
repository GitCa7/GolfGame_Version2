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
    private Loader loader = new Loader();
    private ModelTexture modelTex = new ModelTexture(loader.loadTexture("grass_surf"));
    private RawModel model;

    private float SIZE;
    private static final int triNumBorder = 43520;
    private static final float MAX_HEIGHT = 80;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;


    public float x_start, z_start, x_end, z_end;

    private boolean heightMapUse;
    private int VERTEX_COUNT;

    private boolean useHeightMap;
    private float[] vertices;
    private float[] normals;
    float[] textureCoords;
    int[] indices;
    
    private static final TerrainGeometryCalc terraCalc = new TerrainGeometryCalc();

    public ArrayList<PointNode> leafs;
    
    public float x,z;


    public Terrain (TerrainData data){
    	x = 0;
    	z = 0;
        vertices = data.getVertices();
        normals = data.getNormals();
        textureCoords = data.getTextureCoords();
        indices = data.getIndices();
        leafs = data.getLeafs();
        model = terraCalc.getTerrainFromData(data);
    }

    public Terrain(int gridX, int gridZ, String heightMapPath) {
    	x = 0;
    	z = 0;
    	
        SIZE = 1000;
        heightMapUse = false;
        BufferedImage image = null;
        if(heightMapPath != null)	{
    		
	    	try {
				image = ImageIO.read(new File("res/" + heightMapPath +".png"));
				heightMapUse = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	    leafs = new ArrayList<PointNode>();
	    generateTerrain(loader, heightMapPath);
        
    }

    public Terrain(float size) {
        SIZE = size;
        heightMapUse = false;
        leafs = new ArrayList<PointNode>();
        generateTerrain(loader, null);
    }

    public void addNode(float x, float y, float z) {
        PointNode newNode = new PointNode(x, y, z);
        leafs.add(newNode);
    }

    public TerrainData toData(){
        return new TerrainData(vertices,normals,textureCoords,indices,leafs, 1000);
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

            normals[i] = normal.x;
            normals[i+1] = normal.y;
            normals[i+2] = normal.z;

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
    
    
    

    public void changeHeight(float x_start, float xLim, float z_start, float zLim, float amount)	{
        //int number = 0;
        for(int i = 0; i < vertices.length; i+=3)	{
            if(vertices[i] < x_start && vertices[i] > xLim && vertices[i+2] < z_start && vertices[i+2] > zLim)	{
                if(vertices[i+1] < 30&&vertices[i+1]>-30) {
                    vertices[i + 1] += amount;

                }

            }
        }
        updatePointNode();
        calculateNormal2();
        model = loader.loadToVAO(vertices, textureCoords, normals, indices);
    }
    
    

    public void changeHeightNB(float x_start, float xLim, float z_start, float zLim, float amount)	{
        //int number = 0;
        for(int i = 0; i < vertices.length; i+=3)	{
            if(vertices[i] < x_start && vertices[i] > xLim && vertices[i+2] < z_start && vertices[i+2] > zLim)	{
                if(vertices[i+1] < 80)
                    vertices[i+1] += amount;

            }
        }
        updatePointNode();
        calculateNormal2();
        model = loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    public RawModel getModel() {
        return model;
    }


    public ModelTexture getTexture() {
        return modelTex;
    }

    private void updateVertices() {
        int offset = 0;
        for (PointNode leaf : leafs) {
            vertices[offset] = leaf.getCoordinates().x;
            vertices[offset + 1] = leaf.getCoordinates().y;
            vertices[offset + 2] = leaf.getCoordinates().z;
        }
    }

    private void updateNormals() {
        int offset = 0;
        for (PointNode leaf : leafs) {
            vertices[offset] = leaf.getCoordinates().x;
            vertices[offset + 1] = leaf.getCoordinates().y;
            vertices[offset + 2] = leaf.getCoordinates().z;
        }
    }

    public void updatePointNode()	{
        int offset = 0;
        PointNode tmp;
        for(int i = 0; i < vertices.length-3; i += 3)	{
            tmp = new PointNode(vertices[i], vertices[i+1], vertices[i+2]);
            leafs.set(offset, tmp);
            offset++;
        }
    }


    public void generateTerrain(Loader loader, String heightMap) {

        VERTEX_COUNT = 0;

        BufferedImage image = null;
        if (heightMapUse == true) {

            try {
                image = ImageIO.read(new File("res/" + heightMap + ".png"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            VERTEX_COUNT = image.getHeight();
        }


        if (VERTEX_COUNT == 0) {
            VERTEX_COUNT = 128;
        }

        int count = VERTEX_COUNT * VERTEX_COUNT;
        vertices = new float[count * 3];
        normals = new float[count * 3];
        textureCoords = new float[count * 2];
        indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
        int vertexPointer = 0;
        int leafCount = 0;
        Vector3f normal;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = -((float) j) / ((float) VERTEX_COUNT - 1) * SIZE;

                if (heightMapUse == true) {
                    vertices[vertexPointer * 3 + 1] = getHeight(j, i, image);
                } else {
                    vertices[vertexPointer * 3 + 1] = 1;
                }

                vertices[vertexPointer * 3 + 2] = -(float) i / ((float) VERTEX_COUNT - 1) * SIZE;


                if (i == VERTEX_COUNT - 1) {
                    x_end = -(float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                }

                addNode(vertices[vertexPointer * 3], vertices[vertexPointer * 3 + 1], vertices[vertexPointer * 3 + 2]);


                normal = new Vector3f();
                if (heightMapUse) {
                    normal = calculateNormal(j, i, image);

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

        x_start = leafs.get(0).getCoordinates().x;
        z_start = leafs.get(0).getCoordinates().z;
        
        x_end = leafs.get(leafs.size()-1).getCoordinates().x;
        z_end = leafs.get(leafs.size()-1).getCoordinates().z;
        
        model = loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    
    public void setModel(RawModel model)	{
    	this.model = model;
    }
    
    
    public void displayBoundaries() {
        PointNode lowerLeft, UpperLeft, lowerRight, UpperRight;

        lowerLeft = leafs.get(0);
        UpperLeft = leafs.get(0);
        lowerRight = leafs.get(0);
        UpperRight = leafs.get(0);


        for (PointNode leaf : leafs) {
            //For upper left (most negative Value on x)
            if (leaf.getCoordinates().x == 0 & leaf.getCoordinates().z == 0) {
                UpperLeft = leaf;
            }
            //For lower left
            if (leaf.getCoordinates().x == 0 & leaf.getCoordinates().z < lowerLeft.getCoordinates().z) {
                lowerLeft = leaf;
            }
            //For Upper Right
            if (leaf.getCoordinates().x < UpperRight.getCoordinates().x & leaf.getCoordinates().z == 0) {
                UpperRight = leaf;
            }
            //For Lower Right
            if (leaf.getCoordinates().x < lowerRight.getCoordinates().x & leaf.getCoordinates().z < lowerRight.getCoordinates().z) {
                lowerRight = leaf;
            }
        }

        System.out.println("UpperLeft: " + UpperLeft.getCoordinates().x + "\t | y: " + UpperLeft.getCoordinates().y + "\t | z: " + UpperLeft.getCoordinates().z);
        System.out.println("lowerLeft: " + lowerLeft.getCoordinates().x + "\t | y: " + lowerLeft.getCoordinates().y + "\t | z: " + lowerLeft.getCoordinates().z);
        System.out.println("UpperRight: " + UpperRight.getCoordinates().x + "\t | y: " + UpperRight.getCoordinates().y + "\t | z: " + UpperRight.getCoordinates().z);
        System.out.println("lowerRight: " + lowerRight.getCoordinates().x + "\t | y: " + lowerRight.getCoordinates().y + "\t | z: " + lowerRight.getCoordinates().z);

    }


    private float getHeight(int x, int z, BufferedImage image) {
        if (x < 0 || x > image.getHeight() || z < 0 || z > image.getHeight()) {
            return 0;
        }

        //System.out.println("X: " + x + " | Z: " + z);
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

    public float getHeightSimple(float x, float z) {

        for (int i = 0; i < vertices.length; i += 3) {
            if (vertices[i] == x && vertices[i + 2] == z) {
                return vertices[i + 1];
            }
        }
        return 0;
    }


    //What seems to be necessary is to convert all the triangles of the terrain into Tetrahedons
    //For that we´re gonna need to get all the trainagles that  the terrains composed of

    public void printAllTris() {

        int offset = 1;
        for (int i = 0; i < 15; i += 3) {
            System.out.println(offset + " Triangle: ");

            System.out.print("1:" + indices[i] + "\t");
            leafs.get(indices[i]).printCoord();
            System.out.print("2:" + indices[i + 1] + "\t");
            leafs.get(indices[i + 1]).printCoord();
            System.out.print("3:" + indices[i + 2] + "\t");
            leafs.get(indices[i + 2]).printCoord();
    		/*
    		System.out.println("1:" + indices[i] + "\tX: " + vertices[indices[i]] + "\tY: " + vertices[indices[i]+1] + "\tZ: " + vertices[indices[i]+2]);
    		System.out.println("2: " + indices[i+1] + "\tX: " + vertices[indices[i+1]] + "\tY: " + vertices[indices[i+1]+1] + "\tZ: " + vertices[indices[i+1]+2]);
    		System.out.println("3: " + indices[i+2] + "\tX: " + vertices[indices[i+2]] + "\tY: " + vertices[indices[i+2]+1] + "\tZ: " + vertices[indices[i+2]+2]);

    		*/
            System.out.println();
            offset++;
        }


    }

    public Vector3[] getVertasVec() {
        Vector3[] list = new Vector3[vertices.length / 3];
        int offset = 0;
        for (PointNode leaf : leafs) {
            list[offset] = new Vector3(leaf.getCoordinates().x, leaf.getCoordinates().y, leaf.getCoordinates().z);
            offset++;
        }
        return list;

    }


    public void getAllTetrahedons() {

        float newPointDist = 5f;

        //need to go thoruhall triangles of the terrain
        Triangle[] allTri = getAllTris();

        Vector3[] temp;
        Line[] tempLines;
        Vector3 a, b, c, dNew, eNew, fNew;

        TetrahedronBuilder TetraBuild;
        Tetrahedron[] TetraList = new Tetrahedron[triNumBorder * 3];

        int offset = 0;
        Vector3 lineA, lineB, lineC;


        for (int i = 0; i < triNumBorder; i++) {

            //System.out.println("Number: " + i + "\t" + allTri[i]);

            temp = allTri[i].getVertices();
            tempLines = allTri[i].getLine();
            a = temp[0];
            b = temp[1];
            c = temp[2];


            lineA = tempLines[0].direction();
            lineB = tempLines[1].direction();
            lineC = tempLines[2].direction();
			/*
			System.out.println("Line1: \t" + lineA.x + "\t" + lineA.y + "\t" + lineA.z);
			System.out.println("Line2: \t" + lineB.x + "\t" + lineB.y + "\t" + lineB.z);
			System.out.println("Line3: \t" + lineC.x + "\t" + lineC.y + "\t" + lineC.z);

			System.out.println("Point1: \t" + a.x + "\t" + a.y + "\t" + a.z);
			System.out.println("Point2: \t" + b.x + "\t" + b.y + "\t" + b.z);
			System.out.println("Point3: \t" + c.x + "\t" + c.y + "\t" + c.z);

			System.out.println("");



			if(lineA.dot(lineB) == 0)	{
				dNew = new Vector3(b.x, b.y - newPointDist, b.z);
			}
			else if(lineB.dot(lineC) == 0)	{
				dNew = new Vector3(c.x, c.y - newPointDist, c.z);
			}
			else{
				dNew = new Vector3(a.x, a.y - newPointDist, a.z);
			}
			*/

            dNew = new Vector3(a.x, a.y - newPointDist, a.z);
            eNew = new Vector3(b.x, b.y - newPointDist, b.z);
            fNew = new Vector3(c.x, c.y - newPointDist, c.z);
            //First Tetraeder ABCD
            //Second Tetraeder CBFD
            //Third Tetraeder BEFD


            TetraBuild = new TetrahedronBuilder(new Vector3[]{a, b, c, dNew});
            TetraList[offset] = TetraBuild.build();

            TetraBuild = new TetrahedronBuilder(new Vector3[]{c, b, fNew, dNew});
            TetraList[offset + 1] = TetraBuild.build();

            TetraBuild = new TetrahedronBuilder(new Vector3[]{b, eNew, fNew, dNew});
            TetraList[offset + 2] = TetraBuild.build();
            offset += 3;
        }

        //return TetraList;


    }


    public Triangle[] getAllTris() {
        Vector3f start, middle, end;
        Vector3 startTemp, middleTemp, endTemp;
        Triangle tempTri;
        TriangleBuilder build;
        Triangle[] list = new Triangle[indices.length / 3];

        Vector3[] tempVecAray;
        Line line1, line2, line3;
        Line[] tempLineArray;

        int offset = 0;
        for (int i = 0; i < list.length; i += 3) {

            start = leafs.get(indices[i]).getCoordinates();
            startTemp = new Vector3(start.x, start.y, start.z);

            middle = leafs.get(indices[i + 1]).getCoordinates();
            middleTemp = new Vector3(middle.x, middle.y, middle.z);

            end = leafs.get(indices[i + 2]).getCoordinates();
            endTemp = new Vector3(end.x, end.y, end.z);

            tempVecAray = new Vector3[]{startTemp, middleTemp, endTemp};
            line1 = new Line(startTemp, middleTemp);
            line2 = new Line(middleTemp, endTemp);
            line3 = new Line(endTemp, startTemp);

            //tempLineArray = new Line[]{line1, line2, line3};
    		/*
    		System.out.println("New Triangle constructed");
    		System.out.println("Point1: \t" + startTemp.x + "\t" + startTemp.y + "\t" + startTemp.z);
			System.out.println("Point2: \t" + middleTemp.x + "\t" + middleTemp.y + "\t" + middleTemp.z);
			System.out.println("Point3: \t" + endTemp.x + "\t" + endTemp.y + "\t" + endTemp.z);
    		*/
            build = new TriangleBuilder(tempVecAray);
            tempTri = build.build();
            list[offset] = tempTri;
            offset++;
        }
        return list;
    }
    
    public float getX()	{
    	return x;
    }
    
    public float getZ()	{
    	return z;
    }
}