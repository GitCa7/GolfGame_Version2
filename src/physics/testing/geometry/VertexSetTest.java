package physics.testing.geometry;

import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector3;
import org.junit.Test;

import physics.geometry.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;


/**
 * @autor martin
 * created 09.05.2016
 */
public class VertexSetTest
{
	public static final Random NUMGEN = new Random (System.currentTimeMillis());


	/**
	 * @param maxVal maximum value of coordinate
	 * @param minVal minimum value of coordinate
	 * @return generates random vector in 3d space of coordinates between minVal, maxVal
	 */
	public static Vector3 randomVec (int minVal, int maxVal)
	{
		Vector3 rnd = new Vector3();
		int range = maxVal - minVal;

		rnd.x = minVal + NUMGEN.nextInt (range);
		rnd.y = minVal + NUMGEN.nextInt (range);
		rnd.z = minVal + NUMGEN.nextInt (range);

		return rnd;
	}

	/**
	 * @param maxVal maximum value of coordinate
	 * @param minVal minimum value of coordinate
	 * @param minSize minimum size of set
	 * @param maxSize maximum size of set
	 * @return set of cardinality between minSize, maxSize and vector coordinates between minVal, maxVal
	 */
	public static Vector3[] randomVecSet (int minVal, int maxVal, int minSize, int maxSize)
	{
		Random rGen = new Random (System.currentTimeMillis());
		Vector3[] set = new Vector3[minSize + rGen.nextInt (maxSize - minSize)];

		for (int cElem = 0; cElem < set.length; ++cElem)
			set[cElem] = randomVec (minVal, maxVal);
		return set;
	}

	/**
	 * @param vecs array of vectors
	 * @return true if the sum of vectors generated by VertexSet is equal to
	 * the sum generated manually
	 */
	public static boolean testVectors (boolean debug, Vector3... vecs)
	{

		VertexSet s = new VertexSet (vecs);
		Vector3[] obtained = s.vectors();

		Vector3 checkSumObtained = new Vector3();
		for (Vector3 ob : obtained)
				checkSumObtained.add (ob);

		//catch case with |set| <= 1
		if (vecs.length <= 1 && checkSumObtained.equals (new Vector3()))
			return true;

		//iterate over vectors which could have been picked as base
		for (int cPick = 0; cPick < vecs.length; ++cPick)
		{
			Vector3 checkSumExpect = vecs[cPick].cpy().scl (vecs.length - 1);
			for (int cVec = 0; cVec < vecs.length; ++cVec)
			{
				if (cVec != cPick)
					checkSumExpect.sub (vecs[cVec]);
			}

			if (debug)
			{
				System.out.println ("vecs");
				for (Vector3 vec : vecs)
					System.out.println (vec);
				System.out.println ("pick " + cPick + " sums " + checkSumExpect);
				System.out.println ("vs " + checkSumObtained);
			}

			if (checkSumObtained.equals (checkSumExpect))
				return true;
			if (checkSumExpect.scl(-1f).equals (checkSumExpect))
				return true;
		}

		return false;
	}

	/**
	 * @param s1 set of vectors to search in
	 * @param s2 set of vectors to search for
	 * @return
	 */
	public static boolean testNotContainedIn (Vector3[] s1, Vector3[] s2)
	{
		VertexSet vSet = new VertexSet (s1);

		HashSet<Vector3> set1 = new HashSet<> (Arrays.asList(s1));
		HashSet<Vector3> set2 = new HashSet<> (Arrays.asList (s2));
		HashSet<Vector3> expect = new HashSet<> (set1);
		expect.removeAll (set2);

		HashSet<Vector3> obtained = new HashSet<> (vSet.notContainedIn (s2));
		if (obtained.containsAll (expect))
			return true;
		return false;
	}


	@Test
	/**
	 * runs certain number of tests of random number of vectors, values.
	 * Tests whether VertexSetTest.vector is correct
	 */
	public void testVectors()
	{
		int runs = 100;
		int minSetSize = 0;
		int maxSetSize = 10;

		int minVecEntry = -100;
		int maxVecEntry = 100;

		boolean debug = false;

		for (int cRun = 0; cRun < runs; ++cRun)
		{
			//generate random vectors
			Vector3[] set = new Vector3[minSetSize + NUMGEN.nextInt (maxSetSize - minSetSize)];
			for (int cSetElem = 0; cSetElem < set.length; ++cSetElem)
				set[cSetElem] = randomVec (minVecEntry, maxVecEntry);

			assertTrue (testVectors (debug, set));
		}
	}


	@Test
	/**
	 * tests method VertexSet.notContainedIn
	 */
	public void testNotContainedIn()
	{
		int runs = 100;
		int minCoord = -100;
		int maxCoord = 100;
		int minSize = 0;
		int maxSize = 10;
		double prob = 1/4;

		for (int cRun = 0; cRun < runs; ++cRun)
		{
			Vector3[] setIn = randomVecSet (minCoord, maxCoord, minSize, maxSize);
			Vector3[] setFor = randomVecSet (minCoord, maxCoord, minSize, maxSize);

			for (int cCopy = 0; cCopy < setFor.length && cCopy < setIn.length; ++cCopy)
			{
				if (NUMGEN.nextDouble() < prob)
				setFor[cCopy] = setIn[cCopy];
			}

			assertTrue (testNotContainedIn (setIn, setFor));
		}

	}
}
