package net.patowen.hyperbolicspace.collision;

import java.util.Optional;

import net.patowen.hyperbolicspace.math.Transformation;
import net.patowen.hyperbolicspace.math.Vector3;

public class SphereCollider
{
	public Transformation pos;
	public double radius;
	public Vector3 direction;
	public double speed;
	public double time;
	
	public SphereCollider(Transformation pos, double radius, Vector3 direction, double speed, double time)
	{
		this.pos = pos;
		this.radius = radius;
		this.direction = direction;
		this.speed = speed;
		this.time = time;
	}
	
	public void applyCollision(Optional<Collision> collisionOption)
	{
		Vector3 dPos;
		Vector3 directionAdjusted = direction.hyperTranslate(pos.getTranslation().times(-1));
		if (collisionOption.isPresent())
		{
			Collision collision = collisionOption.get();
			dPos = directionAdjusted.times(Math.tanh(speed*time*collision.distance/2));
			speed = 0;
		}
		else
		{
			dPos = directionAdjusted.times(Math.tanh(speed*time/2));
		}
		Vector3 loc = pos.getTranslation();
		pos = new Transformation(pos.getRotation(), dPos);
		pos = pos.composeAfter(new Transformation(loc));
		direction = directionAdjusted.hyperTranslate(dPos).hyperTranslate(loc);
	}
}
