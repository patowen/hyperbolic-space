package net.patowen.hyperbolicspace;

import java.util.ArrayList;

import javax.media.opengl.GL3;

public class World
{
	private Controller c;
	
	private ArrayList<SceneNode> nodes;
	private Player player;
	
	public World(Controller c)
	{
		this.c = c;
		
		player = new Player(c, this);
		
		nodes = new ArrayList<SceneNode>();
	}
	
	public void addNode(SceneNode sceneNode)
	{
		nodes.add(sceneNode);
	}
	
	public void step(double dt)
	{
		player.step(dt);
		
		c.getInputHandler().updatePressed();
	}
	
	public void render(GL3 gl)
	{
		player.setView();
		
		for (SceneNode node : nodes)
			node.render(gl);
	}
}
