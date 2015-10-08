package net.patowen.hyperbolicspace.collision;

import java.util.Optional;

import net.patowen.hyperbolicspace.Vector3;

public interface Wall
{
	public Optional<Collision> getSphereCollision(Vector3 start, Vector3 direction, double distance, double radius);
}
