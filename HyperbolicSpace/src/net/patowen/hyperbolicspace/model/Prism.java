package net.patowen.hyperbolicspace.model;

import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.modelhelper.Polygon;
import net.patowen.hyperbolicspace.modelhelper.VertexGrid;
import net.patowen.hyperbolicspace.rendering.Model;
import net.patowen.hyperbolicspace.rendering.SceneNodeImpl;
import net.patowen.hyperbolicspace.rendering.SceneNodeType;

/**
 * Represents a tall structure with a pentagonal base. Ideally, this structure can be tessellated
 * in a {5, 4} tessellation on top of a plane to make the hyperbolic equivalent of a city, but
 * this has not yet been implemented.
 * @author Patrick Owen
 */
public class Prism implements SceneNodeType {
	private Controller c;
	private SceneNodeImpl sceneNode;
	
	private int baseSides = 8;
	private double baseRadius = 0.1;
	
	private double totalHeight = 10;
	private int heightStepsPerWrap = 6;
	private int numWraps = 7;
	
	/**
	 * Initializes the {@code Building} mesh.
	 * @param c
	 */
	public Prism(Controller c) {
		this.c = c;
		Model model = new Model();
		
		double radiusC = Math.cosh(baseRadius);
		double radiusS = Math.sinh(baseRadius);
		double heightStep = totalHeight/heightStepsPerWrap/numWraps;
		Transform dzTransform = Transform.translation(new Vector31(Math.sinh(heightStep), 0, 0, Math.cosh(heightStep)));
		
		double initialHeightStep = -totalHeight/2.0;
		Transform initialTransform = Transform.translation(new Vector31(Math.sinh(initialHeightStep), 0, 0, Math.cosh(initialHeightStep)));
		
		Vector31 center = initialTransform.transform(new Vector31(0, 0, 0, 1));
		Vector31[] corners = new Vector31[baseSides];
		Vector31[] sideNormals = new Vector31[baseSides];
		for (int i=0; i<baseSides; i++) {
			double theta = (i-0.5)*Math.PI*2/baseSides;
			double normalTheta = (i)*Math.PI*2/baseSides;
			corners[i] = initialTransform.transform(new Vector31(0, radiusS*Math.cos(theta), radiusS*Math.sin(theta), radiusC));
			sideNormals[i] = initialTransform.transform(new Vector31(0, Math.cos(normalTheta), Math.sin(normalTheta), 0));
		}
		
		//Base
		Polygon base = new Polygon(baseSides);
		base.setCenterPosition(center);
		for (int j=0; j<baseSides; j++) {
			base.setPosition(j, corners[baseSides-j-1]);
		}
		base.setTexCoordsRegular(new Vector2(0.75, 0.25), 0.25, 0.25, 0);
		base.addToModel(model);
		
		//Sides
		VertexGrid[] grids = new VertexGrid[baseSides];
		int numSteps = numWraps*heightStepsPerWrap;
		for (int j=0; j<baseSides; j++) {
			grids[j] = new VertexGrid(1, numSteps);
			grids[j].setTexCoords(0, 0, 0.5, 1, 1, heightStepsPerWrap, 0, 0);
		}
		for (int i=0; i<=numSteps; i++) {
			for (int j=0; j<baseSides; j++) {
				int j1 = j+1; if (j1 == baseSides) j1 = 0;
				grids[j].setPosition(0, i, corners[j]);
				grids[j].setPosition(1, i, corners[j1]);
				grids[j].setNormal(0, i, sideNormals[j]);
				grids[j].setNormal(1, i, sideNormals[j]);
			}
			
			if (i != numSteps) {
				for (int j=0; j<baseSides; j++) {
					corners[j] = dzTransform.transform(corners[j]);
					sideNormals[j] = dzTransform.transform(sideNormals[j]);
				}
				center = dzTransform.transform(center);
			}
		}
		for (int j=0; j<baseSides; j++) {
			grids[j].addToModel(model);
		}
		
		//Top
		Polygon top = new Polygon(baseSides);
		top.setCenterPosition(center);
		for (int j=0; j<baseSides; j++) {
			top.setPosition(j, corners[j]);
		}
		top.setTexCoordsRegular(new Vector2(0.75, 0.75), 0.25, 0.25, 0);
		top.addToModel(model);
		
		sceneNode = new SceneNodeImpl(this.c);
		sceneNode.setModel(model);
	}
	
	public void renderInit(GL3 gl) {
		sceneNode.renderInit(gl);
		sceneNode.setTexture(c.getTextureBank().blank);
	}
	
	public void render(GL3 gl, Transform t) {
		sceneNode.setTransform(t);
		sceneNode.render(gl);
	}
}
