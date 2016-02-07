package net.patowen.hyperbolicspace.entity;

import java.util.Optional;

import com.jogamp.opengl.math.FloatUtil;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.InputHandler;
import net.patowen.hyperbolicspace.World;
import net.patowen.hyperbolicspace.collision.Collision;
import net.patowen.hyperbolicspace.collision.Plane;
import net.patowen.hyperbolicspace.collision.SphereCollider;
import net.patowen.hyperbolicspace.math.MathHelper;
import net.patowen.hyperbolicspace.math.Orientation;
import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Transformation;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.rendering.SceneNode;

/**
 * A controllable camera used to navigate hyperbolic space with some control over the world
 * @author Patrick Owen
 */
public class Player
{
	private Controller c;
	private World w;
	
	private Transform pos;
	private Vector3 vel;
	
	private boolean noclip;
	
	//Perspective
	private double zoom = 0.6;
	
	//Non-noclip values
	private double radius;
	
	private SceneNode indicator;
	
	/**
	 * Initializes the {@code Player} and puts it in the specified {@code World}
	 * @param c
	 * @param w the world the {@code Player} can explore
	 */
	public Player(Controller c, World w)
	{
		this.c = c;
		this.w = w;
		
		indicator = new SceneNode(c.sphere);
//		w.addNode(indicator);
		
		radius = 0.2;
		pos = Transform.translation(Vector31.makePoincare(new Vector3(0, 0, Math.tanh(radius/2))));
		vel = new Vector3();
		
		noclip = true;
		
		handleOrientation();
	}
	
	/**
	 * Moves the camera based on player input and exercises some control, such
	 * as spawning structures
	 * @param dt the time step in seconds
	 */
	public void step(double dt)
	{
		handleNoclip();
		
		handleTurning(dt);
		handleAcceleration(dt);
		
		handleMovement(dt);
		
		handleOrientation();
		
		handleSpawning();
		
		handleZooming(dt);
	}
	
	private void handleNoclip()
	{
		if (c.getInputHandler().getKeyPressed(InputHandler.NOCLIP))
		{
			if (noclip)
			{
				noclip = false;
			}
			else
			{
				noclip = true;
			}
		}
	}
	
