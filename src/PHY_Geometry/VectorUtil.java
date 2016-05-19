package PHY_Geometry;

import com.badlogic.gdx.math.Vector3;

/**
 * advanced operations on vectors
 * @author martin
 */
public class VectorUtil extends Vector3 
{
	/**
	 * @param v vector to use for calculations
	 */
	public VectorUtil (Vector3 v)
	{
		super (v);
	}

	/**
	 * projects this vector onto the vector given
	 * @param onto vector to project this onto
	 * @return this
	 */
	// projected = a * ontoNormal
	VectorUtil orthogonalProject (Vector3 onto)
	{
		Vector3 ontoUnit = onto.cpy().setLength (1f);
		//a = this dot ontoNormal
		ontoUnit.scl (this.dot (ontoUnit));
		set (ontoUnit);
		return this;
	}


}
