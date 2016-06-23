package physics.geometry.linear;

import java.util.Arrays;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import physics.generic.PowersetGenerator;

/**
 * generates the vertices of any shape/solid etc if the vertices can be obtained by
 * adding an element of the power set of the direction vectors to the offset
 * @author martin
 */
public class VertexGenerator 
{
	/**
	 * parametric constructor
	 * @param offset offset vector of shape...
	 * @param directions direction vectors
	 */
	public VertexGenerator (Vector3 offset, Vector3 ... directions)
	{
		mOffset = offset;
		mDirections = directions;
	}
	
	/**
	 * @return generated vertices
	 */
	public Vector3[] generate()
	{
		PowersetGenerator<Vector3> gen = new PowersetGenerator<> (Arrays.asList (mDirections));
		return addUp (gen.generate());
	}
	
	/**
	 * @param minDirVecs minimum number of direction vectors to use
	 * @param maxDirVecs maximum number of direction vectors to use
	 * @return generated vertices obtained by adding at least minDirVecs direction vectors
	 * to the offset and at most maxDirVecs direction vectors
	 */
	public Vector3[] generate (int minDirVecs, int maxDirVecs)
	{
		PowersetGenerator<Vector3> gen = new PowersetGenerator<> (Arrays.asList (mDirections));
		return addUp (gen.generate (minDirVecs, maxDirVecs));
	}
	
	/**
	 * @param addSet list of lists, each containing the vectors to be added to offset
	 * @return array of vectors, each being the sum of the offset and an element of addSet. 
	 * Note: addSet is not modified, new vectors are being constructed
	 */
	public Vector3[] addUp (ArrayList<ArrayList<Vector3>> addSet)
	{
		Vector3[] vecs = new Vector3[addSet.size()];
		for (int cVec = 0; cVec < vecs.length; ++cVec)
		{
			vecs[cVec] = mOffset.cpy();
			for (Vector3 add : addSet.get (cVec))
				vecs[cVec].add (add);
		}
		return vecs;
	}
	
	
	private Vector3 mOffset;
	private Vector3[] mDirections;
}
