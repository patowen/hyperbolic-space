package net.patowen.hyperbolicspace;

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
	
	/**
	 * Initializes the {@code Player} and puts it in the specified {@code World}
	 * @param c
	 * @param w the world the {@code Player} can explore
	 */
	public Player(Controller c, World w)
	{
		this.c = c;
		this.w = w;
		
		pos = new Transformation();
		vel = new Vector3();
	}
	
	/**
	 * Moves the camera based on player input and exercises some control, such
	 * as spawning structures
	 * @param dt the time step in seconds
	 */
	public void step(double dt)
	{
		handleTurning(dt);
		handleAcceleration(dt);
		
		handleMovement(dt);
		
		handleSpawning();
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
					new Orientation(new Vector3(-1,0,0), new Vector3(0,0,1), new Vector3(0,1,0)), new Vector3(0,0,-0.4)));
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
	public void spawnNode(SceneNode sceneNode, Transformation t)
	{
		sceneNode.setTransformation((new Transformation(pos)).composeBefore(t));
		w.addNode(sceneNode);
	}
	
	/**
	 * Accelerates to the desired velocity depending on the controls the user is pressing
	 * @param dt the time step in seconds
	 */
	public void handleAcceleration(double dt)
	{
		InputHandler inputHandler = c.getInputHandler();
		double maxChange, maxVel;
		
		if (inputHandler.getKey(InputHandler.SLOW))
		{
			maxChange = 2*dt;
			maxVel = 1;
		}
		else
		{
			maxChange = 20*dt;
			maxVel = 10;
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
		
		Vector3 goalVel = pos.getRotation().transform(new Vector3(maxVel*dx, maxVel*dy, maxVel*dz));
		approachVelocity(goalVel, maxChange);
	}
	
	/**
	 * Accelerates to the given velocity linearly
	 * @param goalVel the ideal velocity to achieve
	 * @param maxChange the maximum change allowed to be added to the current velocity
	 */
	public void approachVelocity(Vector3 goalVel, double maxChange)
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
	public void handleMovement(double dt)
	{
		Vector3 loc = pos.getTranslation();
		
		Vector3 velPos = convertToPosition(vel.times(0.01));
		Vector3 dPos = convertToPosition(vel.times(0.1*dt));
		pos = new Transformation(pos.getRotation(), new Vector3());
		
		pos = pos.composeAfter(new Transformation(new Orientation(), dPos));
		velPos = velPos.hyperTranslate(dPos);
		
		pos = pos.composeAfter(new Transformation(new Orientation(), loc));
		velPos = velPos.hyperTranslate(loc).hyperTranslate(pos.getTranslation().times(-1));
		
		vel = convertToVelocity(velPos).times(100);
		
		//Spherical precision frontier
		double mag = pos.getTranslation().magnitude();
		double maxMag = 0.9998;
		if (mag > maxMag)
			pos = new Transformation(pos.getRotation(), pos.getTranslation().times(maxMag/mag));
	}
	
	/**
	 * Rotates the camera based on the controls pressed
	 * @param dt the time step
	 */
	public void handleTurning(double dt)
	{
		InputHandler inputHandler = c.getInputHandler();
		
		inputHandler.readMouse();
		Orientation o = new Orientation();
		o.rotate(o.y, -inputHandler.getMouseX()*45);
		o.rotate(o.x, -inputHandler.getMouseY()*45);
		double tilt = 0;
		if (inputHandler.getKey(InputHandler.TILT_LEFT))
			tilt -= 1;
		if (inputHandler.getKey(InputHandler.TILT_RIGHT))
			tilt += 1;
		o.rotate(o.z, -tilt*dt);
		pos = pos.composeBefore(new Transformation(o, new Vector3()));
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
