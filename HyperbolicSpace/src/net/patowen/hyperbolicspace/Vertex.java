package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;

import javax.media.opengl.GL2;


public class Vertex
{
	private Vector3 pos;
	private Vector3 vt;
	
	private Vector3 norm;
	private Vector3 nt;
	
	private FloatBuffer vertBuffer;
	private int offset;
	
	/**
	 * Creates a vertex in the Poincare ball with the given coordinates in the model.
	 * @param x x-position
	 * @param y y-position
	 * @param z z-position
	 */
	public Vertex(double x, double y, double z)
	{
		this(new Vector3(x, y, z), new Vector3());
	}
	
	/**
	 * Creates a vertex in the Poincare ball with the given coordinates in the model.
	 * @param x x-position
	 * @param y y-position
	 * @param z z-position
	 * @param nx x-coordinate of normal vector
	 * @param ny y-coordinate of normal vector
	 * @param nz z-coordinate of normal vector
	 */
	public Vertex(double x, double y, double z, double nx, double ny, double nz)
	{
		this(new Vector3(x, y, z), new Vector3(nx, ny, nz));
	}
	
	/**
	 * Creates a vertex in the Poincare ball with the given coordinates in the model.
	 * @param pos
	 */
	public Vertex(Vector3 pos, Vector3 norm)
	{
		this.pos = pos;
		this.vt = new Vector3(pos);
		
		this.norm = norm;
		this.nt = new Vector3(norm);
	}
	
	/**
	 * Translates the vertex as simply as possible so that the given coordinates are translated
	 * to the origin.
	 * @param x
	 * @param y
	 * @param z
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
		vt = (pos.times(vPosFactor).plus(v.times(vFactor))).times(1/denom);
	}
	
	/**
	 * Calls glVertex with this vertex
	 * @param gl
	 */
	public void use(GL2 gl)
	{
		if (vertBuffer != null)
		{
			vertBuffer.put(offset, (float)vt.x);
			vertBuffer.put(offset+1, (float)vt.y);
			vertBuffer.put(offset+2, (float)vt.z);
		}
		else
		{
			gl.glVertex3d(vt.x, vt.y, vt.z);
		}
	}
	
	/**
	 * Inverts the point about the given circle with given coordinates for the center, and a given radius.
	 * @param c
	 * @param r radius
	 */
	public void invert(Vector3 c, double r)
	{
		vt.subtract(c);
		double s = vt.dot(vt);
		vt.multiply(r*r/s);
		vt.add(c);
	}
	
	/**
	 * Returns the position of the vertex after its transformation.
	 */
	public Vector3 getVt()
	{
		return vt;
	}
	
	/**
	 * Returns the normal of the vertex after its transformation.
	 */
	public Vector3 getNt()
	{
		return nt;
	}
}
