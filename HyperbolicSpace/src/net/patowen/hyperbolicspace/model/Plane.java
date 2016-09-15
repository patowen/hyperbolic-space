package net.patowen.hyperbolicspace.model;

import java.util.ArrayList;

import com.jogamp.opengl.GL3;

import net.patowen.hyperbolicspace.Controller;
import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector3;
import net.patowen.hyperbolicspace.math.Vector31;
import net.patowen.hyperbolicspace.modelhelper.Polygon;
import net.patowen.hyperbolicspace.rendering.Model;
import net.patowen.hyperbolicspace.rendering.SceneNodeImpl;
import net.patowen.hyperbolicspace.rendering.SceneNodeType;

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
	
	private int shapeType;
	private int joinType;
	
	/**
	 * Initializes the {@code Plane} mesh.
	 * @param c
	 */
	public Plane(Controller c)
	{
		shapeType = 4;
		joinType = 5;
		
		this.c = c;
		Model model = new Model();
		
		ArrayList<TessellatorVertex> tVert = new ArrayList<TessellatorVertex>();
		ArrayList<TessellatorFace> tFace = new ArrayList<TessellatorFace>();
		tessellate(tVert, tFace);
		for (TessellatorFace f : tFace)
		{
			Polygon cell = new Polygon(shapeType);
			for (int i=0; i<shapeType; i++)
			{
				cell.setPosition(i, f.vertices[i].getPosition());
			}
			cell.setTexCoordsRegular(new Vector2(0.5, 0.5), 0.5, 0.5, 0);
			cell.addToModel(model);
		}
		
		sceneNode = new SceneNodeImpl(this.c);
		sceneNode.setModel(model);
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
			vertices = new TessellatorVertex[shapeType];
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
		private Transform position; //Location and orientation if neighbors[0] is in the forward direction
		
		/**
		 * Initializes the seed vertex
		 */
		public TessellatorVertex()
		{
			neighbors = new TessellatorVertex[joinType];
			neighborIndices = new int[joinType];
			faces = new boolean[joinType];
			position = Transform.identity();
		}
		
		/**
		 * Initializes a branching vertex
		 * @param neighbor The neighbor the vertex came from
		 * @param neighborIndex The index of this vertex in the perspective of the neighbor
		 * @param position The location of the vertex
		 */
		private TessellatorVertex(TessellatorVertex neighbor, int neighborIndex, Transform position)
		{
			neighbors = new TessellatorVertex[joinType];
			neighborIndices = new int[joinType];
			faces = new boolean[joinType];
			neighbors[0] = neighbor;
			neighborIndices[0] = neighborIndex;
			this.position = position;
		}
		
		/**
		 * Returns the location of the vertex without the orientation
		 */
		public Vector31 getPosition()
		{
			return position.transform(new Vector31(0, 0, 0, 1));
		}
		
		/**
		 * Returns index modulo 4 such that the value returned is 0, 1, 2, or 3
		 * @param index the index to wrap
		 * @return the index wrapped to be a valid value for an array index
		 */
		private int wrapIndex(int index)
		{
			return ((index%joinType)+joinType)%joinType;
		}
		
		/**
		 * Adds a vertex to the specified array index of the neighbors array
		 * in the proper location for a pentagonal tessellation
		 * @param index the location of the vertex in the neighbors array
		 * @return the newly-added vertex
		 */
		private TessellatorVertex addNeighbor(int index)
		{
			double cosShapeAngle = Math.cos(2 * Math.PI / shapeType);
			double cosJoinAngle = Math.cos(2 * Math.PI / joinType);
			double coshLength = (1 + 2*cosShapeAngle + cosJoinAngle) / (1 - cosJoinAngle);
			Transform translation = Transform.translation(new Vector31(Math.sqrt(coshLength*coshLength-1), 0, 0, coshLength));
			Transform rotation = Transform.rotation(new Vector3(0, 0, 1), Math.PI * 2 / joinType * index);
			Transform rotation2 = Transform.rotation(new Vector3(0, 0, 1), Math.PI);
			
			Transform nextTransform = rotation.transform(translation.transform(rotation2));
			
			neighbors[index] = new TessellatorVertex(this, index, position.transform(nextTransform));
			neighborIndices[index] = 0;
			
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
			for (int i=0; i<joinType; i++)
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
			int edgesLeft = shapeType;
			
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
		sceneNode.setTexture(c.getTextureBank().circle);
		sceneNode.setColor(new float[] {0.42f, 0.54f, 0.14f, 1});
	}
	
	public void render(GL3 gl, Transform t)
	{
		sceneNode.setTransform(t);
		sceneNode.render(gl);
	}
}
