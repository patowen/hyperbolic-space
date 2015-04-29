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
		handleTurning();
		handleAcceleration(dt);
		
		handleMovement(dt);
		
		handleSpawning();
	}
	
	public void handleSpawning()
	{
		InputHandler inputHandler = c.getInputHandler();
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_1))
		{
			spawnNode(new SceneNode(c.dodecahedron));
		}
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_2))
		{
			spawnNode(new SceneNode(c.building));
		}
		if (inputHandler.getKeyPressed(InputHandler.SPAWN_3))
		{
			spawnNode(new SceneNode(c.horosphere));
		}
	}
	
	public void spawnNode(SceneNode sceneNode)
	{
		sceneNode.setTransformation(new Transformation(pos));
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
		
		Vector3 velPos = convertToPosition(vel);
		Vector3 dPos = convertToPosition(vel.times(0.1*dt));
		pos = new Transformation(pos.getRotation(), new Vector3());
		
		pos = pos.composeAfter(new Transformation(new Orientation(), dPos));
		velPos = velPos.hyperTranslate(dPos);
		
		pos = pos.composeAfter(new Transformation(new Orientation(), loc));
		velPos = velPos.hyperTranslate(loc).hyperTranslate(pos.getTranslation().times(-1));
		
		vel = convertToVelocity(velPos);
	}
	
	public void handleTurning()
	{
		InputHandler input = c.getInputHandler();
		
		input.readMouse();
		Orientation o = new Orientation();
		o.rotate(o.y, -input.getMouseX()*45);
		o.rotate(o.x, -input.getMouseY()*45);
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
