package net.patowen.hyperbolicspace.collision;

import java.util.Optional;

import net.patowen.hyperbolicspace.math.Transformation;
import net.patowen.hyperbolicspace.math.Vector3;

public class SphereCollider {
	public Transformation pos;
	public double radius;
	public Vector3 direction;
	public double speed;
	public double time;
	
	public SphereCollider(Transformation pos, double radius, Vector3 direction, double speed, double time) {
		this.pos = pos;
		this.radius = radius;
		this.direction = direction;
		this.speed = speed;
		this.time = time;
	}
	
	private Vector3 getRelativeDirection() {
		return direction.hyperTranslate(pos.getTranslation().times(-1));
	}
	
	private void adjustPos(Vector3 relativePos) {
		Vector3 loc = pos.getTranslation();
		pos = new Transformation(pos.getRotation(), relativePos);
		pos = pos.composeAfter(new Transformation(loc));
	}
	
	public void applyCollision(Optional<Collision> collisionOption) {
		Vector3 dPos;
		Vector3 relativeDirection = getRelativeDirection();
		if (collisionOption.isPresent()) {
			Collision collision = collisionOption.get();
			dPos = relativeDirection.times(Math.tanh(speed*time*collision.distance/2));
			adjustPos(dPos);
			time *= (1-collision.distance);
			
			Wall wall = collision.wall;
			Vector3 normal = wall.getProjection(pos.getTranslation()).hyperTranslate(pos.getTranslation().times(-1));
			normal.normalize();
			Vector3 vel = relativeDirection.times(speed);
			vel = vel.minus(normal.times(normal.dot(vel)));
			speed = vel.magnitude();
			vel.normalize();
			relativeDirection = vel;
		} else {
			dPos = relativeDirection.times(Math.tanh(speed*time/2));
			adjustPos(dPos);
			time = 0;
		}
		direction = relativeDirection.hyperTranslate(dPos).hyperTranslate(pos.getTranslation());
	}
}
