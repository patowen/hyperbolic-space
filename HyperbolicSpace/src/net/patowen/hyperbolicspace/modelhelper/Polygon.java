package net.patowen.hyperbolicspace.modelhelper;

import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.rendering.Model;
import net.patowen.hyperbolicspace.rendering.Vertex;

public class Polygon
{
	boolean fanAtCenter;
	private Vector31 centerPosition;
	private Vector2 centerTexCoord;
	
	private Vector31[] positions;
	private Vector31 normal;
	private Vector2[] texCoords;
	private final int numVertices;
	
	public Polygon(int numVertices)
	{
		this.numVertices = numVertices;
		positions = new Vector31[numVertices];
		texCoords = new Vector2[numVertices];
		fanAtCenter = false;
	}
	
	public Polygon(Vector31[] positions)
	{
		this.positions = positions;
		numVertices = positions.length;
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
		Vector31 v1 = positions[0];
		Vector31 v2 = positions[numVertices/3];
		Vector31 v3 = positions[(numVertices*2)/3];
		
		normal = Vector31.makeOrthogonal(v1, v2, v3);
		normal.normalizeAsDirection();
	}
	
	public void addToModel(Model model)
	{
		setNormals();
		int[] indices = new int[numVertices];
		for (int i=0; i<numVertices; i++)
		{
			indices[i] = model.addVertex(new Vertex(positions[i], normal, texCoords[i]));
		}
		
		if (fanAtCenter)
		{
			int centerIndex = model.addVertex(new Vertex(centerPosition, normal, centerTexCoord));
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
