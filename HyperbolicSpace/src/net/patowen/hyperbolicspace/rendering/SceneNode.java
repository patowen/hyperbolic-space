package net.patowen.hyperbolicspace.rendering;

import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.math.Transform;

/**
 * Stores the location of a single instance of a {@code SceneNodeType} and renders it.
 * @author Patrick Owen
 */
public class SceneNode
{
	private SceneNodeType type;
	private Transform t;
	
	/**
	 * Initializes the SceneNode. No heavy computation is done in this method
	 * @param type which mesh the node should render
	 */
	public SceneNode(SceneNodeType type)
	{
		this.type = type;
		t = Transform.identity();
	}
	
	/**
	 * Changes the location of the instance of the {@code SceneNodeType}
	 * @param t the new transformation
	 */
	public void setTransformation(Transform t)
	{
		this.t = t;
	}
	
	/**
	 * Renders the scene node in the location stored
	 * @param gl
	 */
	public void render(GL3 gl)
	{
		type.render(gl, t);
	}
}
