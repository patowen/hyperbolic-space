package net.patowen.hyperbolicspace;

import javax.media.opengl.GL3;

import com.jogamp.opengl.math.FloatUtil;


public class World
{
	private InputHandler input;
	
	private Vector3 pos;
	private Orientation o;
	
	private SceneNode building1;
	private SceneNode building2;
	
	public World(InputHandler inputHandler)
	{
		input = inputHandler;
		
		pos = new Vector3(0, 0, 0);
		o = new Orientation(new Vector3(0, 0, -1), new Vector3(1, 0, 0), new Vector3(0, 1, 0));
		
		building1 = new Building();
		building1.setPosition(new Vector3(0.3, 0, 0));
		building2 = new Building();
	}
	
	public void handleTurning()
	{		
		input.readMouse();
		o.rotate(o.z, -input.getMouseX()*45);
		o.rotate(o.y, -input.getMouseY()*45);
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
	}
	
	public void translateView(Vector3 v)
	{
		o.hyperTranslate(pos, v);
		pos = pos.hyperTranslate(v);
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
		building1.reposition(pos);
		building2.reposition(pos);
		
		handleTurning();
		handleMovement(dt);
		
		input.updatePressed();
	}
	
	public void renderInit(GL3 gl, MatrixHandler mh)
	{
		building1.renderInit(gl, mh);
		building2.renderInit(gl, mh);
	}
	
	public void render(MatrixHandler mh, GL3 gl)
	{
		float[] result = new float[16], tmp = new float[16];
		FloatUtil.makeLookAt(result, 0, new float[]{0, 0, 0}, 0, new float[] {(float)o.x.x, (float)o.x.y, (float)o.x.z},
				0, new float[] {(float)o.z.x, (float)o.z.y, (float)o.z.z}, 0, tmp);
		FloatUtil.multMatrix(mh.transformArray(), result);
		
		mh.update(gl);
		
		building1.render(gl);
		building2.render(gl);
	}
}
