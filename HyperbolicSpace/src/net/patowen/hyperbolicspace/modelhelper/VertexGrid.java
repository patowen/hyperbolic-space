package net.patowen.hyperbolicspace.modelhelper;

import net.patowen.hyperbolicspace.math.MathHelper;
import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.rendering.Model;
import net.patowen.hyperbolicspace.rendering.Vertex;

public class VertexGrid
{
	private static class TexCoordInfo
	{
		private double lower0, lower1, upper0, upper1;
		private int size0, size1, offset0, offset1;
	}
	
	private Vector3[][] positions;
	private Vector3[][] normals;
	private final int size0, size1;
	
	private TexCoordInfo texCoordInfo;
	
	public VertexGrid(int size0, int size1)
	{
		this.size0 = size0;
		this.size1 = size1;
		positions = new Vector3[size0+1][size1+1];
		normals = new Vector3[size0+1][size1+1];
	}
	
	public void setPosition(int coord0, int coord1, Vector3 position)
	{
		positions[coord0][coord1] = position;
	}
	
	public void setNormal(int coord0, int coord1, Vector3 normal)
	{
		normals[coord0][coord1] = normal;
	}
	
	public void setTexCoords(double lower0, double lower1, double upper0, double upper1,
			int size0, int size1, int offset0, int offset1)
	{
		TexCoordInfo t = new TexCoordInfo();
		t.lower0 = lower0; t.lower1 = lower1;
		t.upper0 = upper0; t.upper1 = upper1;
		t.size0 = size0; t.size1 = size1;
		t.offset0 = offset0; t.offset1 = offset1;
		texCoordInfo = t;
	}
	
	public void addToModel(Model model)
	{
		TexCoordInfo t = texCoordInfo;
		
		int start0 = MathHelper.modBound(t.offset0, t.size0, -t.size0+1);
		int start1 = MathHelper.modBound(t.offset1, t.size1, -t.size1+1);
		
		for (int i0=start0; i0<size0; i0+=t.size0)
		{
			for (int i1=start1; i1<size1; i1+=t.size1)
			{
				int[][] vertices = new int[t.size0+1][t.size1+1];
				for (int j0=Math.max(i0, 0); j0<=Math.min(i0+t.size0, size0); j0++)
				{
					for (int j1=Math.max(i1, 0); j1<=Math.min(i1+t.size1, size1); j1++)
					{
						Vector2 texCoord = new Vector2((double)(j0-i0)/t.size0, (double)(j1-i1)/t.size1);
						texCoord.x = texCoord.x*t.upper0 + (1-texCoord.x)*t.lower0;
						texCoord.y = texCoord.y*t.upper1 + (1-texCoord.y)*t.lower1;
						vertices[j0-i0][j1-i1] = model.addVertex(new Vertex(positions[j0][j1], normals[j0][j1], texCoord));
					}
				}
				for (int j0=Math.max(i0, 0); j0<Math.min(i0+t.size0, size0); j0++)
				{
					for (int j1=Math.max(i1, 0); j1<Math.min(i1+t.size1, size1); j1++)
					{
						model.addQuad(
								vertices[j0-i0][j1-i1],
								vertices[j0-i0+1][j1-i1],
								vertices[j0-i0+1][j1-i1+1],
								vertices[j0-i0][j1-i1+1]);
					}
				}
			}
		}
	}
}
