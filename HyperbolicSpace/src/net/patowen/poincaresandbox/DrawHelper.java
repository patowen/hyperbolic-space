package net.patowen.poincaresandbox;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class DrawHelper
{
	double originX, originY;
	double scale;
	
	public void setBounds(int width, int height)
	{
		originX = (width-1)/2.0; originY = (height-1)/2.0;
		double min = Math.min(width-1, height-1);
		scale = min/2.0;
	}
	
	public void drawEuclideanCircle(Graphics2D g, Vector2 center, double radius)
	{
		g.draw(new Ellipse2D.Double(getI(center.x - radius), getJ(center.y - radius),
				radius*scale*2, radius*scale*2));
	}
	
	public void drawCircle(Graphics2D g, Vector2 center, double radius)
	{
		double denom = center.squared() + radius*radius;
		double newRadius = radius * ((center.squared() - 1) / denom);
		Vector2 newCenter = center.times((radius*radius - 1) / denom);
		
		drawEuclideanCircle(g, newCenter, newRadius);
	}
	
	public void drawLineSegment(Graphics2D g, Vector2 v1, Vector2 v2)
	{
		
	}
	
	public double getI(double x)
	{
		return x*scale + originX;
	}
	
	public double getJ(double y)
	{
		return y*scale + originY;
	}
}
