package net.patowen.hyperbolicspace.rendering;

import java.nio.Buffer;

import com.jogamp.opengl.GLUniformData;

public interface ShaderUniformData {
	public Buffer createBuffer();
	public void addToBuffer(Buffer buffer);
	public GLUniformData getGLUniformData(String name, Buffer buffer);
}
