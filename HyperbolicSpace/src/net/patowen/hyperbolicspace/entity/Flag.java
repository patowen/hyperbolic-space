package net.patowen.hyperbolicspace.entity;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.World;
import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.rendering.SceneNode;

public class Flag {
	private Controller c;
	private World w;
	
	private Transform pos;
	
	private SceneNode cone;
	
	public Flag(Controller c, World w)
	{
		this.c = c;
		this.w = w;
		
		pos = Transform.translation(Vector31.makePoincare(new Vector3(0, 0, 0)));
		
		cone = new SceneNode(c.cone);
		cone.setTransformation(pos);
		w.addNode(cone);
	}
	
	public void step(double dt)
	{
		pos = Transform.rotation(new Vector3(0, 1, 0), dt).transform(pos);
		cone.setTransformation(pos);
	}
}
