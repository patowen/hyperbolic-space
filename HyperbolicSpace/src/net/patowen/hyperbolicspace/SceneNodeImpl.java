package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.glsl.ShaderState;
import com.jogamp.opengl.util.texture.Texture;

public class SceneNodeImpl
{
	private boolean movable;
	
	private Vector3 pos;
	private Orientation o;
	
	private Texture texture;
	
	private ArrayList<Vertex> vertices;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texCoordBuffer;
	
	int vertexBufferPos;
	int normalBufferPos;
	int texCoordBufferPos;
	
	private IntBuffer elementBuffer;
	int elementBufferPos;
	
	private ShaderState shaderState;
	
	public SceneNodeImpl()
	{
		movable = false;
		pos = new Vector3();
		o = new Orientation();
	}
	
	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}
	
	public void setVertices(ArrayList<Vertex> vertices)
	{
		this.vertices = vertices;
		
		vertexBuffer = Buffers.newDirectFloatBuffer(3*vertices.size());
		normalBuffer = Buffers.newDirectFloatBuffer(3*vertices.size());
		texCoordBuffer = Buffers.newDirectFloatBuffer(2*vertices.size());
	}
	
	public void reposition(Vector3 v)
	{
		for (int i=0; i<vertices.size(); i++)
			vertices.get(i).translate(v);
		
		for (int i=0; i<vertices.size(); i++)
		{
			vertices.get(i).use(vertexBuffer, normalBuffer);
		}
		vertexBuffer.rewind();
		normalBuffer.rewind();
	}
	
	public void setTexCoordBuffer(FloatBuffer texCoordBuffer)
	{
		this.texCoordBuffer = texCoordBuffer;
	}
	
	public void setElementBuffer(IntBuffer elementBuffer)
	{
		this.elementBuffer = elementBuffer;
	}
	
	public void renderInit(GL3 gl, MatrixHandler mh)
	{
		IntBuffer tempBuffer = Buffers.newDirectIntBuffer(4);
		gl.glGenBuffers(4, tempBuffer);
		vertexBufferPos = tempBuffer.get(0);
		normalBufferPos = tempBuffer.get(1);
		texCoordBufferPos = tempBuffer.get(2);
		elementBufferPos = tempBuffer.get(3);
		
		shaderState = mh.getShaderState();
	}
	
	public void render(GL3 gl)
	{
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertexBufferPos);
		gl.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 0, 0);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertexBuffer.capacity()*4, vertexBuffer, GL3.GL_DYNAMIC_DRAW);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, normalBufferPos);
		gl.glVertexAttribPointer(1, 3, GL3.GL_FLOAT, false, 0, 0);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, normalBuffer.capacity()*4, normalBuffer, GL3.GL_DYNAMIC_DRAW);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, texCoordBufferPos);
		gl.glVertexAttribPointer(2, 2, GL3.GL_FLOAT, false, 0, 0);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, texCoordBuffer.capacity()*4, texCoordBuffer, GL3.GL_DYNAMIC_DRAW);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		
		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBufferPos);
		gl.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBuffer.capacity()*4, elementBuffer, GL3.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		gl.glEnableVertexAttribArray(0);
		gl.glEnableVertexAttribArray(1);
		gl.glEnableVertexAttribArray(2);
		texture.enable(gl);
		texture.bind(gl);
		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBufferPos);
		gl.glDrawElements(GL3.GL_TRIANGLES, elementBuffer.capacity(), GL3.GL_UNSIGNED_INT, 0);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
		texture.disable(gl);
		gl.glDisableVertexAttribArray(2);
		gl.glDisableVertexAttribArray(1);
		gl.glDisableVertexAttribArray(0);
	}
}
