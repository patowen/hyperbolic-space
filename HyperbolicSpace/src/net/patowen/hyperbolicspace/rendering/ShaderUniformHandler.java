package net.patowen.hyperbolicspace.rendering;

import java.util.Stack;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.glsl.ShaderState;

import net.patowen.hyperbolicspace.math.Transform;
import net.patowen.hyperbolicspace.math.Vector31;

/**
 * {@code ShaderUniformHandler} has two purposes. It holds a Transformation stack for the viewpoint and
 * for hierarchical models, and it interacts with the shader to set the uniforms to the appropriate
 * values.
 * @author Patrick Owen
 */
public class ShaderUniformHandler {
	private ShaderState shaderState;
	
	// Matrices
	private ShaderLink<UniformTransform> transform;
	private ShaderLink<UniformFloatList> perspective;
	private ShaderLink<UniformFloatList> color;
	
	// Lights
	private int numLights;
	private ShaderLink<UniformFloatList> lightAmbient;
	private ShaderLink<UniformVector31List> lightPosition;
	private ShaderLink<UniformFloatList> lightDiffuse;
	
	// Material
	private ShaderLink<UniformFloatList> materialAmbient;
	private ShaderLink<UniformFloatList> materialDiffuse;
	
	private Stack<Transform> transformStack;
	private Stack<Vector31[]> lightPositionStack;
	
	/**
	 * Initializes all values to their defaults
	 * @param shaderState the {@code ShaderState} representing the shader whose
	 * uniforms are set
	 */
	public ShaderUniformHandler(ShaderState shaderState) {
		this.shaderState = shaderState;
		transformStack = new Stack<>();
		lightPositionStack = new Stack<>();
		numLights = 1;
		
		transform = new ShaderLink<>("transform", new UniformTransform(Transform.identity()));
		perspective = new ShaderLink<>("perspective", new UniformFloatList(4, 4));
		color = new ShaderLink<>("color", new UniformFloatList(4));
		
		lightAmbient = new ShaderLink<>("light_ambient", new UniformFloatList(3));
		lightPosition = new ShaderLink<>("light_position", new UniformVector31List(numLights));
		lightDiffuse = new ShaderLink<>("light_diffuse", new UniformFloatList(3, 1, numLights));
		
		materialAmbient = new ShaderLink<>("material_ambient", new UniformFloatList(3));
		materialDiffuse = new ShaderLink<>("material_diffuse", new UniformFloatList(3));
	}
	
	/**
	 * Sets the stored transformation to the identity
	 */
	public void reset() {
		transform.data.transform = Transform.identity();
	}
	
	/**
	 * Adds the specified transformation relative to the current transformation
	 * @param t The transformation to apply before the current one
	 */
	public void addTransformation(Transform t) {
		transform.data.transform = transform.data.transform.transform(t);
		
		Transform tInv = t.inverse();
		for (int light = 0; light < numLights; light++) {
			lightPosition.data.vectors[light] = tInv.transform(lightPosition.data.vectors[light]);
		}
	}
	
	/**
	 * Stores the current transformation onto a stack for later retrieval
	 */
	public void pushTransformation() {
		transformStack.push(transform.data.transform);
		lightPositionStack.push(lightPosition.data.vectors.clone());
	}
	
	/**
	 * Sets the current transformation to the previously stored transformation
	 * on the stack
	 */
	public void popTransformation() {
		transform.data.transform = transformStack.pop();
		lightPosition.data.vectors = lightPositionStack.pop();
	}
	
	/**
	 * Sets the stored perspective matrix to the given matrix
	 * @param perspective the perspective matrix represented via a
	 * 16-value array in column-major order
	 */
	public void setPerspective(float[] perspective) {
		System.arraycopy(perspective, 0, this.perspective.data.list, 0, 16);
	}
	
	/**
	 * Sets the stored color to the given color
	 * @param color the color represented via a 4-value array (r, g, b, a)
	 */
	public void setColor(float[] color) {
		System.arraycopy(color, 0, this.color.data.list, 0, 4);
	}
	
	public void setLightAmbient(float[] color) {
		System.arraycopy(color, 0, lightAmbient.data.list, 0, 3);
	}
	
	public void setLightPosition(int light, Vector31 pos) {
		lightPosition.data.vectors[light] = pos;
	}
	
	public void setLightDiffuse(int light, float[] color) {
		System.arraycopy(color, 0, lightDiffuse.data.list, 3*light, 3);
	}
	
	public void setMaterialAmbient(float[] color) {
		System.arraycopy(color, 0, materialAmbient.data.list, 0, 3);
	}
	
	public void setMaterialDiffuse(float[] color) {
		System.arraycopy(color, 0, materialDiffuse.data.list, 0, 3);
	}
	
	/**
	 * Sets all the uniforms of the shader to the currently stored values. This
	 * should be called before drawing anything if anything was changed.
	 * @param gl
	 */
	public void update(GL3 gl) {
		transform.update(shaderState, gl);
		perspective.update(shaderState, gl);
		color.update(shaderState, gl);
		
		lightAmbient.update(shaderState, gl);
		lightPosition.update(shaderState, gl);
		lightDiffuse.update(shaderState, gl);
		
		materialAmbient.update(shaderState, gl);
		materialDiffuse.update(shaderState, gl);
	}
}
