package net.patowen.hyperbolicspace;

import java.util.ArrayList;

import javax.media.opengl.GL3;

public class World
{
	private Controller c;
	
	private Transformation t;
	
	private ArrayList<SceneNode> nodes;
	private Player player;
	
	public World(Controller c)
	{
		this.c = c;
		
		t = new Transformation();
		
		player = new Player(c);
		
		nodes = new ArrayList<SceneNode>();
		
		SceneNode horo1 = new Horosphere(c);
		horo1.setTransformation(new Transformation(new Orientation(), new Vector3(0, 0, -0.65)));
		SceneNode horo2 = new Horosphere(c);
		horo2.setTransformation(new Transformation(new Orientation(), new Vector3(0, 0, -0.67)));
		SceneNode building2 = new Building(c);
		SceneNode dodec = new Dodecahedron(c);
		dodec.setTransformation(new Transformation(new Orientation(), new Vector3(0, 0.8, 0)));
		
		nodes.add(horo1);
		nodes.add(horo2);
		nodes.add(building2);
		nodes.add(dodec);
	}
	
	public void handleMovement(double dt)
	{
//		InputHandler input = c.getInputHandler();
//		
//		double s = 0.01; //speed
//		if (input.getKey(InputHandler.SLOW))
//			s = 0.001;
		
//		if (input.getMouseButton(InputHandler.FORWARDS))
//			translate(new Vector3(0, 0, -s));
//		if (input.getMouseButton(InputHandler.BACKWARDS))
//			translate(new Vector3(0, 0, s));
//		if (input.getKey(InputHandler.UP))
//			translate(new Vector3(0, s, 0));
//		if (input.getKey(InputHandler.DOWN))
//			translate(new Vector3(0, -s, 0));
//		if (input.getKey(InputHandler.RIGHT))
//			translate(new Vector3(s, 0, 0));
//		if (input.getKey(InputHandler.LEFT))
//			translate(new Vector3(-s, 0, 0));
	}
	
	public void step(double dt)
	{
		Transformation trans = t.inverse();
		for (SceneNode node : nodes)
			node.reposition(trans);
//		building2.reposition(trans);
		
		player.step(dt);
		
		c.getInputHandler().updatePressed();
	}
	
	public void renderInit(GL3 gl)
	{
		for (SceneNode node : nodes)
			node.renderInit(gl);
	}
	
	public void render(GL3 gl)
	{
		player.setView();
		
		for (SceneNode node : nodes)
			node.render(gl);
	}
}
