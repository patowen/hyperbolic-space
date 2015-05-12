package net.patowen.hyperbolicspace;

import java.util.ArrayList;

import javax.media.opengl.GL3;

/**
 * Handles the logic and rendering of all elements in the scene and
 * represents the scene itself
 * @author Patrick Owen
 */
public class World
{
	private Controller c;
	
	private ArrayList<SceneNode> nodes; //List of rendered nodes
	private Player player; //Controllable camera
	
	/**
	 * Initializes the world and its {@code Player} object
	 * @param c
	 */
	public World(Controller c)
	{
		this.c = c;
		
		player = new Player(c, this);
		
		nodes = new ArrayList<SceneNode>();
		nodes.add(new SceneNode(c.plane));
	}
	
	/**
	 * Spawns the specified node into the scene
	 * @param sceneNode the node to add
	 */
	public void addNode(SceneNode sceneNode)
	{
		nodes.add(sceneNode);
	}
	
	/**
	 * Handles a step of the world's logic
	 * @param dt the time step in seconds
	 */
	public void step(double dt)
	{
		player.step(dt);
		
		c.getInputHandler().updatePressed();
	}
	
	/**
	 * Renders a single frame of the world
	 * @param gl
	 */
	public void render(GL3 gl)
	{
		player.setView();
		
		for (SceneNode node : nodes)
			node.render(gl);
	}
}
