package net.patowen.hyperbolicspace;

import javax.swing.JOptionPane;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

import net.patowen.hyperbolicspace.model.Cone;
import net.patowen.hyperbolicspace.model.Dodecahedron;
import net.patowen.hyperbolicspace.model.Horosphere;
import net.patowen.hyperbolicspace.model.Plane;
import net.patowen.hyperbolicspace.model.Prism;
import net.patowen.hyperbolicspace.model.Sphere;
import net.patowen.hyperbolicspace.rendering.SceneNodeType;
import net.patowen.hyperbolicspace.rendering.ShaderHandler;
import net.patowen.hyperbolicspace.rendering.ShaderUniformHandler;
import net.patowen.hyperbolicspace.rendering.TextureBank;

/**
 * {@code Controller} acts as a context in which all other classes can access shared
 * data without having to pass things around arbitrary.
 * @author Patrick Owen
 */
public class Controller {
	private FPSAnimator anim;
	private ShaderUniformHandler matrixHandler;
	private InputHandler inputHandler;
	private GLWindow win;
	
	private TextureBank textureBank;
	
	/** A renderable scene node */
	public SceneNodeType dodecahedron, prism, horosphere, plane, sphere, cone;
	
	/**
	 * Constructs all meshes
	 */
	public void init() {
		dodecahedron = new Dodecahedron(this);
		prism = new Prism(this);
		horosphere = new Horosphere(this);
		plane = new Plane(this);
		sphere = new Sphere(this);
		cone = new Cone(this);
	}
	
	/**
	 * Initializes all textures and prepares all meshes for rendering
	 * @param gl
	 */
	public void renderInit(GL3 gl) {
		textureBank = new TextureBank();
		textureBank.initTextures(gl);
		
		dodecahedron.renderInit(gl);
		prism.renderInit(gl);
		horosphere.renderInit(gl);
		plane.renderInit(gl);
		sphere.renderInit(gl);
		cone.renderInit(gl);
	}
	
	/**
	 * Gracefully quits the application
	 */
	public void exit() {
		anim.stop();
	}
	
	/**
	 * Begins the render loop
	 */
	public void startAnimation() {
		anim = new FPSAnimator(win, 60, true);
		anim.start();
	}
	
	/**
	 * Toggles whether the window is fullscreen
	 */
	public void toggleFullscreen() {
		win.setFullscreen(!win.isFullscreen());
	}
	
	/**
	 * Initializes OpenGL and creates a window with the OpenGL3 context
	 */
	public void createWindow() {
		GLCapabilities caps;
		try {
			caps = new GLCapabilities(GLProfile.get("GL3"));
		} catch (GLException e) {
			JOptionPane.showMessageDialog(null, "Your video card does not support OpenGL3, which is required to run this application.",
					"OpenGL3 not supported", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		caps.setDepthBits(24);
		caps.setSampleBuffers(true);
		
		win = GLWindow.create(caps);
	}
	
	/**
	 * Initializes the listener for input
	 */
	public void createInputHandler() {
		inputHandler = new InputHandler(this);
	}
	
	/**
	 * Loads the shader resources and compiles all the shaders
	 * @param gl
	 */
	public void initShaders(GL3 gl) {
		ShaderProgram prog = new ShaderProgram();
		prog.init(gl);
		
		ShaderCode vsCode = ShaderHandler.getShaderCode(GL3.GL_VERTEX_SHADER, "hyperbolic_vs");
		vsCode.compile(gl);
		prog.add(vsCode);
		
		ShaderCode fsCode = ShaderHandler.getShaderCode(GL3.GL_FRAGMENT_SHADER, "hyperbolic_fs");
		fsCode.compile(gl);
		prog.add(fsCode);
		
		ShaderState shaderState = new ShaderState();
		matrixHandler = new ShaderUniformHandler(shaderState);
		shaderState.attachShaderProgram(gl, prog, false);
		shaderState.bindAttribLocation(gl, 0, "vertex_in");
		shaderState.bindAttribLocation(gl, 1, "normal_in");
		shaderState.bindAttribLocation(gl, 2, "tex_coord_in");
		
		prog.link(gl, System.err);
		prog.validateProgram(gl, System.err);
		shaderState.useProgram(gl, true);
	}
	
	/**
	 * Returns the main {@code MatrixHandler} object
	 * @return a reference to the main {@code MatrixHandler} object
	 */
	public ShaderUniformHandler getMatrixHandler() {
		return matrixHandler;
	}
	
	/**
	 * Returns the main {@code InputHandler} object
	 * @return a reference to the main {@code InputHandler} object
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}
	
	/**
	 * Returns the main window
	 * @return a reference to the main window
	 */
	public GLWindow getWindow() {
		return win;
	}
	
	/**
	 * Returns the main {@code TextureBank} object
	 * @return a reference to the main {@code TextureBank} object
	 */
	public TextureBank getTextureBank() {
		return textureBank;
	}
}
