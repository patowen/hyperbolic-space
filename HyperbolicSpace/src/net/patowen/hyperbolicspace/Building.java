package net.patowen.hyperbolicspace;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Building implements SceneNode
{
	SceneNodeImpl sceneNode;
	
	int baseSides = 5;
	double baseRadius = 0.35;
	
	double totalHeight = 2;
	int heightSteps = 40;
	
	public Building()
	{
		ArrayList<Vertex> v = new ArrayList<Vertex>();
		
		double dx = Math.tanh(baseRadius);
		double dz = Math.tanh(totalHeight/heightSteps);
		
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
		int indexSides = v.size();
		for (int i=0; ; i++)
		{
			for (int j=0; j<baseSides; j++)
			{
				int j1 = j+1; if (j1 == baseSides) j1 = 0;
				v.add(new Vertex(corners[j])); v.add(new Vertex(corners[j1]));
			}
			
			if (i == heightSteps) break;
			
			for (int j=0; j<baseSides; j++)
				corners[j] = corners[j].hyperTranslate(new Vector3(0, 0, dz));
			center = center.hyperTranslate(new Vector3(0, 0, dz));
			
		}
		
		//Top
		int indexTop = v.size();
		v.add(new Vertex(center));
		for (int j=0; j<baseSides; j++)
			v.add(new Vertex(corners[j]));
		
		sceneNode = new SceneNodeImpl();
		sceneNode.setVertices(v);
		IntBuffer elementBuffer = Buffers.newDirectIntBuffer(baseSides*(6+6*heightSteps));
		
		//Base
		for (int i=0; i<baseSides; i++)
		{
			int i1 = i+1;
			if (i1 == baseSides) i1 = 0;
			elementBuffer.put(indexBase); elementBuffer.put(indexBase+1+i1); elementBuffer.put(indexBase+1+i);
		}
		
		//Sides
		for (int i=0; i<heightSteps; i++)
		{
			for (int j=0; j<baseSides; j++)
			{
				elementBuffer.put(indexSides+i*2*baseSides+2*j);
				elementBuffer.put(indexSides+i*2*baseSides+2*j+1);
				elementBuffer.put(indexSides+(i+1)*2*baseSides+2*j+1);
				
				elementBuffer.put(indexSides+i*2*baseSides+2*j);
				elementBuffer.put(indexSides+(i+1)*2*baseSides+2*j+1);
				elementBuffer.put(indexSides+(i+1)*2*baseSides+2*j);
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
		
		for (int i=0; i<=heightSteps; i++)
		{
			for (int j=0; j<baseSides; j++)
			{
				textureBuffer.put((float)j / baseSides * 0.5f);
				textureBuffer.put((float)i / heightSteps);
				
				textureBuffer.put((float)(j+1) / baseSides * 0.5f);
				textureBuffer.put((float)i / heightSteps);
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
	}
	
	public void setPosition(Vector3 pos)
	{
		sceneNode.setPosition(pos);
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
			Texture tex = TextureIO.newTexture(new File("flat_clouds.png"), false);
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
