package physics.generic;

/**
 * class performing rounding to a specified accuracy. 
 * Numbers input will always be converted to double type
 * @author martin
 */
public class Rounder
{
	/**
	 * @param precision number of digits relevant for rounding
	 * @param tolerance number of digits relevant for comparing numbers
	 */
	public Rounder (int precision, int tolerance)
	{
		assert (precision >= 0);
		mPrecision = precision;
		mTolerance = tolerance;
		setEpsilon();
		setShift();
	}

	/**
	 * @return the epsilon value used for comparisons, thus 10^-(precision + 1)
	 */
	public double getEpsilon() { return mEpsilon; }

	/**
	 * @return coefficient used to multiply when rounding and divide by after rounding, thus 10^precision
	 */
	public double getShift() { return mShift; }

	/**
	 * @param num number to round
	 * @return num rounded to specified decimal places
	 */
	public double roundDigits (Number num)
	{
		return Math.round (num.doubleValue() * getShift()) / getShift();
	}
	
	/**
	 * @param num number to round
	 * @return num rounded to specified significant figures
	 */
	public double roundSignificant (Number num)
	{
		double firstFigure = Math.log10 (num.doubleValue());
		firstFigure = Math.ceil (firstFigure) - mPrecision;
		
		double shift = Math.pow (10, -firstFigure);
		return (Math.round (num.doubleValue() * shift) / shift);
	}

	/**
	 * @return precision used
	 */
	public int getPrecision() { return mPrecision; }

	/**
	 * @return tolerance used
	 */
	public int getTolerance() { return mTolerance; }

	/**
	 * @param n1 a number
	 * @param n2 another number
	 * @return true if n1 ~= n2 for the specified precision/number of digits
	 */
	public boolean epsilonEquals (Number n1, Number n2)
	{
		double dn1 = n1.doubleValue(), dn2 = n2.doubleValue();
		
		return (dn1 - getEpsilon() <= dn2 && dn1 + getEpsilon() >= dn2);
	}
	
	/**
	 * @param n1 a number
	 * @param n2 another number
	 * @return true if n1, n2 rounded to specified significant figures are equal
	 */
	public boolean significanceEquals (Number n1, Number n2)
	{
		return (roundSignificant (n1) == roundSignificant (n2));
	}

	/**
	 * sets epsilon used
	 */
	private void setEpsilon ()
	{
		mEpsilon = Math.pow(10, -mTolerance) - Math.pow (10, -mTolerance - 1);
	}

	/**
	 * sets the coefficient used for rounding
	 */
	private void setShift()
	{
		mShift = Math.pow (10, mPrecision);
	}

	private double mEpsilon;
	private double mShift;
	private int mPrecision, mTolerance;
}
