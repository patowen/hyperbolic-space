package net.patowen.hyperbolicspace.math;

public class Transform {
	public Vector31 x;
	public Vector31 y;
	public Vector31 z;
	public Vector31 w;
	
	public Transform(Vector31 x, Vector31 y, Vector31 z, Vector31 w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Transform(Transform t)
	{
		this(new Vector31(t.x), new Vector31(t.y), new Vector31(t.z), new Vector31(t.w));
	}
	
	public static Transform identity()
	{
		return new Transform(
			new Vector31(1, 0, 0, 0),
			new Vector31(0, 1, 0, 0),
			new Vector31(0, 0, 1, 0),
			new Vector31(0, 0, 0, 1));
	}
	
	public static Transform translation(Vector31 v)
	{
		double u = 1/(v.w+1);
		return new Transform(
			new Vector31(v.x*v.x*u+1, v.y*v.x*u, v.z*v.x*u, v.x),
			new Vector31(v.x*v.y*u, v.y*v.y*u+1, v.z*v.y*u, v.y),
			new Vector31(v.x*v.z*u, v.y*v.z*u, v.z*v.z*u+1, v.z),
			new Vector31(v.x, v.y, v.z, v.w));
	}
	
	public static Transform rotation(Vector3 v, double theta)
	{
		double xx = v.x, yy = v.y, zz = v.z;
		double c = Math.cos(theta), s = Math.sin(theta);
		
		return new Transform(
			new Vector31(xx*xx+(1-xx*xx)*c, xx*yy-xx*yy*c+zz*s, xx*zz-xx*zz*c-yy*s, 0),
			new Vector31(xx*yy-xx*yy*c-zz*s, yy*yy+(1-yy*yy)*c, yy*zz-yy*zz*c+xx*s, 0),
			new Vector31(xx*zz-xx*zz*c+yy*s, yy*zz-yy*zz*c-xx*s, zz*zz+(1-zz*zz)*c, 0),
			new Vector31(0, 0, 0, 1));
	}
	
	public static Transform fromOrientation(Orientation o)
	{
		return new Transform(
			new Vector31(o.x.x, o.x.y, o.x.z, 0),
			new Vector31(o.y.x, o.y.y, o.y.z, 0),
			new Vector31(o.z.x, o.z.y, o.z.z, 0),
			new Vector31(0, 0, 0, 1));
	}
	
	public Transform inverse()
	{
		return new Transform(
			new Vector31(x.x, y.x, z.x, -w.x),
			new Vector31(x.y, y.y, z.y, -w.y),
			new Vector31(x.z, y.z, z.z, -w.z),
			new Vector31(-x.w, -y.w, -z.w, w.w));
	}
	
	public Transform normalize()
	{
		// Valid vectors are assumed. If this is not the case, NaN's will propagate, and
		// very bad stuff will happen, such as hidden, hard-to-debug errors.
		x = x.times(1.0/Math.sqrt(-x.squared())); // Normalize x
		y = y.plusMultiple(x, y.dot(x)); // Separate y from x
		z = z.plusMultiple(x, z.dot(x)); // Separate z from x
		w = w.plusMultiple(x, w.dot(x)); // Separate w from x
		
		y = y.times(1.0/Math.sqrt(-y.squared())); // Normalize y
		z = z.plusMultiple(y, z.dot(y)); // Separate z from y
		w = w.plusMultiple(y, w.dot(y)); // Separate w from y
		
		z = z.times(1.0/Math.sqrt(-z.squared())); // Normalize z
		w = w.plusMultiple(z, w.dot(z)); // Separate w from z
		
		w = w.times(1.0/Math.sqrt(w.squared())); // Normalize w (with reversed sign)
		
		return this;
	}
	
	public Vector31 transform(Vector31 v)
	{
		return new Vector31(
			x.x*v.x + y.x*v.y + z.x*v.z + w.x*v.w,
			x.y*v.x + y.y*v.y + z.y*v.z + w.y*v.w,
			x.z*v.x + y.z*v.y + z.z*v.z + w.z*v.w,
			x.w*v.x + y.w*v.y + z.w*v.z + w.w*v.w);
	}
	
	public Transform transform(Transform t)
	{
		return new Transform(
			transform(t.x),
			transform(t.y),
			transform(t.z),
			transform(t.w));
	}
	
	public Transform transformedBy(Transform t)
	{
		return new Transform(
			t.transform(x),
			t.transform(y),
			t.transform(z),
			t.transform(w));
	}
}
