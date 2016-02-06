package net.patowen.lorentzsandbox;


public class Vector21
{
	public double x;
	public double y;
	public double w;
	
	public Vector21()
	{
		x = 0; y = 0; w = 1;
	}
	
	// Must be unit vector; creates vector on cone
	public Vector21(Vector2 v)
	{
		this(v.x, v.y, 1);
	}
	
	public Vector21(Vector21 v)
	{
		this(v.x, v.y, v.w);
	}
	
	public void reset()
	{
		x = 0; y = 0;
	}
	
	public Vector21(double x, double y, double w)
	{
		this.x = x;
		this.y = y;
		this.w = w;
	}
	
	// calling this method with zero vector is bad code.
	public void normalizeIdeal()
	{
		double dist = x*x+y*y;
		if (dist > 0)
		{
			double size = Math.sqrt(dist);
			x /= size;
			y /= size;
			w = 1;
		}
	}
	
	// calling this method while on cone is bad code.
	public void normalizeHyperboloid()
	{
		double dist = x*x+y*y-w*w;
		if (dist > 0)
		{
			double size = Math.sqrt(dist);
			x /= size;
			y /= size;
			w /= size;
		}
	}
	
	public void rotate(double theta)
	{
		double c = Math.cos(theta), s = Math.sin(theta);
		
		double xNew = x*c - y*s;
		double yNew = x*s + y*c;
		
		x = xNew; y = yNew;
	}
	
	public Vector21 translate(Vector21 v)
	{
		double u = 1/(v.w+1);
		double xNew = (v.x*v.x*u+1)*x + v.x*v.y*u*y + v.x*w;
		double yNew = v.x*v.y*u*x + (v.y*v.y*u+1)*y + v.y*w;
		double wNew = v.x*x + v.y*y + v.w*w;
		
		return new Vector21(xNew, yNew, wNew);
	}
}
