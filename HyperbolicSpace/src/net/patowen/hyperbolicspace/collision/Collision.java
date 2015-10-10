package net.patowen.hyperbolicspace.collision;

public class Collision
{
	public double distance;
	
	public Collision(double distance)
	{
		this.distance = distance;
	}
	
	public String toString()
	{
		return Double.toString(distance);
	}
}
