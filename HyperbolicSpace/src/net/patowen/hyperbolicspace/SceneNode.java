package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;

public class SceneNode
{
	private boolean movable;
	
	private Vector3 pos;
	private Orientation o;
	
	private ArrayList<Vertex> v;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texCoordBuffer;
	
	int vertexBufferPos;
	int normalBufferPos;
	int texCoordBufferPos;
	
	private IntBuffer elementBuffer;
	int elementBufferPos;
	
	public SceneNode()
	{
		movable = false;
		pos = new Vector3();
		o = new Orientation();
		
		vertexBuffer = Buffers.newDirectFloatBuffer(new float[] {0.5f, 0, 0, 0.5f, 0.5f, 0, 0.5f, 0, 0.5f});
		elementBuffer = Buffers.newDirectIntBuffer(new int[] {0, 1, 0, 2, 1, 2});
	}
	
	public void renderInit(GL3 gl)
	{
		IntBuffer tempBuffer = Buffers.newDirectIntBuffer(2);
		gl.glGenBuffers(2, tempBuffer);
		vertexBufferPos = tempBuffer.get(0);
		elementBufferPos = tempBuffer.get(1);
	}
	
	public void render(GL3 gl)
	{
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertexBufferPos);
		gl.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 0, 0);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertexBuffer.capacity()*4, vertexBuffer, GL3.GL_DYNAMIC_DRAW);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		
		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBufferPos);
		gl.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBuffer.capacity()*4, elementBuffer, GL3.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		gl.glEnableVertexAttribArray(0);
		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBufferPos);
		gl.glDrawElements(GL3.GL_LINES, elementBuffer.capacity(), GL3.GL_UNSIGNED_INT, 0);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glDisableVertexAttribArray(0);
	}
}
