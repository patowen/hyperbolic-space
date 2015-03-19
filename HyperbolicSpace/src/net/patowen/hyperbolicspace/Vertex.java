package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;

/**
 * The {@code Vertex} class represents a vertex in hyperbolic space represented via the
 * Poincaré ball model.
 * @author Patrick Owen
 */
public class Vertex
{
	private Vector3 pos;
	private Vector3 vt;
	
	private Vector3 norm;
	private Vector3 nt;
	
	/**
	 * Constructs a {@code Vertex} object in the Poincaré ball with no defined normal.
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
		this.vt = new Vector3(pos);
		
		this.norm = norm;
		this.nt = new Vector3(norm);
	}
	
	/**
	 * Stores the state of the vertex after a translation in the Poincaré ball model that directly moves the
	 * origin to the initial location of {@code v}.
	 * @param v a vector of magnitude less than 1
	 */
	public void translate(Vector3 v)
	{
		double denom = (v.dot(v))*(pos.dot(pos)) + 2*(v.dot(pos)) + 1;
		double vFactor = 1 + pos.dot(pos) + 2*(v.dot(pos));
		double vPosFactor = 1 - v.dot(v);
		
		double denomN = 2*(v.dot(v)*pos.dot(norm) + v.dot(norm));
		double vFactorN = 2*(pos.dot(norm) + v.dot(norm));
		
		nt = (norm.times(vPosFactor).plus(v.times(vFactorN)).times(denom))
				.minus(pos.times(vPosFactor).plus(v.times(vFactor)).times(denomN));
		nt.normalize();
		vt = pos.hyperTranslate(v);
	}
	
	/**
	 * Stores the state of the vertex after the given transformation
	 * @param v a vector of magnitude less than 1
	 */
	public void transform(Transformation t)
	{
		vt = t.transform(pos);
	}
	
	/**
	 * Places the vertex's x, y, and z coordinates into the given buffers.
	 * @param vertexBuffer the FloatBuffer for the position vector
	 * @param normalBuffer the FloatBuffer for the normal vector
	 */
	public void use(FloatBuffer vertexBuffer, FloatBuffer normalBuffer)
	{
		vertexBuffer.put((float)vt.x);
		vertexBuffer.put((float)vt.y);
		vertexBuffer.put((float)vt.z);
		
		normalBuffer.put((float)nt.x);
		normalBuffer.put((float)nt.y);
		normalBuffer.put((float)nt.z);
	}
}
