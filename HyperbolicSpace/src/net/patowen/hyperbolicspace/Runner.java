package net.patowen.hyperbolicspace;



public class Runner
{
	private static Controller c;
	
	public static void main(String[] args)
	{
		c = new Controller();
		c.createWindow();
		
		new Renderer(c);
		
		c.getWindow().setVisible(true);
	}
}
