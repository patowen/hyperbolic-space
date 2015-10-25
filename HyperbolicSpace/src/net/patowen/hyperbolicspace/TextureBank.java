package net.patowen.hyperbolicspace;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

/**
 * Loads and stores all textures, including a placeholder, that are used in
 * the application
 * @author Patrick Owen
 */
public class TextureBank
{
	//Magenta and black checkerboard texture used when a texture is missing or
	//fails to load.
	private Texture placeholder;
	
	/**
	 * One of the textures used in the application
	 */
	public Texture stone, metal, clouds;
	
	/**
	 * Loads all textures into the OpenGL context. This contributes the most
	 * to the loading time of the application
	 * @param gl
	 */
	public void initTextures(GL3 gl)
	{
		createPlaceholderTexture(gl);
		
		stone = createTextureFromFile(gl, "stone.jpg");
		metal = createTextureFromFile(gl, "metal.png");
		clouds = createTextureFromFile(gl, "flat_clouds.png");
	}
	
	/**
	 * Loads and returns the texture corresponding to the given filename
	 * @param gl
	 * @param fname the filename of the texture, without its path
	 * @return the newly-created texture
	 */
	private Texture createTextureFromFile(GL3 gl, String fname)
	{
		try
		{
			Texture tex = TextureIO.newTexture(new File("textures", fname), true);
			gl.glGenerateMipmap(tex.getTarget());
			return tex;
		}
		catch (IOException e)
		{
			return placeholder;
		}
	}
	
	/**
	 * Creates a 2x2 magenta and black checkerboard pattern to use as a texture if
	 * the correct texture is missing
	 * @param gl
	 */
	private void createPlaceholderTexture(GL3 gl)
	{
		BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		raster.setPixels(0, 0, 2, 2, new int[] {255, 0, 255, 0, 0, 0, 0, 0, 0, 255, 0, 255});
		
		placeholder = AWTTextureIO.newTexture(gl.getGLProfile(), image, false);
		placeholder.setTexParameteri(gl, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST);
	}
}
