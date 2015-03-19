package net.patowen.hyperbolicspace;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.swing.JOptionPane;

import com.jogamp.newt.opengl.GLWindow;


public class Runner
{
	public static void main(String[] args)
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
		
		GLWindow win = GLWindow.create(caps);
		
		new Renderer(win);
		
		win.setVisible(true);
	}
}
