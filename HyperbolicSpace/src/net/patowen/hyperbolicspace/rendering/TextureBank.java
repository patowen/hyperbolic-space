package net.patowen.hyperbolicspace.rendering;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

/**
 * Loads and stores all textures, including a placeholder, that are used in
 * the application
 * @author Patrick Owen
 */
public class TextureBank {
	//Magenta and black checkerboard texture used when a texture is missing or
	//fails to load.
	private Texture placeholder;
	
	/**
	 * One of the textures used in the application
	 */
	public Texture stone, metal, clouds, circle, tile;
	
	/**
	 * Loads all textures into the OpenGL context. This contributes the most
	 * to the loading time of the application
	 * @param gl
	 */
	public void initTextures(GL3 gl) {
		createPlaceholderTexture(gl);
		
		stone = createTextureFromFile(gl, "stone.jpg", "jpg"); //http://wdc3d.com/blog/textures/6-seamless-stone-textures-1/
		metal = createTextureFromFile(gl, "metal.png", "png");
		clouds = createTextureFromFile(gl, "flat_clouds.png", "png");
		circle = createTextureFromFile(gl, "circle.png", "png");
		tile = createTextureFromFile(gl, "tile.png", "png");
	}
	
	/**
	 * Loads and returns the texture corresponding to the given filename
	 * @param gl
	 * @param fname the filename of the texture, without its path
	 * @return the newly-created texture
	 */
	private Texture createTextureFromFile(GL3 gl, String fname, String extension) {
		try {
			ClassLoader cl = TextureBank.class.getClassLoader();
			InputStream stream = cl.getResourceAsStream("net/patowen/hyperbolicspace/textures/"+fname);
			Texture tex = TextureIO.newTexture(stream, true, extension);
			gl.glGenerateMipmap(tex.getTarget());
			return tex;
		} catch (IOException e) {
			return placeholder;
		}
	}
	
	/**
	 * Creates an 8x8 magenta and black checkerboard pattern to use as a texture if
	 * the correct texture is missing
	 * @param gl
	 */
	private void createPlaceholderTexture(GL3 gl) {
		BufferedImage image = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		float[] black = new float[] {0, 0, 0};
		float[] magenta = new float[] {255, 0, 255};
		for (int i=0; i<image.getWidth(); i++) {
			for (int j=0; j<image.getHeight(); j++) {
				raster.setPixel(i, j, (i+j)%2==0 ? magenta : black);
			}
		}
		
		placeholder = AWTTextureIO.newTexture(gl.getGLProfile(), image, false);
		placeholder.setTexParameteri(gl, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST);
	}
}
