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
	private Vector3 translation;
	private Orientation rotation;
	
	/**
	 * Constructs a {@code Transformation} object representing the identity transformation.
	 */
	public Transformation()
	{
		translation = new Vector3();
		rotation = new Orientation();
	}
	
	public Transformation(Vector3 translation, Orientation rotation)
	{
		this.translation = translation;
		this.rotation = rotation;
	}
	
	public Transformation composeAfter(Transformation t)
	{
		Vector3 trans = t.rotation.transform(translation);
		
		return new Transformation(trans.hyperTranslate(t.translation), rotation);
	}
}
