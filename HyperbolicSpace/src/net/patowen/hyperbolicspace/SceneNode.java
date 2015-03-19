package net.patowen.hyperbolicspace;

import javax.media.opengl.GL3;

public interface SceneNode
{
	public void setTransformation(Transformation t);
	
	public void reposition(Transformation t);
	
	public void renderInit(GL3 gl, MatrixHandler mh);
	
	public void render(GL3 gl);
}
