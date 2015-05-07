package net.patowen.hyperbolicspace;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL3;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class TextureBank
{
	private Texture placeholder;
	
	public Texture stone, metal, clouds;
	
	public void initTextures(GL3 gl)
	{
		createPlaceholderTexture(gl);
		
		stone = createTextureFromFile(gl, "stone.jpg");
		metal = createTextureFromFile(gl, "metal.png");
		clouds = createTextureFromFile(gl, "flat_clouds.png");
	}
	
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
	
	private void createPlaceholderTexture(GL3 gl)
	{
		BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		raster.setPixels(0, 0, 2, 2, new int[] {255, 0, 255, 0, 0, 0, 0, 0, 0, 255, 0, 255});
		
		placeholder = AWTTextureIO.newTexture(gl.getGLProfile(), image, false);
		placeholder.setTexParameteri(gl, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST);
	}
}
