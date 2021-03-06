package net.patowen.hyperbolicspace.rendering;

import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.math.Transform;

/**
 * Represents a mesh with a texture that can be drawn at any location
 * @author Patrick Owen
 */
public interface SceneNodeType {
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
	public void render(GL3 gl, Transform t);
}
