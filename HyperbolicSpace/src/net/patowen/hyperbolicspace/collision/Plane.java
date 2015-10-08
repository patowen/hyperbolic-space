package net.patowen.hyperbolicspace.collision;

import java.util.Optional;

import net.patowen.hyperbolicspace.MathHelper;
import net.patowen.hyperbolicspace.Vector3;

public class Plane implements Wall
{
	private Vector3 v1, v2, v3;
	
	public Plane()
	{
		v1 = new Vector3(1, 0, 0);
		v2 = new Vector3(0, 1, 0);
		v3 = new Vector3(-1, 0, 0);
	}
	
	public Optional<Collision> getSphereCollision(Vector3 start, Vector3 direction, double distance, double radius)
	{
		double goalInv = distanceToIsometricInvarient(radius);
		//TODO actually use fast algorithm
		int steps = 64;
		for (int i=0; i<steps; i++)
		{
			double disp = Math.tanh(distance*i/steps);
			Vector3 pos = direction.times(disp).hyperTranslate(start);
			if (getIsometricInvarient(pos) < goalInv)
			{
				return Optional.of(new Collision(distance*(i-1)/steps));
			}
		}
		return Optional.empty();
	}
	
	// Returns the inverse of the circumdiameter of the three points at infinity of the plane
	// on the poincare disk model after being transformed so that pos is moved to the center.
	private double getIsometricInvarient(Vector3 pos)
	{
		Vector3 v1t = pos.hyperDirectionTo(v1), v2t = pos.hyperDirectionTo(v2), v3t = pos.hyperDirectionTo(v3);
		Vector3 a = v2t.minus(v1t), b = v3t.minus(v1t);
		double product = a.squared() * b.squared();
		double numerator = product - MathHelper.sqr(a.dot(b));
		double denominator = product * (a.minus(b)).squared();
		return numerator/denominator;
	}
	
	private double distanceToIsometricInvarient(double dist)
	{
		Vector3 v1 = new Vector3(-1, 0, 0), v2 = new Vector3(1, 0, 0), v3 = new Vector3(0, Math.tanh(dist), 0);
		v1 = v3.hyperDirectionTo(v1);
		v2 = v3.hyperDirectionTo(v2);
		return 1 / (v1.minus(v2).magnitude());
	}
}
