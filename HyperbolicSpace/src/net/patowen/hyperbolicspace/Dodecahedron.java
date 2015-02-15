package net.patowen.hyperbolicspace;

import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;

public class Dodecahedron implements SceneNode
{
	SceneNodeImpl sceneNode;
	
	public Dodecahedron()
	{
		ArrayList<Vertex> v = new ArrayList<Vertex>();
		
		double s = 0.3; //0.3
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
		
		sceneNode = new SceneNodeImpl();
		sceneNode.setVertices(v);
		IntBuffer elementBuffer = Buffers.newDirectIntBuffer(60);
		for (int i=0; i<elementBuffer.capacity(); i++)
		{
			elementBuffer.put(i, i);
		}
		sceneNode.setElementBuffer(elementBuffer);
	}
	
	public void reposition(Vector3 v)
	{
		sceneNode.reposition(v);
	}
	
	public void renderInit(GL3 gl)
	{
		sceneNode.renderInit(gl);
	}
	
	public void render(GL3 gl)
	{
		sceneNode.render(gl);
	}
}
