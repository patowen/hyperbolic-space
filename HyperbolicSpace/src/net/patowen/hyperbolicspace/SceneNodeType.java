package net.patowen.hyperbolicspace;

import javax.media.opengl.GL3;

public interface SceneNodeType
{
	public void render(GL3 gl, Transformation t);
	public void renderInit(GL3 gl);
}
