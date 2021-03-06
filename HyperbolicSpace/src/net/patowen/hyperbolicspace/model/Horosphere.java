package net.patowen.hyperbolicspace.model;

import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.modelhelper.VertexGrid;
import net.patowen.hyperbolicspace.rendering.Model;
import net.patowen.hyperbolicspace.rendering.SceneNodeImpl;
import net.patowen.hyperbolicspace.rendering.SceneNodeType;

/**
 * Represents a section of a horosphere. A horosphere is the limit shape of continually expanding a sphere
 * in hyperbolic space. Its surface has the equivalent curvature of a Euclidean plane, and the section of the
 * horosphere represented is equivalent to a large square in Euclidean geometry.
 * @author Patrick Owen
 */
public class Horosphere implements SceneNodeType {
	private Controller c;
	private SceneNodeImpl sceneNode;
	
	private int textureStepsPerWrap = 4;
	private int numSteps = 200; //200
	private double size = 10; //10
	
	/**
	 * Initializes the {@code Horosphere} mesh.
	 * @param c
	 */
	public Horosphere(Controller c) {
		this.c = c;
		Model model = new Model();
		VertexGrid grid = new VertexGrid(numSteps, numSteps);
		
		for (int i=0; i<=numSteps; i++) {
			for (int j=0; j<=numSteps; j++) {
				double xx = ((double)i/numSteps-0.5)*size;
				double yy = ((double)j/numSteps-0.5)*size;
				Transform t = Transform.horoRotation(xx, yy);
				grid.setPosition(i, j, t.transform(new Vector31(0, 0, 0, 1)));
				grid.setNormal(i, j, t.transform(new Vector31(0, 0, 1, 0)));
			}
		}
		grid.setTexCoords(0, 0, 1, 1, textureStepsPerWrap, textureStepsPerWrap, 0, 0);
		grid.addToModel(model);
		sceneNode = new SceneNodeImpl(this.c);
		sceneNode.setModel(model);
	}
	
	public void renderInit(GL3 gl) {
		sceneNode.renderInit(gl);
		sceneNode.setTexture(c.getTextureBank().tile);
	}
	
	public void render(GL3 gl, Transform t) {
		sceneNode.setTransform(t);
		sceneNode.render(gl);
	}
}
