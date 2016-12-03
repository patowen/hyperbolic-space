package net.patowen.hyperbolicspace;

import java.util.ArrayList;

import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.entity.FlagField;
import net.patowen.hyperbolicspace.entity.Player;
import net.patowen.hyperbolicspace.entity.PrismTree;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.rendering.SceneNode;
import net.patowen.hyperbolicspace.rendering.ShaderUniformHandler;

/**
 * Handles the logic and rendering of all elements in the scene and
 * represents the scene itself
 * @author Patrick Owen
 */
public class World {
	private Controller c;
	
	private ArrayList<SceneNode> nodes; //List of rendered nodes
	private Player player; //Controllable camera
	private FlagField flag;
	private PrismTree prismTree;
	
	private int viewportWidth, viewportHeight;
	
	/**
	 * Initializes the world and its {@code Player} object
	 * @param c
	 */
	public World(Controller c) {
		this.c = c;
		
		nodes = new ArrayList<SceneNode>();
		reset();
	}
	
	/**
	 * Sets the viewport used for perspective in the world
	 * @param width The width in pixels of the viewport
	 * @param height The height in pixels of the viewport
	 */
	public void setViewport(int width, int height) {
		viewportWidth = width;
		viewportHeight = height;
	}
	
	/**
	 * Resets the world to how it was before it was initialized
	 */
	public void reset() {
		nodes.clear();
		player = new Player(c, this);
		flag = new FlagField(c, this);
		prismTree = new PrismTree(c, this);
		//nodes.add(new SceneNode(c.plane));
//		SceneNode plane2 = new SceneNode(c.plane);
//		plane2.setTransformation(new Transformation(
//				new Orientation(new Vector3(1, 0, 0), new Vector3(0, 12.0/13, -5.0/13), new Vector3(0, 5.0/13, 12.0/13)),
//				new Vector3()));
//		nodes.add(plane2);
	}
	
	/**
	 * Spawns the specified node into the scene
	 * @param sceneNode the node to add
	 */
	public void addNode(SceneNode sceneNode) {
		nodes.add(sceneNode);
	}
	
	/**
	 * Handles a step of the world's logic
	 * @param dt the time step in seconds
	 */
	public void step(double dt) {
		InputHandler inputHandler = c.getInputHandler();
		player.step(dt);
		flag.step(dt);
		
		if (inputHandler.getKeyPressed(InputHandler.RESET))
			reset();
		if (inputHandler.getKeyPressed(InputHandler.CLEAR))
			nodes.clear();
		
		inputHandler.updatePressed();
	}
	
	/**
	 * Renders a single frame of the world
	 * @param gl
	 */
	public void render(GL3 gl) {
		player.setPerspective((float)viewportWidth/viewportHeight);
		
		ShaderUniformHandler sh = c.getMatrixHandler();
		sh.setLightAmbient(new float[] {0.1f, 0.1f, 0.1f});
		sh.setLightPosition(0, new Vector31(0, 0, 0, -1));
		sh.setLightDiffuse(0, new float[] {0.9f, 0.9f, 0.9f});
		
		player.setView();
		
		for (SceneNode node : nodes)
			node.render(gl);
	}
}
