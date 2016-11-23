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
		private Vector31 pos;
		private Vector31 dir;
		
		private Transform trans;
		private SceneNode cone;
		
		public Flag(Vector31 position, Vector31 beacon)
		{
			pos = new Vector31(position);
			pos.normalizeAsPoint();
			dir = beacon.plusMultiple(pos, -1);
			dir = dir.plusMultiple(pos, -dir.dot(pos));
			dir.normalizeAsDirection();
			
			init();
		}
		
		private void init()
		{
			trans = Transform.identity();
			trans.x = dir;
			trans.w = pos;
			if (!trans.normalize())
			{
				// Random-ish rotation that should hopefully work
				trans = Transform.rotation(new Vector3(Math.sqrt(1.0/3), Math.sqrt(1.0/3), Math.sqrt(1.0/3)), 1);
				trans.x = dir;
				trans.w = pos;
				if (!trans.normalize())
				{
					System.out.println("Failed flag");
				}
			}
			
			cone = new SceneNode(c.cone);
			cone.setTransformation(trans);
			w.addNode(cone);
		}
	}
	
	public FlagField(Controller c, World w)
	{
		this.c = c;
		this.w = w;
		
		flags = new ArrayList<>();
		Vector31 center = new Vector31(0, 0, 0, -1);
		
		for (int i=0; i<1000; i++)
		{
			Vector3 randomVector;
			do {
				randomVector = new Vector3(Math.random()*2-1, Math.random()*2-1, Math.random()*2-1);
			} while (randomVector.magnitude() > 0.8);
			Vector31 pos = Vector31.makePoincare(randomVector);
			flags.add(new Flag(pos, center));
		}
		//flags.add(new Flag(new Vector31(0, 0, 0, 1), new Vector31(1, 1, 1, 2)));
		//flags.add(new Flag(new Vector31(1, 1, 1, 2), new Vector31(0, 0, 0, 1)));
	}
	
	public void step(double dt)
	{
		/*for (Flag flag : flags)
		{
			flag.step(dt);
		}*/
	}
}
