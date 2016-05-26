package TerrainComponents;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.badlogic.gdx.math.Vector3;

import physics.geometry.linear.Line;
import physics.geometry.planar.Triangle;
import physics.geometry.planar.TriangleBuilder;
import physics.geometry.spatial.TetrahedronBuilder;

public class TerrainGeometryCalc {
	
	public TerrainGeometryCalc()	{
		
	}
	
	
	public Triangle[] getAllTris(proxyTerrain prox)		{
		Vector3f start, middle, end;
    	Vector3 startTemp, middleTemp, endTemp;
    	Triangle tempTri;
    	TriangleBuilder build;
    	int[] indices = prox.getIndices();
    	ArrayList<PointNode> leafs = prox.getLeafs();
    	Triangle[] list = new Triangle[indices.length / 3];
    	
    	Vector3[] tempVecAray;
    	Line line1, line2, line3;
    	Line[] tempLineArray;
    	
    	int offset = 0;
    	for(int i = 0; i < list.length; i+=3)	{
    		
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
    	return list;
	}
	
	 public void getAllTetrahedons(proxyTerrain prox)	{
	    	
	    	float newPointDist = 5f;
	    	
	    	//need to go thoruhall triangles of the terrain
	    	Triangle[] allTri = getAllTris(prox);
	    	
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
	    	
	    	//return TetraList;
	    	
	    	
	    }

}
