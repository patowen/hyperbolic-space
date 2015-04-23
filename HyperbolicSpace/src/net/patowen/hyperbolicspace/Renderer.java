package net.patowen.hyperbolicspace;
import javax.media.nativewindow.WindowClosingProtocol.WindowClosingMode;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.math.FloatUtil;


public class Renderer
{
	private Controller c;
	
	private World world;
	
	public Renderer(Controller c)
	{
		this.c = c;
		
		GLWindow win = c.getWindow();
		win.setSize(1024, 768);
		
		win.setTitle("Hyperworld");
		win.setDefaultCloseOperation(WindowClosingMode.DO_NOTHING_ON_CLOSE);
		
		win.addWindowListener(new WindowAdapter()
		{
			private Controller c = Renderer.this.c;
			
			public void windowDestroyNotify(WindowEvent e)
			{
				c.exit();
			}
		});
		
		win.addGLEventListener(new GLEventListener()
		{
			private Controller c = Renderer.this.c;
			
			public void display(GLAutoDrawable drawable)
			{
				step(1.0/60);
				render(drawable.getGL().getGL3());
			}
			
			public void dispose(GLAutoDrawable drawable)
			{
				
			}
			
			public void init(GLAutoDrawable drawable)
			{
				GL3 gl = drawable.getGL().getGL3();
				gl.glClearColor(0, 0, 0, 1);
				gl.glEnable(GL3.GL_DEPTH_TEST);
				
				c.initShaders(gl);
				
				world.renderInit(gl);
				
			}
			
			public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
			{
				float[] mat = new float[16];
				c.getMatrixHandler().setPerspective(FloatUtil.makePerspective(mat, 0, true, 0.78f, (float)width/height, 0.01f, 8.1f));
			}
		});
		
		initialize();
		c.startAnimation();
	}
	
	public void initialize()
	{
		c.createInputHandler();
		world = new World(c);
	}
	
	public void step(double dt)
	{
		world.step(dt);
	}
	
	public void render(GL3 gl)
	{
		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		c.getMatrixHandler().reset();
		
		world.render(gl);
	}
}
