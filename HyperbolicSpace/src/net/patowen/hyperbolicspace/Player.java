package net.patowen.hyperbolicspace;

public class Player
{
	private Controller c;
	private World w;
	
	private Transformation pos;
	private Vector3 vel;
	
	public Player(Controller c, World w)
	{
		this.c = c;
		this.w = w;
		
		pos = new Transformation();
		vel = new Vector3();
	}
	
	public void step(double dt)
	{
		handleTurning(dt);
		handleAcceleration(dt);
		
		handleMovement(dt);
		
		handleSpawning();
	}
	
	public void handleSpawning()
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
	}
	
	public void spawnNode(SceneNode sceneNode, Transformation t)
	{
		sceneNode.setTransformation((new Transformation(pos)).composeBefore(t));
		w.addNode(sceneNode);
	}
	
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
	
	public void setView()
	{
		c.getMatrixHandler().addTransformation(pos.inverse());
	}
	
	private Vector3 convertToPosition(Vector3 vel)
	{
		double mag = vel.magnitude();
		if (mag == 0)
			return new Vector3(vel);
		else
			return vel.times(Math.tanh(mag)/mag);
	}
	
	private Vector3 convertToVelocity(Vector3 pos)
	{
		double mag = pos.magnitude();
		if (mag == 0)
			return new Vector3(pos);
		else
			return pos.times(MathHelper.atanh(mag)/mag);
	}
}
