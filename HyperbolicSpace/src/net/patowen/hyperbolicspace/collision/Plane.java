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
		double goalInv = distanceToIsometricInvariant(radius);
		//TODO actually use fast algorithm
		int steps = 6400;
		for (int i=0; i<steps; i++)
		{
			double disp = Math.tanh(distance*i/steps);
			Vector3 pos = direction.times(disp).hyperTranslate(start);
			if (getIsometricInvariant(pos) < goalInv)
			{
				if (i == 0) return Optional.empty();
				return Optional.of(new Collision((double)(i-1)/steps));
			}
		}
		return Optional.empty();
	}
	
	// Returns the inverse of the squared circumradius of the three points at infinity of the plane
	// on the poincare disk model after being transformed so that pos is moved to the center.
	public double getIsometricInvariant(Vector3 pos)
	{
		Vector3 v1t = pos.hyperDirectionTo(v1), v2t = pos.hyperDirectionTo(v2), v3t = pos.hyperDirectionTo(v3);
		Vector3 a = v2t.minus(v1t), b = v3t.minus(v1t);
		double product = a.squared() * b.squared();
		double numerator = product - MathHelper.sqr(a.dot(b));
		double denominator = product * (a.minus(b)).squared();
		return 4*numerator/denominator;
	}
	
	private double distanceToIsometricInvariant(double dist)
	{
		Vector3 v1 = new Vector3(-1, 0, 0), v2 = new Vector3(1, 0, 0), v3 = new Vector3(0, Math.tanh(dist), 0);
		v1 = v3.hyperDirectionTo(v1);
		v2 = v3.hyperDirectionTo(v2);
		return 4 / MathHelper.sqr(v1.minus(v2).magnitude());
	}
	
	private double isometricInvariantToDistance(double invariant)
	{
		return 0;
	}
}
