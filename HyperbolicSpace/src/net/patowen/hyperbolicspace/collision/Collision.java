package net.patowen.hyperbolicspace.collision;

public class Collision
{
	public double distance;
	public Wall wall;
	
	public Collision(double distance, Wall wall)
	{
		this.distance = distance;
		this.wall = wall;
	}
}
