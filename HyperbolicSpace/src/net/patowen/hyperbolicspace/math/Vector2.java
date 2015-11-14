package net.patowen.hyperbolicspace.math;

/**
 * The {@code Vector3} class represents a given vector in 3-dimensional Euclidean space.
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
	
	public Vector2(double[] coords)
	{
		x = coords[0];
		y = coords[1];
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
	 * Returns the result of subtracting the argument from the vector.
	 * @param v a vector
	 * @return the vector minus the argument
	 */
	public Vector2 minus(Vector2 v)
	{
		return new Vector2(x-v.x, y-v.y);
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
	 * Returns the dot product of the vector and the argument.
	 * @param v a vector
	 * @return the dot product of the vector and the argument
	 */
	public double dot(Vector2 v)
	{
		return (x*v.x + y*v.y);
	}
	
	/**
	 * Returns a perpendicular vector to the current vector.
	 * @return the cross product of the vector and <0,0,1>
	 */
	public Vector2 cross()
	{
		return new Vector2(y, -x);
	}
	
	/**
	 * Rotates the vector about the origin by {@code theta}.
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
	 * Returns the vector after a translation in the Poincare ball model that directly moves the
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
	
	/**
	 * Returns {@code x} after a translation in the Poincare ball model that directly moves the
	 * vector to the origin.
	 * @param x a unit vector
	 * @return the direction from the vector to {@code x}
	 */
	public Vector2 hyperDirectionTo(Vector2 x)
	{
		Vector2 v = this;
		Vector2 diff = x.minus(v);
		return diff.times(2*diff.dot(x)/diff.squared()).minus(x);
	}
	
	/**
	 * Returns the vector after an ideal rotation in the Poincare ball model
	 * @param q the point at infinity on the horocycle the vector moves along
	 * @param r the direction the origin originally moves. q and r should be orthogonal unit vectors
	 * @param v the distance along the horocycle the origin is moved
	 * @return the rotated vector
	 */
	public Vector2 horoRotate(Vector2 q, Vector2 r, double v)
	{
		double zmqs = this.minus(q).squared();
		double denom = v*v*zmqs + 2*v*this.dot(r) + 1;
		double rFactor = v*zmqs;
		double qFactor = v*v*zmqs + 2*v*this.dot(r);
		double factor = 1;
		
		return times(factor).plusMultiple(r,rFactor).plusMultiple(q, qFactor).times(1/denom);
	}
	
	/**
	 * Returns a string representation of the vector
	 */
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}
}
