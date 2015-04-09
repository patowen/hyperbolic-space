package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;

/**
 * The {@code Vertex} class represents a vertex in hyperbolic space represented via the
 * Poincare ball model.
 * @author Patrick Owen
 */
public class Vertex
{
	private Vector3 pos;
	private Vector3 norm;
	
	/**
	 * Constructs a {@code Vertex} object in the Poincare ball with no defined normal.
	 * @param x the x-position
	 * @param y the y-position
	 * @param z the z-position
	 */
	public Vertex(double x, double y, double z)
	{
		this(new Vector3(x, y, z), new Vector3());
	}
	
	/**
	 * Constructs a {@code Vertex} object with the given coordinates for its position and normal.
	 * @param x the x-coordinate of the position vector
	 * @param y the y-coordinate of the position vector
	 * @param z the z-coordinate of the position vector
	 * @param nx the x-coordinate of the normal vector
	 * @param ny the y-coordinate of the normal vector
	 * @param nz the z-coordinate of the normal vector
	 */
	public Vertex(double x, double y, double z, double nx, double ny, double nz)
	{
		this(new Vector3(x, y, z), new Vector3(nx, ny, nz));
	}
	
	/**
	 * Constructs a {@code Vertex} object with the given vector for its position and no defined normal.
	 * @param pos the position vector
	 * @param norm the normal vector
	 */
	public Vertex(Vector3 pos)
	{
		this(pos, new Vector3());
	}
	
	/**
	 * Constructs a {@code Vertex} object with the given vectors for its position and normal.
	 * @param pos the position vector
	 * @param norm the normal vector
	 */
	public Vertex(Vector3 pos, Vector3 norm)
	{
		this.pos = pos;
		this.norm = norm;
	}
	
	/**
	 * Places the vertex's x, y, and z coordinates into the given buffers.
	 * @param vertexBuffer the FloatBuffer for the position vector
	 * @param normalBuffer the FloatBuffer for the normal vector
	 */
	public void use(FloatBuffer vertexBuffer, FloatBuffer normalBuffer)
	{
		vertexBuffer.put((float)pos.x);
		vertexBuffer.put((float)pos.y);
		vertexBuffer.put((float)pos.z);
		
		normalBuffer.put((float)norm.x);
		normalBuffer.put((float)norm.y);
		normalBuffer.put((float)norm.z);
	}
}
