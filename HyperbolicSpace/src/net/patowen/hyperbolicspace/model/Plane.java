package net.patowen.hyperbolicspace.model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.math.Orientation;
import net.patowen.hyperbolicspace.math.Transformation;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.rendering.SceneNodeImpl;
import net.patowen.hyperbolicspace.rendering.SceneNodeType;
import net.patowen.hyperbolicspace.rendering.Vertex;

/**
 * Represents a hyperbolic plane aligned to the x and y vectors at the origin. The mesh
 * is a {5,4} pentagonal tessellation. To produce the plane, an iterative process starting
 * with a single vertex is used. In each iteration, every vertex has all of its neighbor faces
 * added, creating any necessary vertices for this process to work. This iterative process is
 * repeated five times to produce a plane up to (and slightly past) the floating point precision
 * limit.
 * @author Patrick Owen
 */
public class Plane implements SceneNodeType
{
	private Controller c;
	private SceneNodeImpl sceneNode;
	
	/**
	 * Initializes the {@code Plane} mesh.
	 * @param c
	 */
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
		sceneNode.prepare();
	}
	
	/**
	 * Populates the initially-empty tVert and tFace lists with a 5-iteration tessellation
	 * @param tVert The list of vertices to fill
	 * @param tFace The list of faces to fill
	 */
	private void tessellate(ArrayList<TessellatorVertex> tVert, ArrayList<TessellatorFace> tFace)
	{
		int numComplete = 0;
		tVert.add(new TessellatorVertex()); //Seed
		
		for (int i=0; i<5; i++)
		{
			//Each iteration
			
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
	
	//TODO Abstract to other kinds of tessellations and abstract
	//TODO Make seamless
	
	/**
	 * Represents a face in the tessellation and holds references
	 * to its vertices in counter-clockwise order
	 * @author Patrick Owen
	 */
	private class TessellatorFace
	{
		public TessellatorVertex[] vertices;
		private int numVertices;
		
		/**
		 * Initializes an incomplete face. {@code addBefore} and
		 * {@code addAfter} should be called a sum total of five
		 * times before this class is used.
		 */
		public TessellatorFace()
		{
			vertices = new TessellatorVertex[5];
			numVertices = 0;
		}
		
		/**
		 * Adds the given vertex before the first vertex in the face.
		 * @param vertex the vertex to add
		 */
		public void addBefore(TessellatorVertex vertex)
		{
			for (int i=numVertices; i>0; i--)
				vertices[i] = vertices[i-1];
			vertices[0] = vertex;
			numVertices++;
		}
		
		/**
		 * Adds the given vertex after the last vertex in the face.
		 * @param vertex the vertex to add
		 */
		public void addAfter(TessellatorVertex vertex)
		{
			vertices[numVertices++] = vertex;
		}
	}
	
	/**
	 * Represents a vertex in the tessellation and holds references
	 * to its neighbor vertices in counter-clockwise order and its location
	 * @author Patrick Owen
	 */
	private class TessellatorVertex
	{
		private TessellatorVertex[] neighbors; //All four neighbors of the vertex
		private int[] neighborIndices; //The index in the neighbors array of each neighbor representing this
		private boolean[] faces; //Whether the given face (between neighbors[i] and neighbors[i+1]) has been added
		private Transformation position; //Location and orientation if neighbors[0] is in the forward direction
		
		/**
		 * Initializes the seed vertex
		 */
		public TessellatorVertex()
		{
			neighbors = new TessellatorVertex[4];
			neighborIndices = new int[4];
			faces = new boolean[4];
			position = new Transformation();
		}
		
		/**
		 * Initializes a branching vertex
		 * @param neighbor The neighbor the vertex came from
		 * @param neighborIndex The index of this vertex in the perspective of the neighbor
		 * @param position The location of the vertex
		 */
		private TessellatorVertex(TessellatorVertex neighbor, int neighborIndex, Transformation position)
		{
			neighbors = new TessellatorVertex[4];
			neighborIndices = new int[4];
			faces = new boolean[4];
			neighbors[2] = neighbor;
			neighborIndices[2] = neighborIndex;
			this.position = position;
		}
		
		/**
		 * Returns the location of the vertex without the orientation
		 */
		public Vector3 getPosition()
		{
			return position.getTranslation();
		}
		
		/**
		 * Returns index modulo 4 such that the value returned is 0, 1, 2, or 3
		 * @param index the index to wrap
		 * @return the index wrapped to be a valid value for an array index
		 */
		private int wrapIndex(int index)
		{
			return ((index%4)+4)%4;
		}
		
		/**
		 * Adds a vertex to the specified array index of the neighbors array
		 * in the proper location for a pentagonal tessellation
		 * @param index the location of the vertex in the neighbors array
		 * @return the newly-added vertex
		 */
		private TessellatorVertex addNeighbor(int index)
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
		
		/**
		 * Sets a neighbor in the neighbor array to a preexisting vertex
		 * @param index the location of the vertex in the neighbors array
		 * @param neighbor the preexisting vertex to add as a neighbor
		 * @param neighborIndex the index of this in the neighbor's neighbors array
		 * @return the newly-attached vertex
		 */
		private TessellatorVertex setNeighbor(int index, TessellatorVertex neighbor, int neighborIndex)
		{
			neighbors[index] = neighbor;
			neighbor.neighbors[neighborIndex] = this;
			neighborIndices[index] = neighborIndex;
			neighbor.neighborIndices[neighborIndex] = index;
			return neighbor;
		}
		
		/**
		 * Fully branches out the vertex such that it has four neighboring faces. Does not override preexisting
		 * faces. Stores newly-added faces and vertices in the given lists
		 * @param vOutput the list of vertices to fill
		 * @param fOutput the list of faces to fill
		 */
		public void expand(ArrayList<TessellatorVertex> vOutput, ArrayList<TessellatorFace> fOutput)
		{
			for (int i=0; i<4; i++)
				if (!faces[i]) addFace(vOutput, fOutput, i);
		}
		
		/**
		 * Adds a face given by the specified index. Does not override preexisting vertices
		 * @param vOutput the list of vertices to fill
		 * @param fOutput the list of faces to fill
		 * @param faceIndex which face to add. The face is added between the neighbors given by the indices
		 * faceIndex and faceIndex+1, wrapped if necessary.
		 */
		private void addFace(ArrayList<TessellatorVertex> vOutput, ArrayList<TessellatorFace> fOutput, int faceIndex)
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