	/**
	 * Places meshes in front of the player depending on what controls are pressed
	 */
	private void handleSpawning()
	{
		InputHandler inputHandler = c.getInputHandler();
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_1))
		{
			spawnNode(new SceneNode(c.dodecahedron), getRotateAndTranslate(1,0,0, 0,1,0, 0,0,1, 0,0,-0.7));
		}
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_2))
		{
			spawnNode(new SceneNode(c.building), getRotateAndTranslate(-1,0,0, 0,0,1, 0,1,0, 0,0.003,-0.4));
		}
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_3))
		{
			spawnNode(new SceneNode(c.horosphere), getRotateAndTranslate(1,0,0, 0,1,0, 0,0,1, 0,0,-0.4));
		}
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_4))
		{
			spawnNode(new SceneNode(c.plane), getRotateAndTranslate(1,0,0, 0,1,0, 0,0,1, 0,0,-0.4));
		}
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_5))
		{
			spawnNode(new SceneNode(c.sphere), getRotateAndTranslate(1,0,0, 0,1,0, 0,0,1, 0,0,-0.4));
		}
	}
	
	private Transform getRotateAndTranslate(double xx, double xy, double xz, double yx, double yy,double yz,
			double zx, double zy, double zz, double tx, double ty, double tz)
	{
		int todo;
		Transform t = new Transform(new Vector31(xx, xy, xz, 0), new Vector31(yx, yy, yz, 0),
				new Vector31(zx, zy, zz, 0), new Vector31(0, 0, 0, 1));
		return t.transformedBy(Transform.translation(Vector31.makePoincare(new Vector3(tx, ty, tz))));
	}
	
	/**
	 * Adds the specified node to the world transformed by the given transformation relative to the player
	 * @param sceneNode the node to add
	 * @param t a transformation to apply to the node before applying the player transformation
	 */
	private void spawnNode(SceneNode sceneNode, Transform t)
	{
		sceneNode.setTransformation(pos.transform(t));
		w.addNode(sceneNode);
	}
	
	/**
	 * Accelerates to the desired velocity depending on the controls the user is pressing
	 * @param dt the time step in seconds
	 */
	private void handleAcceleration(double dt)
	{
		InputHandler inputHandler = c.getInputHandler();
		double maxChange, maxVel;
		
		if (inputHandler.getKey(InputHandler.SLOW))
		{
			maxChange = 0.4*dt;
			maxVel = 0.2;
		}
		else
		{
			maxChange = 4*dt;
			maxVel = 2;
		}
		double dx=0, dy=0, dz=0;
		if (inputHandler.getMouseButton(InputHandler.FORWARDS))
			dz -= 1;
		if (inputHandler.getMouseButton(InputHandler.BACKWARDS))
			dz += 1;
		if (inputHandler.getKey(InputHandler.UP))
			dy += 1;
		if (inputHandler.getKey(InputHandler.DOWN))
			dy -= 1;
		if (inputHandler.getKey(InputHandler.RIGHT))
			dx += 1;
		if (inputHandler.getKey(InputHandler.LEFT))
			dx -= 1;
		
		if (noclip)
		{
			Vector3 goalVel = new Vector3(maxVel*dx, maxVel*dy, maxVel*dz);
			approachVelocity(goalVel, maxChange);
		}
		else
		{
			vel = vel.times(Math.exp(-0.2*dt)); //Air friction
		}
	}
	
	/**
	 * Accelerates to the given velocity linearly
	 * @param goalVel the ideal velocity to achieve
	 * @param maxChange the maximum change allowed to be added to the current velocity
	 */
	private void approachVelocity(Vector3 goalVel, double maxChange)
	{
		double dist = goalVel.minus(vel).magnitude();
		if (dist <= maxChange)
		{
			vel = goalVel;
		}
		else
		{
			double progress = maxChange/dist;
			vel = vel.times(1-progress).plus(goalVel.times(progress));
		}
	}
	
	/**
	 * Moves the camera properly given the current velocity
	 * @param dt the time step
	 */
	private void handleMovement(double dt)
	{
		pos = pos.transform(Transform.translation(convertToPosition(vel, dt)));
		
		//Spherical precision frontier
//		double mag = pos.getTranslation().magnitude();
//		double maxMag = 0.9998;
//		if (mag > maxMag)
//			pos = new Transformation(pos.getRotation(), pos.getTranslation().times(maxMag/mag));
	}
	
	private double convertToCircumference(double r)
	{
		return (Math.sinh(MathHelper.atanh(r)));
	}
	
	private double convertToRadius(double a)
	{
		return Math.tanh(MathHelper.asinh(a));
	}
	
	private void handleOrientation()
	{
		// This method should slowly orient the player toward the gravity field.
	}
	
	/**
	 * Rotates the camera based on the controls pressed
	 * @param dt the time step
	 */
	private void handleTurning(double dt)
	{
		InputHandler inputHandler = c.getInputHandler();
		
		inputHandler.readMouse();
		
		Orientation o = new Orientation();
		o.rotate(o.y, -inputHandler.getMouseX()*45*Math.atan(zoom));
		o.rotate(o.x, -inputHandler.getMouseY()*45*Math.atan(zoom));
		double tilt = 0;
		if (inputHandler.getKey(InputHandler.TILT_LEFT))
			tilt -= 1;
		if (inputHandler.getKey(InputHandler.TILT_RIGHT))
			tilt += 1;
		o.rotate(o.z, -tilt*dt);
		vel = o.inverse().transform(vel);
		pos = pos.transform(Transform.fromOrientation(o));
	}
	
	/**
	 * Allows the user to change perspective
	 * @param dt the time step
	 */
	private void handleZooming(double dt)
	{
		InputHandler inputHandler = c.getInputHandler();
		if (inputHandler.getKey(InputHandler.ZOOM_IN))
			zoom *= Math.exp(-dt);
		if (inputHandler.getKey(InputHandler.ZOOM_OUT))
			zoom *= Math.exp(dt);
	}
	
	/**
	 * Sets the perspective matrix to the correct type based on player input
	 * @param aspect Aspect ratio of the screen
	 */
	public void setPerspective(float aspect)
	{
		float[] mat = new float[16];
		c.getMatrixHandler().setPerspective(FloatUtil.makePerspective(mat, 0, true, (float)(Math.atan(zoom)*2), aspect, 0.01f, 8.1f));
	}
	
	/**
	 * Transforms the view such that the player is transformed to the origin facing the default direction
	 */
	public void setView()
	{
		c.getMatrixHandler().addTransformation(pos.inverse());
	}
	
	/**
	 * Converts the velocity to the position an object would reach after traveling at that
	 * velocity for the given amount of time.
	 * @param vel the velocity to convert
	 * @param dt the time in seconds
	 * @return the resulting position
	 */
	private Vector31 convertToPosition(Vector3 vel, double dt)
	{
		double mag = vel.magnitude();
		if (mag == 0)
			return new Vector31(0, 0, 0, 1);
		else
		{
			Vector3 v = vel.times(Math.sinh(mag*dt)/mag);
			return new Vector31(v.x, v.y, v.z, Math.cosh(mag*dt));
		}
	}
	
	/**
	 * Converts the position to the velocity an object would need to travel to that position
	 * in one second.
	 * @param pos the position to convert
	 * @return the resulting velocity
	 */
	private Vector3 convertToVelocity(Vector3 pos)
	{
		double mag = pos.magnitude();
		if (mag == 0)
			return new Vector3(pos);
		else
			return pos.times(MathHelper.atanh(mag)/mag);
	}
}
