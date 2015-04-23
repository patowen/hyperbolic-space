package net.patowen.poincaresandbox;


/**
 * The {@code Vector2} class represents a given vector in 2-dimensional Euclidean space.
 * @author Patrick Owen
 */
public class Vector2
{
	/**
	 * The x-coordinate of the vector.
	 */
	public double x;
	
	/**
	 * The y-coordinate of the vector.
	 */
	public double y;
	
	/**
	 * Constructs a {@code Vector2} object representing the zero vector.
	 */
	public Vector2()
	{
		x = 0; y = 0;
	}
	
	/**
	 * Constructs a {@code Vector2} object that represents the same vector as the argument.
	 * @param v a {@code Vector2}
	 */
	public Vector2(Vector2 v)
	{
		this(v.x, v.y);
	}
	
	/**
	 * Changes the vector to the zero vector.
	 */
	public void reset()
	{
		x = 0; y = 0;
	}
	
	/**
	 * Constructs a {@code Vector2} object with the specified coordinates.
	 * @param x the x-coordinate of the vector.
	 * @param y the y-coordinate of the vector.
	 */
	public Vector2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Changes the vector to the unit vector pointing in the same direction. This method
	 * does nothing to the zero vector.
	 */
	public void normalize()
	{
		double dist = x*x+y*y;
		if (dist > 0)
		{
			double size = Math.sqrt(dist);
			x /= size;
			y /= size;
		}
	}
	
	/**
	 * Returns the magnitude of the vector.
	 * @return the magnitude, or Euclidean norm, of the vector
	 */
	public double magnitude()
	{
		return Math.sqrt(x*x+y*y);
	}
	
	/**
	 * Returns the squared magnitude of the vector.
	 * @return the magnitude squared, or the dot product of the vector and itself
	 */
	public double squared()
	{
		return x*x+y*y;
	}
	
	/**
	 * Returns the result of adding the vector to the argument.
	 * @param v a vector
	 * @return the vector plus the argument
	 */
	public Vector2 plus(Vector2 v)
	{
		return new Vector2(x+v.x, y+v.y);
	}
	
	/**
	 * Adds the argument to the vector.
	 * @param v a vector
	 */
	public void add(Vector2 v)
	{
		x += v.x;
		y += v.y;
	}
	
	/**
	 * Returns the result of adding the vector to the argument times the given scalar.
	 * @param v a vector
	 * @param c a scalar factor
	 * @return the vector plus the argument times the given scalar
	 */
	public Vector2 plusMultiple(Vector2 v, double c)
	{
		return new Vector2(x+v.x*c, y+v.y*c);
	}
	
	/**
	 * Adds the argument times the given scalar to the vector.
	 * @param v a vector
	 * @param c a scalar factor
	 */
	public void addMultiple(Vector2 v, double c)
	{
		x += v.x*c;
		y += v.y*c;
	}
	
	/**
	 * Returns the result of subtracting the argument from the vector.
	 * @param v a vector
	 * @return the vector minus the argument
	 */
	public Vector2 minus(Vector2 v)
	{
		return new Vector2(x-v.x, y-v.y);
	}
	
	/**
	 * Subtracts the argument from the vector.
	 * @param v a vector
	 */
	public void subtract(Vector2 v)
	{
		x -= v.x;
		y -= v.y;
	}
	
	/**
	 * Returns the vector multiplied by the given scalar.
	 * @param c a scalar factor
	 * @return the vector times the scalar factor
	 */
	public Vector2 times(double c)
	{
		return new Vector2(c*x, c*y);
	}
	
	/**
	 * Multiplies the vector by the given scalar.
	 * @param c a scalar factor
	 */
	public void multiply(double c)
	{
		x *= c;
		y *= c;
	}
	
	/**
	 * Returns the dot product of the vector and the argument.
	 * @param v a vector
	 * @return the dot product of the vector and the argument
	 */
	public double dot(Vector2 v)
	{
		return (x*v.x + y*v.y);
	}
	
	/**
	 * Returns a vector perpendicular to the current vector
	 * with a given orientation and the same magnitude.
	 * @param v a vector
	 * @return the perpendicular vector
	 */
	public Vector2 cross()
	{
		return new Vector2(y, -x);
	}
	
	/**
	 * Rotates the vector about the origin by {@code theta}.
	 * @param v a unit vector
	 * @param theta the angle in radians
	 */
	public void rotate(double theta)
	{
		double c = Math.cos(theta), s = Math.sin(theta);
		
		double xNew = x*c - y*s;
		double yNew = x*s + y*c;
		
		x = xNew; y = yNew;
	}
	
	/**
	 * Returns the vector after a translation in the Poincaré ball model that directly moves the
	 * origin to the initial location of {@code v}.
	 * @param v a vector of magnitude less than 1
	 * @return the vector after the translation
	 */
	public Vector2 hyperTranslate(Vector2 v)
	{
		double denom = v.squared()*squared() + 2*dot(v) + 1;
		double vFactor = 1 + squared() + 2*dot(v);
		double factor = 1 - v.squared();
		
		return (times(factor).plusMultiple(v, vFactor)).times(1/denom);
	}
	
	public Vector2 dir(Vector2 x)
	{
		Vector2 v = this;
		Vector2 diff = x.minus(v);
		return diff.times(2*diff.dot(x)/diff.squared()).minus(x);
	}
	
	public Vector2 horoRotate(Vector2 q, Vector2 r, double v)
	{
		double zmqs = this.minus(q).squared();
		double denom = v*v*zmqs + 2*v*this.dot(r) + 1;
		double rFactor = v*zmqs;
		double qFactor = v*v*zmqs + 2*v*this.dot(r);
		double factor = 1;
		
		return times(factor).plusMultiple(r,rFactor).plusMultiple(q, qFactor).times(1/denom);
	}
}
