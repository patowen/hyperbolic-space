package net.patowen.hyperbolicspace.model;

import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.math.Transformation;
import net.patowen.hyperbolicspace.math.Vector3;
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
public class Sphere implements SceneNodeType
{
	private Controller c;
	private SceneNodeImpl sceneNode;
	
	private int textureStepsPerWrap = 10;
	private int numSteps = 20;
	private double radius = 0.02;
	
	/**
	 * Initializes the {@code Horosphere} mesh.
	 * @param c
	 */
	public Sphere(Controller c)
	{
		this.c = c;
		Model model = new Model();
		int slices = numSteps*2, stacks = numSteps;
		VertexGrid grid = new VertexGrid(slices, stacks); //slices, stacks
		
		for (int slice=0; slice<=slices; slice++)
		{
			for (int stack=0; stack<=stacks; stack++)
			{
				double phi = stack*Math.PI/stacks;
				double theta = slice*Math.PI*2/slices;
				Vector3 unitVector = new Vector3(
						Math.cos(theta)*Math.sin(phi),
						-Math.sin(theta)*Math.sin(phi),
						Math.cos(phi));
				grid.setPosition(slice, stack, unitVector.times(radius));
				grid.setNormal(slice, stack, unitVector);
			}
		}
		grid.setTexCoords(0, 0, 1, 1, textureStepsPerWrap, textureStepsPerWrap, 0, 0);
		grid.addToModel(model);
		sceneNode = new SceneNodeImpl(this.c);
		sceneNode.setModel(model);
	}
	
	public void renderInit(GL3 gl)
	{
		sceneNode.renderInit(gl);
		sceneNode.setTexture(c.getTextureBank().stone);
	}
	
	public void render(GL3 gl, Transformation t)
	{
		sceneNode.setTransformation(t);
		sceneNode.render(gl);
	}
}
