package net.patowen.hyperbolicspace.rendering;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GLUniformData;

import net.patowen.hyperbolicspace.math.Vector31;

public class UniformVector31 implements ShaderUniformData {
	public Vector31 vector;
	
	public UniformVector31(Vector31 initialVector) {
		vector = initialVector;
	}
	
	public Buffer createBuffer() {
		return Buffers.newDirectFloatBuffer(4);
	}
	
	public void addToBuffer(Buffer buf) {
		((FloatBuffer)buf).put(new float[] {
				(float)vector.x, (float)vector.y, (float)vector.z, (float)vector.w });
	}
	
	public GLUniformData getGLUniformData(String name, Buffer buffer) {
		return new GLUniformData(name, 4, (FloatBuffer)buffer);
	}
}
