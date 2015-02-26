package net.patowen.hyperbolicspace;

import java.io.InputStream;
import java.util.Scanner;

import com.jogamp.opengl.util.glsl.ShaderCode;

public class ShaderHandler
{
	public static ShaderCode getShaderCode(int type, String fname)
	{
		ClassLoader cl = ShaderHandler.class.getClassLoader();
		InputStream stream = cl.getResourceAsStream("net/patowen/hyperbolicspace/shaders/"+fname+".glsl");
		
		StringBuilder str = new StringBuilder();
		
		Scanner scan = new Scanner(stream);
		
		while (scan.hasNextLine())
		{
			str.append(scan.nextLine() + "\n");
		}
		
		scan.close();
		
		ShaderCode code = new ShaderCode(type, 1, new CharSequence[][]{{str.toString()}});
		return code;
	}
}
