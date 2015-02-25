package net.patowen.hyperbolicspace;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL3;
import javax.media.opengl.GLException;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Dodecahedron implements SceneNode
{
	SceneNodeImpl sceneNode;
	
	public Dodecahedron()
	{
		ArrayList<Vertex> v = new ArrayList<Vertex>();
		
		double s = 0.3; //0.3
		double p = (1+Math.sqrt(5))/2;
		double q = s/p; p = s*p;
		
		v.add(new Vertex( 0,  q,  p,  0,  p,  q)); v.add(new Vertex( s,  s,  s,  0,  p,  q)); v.add(new Vertex( q,  p,  0,  0,  p,  q)); v.add(new Vertex(-q,  p,  0,  0,  p,  q)); v.add(new Vertex(-s,  s,  s,  0,  p,  q));
		v.add(new Vertex( 0,  q, -p,  0,  p, -q)); v.add(new Vertex(-s,  s, -s,  0,  p, -q)); v.add(new Vertex(-q,  p,  0,  0,  p, -q)); v.add(new Vertex( q,  p,  0,  0,  p, -q)); v.add(new Vertex( s,  s, -s,  0,  p, -q));
		v.add(new Vertex( 0, -q,  p,  0, -p,  q)); v.add(new Vertex(-s, -s,  s,  0, -p,  q)); v.add(new Vertex(-q, -p,  0,  0, -p,  q)); v.add(new Vertex( q, -p,  0,  0, -p,  q)); v.add(new Vertex( s, -s,  s,  0, -p,  q));
		v.add(new Vertex( 0, -q, -p,  0, -p, -q)); v.add(new Vertex( s, -s, -s,  0, -p, -q)); v.add(new Vertex( q, -p,  0,  0, -p, -q)); v.add(new Vertex(-q, -p,  0,  0, -p, -q)); v.add(new Vertex(-s, -s, -s,  0, -p, -q));
		
		v.add(new Vertex( p,  0,  q)); v.add(new Vertex( s,  s,  s)); v.add(new Vertex( 0,  q,  p)); v.add(new Vertex( 0, -q,  p)); v.add(new Vertex( s, -s,  s));
		v.add(new Vertex(-p,  0,  q)); v.add(new Vertex(-s, -s,  s)); v.add(new Vertex( 0, -q,  p)); v.add(new Vertex( 0,  q,  p)); v.add(new Vertex(-s,  s,  s));
		v.add(new Vertex( p,  0, -q)); v.add(new Vertex( s, -s, -s)); v.add(new Vertex( 0, -q, -p)); v.add(new Vertex( 0,  q, -p)); v.add(new Vertex( s,  s, -s));
		v.add(new Vertex(-p,  0, -q)); v.add(new Vertex(-s,  s, -s)); v.add(new Vertex( 0,  q, -p)); v.add(new Vertex( 0, -q, -p)); v.add(new Vertex(-s, -s, -s));
		
		v.add(new Vertex( q,  p,  0)); v.add(new Vertex( s,  s,  s)); v.add(new Vertex( p,  0,  q)); v.add(new Vertex( p,  0, -q)); v.add(new Vertex( s,  s, -s));
		v.add(new Vertex( q, -p,  0)); v.add(new Vertex( s, -s, -s)); v.add(new Vertex( p,  0, -q)); v.add(new Vertex( p,  0,  q)); v.add(new Vertex( s, -s,  s));
		v.add(new Vertex(-q,  p,  0)); v.add(new Vertex(-s,  s, -s)); v.add(new Vertex(-p,  0, -q)); v.add(new Vertex(-p,  0,  q)); v.add(new Vertex(-s,  s,  s));
		v.add(new Vertex(-q, -p,  0)); v.add(new Vertex(-s, -s,  s)); v.add(new Vertex(-p,  0,  q)); v.add(new Vertex(-p,  0, -q)); v.add(new Vertex(-s, -s, -s));
		
		sceneNode = new SceneNodeImpl();
		sceneNode.setVertices(v);
		IntBuffer elementBuffer = Buffers.newDirectIntBuffer(v.size()*2);
		
		elementBuffer.put(new int[]
		{
			 0*5+0,  0*5+1,  0*5+2,  0*5+0,  0*5+2,  0*5+3,  0*5+0,  0*5+3,  0*5+4,
			 1*5+0,  1*5+1,  1*5+2,  1*5+0,  1*5+2,  1*5+3,  1*5+0,  1*5+3,  1*5+4,
			 2*5+0,  2*5+1,  2*5+2,  2*5+0,  2*5+2,  2*5+3,  2*5+0,  2*5+3,  2*5+4,
			 3*5+0,  3*5+1,  3*5+2,  3*5+0,  3*5+2,  3*5+3,  3*5+0,  3*5+3,  3*5+4,
			
			 4*5+0,  4*5+1,  4*5+2,  4*5+0,  4*5+2,  4*5+3,  4*5+0,  4*5+3,  4*5+4,
			 5*5+0,  5*5+1,  5*5+2,  5*5+0,  5*5+2,  5*5+3,  5*5+0,  5*5+3,  5*5+4,
			 6*5+0,  6*5+1,  6*5+2,  6*5+0,  6*5+2,  6*5+3,  6*5+0,  6*5+3,  6*5+4,
			 7*5+0,  7*5+1,  7*5+2,  7*5+0,  7*5+2,  7*5+3,  7*5+0,  7*5+3,  7*5+4,
			
			 8*5+0,  8*5+1,  8*5+2,  8*5+0,  8*5+2,  8*5+3,  8*5+0,  8*5+3,  8*5+4,
			 9*5+0,  9*5+1,  9*5+2,  9*5+0,  9*5+2,  9*5+3,  9*5+0,  9*5+3,  9*5+4,
			10*5+0, 10*5+1, 10*5+2, 10*5+0, 10*5+2, 10*5+3, 10*5+0, 10*5+3, 10*5+4,
			11*5+0, 11*5+1, 11*5+2, 11*5+0, 11*5+2, 11*5+3, 11*5+0, 11*5+3, 11*5+4,
		});
		elementBuffer.rewind();
		
		sceneNode.setElementBuffer(elementBuffer);
	}
	
	public void reposition(Vector3 v)
	{
		sceneNode.reposition(v);
	}
	
	public void renderInit(GL3 gl, MatrixHandler mh)
	{
		sceneNode.renderInit(gl, mh);
		
		try
		{
			Texture tex = TextureIO.newTexture(new File("poincare.png"), false);
			sceneNode.setTexture(tex);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void render(GL3 gl)
	{
		sceneNode.render(gl);
	}
}
