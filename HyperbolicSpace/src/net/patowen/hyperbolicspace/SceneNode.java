package net.patowen.hyperbolicspace;

import com.jogamp.opengl.GL3;

/**
 * Stores the location of a single instance of a {@code SceneNodeType} and renders it.
 * @author Patrick Owen
 */
public class SceneNode
{
	private SceneNodeType type;
	private Transformation t;
	
	/**
	 * Initializes the SceneNode. No heavy computation is done in this method
	 * @param type which mesh the node should render
	 */
	public SceneNode(SceneNodeType type)
	{
		this.type = type;
		t = new Transformation();
	}
	
	/**
	 * Changes the location of the instance of the {@code SceneNodeType}
	 * @param t the new transformation
	 */
	public void setTransformation(Transformation t)
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
