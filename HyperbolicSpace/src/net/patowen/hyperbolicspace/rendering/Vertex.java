package net.patowen.hyperbolicspace.rendering;

import java.nio.FloatBuffer;

import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector31;

/**
 * The {@code Vertex} class represents a vertex in hyperbolic space represented via the
 * Poincare ball model.
 * @author Patrick Owen
 */
public class Vertex {
	private Vector31 pos;
	private Vector31 norm;
	private Vector2 texCoord;
	
	public Vertex(Vector31 pos, Vector31 norm, Vector2 texCoord) {
		this.pos = pos;
		this.norm = norm;
		this.texCoord = texCoord;
	}
	
	/**
	 * Places the vertex's x, y, and z coordinates into the given buffers.
	 * @param vertexBuffer the FloatBuffer for the position vector
	 * @param normalBuffer the FloatBuffer for the normal vector
	 */
	public void use(FloatBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer texCoordBuffer) {
		vertexBuffer.put((float)pos.x);
		vertexBuffer.put((float)pos.y);
		vertexBuffer.put((float)pos.z);
		vertexBuffer.put((float)pos.w);
		
		normalBuffer.put((float)norm.x);
		normalBuffer.put((float)norm.y);
		normalBuffer.put((float)norm.z);
		normalBuffer.put((float)norm.w);
		
		texCoordBuffer.put((float)texCoord.x);
		texCoordBuffer.put((float)texCoord.y);
	}
}
