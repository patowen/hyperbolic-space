package net.patowen.hyperbolicspace;

import javax.media.opengl.GL3;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLUniformData;
import javax.swing.JOptionPane;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

/**
 * Controller acts as a context in which all other classes can access shared
 * data without having to pass things around randomly.
 * @author Patrick Owen
 */
public class Controller
{
	private FPSAnimator anim;
	private MatrixHandler matrixHandler;
	private InputHandler inputHandler;
	private GLWindow win;
	
	public SceneNodeType dodecahedron, building, horosphere;
	
	public void init()
	{
		dodecahedron = new Dodecahedron(this);
		building = new Building(this);
		horosphere = new Horosphere(this);
	}
	
	public void renderInit(GL3 gl)
	{
		dodecahedron.renderInit(gl);
		building.renderInit(gl);
		horosphere.renderInit(gl);
	}
	
	public void exit()
	{
		anim.stop();
	}
	
	
	public void startAnimation()
	{
		anim = new FPSAnimator(win, 60);
		anim.start();
	}
	
	public void createWindow()
	{
		GLCapabilities caps;
		try
		{
			caps = new GLCapabilities(GLProfile.get("GL3"));
		}
		catch (GLException e)
		{
			JOptionPane.showMessageDialog(null, "Your video card does not support OpenGL3, which is required to run this application.",
					"OpenGL3 not supported", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		win = GLWindow.create(caps);
	}
	
	public void createInputHandler()
	{
		inputHandler = new InputHandler(this);
	}
	
	public void initShaders(GL3 gl)
	{
		ShaderProgram prog = new ShaderProgram();
		prog.init(gl);
		
		ShaderCode vsCode = ShaderHandler.getShaderCode(GL3.GL_VERTEX_SHADER, "hyperbolic_vs");
		vsCode.compile(gl);
		prog.add(vsCode);
		
		ShaderCode gsCode = ShaderHandler.getShaderCode(GL3.GL_GEOMETRY_SHADER, "hyperbolic_gs");
		gsCode.compile(gl);
		prog.add(gsCode);
		
		ShaderCode fsCode = ShaderHandler.getShaderCode(GL3.GL_FRAGMENT_SHADER, "hyperbolic_fs");
		fsCode.compile(gl);
		prog.add(fsCode);
		
		ShaderState shaderState = new ShaderState();
		matrixHandler = new MatrixHandler(shaderState);
		shaderState.attachShaderProgram(gl, prog, false);
		shaderState.bindAttribLocation(gl, 0, "vertex_position");
		shaderState.bindAttribLocation(gl, 1, "normal_position");
		shaderState.bindAttribLocation(gl, 2, "tex_coord_in");
		
		prog.link(gl, System.err);
		prog.validateProgram(gl, System.err);
		shaderState.useProgram(gl, true);
		
		shaderState.uniform(gl, new GLUniformData("inputColor", 4, Buffers.newDirectFloatBuffer(new float[] {1, 1, 1, 1})));
	}
	
	public MatrixHandler getMatrixHandler()
	{
		return matrixHandler;
	}
	
	public InputHandler getInputHandler()
	{
		return inputHandler;
	}
	
	public GLWindow getWindow()
	{
		return win;
	}
}
