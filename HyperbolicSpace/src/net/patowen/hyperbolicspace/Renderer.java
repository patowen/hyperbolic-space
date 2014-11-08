package net.patowen.hyperbolicspace;
import javax.media.nativewindow.WindowClosingProtocol.WindowClosingMode;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;


public class Renderer
{	
	private GLWindow win;
	private World world;
	private FPSAnimator anim;
		
	public Renderer(GLWindow win)
	{
		this.win = win;
		win.setSize(1024, 768);
		
		win.setTitle("Hyperbolic space");
		win.setDefaultCloseOperation(WindowClosingMode.DO_NOTHING_ON_CLOSE);
		
		win.addWindowListener(new WindowAdapter()
		{
			public void windowDestroyNotify(WindowEvent e)
			{
				anim.stop();
			}
		});
		
		win.setPointerVisible(false);
		
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
				GL gl = drawable.getGL();
				gl.glClearColor(0, 0, 0, 1);
				gl.glEnable(GL.GL_DEPTH_TEST);
			}
			
			public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
			{
				GL2 gl = drawable.getGL().getGL2();
				gl.glMatrixMode(GL2.GL_PROJECTION);
				gl.glLoadIdentity();
				
				GLU glu = new GLU();
				glu.gluPerspective(45, (double)width/height, 0.01, 8.1);
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
		gl.glLoadIdentity();
		
		world.draw(gl);
	}
}
