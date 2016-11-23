package net.patowen.hyperbolicspace.model;

import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.modelhelper.Polygon;
import net.patowen.hyperbolicspace.rendering.Model;
import net.patowen.hyperbolicspace.rendering.SceneNodeImpl;
import net.patowen.hyperbolicspace.rendering.SceneNodeType;

/**
 * Represents a tall structure with a pentagonal base. Ideally, this structure can be tessellated
 * in a {5, 4} tessellation on top of a plane to make the hyperbolic equivalent of a city, but
 * this has not yet been implemented.
 * @author Patrick Owen
 */
public class Cone implements SceneNodeType
{
	private Controller c;
	private SceneNodeImpl sceneNode;
	
	private int baseSides = 8;
	private double baseRadius = 0.1;
	
	private double height = 0.2;
	
	/**
	 * Initializes the {@code Building} mesh.
	 * @param c
	 */
	public Cone(Controller c)
	{
		this.c = c;
		Model model = new Model();
		
		double dx = Math.tanh(baseRadius);
		double dz = Math.tanh(height);
		Transform dzTransform = Transform.translation(Vector31.makePoincare(new Vector3(dz, 0, 0)));
		
		Vector31 center = new Vector31(0, 0, 0, 1);
		Vector31[] corners = new Vector31[baseSides];
		for (int i=0; i<baseSides; i++)
		{
			double theta = i*Math.PI*2/baseSides;
			corners[i] = new Vector31(0, dx*Math.cos(theta), dx*Math.sin(theta), dx+1);
		}
		
		//Base
		Polygon base = new Polygon(baseSides);
		base.setCenterPosition(center);
		for (int j=0; j<baseSides; j++)
		{
			base.setPosition(j, corners[baseSides-j-1]);
		}
		base.setTexCoordsRegular(new Vector2(0.75, 0.25), 0.25, 0.25, 0);
		base.addToModel(model);
		
		//Sides
		Vector31 tip = dzTransform.transform(center);
		Polygon apex = new Polygon(baseSides);
		apex.setCenterPosition(tip);
		for (int j=0; j<baseSides; j++)
		{
			apex.setPosition(j, corners[baseSides-j-1]);
		}
		apex.setTexCoordsRegular(new Vector2(0.75, 0.25), 0.25, 0.25, 0);
		apex.addToModel(model);
		
		sceneNode = new SceneNodeImpl(this.c);
		sceneNode.setModel(model);
	}
	
	public void renderInit(GL3 gl)
	{
		sceneNode.renderInit(gl);
		sceneNode.setTexture(c.getTextureBank().clouds);
	}
	
	public void render(GL3 gl, Transform t)
	{
		sceneNode.setTransform(t);
		sceneNode.render(gl);
	}
}
