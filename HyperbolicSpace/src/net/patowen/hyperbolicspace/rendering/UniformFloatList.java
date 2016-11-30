package net.patowen.hyperbolicspace.rendering;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GLUniformData;

public class UniformFloatList implements ShaderUniformData {
	private int rows, cols;
	public float[] list;
	
	public UniformFloatList(int rows, int cols, int count) {
		this.rows = rows;
		this.cols = cols;
		list = new float[rows*cols*count];
	}
	
	public UniformFloatList(int rows, int cols) {
		this(rows, cols, 1);
	}
	
	public UniformFloatList(int rows) {
		this(rows, 1, 1);
	}
	
	public Buffer createBuffer() {
		return Buffers.newDirectFloatBuffer(list.length);
	}
	
	public void addToBuffer(Buffer buf) {
		((FloatBuffer)buf).put(list);
	}
	
	public GLUniformData getGLUniformData(String name, Buffer buffer) {
		if (cols == 1)
			return new GLUniformData(name, rows, (FloatBuffer)buffer);
		else
			return new GLUniformData(name, rows, cols, (FloatBuffer)buffer);
	}
}
