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
		
		ArrayList<TessellatorVertex> tVert = new ArrayList<TessellatorVertex>();
		ArrayList<TessellatorFace> tFace = new ArrayList<TessellatorFace>();
		tessellate(tVert, tFace);
		for (TessellatorFace f : tFace)
		{
			v.add(new Vertex(f.vertices[0].getPosition()));
			v.add(new Vertex(f.vertices[1].getPosition()));
			v.add(new Vertex(f.vertices[2].getPosition()));
			v.add(new Vertex(f.vertices[3].getPosition()));
			v.add(new Vertex(f.vertices[4].getPosition()));
		}
		
		sceneNode = new SceneNodeImpl(this.c);
		sceneNode.setVertices(v);
		IntBuffer elementBuffer = Buffers.newDirectIntBuffer(3*3*tFace.size());
		for (int i=0; i<tFace.size(); i++)
		{
			elementBuffer.put(new int[]
			{
				i*5+0, i*5+1, i*5+2,
				i*5+0, i*5+2, i*5+3,
				i*5+0, i*5+3, i*5+4,
			});
		}
		elementBuffer.rewind();
		
		FloatBuffer textureBuffer = Buffers.newDirectFloatBuffer(v.size()*2);
		
		for (int i=0; i<tFace.size(); i++)
		{
			textureBuffer.put(new float[]
			{
				0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,
			});
		}
		textureBuffer.rewind();
		
		sceneNode.setElementBuffer(elementBuffer);
		sceneNode.setTexCoordBuffer(textureBuffer);
		sceneNode.reposition();
	}
	
	public void tessellate(ArrayList<TessellatorVertex> tVert, ArrayList<TessellatorFace> tFace)
	{
		int numComplete = 0;
		tVert.add(new TessellatorVertex()); //Seed
		
		for (int i=0; i<5; i++)
		{
			ArrayList<TessellatorVertex> newVert = new ArrayList<TessellatorVertex>();
			ArrayList<TessellatorFace> newFace = new ArrayList<TessellatorFace>();
			
			for (int j=numComplete; j<tVert.size(); j++)
			{
				tVert.get(j).expand(newVert, newFace);
			}
			
			numComplete = tVert.size();
			tVert.addAll(newVert);
			tFace.addAll(newFace);
		}
	}
	
	private class TessellatorFace
	{
		public TessellatorVertex[] vertices;
		private int numVertices;
		
		public TessellatorFace()
		{
			vertices = new TessellatorVertex[5];
			numVertices = 0;
		}
		
		public void addBefore(TessellatorVertex vertex)
		{
			for (int i=numVertices; i>0; i--)
				vertices[i] = vertices[i-1];
			vertices[0] = vertex;
			numVertices++;
		}
		
		public void addAfter(TessellatorVertex vertex)
		{
			vertices[numVertices++] = vertex;
		}
	}
	
	private class TessellatorVertex
	{
		private TessellatorVertex[] neighbors;
		private int[] neighborIndices;
		private boolean[] faces;
		private Transformation position;
		
		public TessellatorVertex()
		{
			neighbors = new TessellatorVertex[4];
			neighborIndices = new int[4];
			faces = new boolean[4];
			position = new Transformation();
		}
		
		public TessellatorVertex(TessellatorVertex neighbor, int neighborIndex, Transformation position)
		{
			neighbors = new TessellatorVertex[4];
			neighborIndices = new int[4];
			faces = new boolean[4];
			neighbors[2] = neighbor;
			neighborIndices[2] = neighborIndex;
			this.position = position;
		}
		
		public Vector3 getPosition()
		{
			return position.getTranslation();
		}
		
		public int wrapIndex(int index)
		{
			return ((index%4)+4)%4;
		}
		
		public TessellatorVertex addNeighbor(int index)
		{
			double s = 0.4858682717566457; //phi^(3/2)?
			
			Transformation nextTransform;
			switch (index)
			{
			case 0:
				nextTransform = new Transformation(new Orientation(new Vector3(1,0,0), new Vector3(0,1,0), new Vector3(0,0,1)), new Vector3(s, 0, 0));
				break;
			case 1:
				nextTransform = new Transformation(new Orientation(new Vector3(0,1,0), new Vector3(-1,0,0), new Vector3(0,0,1)), new Vector3(0, s, 0));
				break;
			case 2:
				nextTransform = new Transformation(new Orientation(new Vector3(-1,0,0), new Vector3(0,-1,0), new Vector3(0,0,1)), new Vector3(-s, 0, 0));
				break;
			case 3:
				nextTransform = new Transformation(new Orientation(new Vector3(0,-1,0), new Vector3(1,0,0), new Vector3(0,0,1)), new Vector3(0, -s, 0));
				break;
			default:
				throw new IndexOutOfBoundsException(Integer.toString(index));
			}
			
			neighbors[index] = new TessellatorVertex(this, index, position.composeBefore(nextTransform));
			neighborIndices[index] = 2;
			
			return neighbors[index];
		}
		
		public TessellatorVertex setNeighbor(int index, TessellatorVertex neighbor, int neighborIndex)
		{
			neighbors[index] = neighbor;
			neighbor.neighbors[neighborIndex] = this;
			neighborIndices[index] = neighborIndex;
			neighbor.neighborIndices[neighborIndex] = index;
			return neighbor;
		}
		
		public void expand(ArrayList<TessellatorVertex> vOutput, ArrayList<TessellatorFace> fOutput)
		{
			for (int i=0; i<4; i++)
				if (!faces[i]) addFace(vOutput, fOutput, i);
		}
		
		public void addFace(ArrayList<TessellatorVertex> vOutput, ArrayList<TessellatorFace> fOutput, int faceIndex)
		{
			int edgesLeft = 5;
			
			TessellatorFace face = new TessellatorFace();
			face.addAfter(this);
			faces[faceIndex] = true;
			
			//Backtrack
			TessellatorVertex endVertex = this;
			int endIndex;
			{
				int nextIndex = wrapIndex(faceIndex+1);
				endIndex = nextIndex;
				TessellatorVertex currentVertex = this;
				while (true)
				{
					TessellatorVertex nextVertex = currentVertex.neighbors[nextIndex];
					if (nextVertex == null)
						break;
					nextIndex = wrapIndex(currentVertex.neighborIndices[nextIndex]+1);
					currentVertex = nextVertex;
					edgesLeft--;
					currentVertex.faces[wrapIndex(nextIndex-1)] = true;
					endVertex = currentVertex;
					endIndex = nextIndex;
					if (currentVertex == this)
						break;
					face.addBefore(currentVertex);
				}
			}
			
			//Advance
			{
				int nextIndex = faceIndex;
				TessellatorVertex currentVertex = this;
				while (edgesLeft > 0)
				{
					TessellatorVertex nextVertex = currentVertex.neighbors[nextIndex];
					if (nextVertex == null)
					{
						if (edgesLeft == 1)
							nextVertex = currentVertex.setNeighbor(nextIndex, endVertex, endIndex);
						else
						{
							nextVertex = currentVertex.addNeighbor(nextIndex);
							vOutput.add(nextVertex);
						}
					}
					nextIndex = wrapIndex(currentVertex.neighborIndices[nextIndex]-1);
					currentVertex = nextVertex;
					edgesLeft--;
					if (currentVertex != endVertex)
					{
						currentVertex.faces[nextIndex] = true;
						face.addAfter(currentVertex);
					}
				}
			}
			
			fOutput.add(face);
		}
	}
	
	public void renderInit(GL3 gl)
	{
		sceneNode.renderInit(gl);
		sceneNode.setTexture(c.getTextureBank().clouds);
		sceneNode.setColor(new float[] {0.42f, 0.54f, 0.14f, 1});
	}
	
	public void render(GL3 gl, Transformation t)
	{
		sceneNode.setTransformation(t);
		sceneNode.render(gl);
	}
}
