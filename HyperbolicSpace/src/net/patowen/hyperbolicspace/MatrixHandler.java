package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;

import javax.media.opengl.GL3;
import javax.media.opengl.GLUniformData;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.glsl.ShaderState;

public class MatrixHandler
{
	private ShaderState shaderState;
	
	private FloatBuffer perspectiveBuf;
	private float[] perspectiveArray;
	
	private FloatBuffer transformBuf;
	private float[] transformArray;
	
	private FloatBuffer totalBuf;
	private float[] totalArray;
	
	public MatrixHandler(ShaderState shaderState)
	{
		this.shaderState = shaderState;
		
		transformArray = new float[16];
		perspectiveArray = new float[16];
		totalArray = new float[16];
		
		FloatUtil.makeIdentity(transformArray);
		FloatUtil.makeIdentity(perspectiveArray);
		FloatUtil.makeIdentity(totalArray);
		
//		totalBuf = Buffers.newDirectFloatBuffer(totalArray);
		transformBuf = Buffers.newDirectFloatBuffer(transformArray);
		perspectiveBuf = Buffers.newDirectFloatBuffer(perspectiveArray);
	}
	
	public float[] transformArray()
	{
		return transformArray;
	}
	
	public void add(float[] transform)
	{
		FloatUtil.multMatrix(transformArray, transform);
	}
	
	public void reset()
	{
		FloatUtil.makeIdentity(transformArray);
	}
	
	public void setPerspective(float[] perspective)
	{
		perspectiveArray = perspective;
	}
	
	public void update(GL3 gl)
	{
		transformBuf.put(transformArray).rewind();
		perspectiveBuf.put(perspectiveArray).rewind();
		shaderState.uniform(gl, new GLUniformData("transform", 4, 4, transformBuf));
		shaderState.uniform(gl, new GLUniformData("perspective", 4, 4, perspectiveBuf));
	}
}
