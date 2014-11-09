package net.patowen.hyperbolicspace;

import java.io.File;
import java.util.ArrayList;

import com.jogamp.opengl.util.glsl.ShaderCode;

public class ShaderHandler
{
	public void createShaders(int type, String fname)
	{
		ClassLoader cl = getClass().getClassLoader();
		File file = new File(cl.getResource("shaders/"+fname).getFile());
		
		StringBuilder str = new StringBuilder();
		
		ShaderCode code = new ShaderCode(type, 1, new CharSequence[][]{{"Hi"}});
		
	}
}
