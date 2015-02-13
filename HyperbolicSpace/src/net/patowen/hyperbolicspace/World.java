package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.math.FloatUtil;


public class World
{
	private InputHandler input;
	
	private Vector3 pos;
	private Orientation o;
	
	private ArrayList<Vertex> v;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	
	private IntBuffer positionBufferObject;
	
	public World(InputHandler inputHandler)
	{
		input = inputHandler;
		
//		xPos = -0.9; yPos = 0; zPos = 0;
		pos = new Vector3(0, 0, 0);
		o = new Orientation(new Vector3(0, 0, -1), new Vector3(1, 0, 0), new Vector3(0, 1, 0));
		
		v = new ArrayList<Vertex>();
		makeHorosphere();
//		makeDodecahedron();
//		makeCylinder();
		
		vertexBuffer = Buffers.newDirectFloatBuffer(3*v.size());
		normalBuffer = Buffers.newDirectFloatBuffer(3*v.size());
		
		System.out.println(v.size());
	}
	
	public void makeHorosphere()
	{
		int res = 100, focus = 10;
		double xBase = 0, yBase = 0, zBase = 0;
		double[][] x, y, z;
		x = new double[res][res];
		y = new double[res][res];
		z = new double[res][res];
		double[] point = new double[3];
		point[0] = 0; point[1] = 0; point[2] = 0;
		
		for (int i=0; i<res/2; i++)
		{
			point[0] = -point[0];
			invert(point, -focus, 0, -1, focus);
			point[1] = -point[1];
			invert(point, 0, -focus, -1, focus);
		}
		
		xBase = point[0]; yBase = point[1]; zBase = point[2];
		
		for (int i=0; i<res; i++)
		{			
			for (int j=0; j<res; j++)
			{	
				point[0] = xBase; point[1] = yBase; point[2] = zBase;
				
				for (int ii=0; ii<i; ii++)
				{
					point[0] = -point[0];
					invert(point, focus, 0, -1, focus);
				}
				
				for (int jj=0; jj<j; jj++)
				{
					point[1] = -point[1];
					invert(point, 0, focus, -1, focus);
				}
				
				x[i][j] = point[0]; y[i][j] = point[1]; z[i][j] = point[2];
			}
		}
		
		for (int i=0; i<res; i++)
		{
			for (int j=0; j<res; j++)
			{
				if (i < res-1)
				{
					v.add(new Vertex(x[i][j], y[i][j], z[i][j]));
					v.add(new Vertex(x[i+1][j], y[i+1][j], z[i+1][j]));
				}
				
				if (j < res-1)
				{
					v.add(new Vertex(x[i][j], y[i][j], z[i][j]));
					v.add(new Vertex(x[i][j+1], y[i][j+1], z[i][j+1]));
				}
			}
		}
	}
	
	private void invert(double[] point, double cx, double cy, double cz, double r)
	{
		double x = point[0]-cx, y = point[1]-cy, z = point[2]-cz;
		double s = x*x + y*y + z*z;
		
		x *= r*r/s; y *= r*r/s; z *= r*r/s;
		point[0] = x+cx; point[1] = y+cy; point[2] = z+cz;
	}
	
	public void makeCylinder()
	{		
		for (int i=0; i<20; i++)
		{
			double theta = Math.PI*2*i/20;
			makeLine(new Vertex(0, 0.5*Math.cos(theta), 0.5*Math.sin(theta)));
		}
	}
	
	public void makeLine(Vertex ver)
	{
		Vector3 back = new Vector3(-0.05, 0, 0);
		Vector3 forth = back.times(-1);
		
		for (int i=0; i<80; i++)
		{
			ver.translate(back);
			ver = new Vertex(ver.getVt(), ver.getNt());
		}
		
		v.add(ver);
		
		for (int i=0; i<160; i++)
		{
			ver.translate(forth);
			ver = new Vertex(ver.getVt(), ver.getNt());
			v.add(ver);
			
			if (i != 159) v.add(ver);
		}
	}
	
