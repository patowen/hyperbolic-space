package net.patowen.hyperbolicspace;

/**
 * This class is the entry point of the application.
 * @author Patrick Owen
 */
public class Runner
{
	/**
	 * Entry point of the application
	 * @param args
	 */
	public static void main(String[] args)
	{
		Controller c = new Controller();
		c.init();
		c.createWindow();
		
		new Renderer(c);
		
		c.getWindow().setVisible(true);
	}
}
