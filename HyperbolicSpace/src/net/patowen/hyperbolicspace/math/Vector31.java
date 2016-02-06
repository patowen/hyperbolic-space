package net.patowen.hyperbolicspace.math;

public class Vector31
{
	public double x;
	public double y;
	public double z;
	public double w;
	
	public Vector31()
	{
		x = 0; y = 0; z = 0; w = 1;
	}
	
	public Vector31(double x, double y, double z, double w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector31(Vector31 v)
	{
		this(v.x, v.y, v.z, v.w);
	}
	
	public static Vector31 makePoincare(Vector3 v)
	{
		double dist = 1.0 - v.x*v.x - v.y*v.y - v.z*v.z;
		if (dist <= 0)
		{
			throw new IllegalArgumentException("Magnitude must be less than 1.");
		}
		double factor = 2/dist;
		return new Vector31(v.x*factor, v.y*factor, v.z*factor, factor-1);
	}
	
	public void reset()
	{
		x = 0; y = 0; z = 0; w = 1;
	}
	
	public void normalizeAsPoint()
	{
		double dist = w*w - x*x - y*y - z*z;
		if (dist > 0)
		{
			double factor = 1.0/Math.sqrt(dist);
			x *= factor;
			y *= factor;
			z *= factor;
			w *= factor;
		}
	}
	
	public void rotate(Vector3 v, double theta)
	{
		double xx = v.x, yy = v.y, zz = v.z;
		double c = Math.cos(theta), s = Math.sin(theta);
		
		double xNew = x*(xx*xx+(1-xx*xx)*c) + y*(xx*yy-xx*yy*c-zz*s) + z*(xx*zz-xx*zz*c+yy*s);
		double yNew = x*(xx*yy-xx*yy*c+zz*s) + y*(yy*yy+(1-yy*yy)*c) + z*(yy*zz-yy*zz*c-xx*s);
		double zNew = x*(xx*zz-xx*zz*c-yy*s) + y*(yy*zz-yy*zz*c+xx*s) + z*(zz*zz+(1-zz*zz)*c);
		
		x = xNew; y = yNew; z = zNew;
	}
	
	public Vector31 times(double c)
	{
		return new Vector31(x*c, y*c, z*c, w*c);
	}
	
	public Vector31 plusMultiple(Vector31 v, double c)
	{
		return new Vector31(x+v.x*c, y+v.y*c, z+v.z*c, w+v.w*c);
	}
	
	public double squared()
	{
		return x*x + y*y + z*z - w*w;
	}
	
	public double dot(Vector31 v)
	{
		return x*v.x + y*v.y + z*v.z - w*v.w;
	}
	
	public Vector3 getPoincare()
	{
		return new Vector3(x/(w+1), y/(w+1), z/(w+1));
	}
}
