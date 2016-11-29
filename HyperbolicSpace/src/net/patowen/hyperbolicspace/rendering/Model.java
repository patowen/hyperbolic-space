package net.patowen.hyperbolicspace.rendering;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

public class Model {
	private ArrayList<Vertex> vertices;
	private ArrayList<Integer> elements;
	
	private int vertexArrayBufferPos;
	
	private int vertexBufferPos;
	private int normalBufferPos;
	private int texCoordBufferPos;
	private int elementBufferPos;
	
	public Model() {
		vertices = new ArrayList<>();
		elements = new ArrayList<>();
	}
	
	public void init(GL3 gl) {
		FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(4*vertices.size());
		FloatBuffer normalBuffer = Buffers.newDirectFloatBuffer(4*vertices.size());
		FloatBuffer texCoordBuffer = Buffers.newDirectFloatBuffer(2*vertices.size());
		IntBuffer elementBuffer = Buffers.newDirectIntBuffer(elements.size());
		
		for (Vertex vertex : vertices) {
			vertex.use(vertexBuffer, normalBuffer, texCoordBuffer);
		}
		vertexBuffer.rewind();
		normalBuffer.rewind();
		texCoordBuffer.rewind();
		
		for (int element : elements) {
			elementBuffer.put(element);
		}
		elementBuffer.rewind();
		
		IntBuffer tempBuffer2 = Buffers.newDirectIntBuffer(1);
		gl.glGenVertexArrays(1, tempBuffer2);
		vertexArrayBufferPos = tempBuffer2.get(0);
		
		IntBuffer tempBuffer = Buffers.newDirectIntBuffer(4);
		gl.glGenBuffers(4, tempBuffer);
		vertexBufferPos = tempBuffer.get(0);
		normalBufferPos = tempBuffer.get(1);
		texCoordBufferPos = tempBuffer.get(2);
		elementBufferPos = tempBuffer.get(3);
		
		gl.glBindVertexArray(vertexArrayBufferPos);
		gl.glEnableVertexAttribArray(0);
		gl.glEnableVertexAttribArray(1);
		gl.glEnableVertexAttribArray(2);
		
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertexBufferPos);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertexBuffer.capacity()*4, vertexBuffer, GL3.GL_DYNAMIC_DRAW);
		gl.glVertexAttribPointer(0, 4, GL3.GL_FLOAT, false, 0, 0);
		
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, normalBufferPos);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, normalBuffer.capacity()*4, normalBuffer, GL3.GL_DYNAMIC_DRAW);
		gl.glVertexAttribPointer(1, 4, GL3.GL_FLOAT, false, 0, 0);
		
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, texCoordBufferPos);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, texCoordBuffer.capacity()*4, texCoordBuffer, GL3.GL_DYNAMIC_DRAW);
		gl.glVertexAttribPointer(2, 2, GL3.GL_FLOAT, false, 0, 0);
		
		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBufferPos);
		gl.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBuffer.capacity()*4, elementBuffer, GL3.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		gl.glBindVertexArray(0);
	}
	
	public int addVertex(Vertex vertex) {
		int nextVertex = vertices.size();
		vertices.add(vertex);
		return nextVertex;
	}
	
	public void addTriangle(int v1, int v2, int v3) {
		elements.add(v1);
		elements.add(v2);
		elements.add(v3);
	}
	
	public void addQuad(int v1, int v2, int v3, int v4) {
		addTriangle(v1, v2, v3);
		addTriangle(v1, v3, v4);
	}
	
	public void render(GL3 gl) {
		gl.glBindVertexArray(vertexArrayBufferPos);
		
		gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBufferPos);
		gl.glDrawElements(GL3.GL_TRIANGLES, elements.size(), GL3.GL_UNSIGNED_INT, 0);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		gl.glBindVertexArray(0);
	}
}
