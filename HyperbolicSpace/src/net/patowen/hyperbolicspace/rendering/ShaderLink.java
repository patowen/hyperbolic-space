package net.patowen.hyperbolicspace.rendering;

import java.nio.Buffer;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.glsl.ShaderState;

public class ShaderLink<E extends ShaderUniformData> {
	public E data;
	
	private final String name;
	private final Buffer buffer;
	
	public ShaderLink(String name, E data) {
		this.name = name;
		this.data = data;
		
		buffer = data.createBuffer();
	}
	
	public void update(ShaderState shaderState, GL3 gl) {
		data.addToBuffer(buffer);
		buffer.rewind();
		
		shaderState.uniform(gl, data.getGLUniformData(name, buffer));
	}
}
