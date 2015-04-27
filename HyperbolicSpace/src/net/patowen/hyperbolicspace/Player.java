package net.patowen.hyperbolicspace;

public class Player
{
	private Controller c;
	
	private Transformation pos;
	private Vector3 vel;
	
	public Player(Controller c)
	{
		this.c = c;
		
		pos = new Transformation();
		vel = new Vector3();
	}
	
	public void step(double dt)
	{
		handleTurning();
		handleAcceleration(dt);
		
		handleMovement(dt);
	}
	
	public void handleAcceleration(double dt)
	{
		InputHandler inputHandler = c.getInputHandler();
		
		if (inputHandler.getMouseButton(InputHandler.FORWARDS))
			vel.addMultiple(pos.getRotation().z, -dt);
		if (inputHandler.getMouseButton(InputHandler.BACKWARDS))
			vel.addMultiple(pos.getRotation().z, dt);
		if (inputHandler.getKey(InputHandler.UP))
			vel.addMultiple(pos.getRotation().y, dt);
		if (inputHandler.getKey(InputHandler.DOWN))
			vel.addMultiple(pos.getRotation().y, -dt);
		if (inputHandler.getKey(InputHandler.RIGHT))
			vel.addMultiple(pos.getRotation().x, dt);
		if (inputHandler.getKey(InputHandler.LEFT))
			vel.addMultiple(pos.getRotation().x, -dt);
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
