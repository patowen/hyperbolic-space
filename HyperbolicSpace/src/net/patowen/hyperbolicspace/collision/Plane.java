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
	
	public Plane(Vector3 pos, Vector3 norm)
	{
		this.pos = pos;
		this.norm = norm;
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
	
	// Returns the hyperbolic sine of the distance from the plane to the specified point.
	public double getIsometricInvariant(Vector3 point)
	{
		Vector3 pointT = point.hyperTranslate(pos), normT = pos.hyperDirectionTo(norm);
		double dot = pointT.dot(normT);
		double sinhC = 2*dot/(1-pointT.squared());
		return sinhC;
	}
	
	public Vector3 getProjection(Vector3 point)
	{
		Vector3 pointT = point.hyperTranslate(pos), normT = pos.hyperDirectionTo(norm);
		Vector3 euclidProj = pointT.minus(normT.times(normT.dot(pointT)));
		Vector3 tanhB = euclidProj.times(2.0/(1.0+point.squared()));
		return tanhB.times(1.0/(1+Math.sqrt(1-tanhB.squared())));
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
