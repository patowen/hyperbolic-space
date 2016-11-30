package net.patowen.hyperbolicspace.rendering;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GLUniformData;

import net.patowen.hyperbolicspace.math.Transform;

public class UniformTransform implements ShaderUniformData {
	public Transform transform;
	
	public UniformTransform(Transform initialTransform) {
		transform = initialTransform;
	}
	
	public Buffer createBuffer() {
		return Buffers.newDirectFloatBuffer(16);
	}
	
	public void addToBuffer(Buffer buf) {
		((FloatBuffer)buf).put(new float[] {
				(float)transform.x.x, (float)transform.x.y, (float)transform.x.z, (float)transform.x.w,
				(float)transform.y.x, (float)transform.y.y, (float)transform.y.z, (float)transform.y.w,
				(float)transform.z.x, (float)transform.z.y, (float)transform.z.z, (float)transform.z.w,
				(float)transform.w.x, (float)transform.w.y, (float)transform.w.z, (float)transform.w.w});
	}
	
	public GLUniformData getGLUniformData(String name, Buffer buffer) {
		return new GLUniformData(name, 4, 4, (FloatBuffer)buffer);
	}
}
