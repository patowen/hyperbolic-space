package net.patowen.hyperbolicspace.rendering;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GLUniformData;

import net.patowen.hyperbolicspace.math.Vector31;

public class UniformVector31List implements ShaderUniformData {
	public Vector31[] vectors;
	
	public UniformVector31List(int size) {
		vectors = new Vector31[size];
		
		for (int i=0; i<size; i++) {
			vectors[i] = new Vector31();
		}
	}
	
	public Buffer createBuffer() {
		return Buffers.newDirectFloatBuffer(4*vectors.length);
	}
	
	public void addToBuffer(Buffer buf) {
		for (Vector31 vector : vectors) {
			((FloatBuffer)buf).put(new float[] {
					(float)vector.x, (float)vector.y, (float)vector.z, (float)vector.w });
		}
	}
	
	public GLUniformData getGLUniformData(String name, Buffer buffer) {
		return new GLUniformData(name, 4, (FloatBuffer)buffer);
	}
}
