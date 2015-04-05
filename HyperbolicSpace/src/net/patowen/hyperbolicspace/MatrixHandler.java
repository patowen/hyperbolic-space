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
	
	private Transformation transformation;
	
	private FloatBuffer perspectiveBuf;
	private float[] perspectiveArray;
	
	private FloatBuffer orientBuf;
	private FloatBuffer translateBuf;
	
	public MatrixHandler(ShaderState shaderState)
	{
		this.shaderState = shaderState;
		
		transformation = new Transformation();
		perspectiveArray = new float[16];
		
		FloatUtil.makeIdentity(perspectiveArray);
		
		orientBuf = Buffers.newDirectFloatBuffer(9);
		translateBuf = Buffers.newDirectFloatBuffer(3);
		perspectiveBuf = Buffers.newDirectFloatBuffer(perspectiveArray);
	}
	
	public ShaderState getShaderState()
	{
		return shaderState;
	}
	
	public void reset()
	{
		transformation = new Transformation();
	}
	
	public void setPerspective(float[] perspective)
	{
		perspectiveArray = perspective;
	}
	
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
		shaderState.uniform(gl, new GLUniformData("transform", 3, 3, orientBuf));
		shaderState.uniform(gl, new GLUniformData("perspective", 4, 4, perspectiveBuf));
		shaderState.uniform(gl, new GLUniformData("translate", 3, translateBuf));
	}
}
