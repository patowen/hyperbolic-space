package net.patowen.poincaresandbox;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

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
			g.fill(new Ellipse2D.Double(getI(center.x - radius), getJ(center.y - radius),
					radius*scale*2, radius*scale*2));
		else
			g.draw(new Ellipse2D.Double(getI(center.x - radius), getJ(center.y - radius),
					radius*scale*2, radius*scale*2));
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
		if (Math.abs(v1.x * v2.y - v2.x * v1.y) < 0.01)
			g.draw(new Line2D.Double(getI(v1.x), getJ(v1.y), getI(v2.x), getJ(v2.y)));
		//Vector2 w2 = v2.hyperTranslate(v1.times(-1));
	}
	
	public double getI(double x)
	{
		return x*scale + originX;
	}
	
	public double getJ(double y)
	{
		return y*scale + originY;
	}
	
	public double getX(double i)
	{
		return (i-originX) / scale;
	}
	
	public double getY(double j)
	{
		return (j-originY) / scale;
	}
}
