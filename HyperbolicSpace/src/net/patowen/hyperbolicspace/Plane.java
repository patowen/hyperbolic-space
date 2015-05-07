package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;

public class Plane implements SceneNodeType
{
	private Controller c;
	private SceneNodeImpl sceneNode;
	
	public Plane(Controller c)
	{
		this.c = c;
		ArrayList<Vertex> v = new ArrayList<Vertex>();
		
		v.add(new Vertex( 0,  0,  0)); v.add(new Vertex( 0.5,  0,  0)); v.add(new Vertex( 0.5,  0.3,  0)); v.add(new Vertex(0.3,  0.5,  0)); v.add(new Vertex(0,  0.5,  0));
		
		sceneNode = new SceneNodeImpl(this.c);
		sceneNode.setVertices(v);
		IntBuffer elementBuffer = Buffers.newDirectIntBuffer(3*3);
		
		elementBuffer.put(new int[]
		{
			 0,  1,  2,  0,  2,  3,  0,  3,  4,
		});
		elementBuffer.rewind();
		
		FloatBuffer textureBuffer = Buffers.newDirectFloatBuffer(v.size()*2);
		
		textureBuffer.put(new float[]
		{
			0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
		});
		textureBuffer.rewind();
		
		sceneNode.setElementBuffer(elementBuffer);
		sceneNode.setTexCoordBuffer(textureBuffer);
		sceneNode.reposition();
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
