package net.patowen.hyperbolicspace.model;

import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.math.Transformation;
import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.rendering.SceneNodeImpl;
import net.patowen.hyperbolicspace.rendering.SceneNodeType;
import net.patowen.hyperbolicspace.rendering.Vertex;

/**
 * Represents a section of a horosphere. A horosphere is the limit shape of continually expanding a sphere
 * in hyperbolic space. Its surface has the equivalent curvature of a Euclidean plane, and the section of the
 * horosphere represented is equivalent to a large square in Euclidean geometry.
 * @author Patrick Owen
 */
public class Horosphere implements SceneNodeType
{
	private Controller c;
	private SceneNodeImpl sceneNode;
	
	private int textureStepsPerWrap = 10;
	private int numWraps = 20;
	private double size = 10;
	
	/**
	 * Initializes the {@code Horosphere} mesh.
	 * @param c
	 */
	public Horosphere(Controller c)
	{
		this.c = c;
		
		ArrayList<Vertex> v = new ArrayList<Vertex>();
		
		int numSteps = textureStepsPerWrap*numWraps;
		int[][][][] vertices = new int[numWraps][numWraps][textureStepsPerWrap+1][textureStepsPerWrap+1];
		
		
		for (int xx=0; xx<numWraps; xx++)
		{
			for (int yy=0; yy<numWraps; yy++)
			{
				for (int i=0; i<=textureStepsPerWrap; i++)
				{
					for (int j=0; j<=textureStepsPerWrap; j++)
					{
						int ii = xx*textureStepsPerWrap+i;
						int jj = yy*textureStepsPerWrap+j;
						
						vertices[xx][yy][i][j] = v.size();
						Vector3 vertex = new Vector3();
						vertex = vertex.horoRotate(new Vector3(0,0,-1), new Vector3(1,0,0), ((double)ii/numSteps-0.5)*size);
						vertex = vertex.horoRotate(new Vector3(0,0,-1), new Vector3(0,1,0), ((double)jj/numSteps-0.5)*size);
//						vertex = vertex.hyperTranslate(new Vector3(0.02*ii, 0.02*jj, 0));
						v.add(new Vertex(vertex, new Vector3(), new Vector2((double)i/textureStepsPerWrap, (double)j/textureStepsPerWrap)));
					}
				}
			}
		}
		
		sceneNode = new SceneNodeImpl(this.c);
		sceneNode.setVertices(v);
		IntBuffer elementBuffer = Buffers.newDirectIntBuffer(6*numWraps*numWraps*textureStepsPerWrap*textureStepsPerWrap);
		
		for (int xx=0; xx<numWraps; xx++)
		{
			for (int yy=0; yy<numWraps; yy++)
			{
				int[][] section = vertices[xx][yy];
				for (int i=0; i<textureStepsPerWrap; i++)
				{
					for (int j=0; j<textureStepsPerWrap; j++)
					{
						elementBuffer.put(new int[] {section[i][j], section[i+1][j], section[i+1][j+1]});
						elementBuffer.put(new int[] {section[i][j], section[i+1][j+1], section[i][j+1]});
					}
				}
			}
		}
		
		elementBuffer.rewind();
		
		sceneNode.setElementBuffer(elementBuffer);
		sceneNode.prepare();
	}
	
	public void renderInit(GL3 gl)
	{
		sceneNode.renderInit(gl);
		sceneNode.setTexture(c.getTextureBank().stone);
	}
	
	public void render(GL3 gl, Transformation t)
	{
		sceneNode.setTransformation(t);
		sceneNode.render(gl);
	}
}
