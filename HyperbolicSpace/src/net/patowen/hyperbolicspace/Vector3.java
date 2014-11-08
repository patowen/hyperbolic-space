package net.patowen.hyperbolicspace;

public class Vector3
{
	public double x, y, z;
	
	public Vector3()
	{
		x = 0; y = 0; z = 0;
	}
	
	public Vector3(Vector3 v)
	{
		this(v.x, v.y, v.z);
	}
	
	public void reset()
	{
		x = 0; y = 0; z = 0;
	}
	
	public Vector3(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void normalize()
	{
		if (x*x + y*y + z*z > 0)
		{
			double size = Math.sqrt(x*x+y*y+z*z);
			x /= size;
			y /= size;
			z /= size;
		}
	}
	
	public double magnitude()
	{
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public Vector3 plus(Vector3 v)
	{
		return new Vector3(x+v.x, y+v.y, z+v.z);
	}
	
	public void add(Vector3 v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	public Vector3 plusMultiple(Vector3 v, double c)
	{
		return new Vector3(x+v.x*c, y+v.y*c, z+v.z*c);
	}
	
	public void addMultiple(Vector3 v, double c)
	{
		x += v.x*c;
		y += v.y*c;
		z += v.z*c;
	}
	
	public Vector3 minus(Vector3 v)
	{
		return new Vector3(x-v.x, y-v.y, z-v.z);
	}
	
	public void subtract(Vector3 v)
	{
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	public Vector3 times(double c)
	{
		return new Vector3(c*x, c*y, c*z);
	}
	
	public void multiply(double c)
	{
		x *= c;
		y *= c;
		z *= c;
	}
	
	public double dot(Vector3 v)
	{
		return (x*v.x + y*v.y + z*v.z);
	}
	
	public Vector3 cross(Vector3 v)
	{
		return new Vector3(y*v.z-z*v.y, z*v.x-x*v.z, x*v.y-y*v.x);
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
}
