package net.patowen.hyperbolicspace.math;

/**
 * The {@code Vector3} class represents a given vector in 3-dimensional Euclidean space.
 * @author Patrick Owen
 */
public class Vector3
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
	 * The z-coordinate of the vector.
	 */
	public double z;
	
	/**
	 * Constructs a {@code Vector3} object representing the zero vector.
	 */
	public Vector3()
	{
		x = 0; y = 0; z = 0;
	}
	
	/**
	 * Constructs a {@code Vector3} object that represents the same vector as the argument.
	 * @param v a {@code Vector3}
	 */
	public Vector3(Vector3 v)
	{
		this(v.x, v.y, v.z);
	}
	
	/**
	 * Changes the vector to the zero vector.
	 */
	public void reset()
	{
		x = 0; y = 0; z = 0;
	}
	
	/**
	 * Constructs a {@code Vector3} object with the specified coordinates.
	 * @param x the x-coordinate of the vector.
	 * @param y the y-coordinate of the vector.
	 * @param z the z-coordinate of the vector.
	 */
	public Vector3(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Changes the vector to the unit vector pointing in the same direction. This method
	 * does nothing to the zero vector.
	 */
	public void normalize()
	{
		double dist = x*x+y*y+z*z;
		if (dist > 0)
		{
			double size = Math.sqrt(dist);
			x /= size;
			y /= size;
			z /= size;
		}
	}
	
	/**
	 * Returns the magnitude of the vector.
	 * @return the magnitude, or Euclidean norm, of the vector
	 */
	public double magnitude()
	{
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	/**
	 * Returns the squared magnitude of the vector.
	 * @return the magnitude squared, or the dot product of the vector and itself
	 */
	public double squared()
	{
		return x*x+y*y+z*z;
	}
	
	/**
	 * Returns the result of adding the vector to the argument.
	 * @param v a vector
	 * @return the vector plus the argument
	 */
	public Vector3 plus(Vector3 v)
	{
		return new Vector3(x+v.x, y+v.y, z+v.z);
	}
	
	/**
	 * Returns the result of adding the vector to the argument times the given scalar.
	 * @param v a vector
	 * @param c a scalar factor
	 * @return the vector plus the argument times the given scalar
	 */
	public Vector3 plusMultiple(Vector3 v, double c)
	{
		return new Vector3(x+v.x*c, y+v.y*c, z+v.z*c);
	}
	
	/**
	 * Returns the result of subtracting the argument from the vector.
	 * @param v a vector
	 * @return the vector minus the argument
	 */
	public Vector3 minus(Vector3 v)
	{
		return new Vector3(x-v.x, y-v.y, z-v.z);
	}
	
	/**
	 * Returns the vector multiplied by the given scalar.
	 * @param c a scalar factor
	 * @return the vector times the scalar factor
	 */
	public Vector3 times(double c)
	{
		return new Vector3(c*x, c*y, c*z);
	}
	
	/**
	 * Returns the dot product of the vector and the argument.
	 * @param v a vector
	 * @return the dot product of the vector and the argument
	 */
	public double dot(Vector3 v)
	{
		return (x*v.x + y*v.y + z*v.z);
	}
	
	/**
	 * Returns the cross product of the vector and the argument.
	 * @param v a vector
	 * @return the cross product of the vector and the argument
	 */
	public Vector3 cross(Vector3 v)
	{
		return new Vector3(y*v.z-z*v.y, z*v.x-x*v.z, x*v.y-y*v.x);
	}
	
	/**
	 * Rotates the vector about the given unit vector by {@code theta}.
	 * @param v a unit vector
	 * @param theta the angle in radians
	 */
	public void rotate(Vector3 v, double theta)
	{
		double xx = v.x, yy = v.y, zz = v.z;
		double c = Math.cos(theta), s = Math.sin(theta);
		
		double xNew = x*(xx*xx+(1-xx*xx)*c) + y*(xx*yy-xx*yy*c-zz*s) + z*(xx*zz-xx*zz*c+yy*s);
		double yNew = x*(xx*yy-xx*yy*c+zz*s) + y*(yy*yy+(1-yy*yy)*c) + z*(yy*zz-yy*zz*c-xx*s);
		double zNew = x*(xx*zz-xx*zz*c-yy*s) + y*(yy*zz-yy*zz*c+xx*s) + z*(zz*zz+(1-zz*zz)*c);
		
		x = xNew; y = yNew; z = zNew;
	}
	
	/**
	 * Returns the vector after a translation in the Poincare ball model that directly moves the
	 * origin to the initial location of {@code v}.
	 * @param v a vector of magnitude less than 1
	 * @return the vector after the translation
	 */
	public Vector3 hyperTranslate(Vector3 v)
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
	public Vector3 hyperDirectionTo(Vector3 x)
	{
		Vector3 v = this;
		Vector3 diff = x.minus(v);
		return diff.times(2*diff.dot(x)/diff.squared()).minus(x);
	}
	
	/**
	 * Returns the vector after an ideal rotation in the Poincare ball model
	 * @param q the point at infinity on the horocycle the vector moves along
	 * @param r the direction the origin originally moves. q and r should be orthogonal unit vectors
	 * @param v the distance along the horocycle the origin is moved
	 * @return the rotated vector
	 */
	public Vector3 horoRotate(Vector3 q, Vector3 r, double v)
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
		return "(" + x + "," + y + "," + z + ")";
	}
}
