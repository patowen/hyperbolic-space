package net.patowen.hyperbolicspace;

/**
 * The {@code Orientation} class holds a 3x3 matrix that represents the orientation of a given object
 * in the Poincaré ball. The component vectors are orthogonal and normalized, and they represent the
 * orientation the object would assume if directly translated to the origin.
 * @author Patrick Owen
 */
public class Orientation
{
	/**
	 * The forward-facing component of the orientation, or the first column of the matrix.
	 */
	public Vector3 x;
	
	/**
	 * The right-facing component of the orientation, or the second column of the matrix.
	 */
	public Vector3 y;
	
	/**
	 * The up-facing component of the orientation, or the third column of the matrix.
	 */
	public Vector3 z;
	
	/**
	 * Constructs an {@code Orientation} object with its matrix's columns set to the given vectors.
	 * @param x the forward-facing component of the orientation, or the first column of the matrix
	 * @param y the right-facing component of the orientation, or the second column of the matrix
	 * @param z the up-facing component of the orientation, or the third column of the matrix
	 */
	public Orientation(Vector3 x, Vector3 y, Vector3 z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Constructs an {@code Orientation} object that represents the same orientation as the argument.
	 * @param o an {@code Orientation}
	 */
	public Orientation(Orientation o)
	{
		this(new Vector3(o.x), new Vector3(o.y), new Vector3(o.z));
	}
	
	/**
	 * Constructs an {@code Orientation} object with the identity matrix, representing a neutral orientation.
	 */
	public Orientation()
	{
		this(new Vector3(1, 0, 0), new Vector3(0, 1, 0), new Vector3(0, 0, 1));
	}
	
	/**
	 * Changes the columns of the underlying matrix to be orthonormal. The direction of the x-vector
	 * is unchanged, while the direction of the y-vector is completely determined by the directions
	 * of the x and z vectors before the call to this method. The purpose of this method is to prevent
	 * floating point errors from building up and ruining the matrix.
	 */
	public void normalize()
	{
		x.normalize();
		y = x.cross(z);
		y.normalize();
		z = y.cross(x);
	}
	
	/**
	 * Rotates the orientation about the given unit vector by {@code theta}.
	 * @param v a unit vector
	 * @param theta the angle in radians
	 */
	public void rotate(Vector3 v, double theta)
	{
		x.rotate(v, theta);
		y.rotate(v, theta);
		z.rotate(v, theta);
		
		normalize();
	}
	
	/**
	 * Returns a vector that represents the argument in the basis given by the orientation.
	 * @param v a vector
	 * @return a vector that represents the argument in the basis given by the orientation
	 */
	public Vector3 apply(Vector3 v)
	{
		return new Vector3(x.dot(v), y.dot(v), z.dot(v));
	}
	
	/**
	 * Returns the orientation after a translation in the Poincaré ball model that directly moves the
	 * origin to the initial location of {@code v}.
	 * @param pos the initial location of the object that the orientation describes
	 * @param v a vector of magnitude less than 1
	 * @return the orientation after the translation
	 */
	public void hyperTranslate(Vector3 pos, Vector3 v)
	{
		x = x.hyperTranslate(pos).hyperTranslate(v);
		z = z.hyperTranslate(pos).hyperTranslate(v);
		
		pos = pos.hyperTranslate(v);
		
		x = x.hyperTranslate(pos.times(-1));
		z = z.hyperTranslate(pos.times(-1));
		
		normalize();
	}
}
