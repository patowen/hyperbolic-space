package net.patowen.hyperbolicspace;

/**
 * The {@code Transformation} class represents an isometric transformation in hyperbolic space. Like
 * matrix transformation in Euclidean geometry, transformations can be composed in either order, and they
 * can be applied to vertices and to normals.
 * @author Patrick Owen
 */
public class Transformation
{
	/*
	 * Internally, a transformation is represented as a rotation
	 * followed by a hyperbolic translation.
	 */
	private Orientation rotation;
	private Vector3 translation;
	
	/**
	 * Constructs a {@code Transformation} object representing the identity transformation.
	 */
	public Transformation()
	{
		rotation = new Orientation();
		translation = new Vector3();
	}
	
	/**
	 * Constructs a {@code Transformation} object representing the given rotation followed
	 * by the given translation.
	 */
	public Transformation(Orientation rotation, Vector3 translation)
	{
		this.rotation = rotation;
		this.translation = translation;
	}
	
	/**
	 * Copy constructor. Completes a shallow copy of the given transformation object
	 */
	public Transformation(Transformation t)
	{
		rotation = t.rotation;
		translation = t.translation;
	}
	
	/**
	 * Returns the transform that when combined with the current one, will return the identity transformation.
	 * @return the inverse transformation
	 */
	public Transformation inverse()
	{
		Orientation inverseRotation = rotation.inverse();
		return new Transformation(inverseRotation, inverseRotation.transform(translation).times(-1));
	}
	
	/**
	 * Returns the transformation that results from composing the transformation with
	 * the argument's transformation such that the argument's transformation is done last.
	 * @param t a transformation
	 * @return the resulting transformation
	 */
	public Transformation composeAfter(Transformation t)
	{
		Vector3 trans = t.rotation.transform(translation);
		Orientation rot = t.rotation.transform(rotation);
		
		return new Transformation(rot.hyperTranslate(trans, t.translation), trans.hyperTranslate(t.translation));
	}
	
	/**
	 * Returns the transformation that results from composing the transformation with
	 * the argument's transformation such that the argument's transformation is done first.
	 * @param t a transformation
	 * @return the resulting transformation
	 */
	public Transformation composeBefore(Transformation t)
	{
		Vector3 trans = rotation.transform(t.translation);
		Orientation rot = rotation.transform(t.rotation);
		
		return new Transformation(rot.hyperTranslate(trans, translation), trans.hyperTranslate(translation));
	}
	
	/**
	 * Returns the given vector transformed by the represented transformation.
	 * @param v a vector
	 * @return the transformed vector
	 */
	public Vector3 transform(Vector3 v)
	{
		return rotation.transform(v).hyperTranslate(translation);
	}
	
	/**
	 * Returns the given vector transformed by the inverse of the represented transformation.
	 * @param v a vector
	 * @return the transformed vector
	 */
	public Vector3 inverseTransform(Vector3 v)
	{
		return rotation.useAsBasis(v.hyperTranslate(translation.times(-1)));
	}
	
	/**
	 * Returns the initial rotation of the transformation.
	 * @return the initial rotation of the transformation
	 */
	public Orientation getRotation()
	{
		return rotation;
	}
	
	/**
	 * Returns the final translation of the transformation.
	 * @return the final translation of the transformation
	 */
	public Vector3 getTranslation()
	{
		return translation;
	}
}
