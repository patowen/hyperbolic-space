package net.patowen.hyperbolicspace;

import javax.media.opengl.GL3;

public interface SceneNodeType
{
	/**
	 * Prepares all scene nodes of this type for rendering.
	 * @param gl
	 */
	public void renderInit(GL3 gl);
	
	/**
	 * Renders the scene node transformed by the given transformation.
	 * @param gl
	 * @param t where the scene node should be relocated before rendering
	 */
	public void render(GL3 gl, Transformation t);
}
