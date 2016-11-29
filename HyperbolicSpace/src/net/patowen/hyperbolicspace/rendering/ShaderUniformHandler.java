package net.patowen.hyperbolicspace.rendering;

import java.nio.FloatBuffer;
import java.util.Stack;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLUniformData;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.glsl.ShaderState;

import net.patowen.hyperbolicspace.math.Transform;

/**
 * {@code MatrixHandler} has two purposes. It holds a Transformation stack for the viewpoint and
 * for hierarchical models, and it interacts with the shader to set the uniforms to the appropriate
 * values.
 * @author Patrick Owen
 */
public class ShaderUniformHandler {
	private ShaderState shaderState;
	
	private Transform transform;
	private Stack<Transform> transformStack;
	
	private FloatBuffer perspectiveBuf;
	private float[] perspectiveArray;
	
	private FloatBuffer colorBuf;
	private float[] colorArray;
	
	private FloatBuffer transformBuf;
	
	/**
	 * Initializes all values to their defaults
	 * @param shaderState the {@code ShaderState} representing the shader whose
	 * uniforms are set
	 */
	public ShaderUniformHandler(ShaderState shaderState) {
		this.shaderState = shaderState;
		
		transform = Transform.identity();
		transformStack = new Stack<Transform>();
		
		perspectiveArray = new float[16];
		colorArray = new float[] {1, 1, 1, 1};
		
		FloatUtil.makeIdentity(perspectiveArray);
		
		transformBuf = Buffers.newDirectFloatBuffer(16);
		perspectiveBuf = Buffers.newDirectFloatBuffer(perspectiveArray);
		colorBuf = Buffers.newDirectFloatBuffer(colorArray);
	}
	
	/**
	 * Sets the stored transformation to the identity
	 */
	public void reset() {
		transform = Transform.identity();
	}
	
	/**
	 * Adds the specified transformation relative to the current transformation
	 * @param t The transformation to apply before the current one
	 */
	public void addTransformation(Transform t) {
		transform = transform.transform(t);
	}
	
	/**
	 * Stores the current transformation onto a stack for later retrieval
	 */
	public void pushTransformation() {
		transformStack.push(transform);
	}
	
	/**
	 * Sets the current transformation to the previously stored transformation
	 * on the stack
	 */
	public void popTransformation() {
		transform = transformStack.pop();
	}
	
	/**
	 * Sets the stored perspective matrix to the given matrix
	 * @param perspective the perspective matrix represented via a
	 * 16-value array in column-major order
	 */
	public void setPerspective(float[] perspective) {
		System.arraycopy(perspective, 0, perspectiveArray, 0, 16);
	}
	
	/**
	 * Sets the stored color to the given color
	 * @param color the color represented via a 4-value array (r, g, b, a)
	 */
	public void setColor(float[] color) {
		System.arraycopy(color, 0, colorArray, 0, 4);
	}
	
	/**
	 * Sets all the uniforms of the shader to the currently stored values. This
	 * should be called before drawing anything if anything was changed.
	 * @param gl
	 */
	public void update(GL3 gl) {
		transformBuf.put(new float[] {
				(float)transform.x.x, (float)transform.x.y, (float)transform.x.z, (float)transform.x.w,
				(float)transform.y.x, (float)transform.y.y, (float)transform.y.z, (float)transform.y.w,
				(float)transform.z.x, (float)transform.z.y, (float)transform.z.z, (float)transform.z.w,
				(float)transform.w.x, (float)transform.w.y, (float)transform.w.z, (float)transform.w.w});
		transformBuf.rewind();
		
		perspectiveBuf.put(perspectiveArray).rewind();
		
		colorBuf.put(colorArray).rewind();
		
		shaderState.uniform(gl, new GLUniformData("transform", 4, 4, transformBuf));
		shaderState.uniform(gl, new GLUniformData("perspective", 4, 4, perspectiveBuf));
		shaderState.uniform(gl, new GLUniformData("color", 4, colorBuf));
	}
}
