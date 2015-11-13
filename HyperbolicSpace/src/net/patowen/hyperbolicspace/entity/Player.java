package net.patowen.hyperbolicspace.entity;

import java.util.Optional;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.InputHandler;
import net.patowen.hyperbolicspace.World;
import net.patowen.hyperbolicspace.collision.Collision;
import net.patowen.hyperbolicspace.collision.Plane;
import net.patowen.hyperbolicspace.math.MathHelper;
import net.patowen.hyperbolicspace.math.Orientation;
import net.patowen.hyperbolicspace.math.Transformation;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.rendering.SceneNode;

import com.jogamp.opengl.math.FloatUtil;

/**
 * A controllable camera used to navigate hyperbolic space with some control over the world
 * @author Patrick Owen
 */
public class Player
{
	private Controller c;
	private World w;
	
	private Transformation pos;
	private Vector3 vel;
	
	private boolean noclip;
	
	//Perspective
	private double zoom = 0.6;
	
	//Non-noclip values
	private double radius;
	private boolean grounded;
	private Orientation horizontalDir;
	private double verticalDir;
	private double tilt;
	
	/**
	 * Initializes the {@code Player} and puts it in the specified {@code World}
	 * @param c
	 * @param w the world the {@code Player} can explore
	 */
	public Player(Controller c, World w)
	{
		this.c = c;
		this.w = w;
		
		radius = 0.2;
		pos = new Transformation(new Orientation(), new Vector3(0, 0, radius));
		vel = new Vector3();
		
		noclip = false;
		grounded = true;
		horizontalDir = new Orientation();
		verticalDir = -0.3;
		tilt = 0;
		
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
				
				//Fix options that were left unchanged during noclip
				Orientation o = pos.getRotation();
				
				Vector3 planePoint = getPlanePoint(pos.getTranslation());
				Vector3 offset = pos.getTranslation().hyperTranslate(planePoint.times(-1));
				
				//Change size if necessary
				if (offset.z < radius) radius = Math.max(0.01, offset.z);
				
				o = o.hyperTranslate(offset.times(-1), planePoint);
				
				double dir = Math.atan2(-o.z.y, -o.z.x);
				horizontalDir = new Orientation();
				horizontalDir.rotate(new Vector3(0, 0, 1), dir);
				o = horizontalDir.inverse().transform(o);
				
				verticalDir = Math.atan2(-o.z.z, -o.z.x);
				o.rotate(new Vector3(0, -1, 0), -verticalDir);
				
				tilt = -Math.atan2(o.y.y, o.y.z);
				o.rotate(new Vector3(1, 0, 0), tilt);
			}
			else
			{
				noclip = true;
				grounded = false;
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
			spawnNode(new SceneNode(c.dodecahedron), new Transformation(
					new Orientation(new Vector3(1,0,0), new Vector3(0,1,0), new Vector3(0,0,1)), new Vector3(0,0,-0.7)));
		}
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_2))
		{
			spawnNode(new SceneNode(c.building), new Transformation(
					new Orientation(new Vector3(-1,0,0), new Vector3(0,0,1), new Vector3(0,1,0)), new Vector3(0,0.003,-0.4)));
		}
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_3))
		{
			spawnNode(new SceneNode(c.horosphere), new Transformation(
					new Orientation(new Vector3(1,0,0), new Vector3(0,1,0), new Vector3(0,0,1)), new Vector3(0,0,-0.4)));
		}
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_4))
		{
			spawnNode(new SceneNode(c.plane), new Transformation(
					new Orientation(new Vector3(1,0,0), new Vector3(0,1,0), new Vector3(0,0,1)), new Vector3(0,0,-0.4)));
		}
	}
	
	/**
	 * Adds the specified node to the world transformed by the given transformation relative to the player
	 * @param sceneNode the node to add
	 * @param t a transformation to apply to the node before applying the player transformation
	 */
	private void spawnNode(SceneNode sceneNode, Transformation t)
	{
		if (noclip)
			sceneNode.setTransformation(pos.composeBefore(t));
		else
		{
			Vector3 groundPos = getPlanePoint(pos.getTranslation());
			Orientation groundDir = new Orientation(new Vector3(0, -1, 0), new Vector3(0, 0, 1), new Vector3(-1, 0, 0));
			groundDir = horizontalDir.transform(groundDir);
			sceneNode.setTransformation((new Transformation(groundDir, groundPos)).composeBefore(t));
		}
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
			maxChange = 0.2*dt;
			maxVel = 0.1;
		}
		else
		{
			maxChange = 2*dt;
			maxVel = 1;
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
			Vector3 goalVel = pos.getRotation().transform(new Vector3(maxVel*dx, maxVel*dy, maxVel*dz));
			approachVelocity(goalVel, maxChange);
		}
		else if (grounded)
		{
			Orientation o = new Orientation();
			o = horizontalDir.transform(o);
			Vector3 planePoint = getPlanePoint(pos.getTranslation());
			Vector3 offset = pos.getTranslation().hyperTranslate(planePoint.times(-1));
			o = o.hyperTranslate(offset, planePoint);
			
			Vector3 goalVel = o.transform(new Vector3(maxVel*dy, -maxVel*dx, 0));
			approachVelocity(goalVel, maxChange);
			
			if (inputHandler.getKeyPressed(InputHandler.JUMP))
			{
				vel = vel.plusMultiple(o.z, 0.5);
				grounded = false;
			}
		}
		else
		{
			vel = vel.plusMultiple(getGravity(), dt); //Gravity
			vel = vel.times(Math.exp(-0.2*dt)); //Air friction
		}
	}
	
	private Vector3 getGravity()
	{
		Vector3 goal = getPlaneDirection(pos.getTranslation());
		return goal.hyperTranslate(pos.getTranslation().times(-1)).times(1); //1 is the gravitational acceleration
	}
	
	private void snapToPlane()
	{
		Vector3 start = pos.getTranslation();
		Vector3 goal = (new Vector3(0, 0, radius)).hyperTranslate(getPlanePoint(start)).hyperTranslate(start.times(-1));
		
		pos = pos.composeAfter(new Transformation(new Orientation(), start.times(-1)));
		pos = pos.composeAfter(new Transformation(new Orientation(), goal));
		pos = pos.composeAfter(new Transformation(new Orientation(), start));
	}
	
	private Vector3 getPlanePoint(Vector3 p)
	{
		Vector3 klein = p.times(2/(1+p.squared()));
		Vector3 perpen = new Vector3(klein.x, klein.y, 0);
		return perpen.times(1.0 / (1 + Math.sqrt(Math.max(0, 1-perpen.squared()))));
	}
	
	private Vector3 getPlaneDirection(Vector3 p)
	{
		Vector3 klein = p.times(2/(1+p.squared()));
		Vector3 perpen = new Vector3(klein.x, klein.y, -Math.sqrt(1-klein.x*klein.x-klein.y*klein.y));
		return perpen.times(1.0 / (1 + Math.sqrt(Math.max(0, 1-perpen.squared()))));
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
		Vector3 loc = pos.getTranslation();
		
		Vector3 velPos = convertToPosition(vel.times(0.01));
		Vector3 dPos = convertToPosition(vel.times(dt));
		
		Plane plane = new Plane();
		Optional<Collision> o = plane.getSphereCollision(loc, vel.times(1/vel.magnitude()), vel.magnitude()*dt, MathHelper.atanh(radius));
		if (o.isPresent())
		{
			System.out.println(o.get());
			dPos = convertToPosition(vel.times(dt*o.get().distance));
		}
		
		pos = new Transformation(pos.getRotation(), new Vector3());
		
		pos = pos.composeAfter(new Transformation(new Orientation(), dPos));
		velPos = velPos.hyperTranslate(dPos);
		
		pos = pos.composeAfter(new Transformation(new Orientation(), loc));
		velPos = velPos.hyperTranslate(loc).hyperTranslate(pos.getTranslation().times(-1));
		
		vel = convertToVelocity(velPos).times(100);
		
		if (!noclip)
		{
			//Grow or shrink
			InputHandler inputHandler = c.getInputHandler();
			if (inputHandler.getKey(InputHandler.GROW))
				radius = convertToRadius(convertToCircumference(radius)*Math.exp(dt));
			if (inputHandler.getKey(InputHandler.SHRINK))
				radius = convertToRadius(convertToCircumference(radius)*Math.exp(-dt));
			
			//Move horizontally
			Vector3 oldPlanePos = getPlanePoint(loc);
			Vector3 newPlanePos = getPlanePoint(pos.getTranslation());
			Vector3 dPlanePos = oldPlanePos.hyperTranslate(newPlanePos.times(-1));
			horizontalDir = horizontalDir.hyperTranslate(oldPlanePos, dPlanePos);
			
			Vector3 offset = pos.getTranslation().hyperTranslate(newPlanePos.times(-1));
			if (offset.z < radius && !grounded)
			{
				grounded = true;
				snapToPlane();
				
				//Reset velocity
				Vector3 perpen = newPlanePos.hyperTranslate(pos.getTranslation().times(-1));
				vel = vel.plusMultiple(perpen, -perpen.dot(vel)/perpen.squared());
			}
			else if (grounded)
				snapToPlane();
		}
		
		//Spherical precision frontier
		double mag = pos.getTranslation().magnitude();
		double maxMag = 0.9998;
		if (mag > maxMag)
			pos = new Transformation(pos.getRotation(), pos.getTranslation().times(maxMag/mag));
	}
	
	private double convertToCircumference(double r)
	{
		return (Math.sinh(MathHelper.atanh(radius)));
	}
	
	private double convertToRadius(double a)
	{
		return Math.tanh(MathHelper.asinh(a));
	}
	
	private void handleOrientation()
	{
		if (!noclip)
		{
			Orientation o = new Orientation(new Vector3(0, -1, 0), new Vector3(0, 0, 1), new Vector3(-1, 0, 0));
			o.rotate(new Vector3(1, 0, 0), tilt);
			o.rotate(new Vector3(0, -1, 0), verticalDir);
			o = horizontalDir.transform(o);
			Vector3 planePoint = getPlanePoint(pos.getTranslation());
			Vector3 offset = pos.getTranslation().hyperTranslate(planePoint.times(-1));
			o = o.hyperTranslate(offset, planePoint);
			pos = new Transformation(o, pos.getTranslation());
		}
	}
	
	/**
	 * Rotates the camera based on the controls pressed
	 * @param dt the time step
	 */
	private void handleTurning(double dt)
	{
		InputHandler inputHandler = c.getInputHandler();
		
		inputHandler.readMouse();
		
		if (noclip)
		{
			Orientation o = new Orientation();
			o.rotate(o.y, -inputHandler.getMouseX()*45*Math.atan(zoom));
			o.rotate(o.x, -inputHandler.getMouseY()*45*Math.atan(zoom));
			double tilt = 0;
			if (inputHandler.getKey(InputHandler.TILT_LEFT))
				tilt -= 1;
			if (inputHandler.getKey(InputHandler.TILT_RIGHT))
				tilt += 1;
			o.rotate(o.z, -tilt*dt);
			pos = pos.composeBefore(new Transformation(o, new Vector3()));
		}
		else
		{
			verticalDir += -inputHandler.getMouseY()*45*Math.atan(zoom);
			if (verticalDir > Math.PI/2) verticalDir = Math.PI/2;
			if (verticalDir < -Math.PI/2) verticalDir = -Math.PI/2;
			if (tilt > 0) tilt = Math.max(0, tilt - 0.1);
			if (tilt < 0) tilt = Math.min(0, tilt + 0.1);
			Orientation o = new Orientation();
			
			o.rotate(new Vector3(0, 0, 1), -inputHandler.getMouseX()*45*Math.atan(zoom));
			
			horizontalDir = o.transform(horizontalDir);
			horizontalDir.normalize();
		}
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
	 * velocity for one second.
	 * @param vel the velocity to convert
	 * @return the resulting position
	 */
	private Vector3 convertToPosition(Vector3 vel)
	{
		double mag = vel.magnitude();
		if (mag == 0)
			return new Vector3(vel);
		else
			return vel.times(Math.tanh(mag)/mag);
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
