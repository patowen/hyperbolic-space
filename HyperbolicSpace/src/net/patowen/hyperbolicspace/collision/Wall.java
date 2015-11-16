package net.patowen.hyperbolicspace.collision;

import java.util.Optional;

import net.patowen.hyperbolicspace.math.Vector3;

public interface Wall
{
	public Optional<Collision> getSphereCollision(SphereCollider collider);
	public Vector3 getProjection(Vector3 point);
}
