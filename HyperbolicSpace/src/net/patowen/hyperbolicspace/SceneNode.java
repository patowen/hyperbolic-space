package net.patowen.hyperbolicspace;

import javax.media.opengl.GL3;

public class SceneNode
{
	private SceneNodeType type;
	private Transformation t;
	
	public SceneNode(SceneNodeType type)
	{
		this.type = type;
		t = new Transformation();
	}
	
	public void setTransformation(Transformation t)
	{
		this.t = t;
	}
	
	public void render(GL3 gl)
	{
		type.render(gl, t);
	}
}
