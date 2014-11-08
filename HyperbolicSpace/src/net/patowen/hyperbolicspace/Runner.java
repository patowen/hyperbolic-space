package net.patowen.hyperbolicspace;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import com.jogamp.newt.opengl.GLWindow;


public class Runner
{
	public static void main(String[] args)
	{
		GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());
		GLWindow win = GLWindow.create(caps);
		
		new Renderer(win);
		
		win.setVisible(true);
	}
}
