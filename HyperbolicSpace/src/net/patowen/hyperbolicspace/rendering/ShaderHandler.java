package net.patowen.hyperbolicspace.rendering;

import java.io.InputStream;
import java.util.Scanner;

import com.jogamp.opengl.util.glsl.ShaderCode;

/**
 * Helper class to allow shaders to be loaded from files in the shaders package
 * @author Patrick Owen
 */
public class ShaderHandler {
	/**
	 * Returns a {@code ShaderCode} object for the specified file
	 * @param type GL_VERTEX_SHADER, GL_GEOMETRY_SHADER, GL_FRAGMENT_SHADER, etc.
	 * @param fname the name of the shader file, without its directory structure
	 * @return the fully prepared, but not compiled, {@code ShaderCode} object
	 */
	public static ShaderCode getShaderCode(int type, String fname) {
		ClassLoader cl = ShaderHandler.class.getClassLoader();
		InputStream stream = cl.getResourceAsStream("net/patowen/hyperbolicspace/shaders/"+fname+".glsl");
		
		StringBuilder str = new StringBuilder();
		
		Scanner scan = new Scanner(stream);
		
		while (scan.hasNextLine()) {
			str.append(scan.nextLine() + "\n");
		}
		
		scan.close();
		
		ShaderCode code = new ShaderCode(type, 1, new CharSequence[][]{{str.toString()}});
		return code;
	}
}
