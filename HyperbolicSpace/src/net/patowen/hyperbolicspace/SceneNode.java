package net.patowen.hyperbolicspace;

import javax.media.opengl.GL3;

public interface SceneNode
{
	public void reposition(Vector3 v);
	
	public void renderInit(GL3 gl);
	
	public void render(GL3 gl);
}
