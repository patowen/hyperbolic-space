package net.patowen.hyperbolicspace.rendering;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.math.Transform;

/**
 * Holds the logic common to all {@code SceneNodeType}s
 * @author Patrick Owen
 */
public class SceneNodeImpl {
	private Controller c;
	
	private Transform transform;
	
	private Texture texture;
	
	private ArrayList<Vertex> vertices;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texCoordBuffer;
	
	private int vertexBufferPos;
	private int normalBufferPos;
	private int texCoordBufferPos;
	
	private IntBuffer elementBuffer;
	private int elementBufferPos;
	
	private int vertexArrayBufferPos;
	
	private float[] color;
	
	private Model model;
	
	/**
	 * Initializes the scene node type with defaults
	 * @param c
	 */
	public SceneNodeImpl(Controller c) {
		this.c = c;
		transform = Transform.identity();
		color = new float[] {1, 1, 1, 1};
		model = null;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	/**
	 * Sets the texture with which to render the scene node
	 * @param texture a reference to the texture with which to render the scene node
	 */
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	/**
	 * Sets the color with which to render the scene node
	 * @param color a 4-element array representing the color with which to render the scene node
	 */
	public void setColor(float[] color) {
		System.arraycopy(color, 0, this.color, 0, 4);
	}
	
	/**
	 * Sets the list of vertices used to render the scene node. Initializes
	 * buffers to the correct size based on the size of the given array
	 * @param vertices a list of vertices to render with
	 */
	public void setVertices(ArrayList<Vertex> vertices) {
		this.vertices = vertices;
		
		vertexBuffer = Buffers.newDirectFloatBuffer(3*vertices.size());
		normalBuffer = Buffers.newDirectFloatBuffer(3*vertices.size());
		texCoordBuffer = Buffers.newDirectFloatBuffer(2*vertices.size());
	}
	
	/**
	 * Changes the location to draw the mesh when {@code render} is called
	 * @param t the new transformation
	 */
	public void setTransform(Transform t) {
		this.transform = t;
	}
	
	/**
	 * Fills the vertex, normal, and texCoord buffers with the proper data so that the scene
	 * node can be rendered
	 */
	public void prepare() {
		if (model == null) {
			for (int i=0; i<vertices.size(); i++) {
				vertices.get(i).use(vertexBuffer, normalBuffer, texCoordBuffer);
			}
			vertexBuffer.rewind();
			normalBuffer.rewind();
			texCoordBuffer.rewind();
		}
	}
	
	/**
	 * Directly sets the element buffer to use for rendering
	 * @param texCoordBuffer a reference to the element buffer to use for rendering
	 */
	public void setElementBuffer(IntBuffer elementBuffer) {
		this.elementBuffer = elementBuffer;
	}
	
	/**
	 * Requests buffers from OpenGL to allow the vertex, normal, texCoord, and element buffers to
	 * be used when rendering. This must be called before {@code render}
	 * @param gl
	 */
	public void renderInit(GL3 gl) {
		if (model == null) {
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
			gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertexBuffer.capacity()*4, vertexBuffer, GL3.GL_STATIC_DRAW);
			gl.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 0, 0);
			
			gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, normalBufferPos);
			gl.glBufferData(GL3.GL_ARRAY_BUFFER, normalBuffer.capacity()*4, normalBuffer, GL3.GL_STATIC_DRAW);
			gl.glVertexAttribPointer(1, 3, GL3.GL_FLOAT, false, 0, 0);
			
			gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, texCoordBufferPos);
			gl.glBufferData(GL3.GL_ARRAY_BUFFER, texCoordBuffer.capacity()*4, texCoordBuffer, GL3.GL_STATIC_DRAW);
			gl.glVertexAttribPointer(2, 2, GL3.GL_FLOAT, false, 0, 0);
			
			gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBufferPos);
			gl.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBuffer.capacity()*4, elementBuffer, GL3.GL_STATIC_DRAW);
			gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
			
			gl.glBindVertexArray(0);
		} else {
			model.init(gl);
		}
	}
	
	/**
	 * Renders the mesh at the location previously specified
	 * @param gl
	 */
	public void render(GL3 gl) {
		ShaderUniformHandler mh = c.getMatrixHandler();
		
		if (model == null)
			gl.glBindVertexArray(vertexArrayBufferPos);
		
		mh.pushTransformation();
		mh.addTransformation(transform);
		mh.setMaterialAmbient(color);
		mh.setMaterialDiffuse(color);
		mh.update(gl);
		texture.bind(gl);
		
		if (model == null) {
			gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, elementBufferPos);
			gl.glDrawElements(GL3.GL_TRIANGLES, elementBuffer.capacity(), GL3.GL_UNSIGNED_INT, 0);
			gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
		} else {
			model.render(gl);
		}
		
		if (model == null)
			gl.glBindVertexArray(0);
		
		mh.popTransformation();
	}
}
