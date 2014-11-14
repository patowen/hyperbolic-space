package net.patowen.hyperbolicspace;
import javax.media.nativewindow.WindowClosingProtocol.WindowClosingMode;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLUniformData;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;


public class Renderer
{	
	private GLWindow win;
	private World world;
	private ShaderState shaderState;
	private FPSAnimator anim;
	
	private MatrixHandler mh;
		
	public Renderer(GLWindow win)
	{
		this.win = win;
		win.setSize(1024, 768);
		
		win.setTitle("Hyperbolic space");
		win.setDefaultCloseOperation(WindowClosingMode.DISPOSE_ON_CLOSE);
		
		win.addWindowListener(new WindowAdapter()
		{
			public void windowDestroyNotify(WindowEvent e)
			{
				anim.stop();
			}
		});
		
		win.addGLEventListener(new GLEventListener()
		{
			public void display(GLAutoDrawable drawable)
			{
				step(1.0/60);
				render(drawable.getGL().getGL2());
			}
			
			public void dispose(GLAutoDrawable drawable)
			{
				
			}
			
			public void init(GLAutoDrawable drawable)
			{
				GL2 gl = drawable.getGL().getGL2();
				gl.glClearColor(0, 0, 0, 1);
				gl.glEnable(GL.GL_DEPTH_TEST);
				gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
				
				ShaderProgram prog = new ShaderProgram();
				prog.init(gl);
				
				ShaderCode vsCode = ShaderHandler.getShaderCode(GL2.GL_VERTEX_SHADER, "hyperbolic_vs");
				vsCode.compile(gl);
				prog.add(vsCode);
				
				ShaderCode fsCode = ShaderHandler.getShaderCode(GL2.GL_FRAGMENT_SHADER, "hyperbolic_fs");
				fsCode.compile(gl);
				prog.add(fsCode);
				
				prog.link(gl, System.err);
				prog.validateProgram(gl, System.err);
				
				shaderState = new ShaderState();
				mh = new MatrixHandler(shaderState);
				shaderState.attachShaderProgram(gl, prog, true);
				shaderState.uniform(gl, new GLUniformData("inputColor", 4, Buffers.newDirectFloatBuffer(new float[] {1, 1, 1, 1})));
			}
			
			public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
			{
				GL2 gl = drawable.getGL().getGL2();
				gl.glMatrixMode(GL2.GL_PROJECTION);
				gl.glLoadIdentity();
				
				float[] mat = new float[16];
				mh.setPerspective(FloatUtil.makePerspective(mat, 0, true, 0.78f, (float)width/height, 0.01f, 8.1f));
			}
		});
		
		anim = new FPSAnimator(win, 60);
		anim.start();
		
		initialize();
	}
	
	public void initialize()
	{
		world = new World(new InputHandler(win));
	}
	
	public void step(double dt)
	{
		world.step(dt);
	}
	
	public void render(GL2 gl)
	{		
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
//		gl.glLoadIdentity();
		mh.reset();
		
		world.render(mh, gl);
	}
}
