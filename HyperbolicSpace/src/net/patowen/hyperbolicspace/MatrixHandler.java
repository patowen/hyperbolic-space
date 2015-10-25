package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;
import java.util.Stack;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLUniformData;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.glsl.ShaderState;

/**
 * {@code MatrixHandler} has two purposes. It holds a Transformation stack for the viewpoint and
 * for hierarchical models, and it interacts with the shader to set the uniforms to the appropriate
 * values.
 * @author Patrick Owen
 */
public class MatrixHandler
{
	private ShaderState shaderState;
	
	private Transformation transformation;
	private Stack<Transformation> transformationStack;
	
	private FloatBuffer perspectiveBuf;
	private float[] perspectiveArray;
	
	private FloatBuffer colorBuf;
	private float[] colorArray;
	
	private FloatBuffer orientBuf;
	private FloatBuffer translateBuf;
	
	/**
	 * Initializes all values to their defaults
	 * @param shaderState the {@code ShaderState} representing the shader whose
	 * uniforms are set
	 */
	public MatrixHandler(ShaderState shaderState)
	{
		this.shaderState = shaderState;
		
		transformation = new Transformation();
		transformationStack = new Stack<Transformation>();
		
		perspectiveArray = new float[16];
		colorArray = new float[] {1, 1, 1, 1};
		
		FloatUtil.makeIdentity(perspectiveArray);
		
		orientBuf = Buffers.newDirectFloatBuffer(9);
		translateBuf = Buffers.newDirectFloatBuffer(3);
		perspectiveBuf = Buffers.newDirectFloatBuffer(perspectiveArray);
		colorBuf = Buffers.newDirectFloatBuffer(colorArray);
	}
	
	/**
	 * Sets the stored transformation to the identity
	 */
	public void reset()
	{
		transformation = new Transformation();
	}
	
	/**
	 * Adds the specified transformation relative to the current transformation
	 * @param t The transformation to apply before the current one
	 */
	public void addTransformation(Transformation t)
	{
		transformation = transformation.composeBefore(t);
	}
	
	/**
	 * Stores the current transformation onto a stack for later retrieval
	 */
	public void pushTransformation()
	{
		transformationStack.push(transformation);
	}
	
	/**
	 * Sets the current transformation to the previously stored transformation
	 * on the stack
	 */
	public void popTransformation()
	{
		transformation = transformationStack.pop();
	}
	
	/**
	 * Sets the stored perspective matrix to the given matrix
	 * @param perspective the perspective matrix represented via a
	 * 16-value array in column-major order
	 */
	public void setPerspective(float[] perspective)
	{
		System.arraycopy(perspective, 0, perspectiveArray, 0, 16);
	}
	
	/**
	 * Sets the stored color to the given color
	 * @param color the color represented via a 4-value array (r, g, b, a)
	 */
	public void setColor(float[] color)
	{
		System.arraycopy(color, 0, colorArray, 0, 4);
	}
	
	/**
	 * Sets all the uniforms of the shader to the currently stored values. This
	 * should be called before drawing anything if anything was changed.
	 * @param gl
	 */
	public void update(GL3 gl)
	{
		Orientation o = transformation.getRotation();
		orientBuf.put(new float[] {
				(float)o.x.x, (float)o.x.y, (float)o.x.z,
				(float)o.y.x, (float)o.y.y, (float)o.y.z,
				(float)o.z.x, (float)o.z.y, (float)o.z.z});
		orientBuf.rewind();
		
		Vector3 v = transformation.getTranslation();
		translateBuf.put(new float[] {(float)v.x, (float)v.y, (float)v.z});
		translateBuf.rewind();
		
		perspectiveBuf.put(perspectiveArray).rewind();
		
		colorBuf.put(colorArray).rewind();
		
		shaderState.uniform(gl, new GLUniformData("transform", 3, 3, orientBuf));
		shaderState.uniform(gl, new GLUniformData("perspective", 4, 4, perspectiveBuf));
		shaderState.uniform(gl, new GLUniformData("translate", 3, translateBuf));
		shaderState.uniform(gl, new GLUniformData("color", 4, colorBuf));
	}
}
