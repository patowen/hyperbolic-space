package net.patowen.hyperbolicspace.rendering;

import java.util.Stack;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.glsl.ShaderState;

import net.patowen.hyperbolicspace.math.Transform;

/**
 * {@code ShaderUniformHandler} has two purposes. It holds a Transformation stack for the viewpoint and
 * for hierarchical models, and it interacts with the shader to set the uniforms to the appropriate
 * values.
 * @author Patrick Owen
 */
public class ShaderUniformHandler {
	private ShaderState shaderState;
	
	private ShaderLink<UniformTransform> transform;
	private ShaderLink<UniformFloatList> perspective;
	private ShaderLink<UniformFloatList> color;
	
	private Stack<Transform> transformStack;
	
	/**
	 * Initializes all values to their defaults
	 * @param shaderState the {@code ShaderState} representing the shader whose
	 * uniforms are set
	 */
	public ShaderUniformHandler(ShaderState shaderState) {
		this.shaderState = shaderState;
		transformStack = new Stack<Transform>();
		
		transform = new ShaderLink<>("transform", new UniformTransform(Transform.identity()));
		perspective = new ShaderLink<>("perspective", new UniformFloatList(4, 4));
		color = new ShaderLink<>("color", new UniformFloatList(4, 1));
	}
	
	/**
	 * Sets the stored transformation to the identity
	 */
	public void reset() {
		transform.data.transform = Transform.identity();
	}
	
	/**
	 * Adds the specified transformation relative to the current transformation
	 * @param t The transformation to apply before the current one
	 */
	public void addTransformation(Transform t) {
		transform.data.transform = transform.data.transform.transform(t);
	}
	
	/**
	 * Stores the current transformation onto a stack for later retrieval
	 */
	public void pushTransformation() {
		transformStack.push(transform.data.transform);
	}
	
	/**
	 * Sets the current transformation to the previously stored transformation
	 * on the stack
	 */
	public void popTransformation() {
		transform.data.transform = transformStack.pop();
	}
	
	/**
	 * Sets the stored perspective matrix to the given matrix
	 * @param perspective the perspective matrix represented via a
	 * 16-value array in column-major order
	 */
	public void setPerspective(float[] perspective) {
		System.arraycopy(perspective, 0, this.perspective.data.list, 0, 16);
	}
	
	/**
	 * Sets the stored color to the given color
	 * @param color the color represented via a 4-value array (r, g, b, a)
	 */
	public void setColor(float[] color) {
		System.arraycopy(color, 0, this.color.data.list, 0, 4);
	}
	
	/**
	 * Sets all the uniforms of the shader to the currently stored values. This
	 * should be called before drawing anything if anything was changed.
	 * @param gl
	 */
	public void update(GL3 gl) {
		transform.update(shaderState, gl);
		perspective.update(shaderState, gl);
		color.update(shaderState, gl);
	}
}
