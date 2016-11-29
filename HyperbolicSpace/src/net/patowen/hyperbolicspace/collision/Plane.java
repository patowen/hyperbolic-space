package net.patowen.hyperbolicspace.collision;

import java.util.Optional;

import net.patowen.hyperbolicspace.math.MathHelper;
import net.patowen.hyperbolicspace.math.Vector3;

public class Plane implements Wall {
	private Vector3 pos, norm;
	
	public Plane() {
		pos = new Vector3();
		norm = new Vector3(0, 0, 1);
	}
	
	public Plane(Vector3 pos, Vector3 norm) {
		this.pos = pos;
		this.norm = norm;
	}
	
	public Optional<Collision> getSphereCollision(SphereCollider collider) {
		double dist = getIsometricInvariant(collider.pos.getTranslation());
		double goalInv = distanceToIsometricInvariant(collider.radius);
		System.out.print(dist-goalInv + " ");
		if (dist < goalInv || collider.speed == 0 || collider.time == 0)
			return Optional.empty();
		double x1 = 0, x2 = 0.5*(isometricInvariantToDistance(dist)-collider.radius)/collider.speed/collider.time;
		if (x2 < 0 || x2 > 1)
			return Optional.empty();
		System.out.print("#");
		while (x2-x1 > 1e-8) {
			double y1 = getIsometricInvariant(collider, x1)-goalInv;
			double y2 = getIsometricInvariant(collider, x2)-goalInv;
			System.out.print(y2 + " ");
			double x3 = x2-y2*(x2-x1)/(y2-y1);
			if (x3 < 0 || x3 > 1) {
				System.out.println();
				return Optional.empty();
			}
			x1 = x2;
			x2 = x3;
		}
		System.out.println();
		if (getIsometricInvariant(collider, x2) > getIsometricInvariant(collider, x1))
			return Optional.empty();
//		System.out.println();
		return Optional.of(new Collision(x1, this));
		
		
		//TODO actually use fast algorithm
//		int steps = 6400;
//		for (int i=0; i<steps; i++)
//		{
//			if (getIsometricInvariant(collider, (double)i/steps) < goalInv)
//			{
//				if (i == 0) return Optional.empty();
//				return Optional.of(new Collision((double)(i-1)/steps, this));
//			}
//		}
//		return Optional.empty();
	}
	
	private double getIsometricInvariant(SphereCollider collider, double distanceRatio) {
		Vector3 relativeDirection = collider.direction.hyperTranslate(collider.pos.getTranslation().times(-1));
		double disp = Math.tanh(collider.speed*collider.time*distanceRatio/2);
		Vector3 pos = relativeDirection.times(disp).hyperTranslate(collider.pos.getTranslation());
		return getIsometricInvariant(pos);
		
	}
	
	// Returns the hyperbolic sine of the distance from the plane to the specified point.
	public double getIsometricInvariant(Vector3 point) {
		Vector3 pointT = point.hyperTranslate(pos), normT = pos.hyperDirectionTo(norm);
		double dot = pointT.dot(normT);
		double sinhC = 2*dot/(1-pointT.squared());
		return Math.abs(sinhC);
	}
	
	public Vector3 getProjection(Vector3 point) {
		Vector3 pointT = point.hyperTranslate(pos), normT = pos.hyperDirectionTo(norm);
		Vector3 euclidProj = pointT.minus(normT.times(normT.dot(pointT)));
		Vector3 tanhB = euclidProj.times(2.0/(1.0+point.squared()));
		return tanhB.times(1.0/(1+Math.sqrt(1-tanhB.squared())));
	}
	
	private double distanceToIsometricInvariant(double dist) {
		return Math.sinh(dist);
	}
	
	private double isometricInvariantToDistance(double invariant) {
		return MathHelper.asinh(invariant);
	}
}
