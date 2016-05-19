package physics.components;


/**
 * static friction component\n
 * provides direct access to friction coefficient (float primitive)
 * @author martin
 */
public class Friction implements Component
{
	/** indicate static or dynamic friction **/
	public enum State {STATIC, DYNAMIC};
	/** indicate movement or spinning friction **/
	public enum Type {MOVE, SPIN};
	
	/**
	 * default constructor, friction initialized to 0
	 */
	public Friction ()
	{
		
	}
	
	/**
	 * parametric constructor specifying all friction coefficients
	 * @param staticMove static movement coefficient
	 * @param dynamicMove dynamic movement coefficient
	 * @param staticSpin static spinning coefficient
	 * @param dynamicSpin dynamic spinning coefficient
	 */
	public Friction (float staticMove, float dynamicMove, float staticSpin, float dynamicSpin)
	{
		mStaticMoveCoefficient = staticMove;
		mDynamicMoveCoefficient = dynamicMove;
		mStaticSpinCoefficient = staticSpin;
		mDynamicSpinCoefficient = dynamicSpin;
	}


	public Friction clone()
	{
		 return new Friction (mStaticMoveCoefficient, mDynamicMoveCoefficient, mStaticSpinCoefficient, mDynamicSpinCoefficient);
	}

	/**
	 * coefficient getter
	 * @param s friction state
	 * @param t friction type
	 * @return friction coefficient corresponding to state, type specified
	 */
	public float get (State s, Type t)
	{
		if (s == State.STATIC)
		{
			if (t == Type.MOVE)
				return mStaticMoveCoefficient;
			if (t == Type.SPIN)
				return mStaticSpinCoefficient;
		}
		else if (s == State.DYNAMIC)
		{
			if (t == Type.MOVE)
				return mDynamicMoveCoefficient;
			if (t == Type.SPIN)
				return mDynamicSpinCoefficient;
		}
		throw new IllegalArgumentException ("invalid state or type parameter");
	}
	
	/**
	 * coefficient setter
	 * @param s friction state
	 * @param t friction type
	 */
	public void set (State s, Type t, float value)
	{
		if (s == State.STATIC)
		{
			if (t == Type.MOVE)
				mStaticMoveCoefficient = value;
			if (t == Type.SPIN)
				mStaticSpinCoefficient = value;
		}
		else if (s == State.DYNAMIC)
		{
			if (t == Type.MOVE)
				mDynamicMoveCoefficient = value;
			if (t == Type.SPIN)
				mDynamicSpinCoefficient = value;
		}
		throw new IllegalArgumentException ("invalid state or type parameter");
	}
	
	private float mStaticMoveCoefficient, mDynamicMoveCoefficient;
	private float mStaticSpinCoefficient, mDynamicSpinCoefficient;
}