	public void makeDodecahedron()
	{
		//v = new Vertex[60]
		
		double s = 0.3; //0.3
		//s = Math.sqrt(1.0/3.0)-0.00005;
		double p = (1+Math.sqrt(5))/2;
		double q = s/p; p = s*p;
		
		v.add(new Vertex(s, s, s)); v.add(new Vertex(0, q, p));
		v.add(new Vertex(0, q, p)); v.add(new Vertex(-s, s, s));
		v.add(new Vertex(-s, s, s)); v.add(new Vertex(-q, p, 0));
		v.add(new Vertex(-q, p, 0)); v.add(new Vertex(q, p, 0));
		v.add(new Vertex(q, p, 0)); v.add(new Vertex(s, s, s));
		
		v.add(new Vertex(s, s, s)); v.add(new Vertex(p, 0, q));
		v.add(new Vertex(p, 0, q)); v.add(new Vertex(p, 0, -q));
		v.add(new Vertex(p, 0, -q)); v.add(new Vertex(s, s, -s));
		v.add(new Vertex(s, s, -s)); v.add(new Vertex(q, p, 0));
		
		v.add(new Vertex(s, s, -s)); v.add(new Vertex(0, q, -p));
		v.add(new Vertex(0, q, -p)); v.add(new Vertex(-s, s, -s));
		v.add(new Vertex(-s, s, -s)); v.add(new Vertex(-q, p, 0));
		
		v.add(new Vertex(p, 0, q)); v.add(new Vertex(s, -s, s));
		v.add(new Vertex(s, -s, s)); v.add(new Vertex(0, -q, p));
		v.add(new Vertex(0, -q, p)); v.add(new Vertex(0, q, p));
		
		v.add(new Vertex(0, -q, p)); v.add(new Vertex(-s, -s, s));
		v.add(new Vertex(-s, -s, s)); v.add(new Vertex(-p, 0, q));
		v.add(new Vertex(-p, 0, q)); v.add(new Vertex(-s, s, s));
		
		v.add(new Vertex(-p, 0, q)); v.add(new Vertex(-p, 0, -q));
		v.add(new Vertex(-p, 0, -q)); v.add(new Vertex(-s, s, -s));
		
		v.add(new Vertex(-s, -s, s)); v.add(new Vertex(-q, -p, 0));
		v.add(new Vertex(-q, -p, 0)); v.add(new Vertex(-s, -s, -s));
		v.add(new Vertex(-s, -s, -s)); v.add(new Vertex(-p, 0, -q));
		
		v.add(new Vertex(s, -s, s)); v.add(new Vertex(q, -p, 0));
		v.add(new Vertex(q, -p, 0)); v.add(new Vertex(-q, -p, 0));
		
		v.add(new Vertex(0, q, -p)); v.add(new Vertex(0, -q, -p));
		v.add(new Vertex(0, -q, -p)); v.add(new Vertex(s, -s, -s));
		v.add(new Vertex(s, -s, -s)); v.add(new Vertex(p, 0, -q));
		
		v.add(new Vertex(q, -p, 0)); v.add(new Vertex(s, -s, -s));
		
		v.add(new Vertex(-s, -s, -s)); v.add(new Vertex(0, -q, -p));
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
		for (int i=0; i<v.size(); i++)
			v.get(i).translate(pos);
		
		handleTurning();
		handleMovement(dt);
		
		normalize();
		
		input.updatePressed();
	}
	
	public void renderInit(GL3 gl)
	{
		positionBufferObject = Buffers.newDirectIntBuffer(1);
		gl.glGenBuffers(1, positionBufferObject);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, positionBufferObject.get(0));
		gl.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 0, 0);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
	}
	
	public void render(MatrixHandler mh, GL3 gl)
	{
		float[] result = new float[16], tmp = new float[16];
		FloatUtil.makeLookAt(result, 0, new float[]{0, 0, 0}, 0, new float[] {(float)o.x.x, (float)o.x.y, (float)o.x.z},
				0, new float[] {(float)o.z.x, (float)o.z.y, (float)o.z.z}, 0, tmp);
		FloatUtil.multMatrix(mh.transformArray(), result);
		
		mh.update(gl);
		
		for (int i=0; i<v.size(); i++)
		{
			v.get(i).use(vertexBuffer, normalBuffer);
		}
		vertexBuffer.rewind();
		normalBuffer.rewind();
		
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, positionBufferObject.get(0));
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertexBuffer.capacity()*4, vertexBuffer, GL3.GL_DYNAMIC_DRAW);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		
		gl.glEnableVertexAttribArray(0);
		gl.glDrawArrays(GL3.GL_LINES, 0, v.size());
		gl.glDisableVertexAttribArray(0);
	}
}
