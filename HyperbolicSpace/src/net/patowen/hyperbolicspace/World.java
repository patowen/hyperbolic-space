package net.patowen.hyperbolicspace;

import javax.media.opengl.GL3;

import com.jogamp.opengl.math.FloatUtil;


public class World
{
	private InputHandler input;
	
	private Vector3 pos;
	private Orientation o;
	
	private SceneNode dodeca;
	
	public World(InputHandler inputHandler)
	{
		input = inputHandler;
		
		pos = new Vector3(0, 0, 0);
		o = new Orientation(new Vector3(0, 0, -1), new Vector3(1, 0, 0), new Vector3(0, 1, 0));
		
		dodeca = new Dodecahedron();
		dodeca.reposition(pos);
	}
	
	public void handleTurning()
	{		
		input.readMouse();
		o.rotate(o.z, -input.getMouseX()*45);
		o.rotate(o.y, -input.getMouseY()*45);
	}
	
	public void normalize()
	{
		o.normalize();
	}
	
	public void translate(double x, double y, double z)
	{
		translate(new Vector3(x, y, z));
	}
	
	public void translate(Vector3 v)
	{
		Vector3 vv = pos;
		pos = v.times(-1);
		translateView(vv);
		
		normalize();
	}
	
	/**
	 * Translates the vertex as simply as possible so that the given coordinates are translated
	 * to the origin.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void translateView(Vector3 v)
	{
		//Function determined through circle inversion followed by reflection
		double denom = v.dot(v)*pos.dot(pos) + 2*v.dot(pos) + 1;
		double vFactor = 1 + pos.dot(pos) + 2*v.dot(pos);
		double vPosFactor = 1 - v.dot(v);
		
		//Derivative of xto, yto, and zto
		double denomTo = 2*(v.dot(v)*pos.dot(o.x) + v.dot(o.x));
		double vFactorTo = 2*(pos.dot(o.x) + v.dot(o.x));
		
		//Derivative of xup, yup, and zup
		double denomUp = 2*(v.dot(v)*pos.dot(o.z) + v.dot(o.z));
		double vFactorUp = 2*(pos.dot(o.z) + v.dot(o.z));
		
		//denom*(o.x*vPosFactor + v*vFactorTo) - denomTo*(pos*vPosFactor + v*vFactor)
		o.x = (o.x.times(vPosFactor).plus(v.times(vFactorTo)).times(denom))
				.minus(pos.times(vPosFactor).plus(v.times(vFactor)).times(denomTo));
		o.z = (o.z.times(vPosFactor).plus(v.times(vFactorUp)).times(denom))
				.minus(pos.times(vPosFactor).plus(v.times(vFactor)).times(denomUp));
		o.normalize();
		
		pos = (pos.times(vPosFactor).plus(v.times(vFactor))).times(1/denom);
	}
	
	public void handleMovement(double dt)
	{
		double s = 0.01; //speed
		if (input.getKey(InputHandler.SLOW))
			s = 0.001;
		
		if (input.getMouseButton(InputHandler.FORWARDS))
			translate(o.x.times(s));
		if (input.getMouseButton(InputHandler.BACKWARDS))
			translate(o.x.times(-s));
		if (input.getKey(InputHandler.UP))
			translate(o.z.times(s));
		if (input.getKey(InputHandler.DOWN))
			translate(o.z.times(-s));
		if (input.getKey(InputHandler.RIGHT))
			translate(o.y.times(s));
		if (input.getKey(InputHandler.LEFT))
			translate(o.y.times(-s));
	}
	
	public void step(double dt)
	{		
		dodeca.reposition(pos);
		
		handleTurning();
		handleMovement(dt);
		
		normalize();
		
		input.updatePressed();
	}
	
	public void renderInit(GL3 gl)
	{
		dodeca.renderInit(gl);
	}
	
	public void render(MatrixHandler mh, GL3 gl)
	{
		float[] result = new float[16], tmp = new float[16];
		FloatUtil.makeLookAt(result, 0, new float[]{0, 0, 0}, 0, new float[] {(float)o.x.x, (float)o.x.y, (float)o.x.z},
				0, new float[] {(float)o.z.x, (float)o.z.y, (float)o.z.z}, 0, tmp);
		FloatUtil.multMatrix(mh.transformArray(), result);
		
		mh.update(gl);
		
		dodeca.render(gl);
	}
}
