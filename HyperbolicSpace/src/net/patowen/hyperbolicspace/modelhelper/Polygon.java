package net.patowen.hyperbolicspace.modelhelper;

import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.rendering.Model;
import net.patowen.hyperbolicspace.rendering.Vertex;

public class Polygon
{
	boolean fanAtCenter;
	private Vector31 centerPosition;
	private Vector31 centerNormal;
	private Vector2 centerTexCoord;
	
	private Vector31[] positions;
	private Vector31[] normals;
	private Vector2[] texCoords;
	private final int numVertices;
	
	public Polygon(int numVertices)
	{
		this.numVertices = numVertices;
		positions = new Vector31[numVertices];
		normals = new Vector31[numVertices];
		texCoords = new Vector2[numVertices];
		fanAtCenter = false;
	}
	
	public Polygon(Vector31[] positions)
	{
		this.positions = positions;
		numVertices = positions.length;
		normals = new Vector31[numVertices];
		texCoords = new Vector2[numVertices];
		fanAtCenter = false;
	}
	
	public void setPosition(int i, Vector31 pos)
	{
		positions[i] = pos;
	}
	
	public void setCenterPosition(Vector31 pos)
	{
		fanAtCenter = true;
		centerPosition = pos;
	}
	
	public void setTexCoordsRegular(Vector2 center, double xRadius, double yRadius, double initialRatio)
	{
		for (int i=0; i<numVertices; i++)
		{
			double angle = (initialRatio + (double)i/numVertices) * Math.PI * 2;
			texCoords[i] = new Vector2(xRadius*Math.cos(angle), yRadius*Math.sin(angle)).plus(center);
		}
		centerTexCoord = center;
	}
	
	public void setTexCoords(Vector2[] texCoords)
	{
		this.texCoords = texCoords;
	}
	
	public void setCenterTexCoord(Vector2 texCoord)
	{
		this.centerTexCoord = texCoord;
	}
	
	public void setNormals()
	{
		for (int i=0; i<numVertices; i++)
		{
			int iPrevious = i-1;
			if (iPrevious < 0) iPrevious += numVertices;
			int iNext = i+1;
			if (iNext >= numVertices) iNext -= numVertices;
			
			Transform decentralizer = Transform.translation(positions[i]);
			Transform centralizer = decentralizer.inverse();
			
			Vector31 vectorPrevious = centralizer.transform(positions[iPrevious]);
			Vector31 vectorNext = centralizer.transform(positions[iNext]);
			
			Vector3 normal = vectorNext.getOrtho().cross(vectorPrevious.getOrtho());
			normal.normalize();
			normals[i] = decentralizer.transform(Vector31.makeOrtho(normal));
		}
		
		if (fanAtCenter)
		{
			Transform decentralizer = Transform.translation(centerPosition);
			Transform centralizer = decentralizer.inverse();
			
			Vector31 vector1 = centralizer.transform(positions[0]);
			Vector31 vector2 = centralizer.transform(positions[1]);
			
			Vector3 normal = vector1.getOrtho().cross(vector2.getOrtho());
			normal.normalize();
			centerNormal = decentralizer.transform(Vector31.makeOrtho(normal));
		}
	}
	
	public void addToModel(Model model)
	{
		setNormals();
		int[] indices = new int[numVertices];
		for (int i=0; i<numVertices; i++)
		{
			indices[i] = model.addVertex(new Vertex(positions[i], normals[i], texCoords[i]));
		}
		
		if (fanAtCenter)
		{
			int centerIndex = model.addVertex(new Vertex(centerPosition, centerNormal, centerTexCoord));
			for (int i=0; i<numVertices; i++)
			{
				model.addTriangle(centerIndex, indices[i], indices[(i+1)%numVertices]);
			}
		}
		else
		{
			for (int i=2; i<numVertices; i++)
			{
				model.addTriangle(indices[0], indices[i-1], indices[i]);
			}
		}
	}
}
