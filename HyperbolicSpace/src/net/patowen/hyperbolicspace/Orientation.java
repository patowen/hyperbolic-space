package net.patowen.hyperbolicspace;

public class Orientation
{
	public Vector3 x, y, z; //to, right, up
	
	public Orientation(Vector3 x, Vector3 y, Vector3 z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Orientation(Orientation o)
	{
		this(new Vector3(o.x), new Vector3(o.y), new Vector3(o.z));
	}
	
	public Orientation()
	{
		this(new Vector3(1, 0, 0), new Vector3(0, 1, 0), new Vector3(0, 0, 1));
	}
	
	public void normalize()
	{
		x.normalize();
		y = x.cross(z);
		y.normalize();
		z = y.cross(x);
	}
	
	public void rotate(Vector3 v, double theta)
	{
		x.rotate(v, theta);
		y.rotate(v, theta);
		z.rotate(v, theta);
	}
	
	public Vector3 apply(Vector3 v)
	{
		return new Vector3(x.dot(v), y.dot(v), z.dot(v));
	}
}
