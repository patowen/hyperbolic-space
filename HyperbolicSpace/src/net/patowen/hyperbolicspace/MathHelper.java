package net.patowen.hyperbolicspace;

/**
 * {@code MathHelper} includes some trigonometric functions that were missing from
 * the {@code Math} class for certain operations.
 * @author Patrick Owen
 */
public class MathHelper
{
	/**
	 * Returns the inverse hyperbolic sine of the argument
	 * @param x
	 * @return arsinh x
	 */
	public static double asinh(double x)
	{
		return Math.log(x + Math.sqrt(1+x*x));
	}
	
	/**
	 * Returns the inverse hyperbolic cosine of the argument
	 * @param x
	 * @return arcosh x
	 */
	public static double acosh(double x)
	{
		return Math.log(x + Math.sqrt(x*x-1));
	}
	
	/**
	 * Returns the inverse hyperbolic tangent of the argument
	 * @param x
	 * @return artanh x
	 */
	public static double atanh(double x)
	{
		return 0.5 * (Math.log1p(x) - Math.log1p(-x));
	}
	
	/**
	 * Returns the square of the argument
	 * @param x
	 * @return x^2
	 */
	public static double sqr(double x)
	{
		return x*x;
	}
}
