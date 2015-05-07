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
		
		for (int i=-3; i<=3; i++)
		{
			Transformation t1;
			{
				Vector3 v = (new Vector3()).horoRotate(new Vector3(0,0,-1), new Vector3(1,0,0), 5*i);
				Vector3 yrot = new Vector3(0, 1, 0);
				Vector3 zrot = v.minus(new Vector3(0, 0, -0.5));
				zrot.normalize();
				Vector3 xrot = yrot.cross(zrot);
				t1 = new Transformation(new Orientation(xrot, yrot, zrot), v);
			}
			for (int j=-3; j<=3; j++)
			{
				SceneNode horo = new SceneNode(c.horosphere);
				
				Transformation t2;
				{
					Vector3 v = (new Vector3()).horoRotate(new Vector3(0,0,-1), new Vector3(0,1,0), 5*j);
					Vector3 xrot = new Vector3(1, 0, 0);
					Vector3 zrot = v.minus(new Vector3(0, 0, -0.5));
					zrot.normalize();
					Vector3 yrot = zrot.cross(xrot);
					t2 = new Transformation(new Orientation(xrot, yrot, zrot), v);
				}
				
				horo.setTransformation(t1.composeBefore(t2));
				addNode(horo);
			}
		}
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
