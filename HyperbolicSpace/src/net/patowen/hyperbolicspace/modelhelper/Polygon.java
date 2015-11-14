package net.patowen.hyperbolicspace.modelhelper;

import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.rendering.Model;
import net.patowen.hyperbolicspace.rendering.Vertex;

public class Polygon
{
	private Vector3[] positions;
	private Vector3[] normals;
	private Vector2[] texCoords;
	private final int numVertices;
	
	public Polygon(Vector3[] positions)
	{
		this.positions = positions;
		numVertices = positions.length;
		normals = new Vector3[numVertices];
		texCoords = new Vector2[numVertices];
		
		setNormals();
	}
	
	public void setTexCoordsRegular(Vector2 center, double radius, double initialRatio)
	{
		for (int i=0; i<numVertices; i++)
		{
			double angle = (initialRatio + (double)i/numVertices) * Math.PI * 2;
			texCoords[i] = center.plusMultiple(new Vector2(Math.cos(angle), Math.sin(angle)), radius);
		}
	}
	
	public void setTexCoords(Vector2[] texCoords)
	{
		this.texCoords = texCoords;
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
	}
	
	public void addToModel(Model model)
	{
		int[] indices = new int[numVertices];
		for (int i=0; i<numVertices; i++)
		{
			indices[i] = model.addVertex(new Vertex(positions[i], normals[i], texCoords[i]));
		}
		
		for (int i=2; i<numVertices; i++)
		{
			model.addTriangle(indices[0], indices[i-1], indices[i]);
		}
	}
}
