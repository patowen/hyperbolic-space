package net.patowen.hyperbolicspace.model;

import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.modelhelper.Polygon;
import net.patowen.hyperbolicspace.modelhelper.VertexHelper;
import net.patowen.hyperbolicspace.rendering.Model;
import net.patowen.hyperbolicspace.rendering.SceneNodeImpl;
import net.patowen.hyperbolicspace.rendering.SceneNodeType;

/**
 * Represents a regular dodecahedron with right angles on every vertex. Such a shape
 * can be tiled throughout hyperbolic space, although this has not yet been implemented.
 * @author Patrick Owen
 */
public class Dodecahedron implements SceneNodeType
{
	private Controller c;
	private SceneNodeImpl sceneNode;
	
	/**
	 * Initializes the {@code Dodecahedron} mesh.
	 * @param c
	 */
	public Dodecahedron(Controller c)
	{
		this.c = c;
		Model model = new Model();
		
		double s = 0.31546169558954995; //(1/6)*(2-3r2+r10)*(phi^(3/2))? r means square root
		double p = (1+Math.sqrt(5))/2;
		double q = s/p; p = s*p;
		
		double[][][] vertices = new double[][][] {
			{{ 0,  q,  p}, { s,  s,  s}, { q,  p,  0}, {-q,  p,  0}, {-s,  s,  s}},
			{{ 0,  q, -p}, {-s,  s, -s}, {-q,  p,  0}, { q,  p,  0}, { s,  s, -s}},
			{{ 0, -q,  p}, {-s, -s,  s}, {-q, -p,  0}, { q, -p,  0}, { s, -s,  s}},
			{{ 0, -q, -p}, { s, -s, -s}, { q, -p,  0}, {-q, -p,  0}, {-s, -s, -s}},
			
			{{ p,  0,  q}, { s,  s,  s}, { 0,  q,  p}, { 0, -q,  p}, { s, -s,  s}},
			{{-p,  0,  q}, {-s, -s,  s}, { 0, -q,  p}, { 0,  q,  p}, {-s,  s,  s}},
			{{ p,  0, -q}, { s, -s, -s}, { 0, -q, -p}, { 0,  q, -p}, { s,  s, -s}},
			{{-p,  0, -q}, {-s,  s, -s}, { 0,  q, -p}, { 0, -q, -p}, {-s, -s, -s}},
			
			{{ q,  p,  0}, { s,  s,  s}, { p,  0,  q}, { p,  0, -q}, { s,  s, -s}},
			{{ q, -p,  0}, { s, -s, -s}, { p,  0, -q}, { p,  0,  q}, { s, -s,  s}},
			{{-q,  p,  0}, {-s,  s, -s}, {-p,  0, -q}, {-p,  0,  q}, {-s,  s,  s}},
			{{-q, -p,  0}, {-s, -s,  s}, {-p,  0,  q}, {-p,  0, -q}, {-s, -s, -s}}
		};
		
		for (int i=0; i<vertices.length; i++) {
			Polygon face = new Polygon(VertexHelper.arrayToVector3(vertices[i]));
			face.setTexCoordsRegular(new Vector2(0.5, 0.5), 0.5, 0.5, 0);
			face.addToModel(model);
		}
		
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
