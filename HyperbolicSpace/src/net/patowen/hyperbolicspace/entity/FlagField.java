package net.patowen.hyperbolicspace.entity;

import java.util.ArrayList;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.World;
import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.rendering.SceneNode;

public class FlagField {
	private Controller c;
	private World w;
	
	private ArrayList<Flag> flags;
	
	private class Flag
	{
		private Transform pos;
		private SceneNode cone;
		
		public Flag(double angle)
		{
			pos = Transform.translation(Vector31.makePoincare(new Vector3(0, 0, 0)));
			pos = Transform.rotation(new Vector3(0, 1, 0), angle).transform(pos);
			
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
	
	public FlagField(Controller c, World w)
	{
		this.c = c;
		this.w = w;
		
		flags = new ArrayList<>();
		for (int i=0; i<4; i++)
		{
			flags.add(new Flag((double)i/4*Math.PI*2));
		}
	}
	
	public void step(double dt)
	{
		for (Flag flag : flags)
		{
			flag.step(dt);
		}
	}
}
