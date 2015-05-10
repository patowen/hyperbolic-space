package net.patowen.hyperbolicspace;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;

/**
 * Represents a tall structure with a pentagonal base. Ideally, this structure can be tessellated
 * in a {5, 4} tessellation on top of a plane to make the hyperbolic equivalent of a city, but
 * this has not yet been implemented.
 * @author Patrick Owen
 */
public class Building implements SceneNodeType
{
	private Controller c;
	private SceneNodeImpl sceneNode;
	
	private int baseSides = 5;
	private double baseRadius = 0.35;
	
	private double totalHeight = 2;
	private int heightStepsPerWrap = 6;
	private int numWraps = 7;
	
	/**
	 * Initializes the {@code Building} mesh.
	 * @param c
	 */
	public Building(Controller c)
	{
		this.c = c;
		ArrayList<Vertex> v = new ArrayList<Vertex>();
		
		double dx = Math.tanh(baseRadius);
		double dz = Math.tanh(totalHeight/heightStepsPerWrap/numWraps);
		
		Vector3 center = new Vector3(0, 0, 0);
		Vector3[] corners = new Vector3[baseSides];
		for (int i=0; i<baseSides; i++)
		{
			double theta = i*Math.PI*2/baseSides;
			corners[i] = new Vector3(dx*Math.cos(theta), dx*Math.sin(theta), 0);
		}
		
		//Base
		int indexBase = v.size();
		v.add(new Vertex(center));
		for (int j=0; j<baseSides; j++)
			v.add(new Vertex(corners[j]));
		
		//Sides
		int[] indexSides = new int[numWraps];
		for (int wrap=0; wrap<numWraps; wrap++)
		{
			indexSides[wrap] = v.size();
			for (int i=0; ; i++)
			{
				for (int j=0; j<baseSides; j++)
				{
					int j1 = j+1; if (j1 == baseSides) j1 = 0;
					v.add(new Vertex(corners[j])); v.add(new Vertex(corners[j1]));
				}
				
				if (i == heightStepsPerWrap) break;
				
				for (int j=0; j<baseSides; j++)
					corners[j] = corners[j].hyperTranslate(new Vector3(0, 0, dz));
				center = center.hyperTranslate(new Vector3(0, 0, dz));
			}
		}
		
		//Top
		int indexTop = v.size();
		v.add(new Vertex(center));
		for (int j=0; j<baseSides; j++)
			v.add(new Vertex(corners[j]));
		
		sceneNode = new SceneNodeImpl(this.c);
		sceneNode.setVertices(v);
		IntBuffer elementBuffer = Buffers.newDirectIntBuffer(baseSides*((6+6*heightStepsPerWrap)*numWraps));
		
		//Base
		for (int i=0; i<baseSides; i++)
		{
			int i1 = i+1;
			if (i1 == baseSides) i1 = 0;
			elementBuffer.put(indexBase); elementBuffer.put(indexBase+1+i1); elementBuffer.put(indexBase+1+i);
		}
		
		//Sides
		for (int wrap=0; wrap<numWraps; wrap++)
		{
			for (int i=0; i<heightStepsPerWrap; i++)
			{
				for (int j=0; j<baseSides; j++)
				{
					elementBuffer.put(indexSides[wrap]+i*2*baseSides+2*j);
					elementBuffer.put(indexSides[wrap]+i*2*baseSides+2*j+1);
					elementBuffer.put(indexSides[wrap]+(i+1)*2*baseSides+2*j+1);
					
					elementBuffer.put(indexSides[wrap]+i*2*baseSides+2*j);
					elementBuffer.put(indexSides[wrap]+(i+1)*2*baseSides+2*j+1);
					elementBuffer.put(indexSides[wrap]+(i+1)*2*baseSides+2*j);
				}
			}
		}
		
		//Top
		for (int i=0; i<baseSides; i++)
		{
			int i1 = i+1;
			if (i1 == baseSides) i1 = 0;
			elementBuffer.put(indexTop); elementBuffer.put(indexTop+1+i); elementBuffer.put(indexTop+1+i1);
		}
		
		elementBuffer.rewind();
		
		FloatBuffer textureBuffer = Buffers.newDirectFloatBuffer(v.size()*2);
		
		textureBuffer.put(0.75f); textureBuffer.put(0.25f);
		for (int i=0; i<baseSides; i++)
		{
			double theta = i*Math.PI*2/baseSides;
			textureBuffer.put((float)(0.75+0.25*Math.cos(theta)));
			textureBuffer.put((float)(0.25+0.25*Math.sin(theta)));
		}
		
		for (int wrap=0; wrap<numWraps; wrap++)
		{
			for (int i=0; i<=heightStepsPerWrap; i++)
			{
				for (int j=0; j<baseSides; j++)
				{
					textureBuffer.put(0.0f);
					textureBuffer.put((float)i / heightStepsPerWrap);
					
					textureBuffer.put(0.5f);
					textureBuffer.put((float)i / heightStepsPerWrap);
				}
			}
		}
		
		textureBuffer.put(0.75f); textureBuffer.put(0.75f);
		for (int i=0; i<baseSides; i++)
		{
			double theta = i*Math.PI*2/baseSides;
			textureBuffer.put((float)(0.75+0.25*Math.cos(theta)));
			textureBuffer.put((float)(0.75+0.25*Math.sin(theta)));
		}
		
		textureBuffer.rewind();
		
		sceneNode.setElementBuffer(elementBuffer);
		sceneNode.setTexCoordBuffer(textureBuffer);
		sceneNode.prepare();
	}
	
	public void renderInit(GL3 gl)
	{
		sceneNode.renderInit(gl);
		sceneNode.setTexture(c.getTextureBank().metal);
	}
	
	public void render(GL3 gl, Transformation t)
	{
		sceneNode.setTransformation(t);
		sceneNode.render(gl);
	}
}
