package net.patowen.hyperbolicspace;

import javax.media.opengl.GL3;

public class World
{
	private Controller c;
	
	private Transformation t;
	
	private SceneNode building1;
	private SceneNode building2;
	
	public World(Controller c)
	{
		this.c = c;
		
		t = new Transformation();
		
		building1 = new Building(c);
		building1.setTransformation(new Transformation(new Orientation(), new Vector3(0.65, 0, 0)));
		building2 = new Building(c);
	}
	
	public void handleTurning()
	{
		InputHandler input = c.getInputHandler();
		
		input.readMouse();
		Orientation o = new Orientation();
		o.rotate(o.y, -input.getMouseX()*45);
		o.rotate(o.x, -input.getMouseY()*45);
		t = t.composeBefore(new Transformation(o, new Vector3()));
	}
	
	public void translate(double x, double y, double z)
	{
		translate(new Vector3(x, y, z));
	}
	
	public void translate(Vector3 v)
	{
		t = t.composeBefore(new Transformation(new Orientation(), v));
	}
	
	public void handleMovement(double dt)
	{
		InputHandler input = c.getInputHandler();
		
		double s = 0.01; //speed
		if (input.getKey(InputHandler.SLOW))
			s = 0.001;
		
		if (input.getMouseButton(InputHandler.FORWARDS))
			translate(new Vector3(0, 0, -s));
		if (input.getMouseButton(InputHandler.BACKWARDS))
			translate(new Vector3(0, 0, s));
		if (input.getKey(InputHandler.UP))
			translate(new Vector3(0, s, 0));
		if (input.getKey(InputHandler.DOWN))
			translate(new Vector3(0, -s, 0));
		if (input.getKey(InputHandler.RIGHT))
			translate(new Vector3(s, 0, 0));
		if (input.getKey(InputHandler.LEFT))
			translate(new Vector3(-s, 0, 0));
	}
	
	public void step(double dt)
	{
		Transformation trans = t.inverse();
		building1.reposition(trans);
		building2.reposition(trans);
		
		handleTurning();
		handleMovement(dt);
		
		c.getInputHandler().updatePressed();
	}
	
	public void renderInit(GL3 gl)
	{
		building1.renderInit(gl);
		building2.renderInit(gl);
	}
	
	public void render(GL3 gl)
	{
		c.getMatrixHandler().addTransformation(t.inverse());
		
		building1.render(gl);
		building2.render(gl);
	}
}
