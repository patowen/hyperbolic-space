package net.patowen.hyperbolicspace;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.jogamp.opengl.util.glsl.ShaderCode;

public class ShaderHandler
{
	public static ShaderCode getShaderCode(int type, String fname)
	{
		ClassLoader cl = ShaderCode.class.getClassLoader();
		File file = new File(cl.getResource("net/patowen/hyperbolicspace/shaders/"+fname+".glsl").getFile());
		
		StringBuilder str = new StringBuilder();
		try
		{
			Scanner scan = new Scanner(file);
			
			while (scan.hasNextLine())
			{
				str.append(scan.nextLine() + "\n");
			}
			
			scan.close();
		}
		catch (FileNotFoundException e)
		{
			//The file would only fail to exist in a broken installation
			throw new RuntimeException(e);
		}
		
		ShaderCode code = new ShaderCode(type, 1, new CharSequence[][]{{str.toString()}});
		return code;
	}
}
