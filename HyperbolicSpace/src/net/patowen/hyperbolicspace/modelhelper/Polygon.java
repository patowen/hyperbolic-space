package net.patowen.hyperbolicspace.modelhelper;

import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.rendering.Model;
import net.patowen.hyperbolicspace.rendering.Vertex;

public class Polygon
{
	boolean fanAtCenter;
	private Vector3 centerPosition;
	private Vector3 centerNormal;
	private Vector2 centerTexCoord;
	
	private Vector3[] positions;
	private Vector3[] normals;
	private Vector2[] texCoords;
	private final int numVertices;
	
	public Polygon(int numVertices)
	{
		this.numVertices = numVertices;
		positions = new Vector3[numVertices];
		normals = new Vector3[numVertices];
		texCoords = new Vector2[numVertices];
		fanAtCenter = false;
	}
	
	public Polygon(Vector3[] positions)
	{
		this.positions = positions;
		numVertices = positions.length;
		normals = new Vector3[numVertices];
		texCoords = new Vector2[numVertices];
		fanAtCenter = false;
	}
	
	public void setPosition(int i, Vector3 pos)
	{
		positions[i] = pos;
	}
	
	public void setCenterPosition(Vector3 pos)
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
			
			Vector3 vectorPrevious = positions[iPrevious].hyperTranslate(positions[i].times(-1));
			Vector3 vectorNext = positions[iNext].hyperTranslate(positions[i].times(-1));
			
			Vector3 normal = vectorNext.cross(vectorPrevious);
			normal.normalize();
			
			normal = positions[i].times(-1).hyperDirectionTo(normal);
			normal.normalize();
			normals[i] = normal;
		}
		
		if (fanAtCenter)
		{
			Vector3 vector1 = positions[0].hyperTranslate(centerPosition.times(-1));
			Vector3 vector2 = positions[1].hyperTranslate(centerPosition.times(-1));
			
			Vector3 normal = vector1.cross(vector2);
			normal.normalize();
			
			normal = centerPosition.times(-1).hyperDirectionTo(normal);
			normal.normalize();
			centerNormal = normal;
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
