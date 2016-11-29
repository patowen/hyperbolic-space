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
	
	@SuppressWarnings("unused")
	private double time;
	
	private static class Flag {
		private Controller c;
		@SuppressWarnings("unused")
		private World w;
		
		private Vector31 pos;
		private Vector31 dir;
		
		private Transform trans;
		private SceneNode cone;
		
		public Flag(Controller c, World w, Vector31 pos, Vector31 dir) {
			this.c = c;
			this.w = w;
			this.pos = pos;
			this.dir = dir;
			init();
		}
		
		@SuppressWarnings("unused")
		public static Flag fromBeacon(Controller c, World w, Vector31 position, Vector31 beacon) {
			Vector31 pos = new Vector31(position);
			pos.normalizeAsPoint();
			Vector31 dir = beacon.plusMultiple(pos, -1);
			dir = dir.plusMultiple(pos, -dir.dot(pos));
			dir.normalizeAsDirection();
			
			return new Flag(c, w, pos, dir);
		}
		
		private void init() {
			trans = Transform.identity();
			trans.x = dir;
			trans.w = pos;
			if (!trans.normalize()) {
				// Random-ish rotation that should hopefully work
				trans = Transform.rotation(new Vector3(Math.sqrt(1.0/3), Math.sqrt(1.0/3), Math.sqrt(1.0/3)), 1);
				trans.x = dir;
				trans.w = pos;
				if (!trans.normalize()) {
					System.out.println("Failed flag");
				}
			}
			
			cone = new SceneNode(c.cone);
			cone.setTransformation(trans);
			//w.addNode(cone);
		}
		
		@SuppressWarnings("unused")
		public void setVisible(boolean visible) {
			if (!visible)
				cone.setTransformation(Transform.identity());
			else
				cone.setTransformation(trans);
		}
	}
	
	public FlagField(Controller c, World w) {
		this.c = c;
		this.w = w;
		time = 0;
		
		flags = new ArrayList<>();
		ArrayList<Vector31> pos = new ArrayList<>();
		
		for (int i=0; i<3; i++) {
			Vector3 randomVector;
			do {
				randomVector = new Vector3(Math.random()*2-1, Math.random()*2-1, Math.random()*2-1);
			} while (randomVector.magnitude() > 0.5);
			pos.add(Vector31.makePoincare(randomVector));
		}
		Vector31 direction = Vector31.makeOrthogonal(pos.get(0), pos.get(1), pos.get(2));
		direction.normalizeAsDirection();
		
		for (int i=0; i<3; i++) {
			flags.add(new Flag(this.c, this.w, pos.get(i), direction));
		}
	}
	
	public void step(double dt) {
		/*time += dt;
		double t = (time - Math.floor(time)) * 3;
		for (int i=0; i<3; i++) {
			flags.get(i).setVisible(t >= i && t < i+2 || t < i-1);
		}*/
	}
}
