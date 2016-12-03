package net.patowen.hyperbolicspace.entity;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.World;
import net.patowen.hyperbolicspace.math.MathHelper;
import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.rendering.SceneNode;

public class PrismTree {
	private Controller c;
	private World w;
	
	public PrismTree(Controller c, World w) {
		this.c = c;
		this.w = w;
		
		int iterations = 4;
		populatePrisms(iterations, Transform.identity());
		populatePrisms(iterations, Transform.rotation(new Vector3(0, 0, 1), Math.PI/2));
		populatePrisms(iterations, Transform.rotation(new Vector3(0, 0, -1), Math.PI/2));
		populatePrisms(iterations, Transform.rotation(new Vector3(0, 1, 0), Math.PI/2));
		populatePrisms(iterations, Transform.rotation(new Vector3(0, -1, 0), Math.PI/2));
		populatePrisms(iterations, Transform.rotation(new Vector3(0, 0, 1), Math.PI));
	}
	
	private void populatePrisms(int iterations, Transform t) {
		SceneNode prism = new SceneNode(c.prism);
		prism.setTransformation(t);
		w.addNode(prism);
		
		if (iterations > 0) {
			double length = MathHelper.acosh(3);
			double sinhLength = Math.sinh(length);
			double coshLength = Math.cosh(length);
			Transform tNew = t.transform(Transform.translation(new Vector31(sinhLength, 0, 0, coshLength)));
			
			populatePrisms(iterations-1, tNew);
			populatePrisms(iterations-1, tNew.transform(Transform.rotation(new Vector3(0, 0, 1), Math.PI/2)));
			populatePrisms(iterations-1, tNew.transform(Transform.rotation(new Vector3(0, 0, -1), Math.PI/2)));
			populatePrisms(iterations-1, tNew.transform(Transform.rotation(new Vector3(0, 1, 0), Math.PI/2)));
			populatePrisms(iterations-1, tNew.transform(Transform.rotation(new Vector3(0, -1, 0), Math.PI/2)));
		}
	}
}
