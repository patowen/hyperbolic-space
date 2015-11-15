package net.patowen.hyperbolicspace.collision;

import java.util.Optional;

import net.patowen.hyperbolicspace.math.MathHelper;
import net.patowen.hyperbolicspace.math.Vector3;

public class Plane implements Wall
{
	private Vector3 pos, norm;
	
	public Plane()
	{
		pos = new Vector3();
		norm = new Vector3(0, 0, 1);
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
	public double getIsometricInvariant(Vector3 point)
	{
		Vector3 pointT = point.hyperTranslate(pos), normT = pos.hyperDirectionTo(norm);
		double dot = pointT.dot(normT);
		double sinhC = 2*dot/(1-pointT.squared());
		return sinhC;
	}
	
	private double distanceToIsometricInvariant(double dist)
	{
		return Math.sinh(dist);
	}
	
	private double isometricInvariantToDistance(double invariant)
	{
		return MathHelper.asinh(invariant);
	}
}
