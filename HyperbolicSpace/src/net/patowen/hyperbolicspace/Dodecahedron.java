package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;

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
		ArrayList<Vertex> v = new ArrayList<Vertex>();
		
		double s = 0.31546169558954995; //(1/6)*(2-3r2+r10)*(phi^(3/2))? r means square root
		double p = (1+Math.sqrt(5))/2;
		double q = s/p; p = s*p;
		
		v.add(new Vertex( 0,  q,  p)); v.add(new Vertex( s,  s,  s)); v.add(new Vertex( q,  p,  0)); v.add(new Vertex(-q,  p,  0)); v.add(new Vertex(-s,  s,  s));
		v.add(new Vertex( 0,  q, -p)); v.add(new Vertex(-s,  s, -s)); v.add(new Vertex(-q,  p,  0)); v.add(new Vertex( q,  p,  0)); v.add(new Vertex( s,  s, -s));
		v.add(new Vertex( 0, -q,  p)); v.add(new Vertex(-s, -s,  s)); v.add(new Vertex(-q, -p,  0)); v.add(new Vertex( q, -p,  0)); v.add(new Vertex( s, -s,  s));
		v.add(new Vertex( 0, -q, -p)); v.add(new Vertex( s, -s, -s)); v.add(new Vertex( q, -p,  0)); v.add(new Vertex(-q, -p,  0)); v.add(new Vertex(-s, -s, -s));
		
		v.add(new Vertex( p,  0,  q)); v.add(new Vertex( s,  s,  s)); v.add(new Vertex( 0,  q,  p)); v.add(new Vertex( 0, -q,  p)); v.add(new Vertex( s, -s,  s));
		v.add(new Vertex(-p,  0,  q)); v.add(new Vertex(-s, -s,  s)); v.add(new Vertex( 0, -q,  p)); v.add(new Vertex( 0,  q,  p)); v.add(new Vertex(-s,  s,  s));
		v.add(new Vertex( p,  0, -q)); v.add(new Vertex( s, -s, -s)); v.add(new Vertex( 0, -q, -p)); v.add(new Vertex( 0,  q, -p)); v.add(new Vertex( s,  s, -s));
		v.add(new Vertex(-p,  0, -q)); v.add(new Vertex(-s,  s, -s)); v.add(new Vertex( 0,  q, -p)); v.add(new Vertex( 0, -q, -p)); v.add(new Vertex(-s, -s, -s));
		
		v.add(new Vertex( q,  p,  0)); v.add(new Vertex( s,  s,  s)); v.add(new Vertex( p,  0,  q)); v.add(new Vertex( p,  0, -q)); v.add(new Vertex( s,  s, -s));
		v.add(new Vertex( q, -p,  0)); v.add(new Vertex( s, -s, -s)); v.add(new Vertex( p,  0, -q)); v.add(new Vertex( p,  0,  q)); v.add(new Vertex( s, -s,  s));
		v.add(new Vertex(-q,  p,  0)); v.add(new Vertex(-s,  s, -s)); v.add(new Vertex(-p,  0, -q)); v.add(new Vertex(-p,  0,  q)); v.add(new Vertex(-s,  s,  s));
		v.add(new Vertex(-q, -p,  0)); v.add(new Vertex(-s, -s,  s)); v.add(new Vertex(-p,  0,  q)); v.add(new Vertex(-p,  0, -q)); v.add(new Vertex(-s, -s, -s));
		
		sceneNode = new SceneNodeImpl(this.c);
		sceneNode.setVertices(v);
		IntBuffer elementBuffer = Buffers.newDirectIntBuffer(12*3*3);
		
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
		
		FloatBuffer textureBuffer = Buffers.newDirectFloatBuffer(v.size()*2);
		
		textureBuffer.put(new float[]
		{
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
		});
		textureBuffer.rewind();
		
		sceneNode.setElementBuffer(elementBuffer);
		sceneNode.setTexCoordBuffer(textureBuffer);
		sceneNode.prepare();
	}
	
	public void renderInit(GL3 gl)
	{
		sceneNode.renderInit(gl);
		sceneNode.setTexture(c.getTextureBank().clouds);
	}
	
	public void render(GL3 gl, Transformation t)
	{
		sceneNode.setTransformation(t);
		sceneNode.render(gl);
	}
}
