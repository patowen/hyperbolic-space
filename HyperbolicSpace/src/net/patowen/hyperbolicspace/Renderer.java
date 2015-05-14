package net.patowen.hyperbolicspace;
import javax.media.nativewindow.WindowClosingProtocol.WindowClosingMode;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;

/**
 * Handles the window and its render loop.
 * @author Patrick Owen
 */
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
			
			//Exit the simulation if the user closes the window
			public void windowDestroyNotify(WindowEvent e)
			{
				c.exit();
			}
		});
		
		win.addGLEventListener(new GLEventListener()
		{
			private Controller c = Renderer.this.c;
			
			//Performs a single frame of the render loop, also updating the physics of the world
			public void display(GLAutoDrawable drawable)
			{
				step(1.0/60);
				render(drawable.getGL().getGL3());
			}
			
			public void dispose(GLAutoDrawable drawable)
			{
				//Nothing to dispose. Garbage collection takes care of everything.
			}
			
			//Configures OpenGL's settings, initializes textures and meshes for rendering, and sets up shaders
			public void init(GLAutoDrawable drawable)
			{
				GL3 gl = drawable.getGL().getGL3();
				c.renderInit(gl);
				gl.glClearColor(0, 0, 0, 1);
				gl.glEnable(GL3.GL_DEPTH_TEST);
				
				c.initShaders(gl);
			}
			
			//Modifies the perspective to match the size of the window
			public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
			{
				//Perspective is done in the player's main method
				world.setViewport(width, height);
			}
		});
		
		initialize();
		c.startAnimation();
	}
	
	/**
	 * Creates the world and initializes input
	 */
	public void initialize()
	{
		c.createInputHandler();
		world = new World(c);
	}
	
	/**
	 * Performs a single frame of the simulation
	 * @param dt the time step in seconds
	 */
	public void step(double dt)
	{
		world.step(dt);
	}
	
	/**
	 * Renders the world at its current state
	 * @param gl
	 */
	public void render(GL3 gl)
	{
		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		c.getMatrixHandler().reset();
		
		world.render(gl);
	}
}
