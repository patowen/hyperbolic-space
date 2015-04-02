package net.patowen.poincaresandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private DrawHelper helper;

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Poincare Sandbox");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Display display = new Display();
		frame.add(display);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public Display()
	{
		setPreferredSize(new Dimension(480, 480));
		helper = new DrawHelper();
	}
	
	public void paint(Graphics g1)
	{
		Graphics2D g = (Graphics2D)g1;
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		
		helper.setBounds(getWidth(), getHeight());
		draw(g);
	}
	
	public void draw(Graphics2D g)
	{
		helper.drawEuclideanCircle(g, new Vector2(), 1);
	}
}
