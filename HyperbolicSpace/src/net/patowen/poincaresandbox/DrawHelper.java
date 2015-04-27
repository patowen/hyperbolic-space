package net.patowen.poincaresandbox;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

public class DrawHelper
{
	double originX, originY;
	double scale;
	
	public void setBounds(int width, int height)
	{
		originX = (width-1)/2.0; originY = (height-1)/2.0;
		double min = Math.min(width-1, height-1);
		scale = min/2.0-2;
	}
	
	public void drawEuclideanCircle(Graphics2D g, Vector2 center, double radius, boolean fill)
	{
		if (fill)
			g.fill(new Ellipse2D.Double(getI(center.x - radius), getJ(center.y + radius),
					radius*scale*2, radius*scale*2));
		else
			g.draw(new Ellipse2D.Double(getI(center.x - radius), getJ(center.y + radius),
					radius*scale*2, radius*scale*2));
	}
	
	public void drawEuclideanLine(Graphics2D g, Vector2 v1, Vector2 v2)
	{
		g.draw(new Line2D.Double(getI(v1.x), getJ(v1.y), getI(v2.x), getJ(v2.y)));
	}
	
	public void drawCircle(Graphics2D g, Vector2 center, double radius, boolean fill)
	{
		double denom = center.squared()*radius*radius - 1;
		double newRadius = radius * ((center.squared() - 1) / denom);
		Vector2 newCenter = center.times((radius*radius - 1) / denom);
		
		drawEuclideanCircle(g, newCenter, newRadius, fill);
	}
	
	public void drawLineSegment(Graphics2D g, Vector2 v1, Vector2 v2)
	{
		Vector2 k1 = v1.times(2/(1+v1.squared()));
		Vector2 k2 = v2.times(2/(1+v2.squared()));
		
		int steps = 256;
		
		Path2D path = new Path2D.Double();
		
		for (int i=0; i<=steps; i++)
		{
			Vector2 x = k1.times((double)(steps-i)/steps).plus(k2.times((double)i/steps));
			Vector2 p = x.times(1.0/(1+Math.sqrt(1-x.squared())));
			
			if (i == 0)
				path.moveTo(getI(p.x), getJ(p.y));
			else
				path.lineTo(getI(p.x), getJ(p.y));
		}
		
		g.draw(path);
	}
	
	public void drawGraph(Graphics2D g, Vector2 x, Vector2 y, Vector2 v, int width)
	{
		Path2D path = new Path2D.Double();
		boolean first = true;
		
		for (int i=0; i<width; i++)
		{
			double dist = getX(i);
			Vector2 w = v.times(Math.tanh(dist));
			Vector2 a = w.dir(x);
			Vector2 b = w.dir(y);
			
			double result = a.dot(b);
			
			if (first)
				path.moveTo(i, getJ(result));
			else
				path.lineTo(i, getJ(result));
			first = false;
		}
		
		g.draw(path);
	}
	
	public double getI(double x)
	{
		return x*scale + originX;
	}
	
	public double getJ(double y)
	{
		return -y*scale + originY;
	}
	
	public double getX(double i)
	{
		return (i-originX) / scale;
	}
	
	public double getY(double j)
	{
		return - (j-originY) / scale;
	}
}
