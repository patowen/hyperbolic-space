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
		
		SceneNode horo = new SceneNode(c.horosphere);
		horo.setTransformation(new Transformation(new Orientation(), new Vector3(0, 0, -0.1)));
		SceneNode building = new SceneNode(c.building);
		SceneNode dodec = new SceneNode(c.dodecahedron);
		dodec.setTransformation(new Transformation(new Orientation(), new Vector3(0, 0.8, 0)));
		
		addNode(horo);
		addNode(building);
		addNode(dodec);
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
