package net.patowen.poincaresandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel implements MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	
	private DrawHelper helper;
	
	private Vector2 mouse;

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
		
		mouse = new Vector2();
		
		addMouseMotionListener(this);
	}
	
	public void paint(Graphics g1)
	{
		Graphics2D g = (Graphics2D)g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		
		helper.setBounds(getWidth(), getHeight());
		draw(g);
	}
	
	public void draw(Graphics2D g)
	{
		helper.drawEuclideanCircle(g, new Vector2(), 1, false);
		helper.drawCircle(g, mouse, 0.3, true);
		helper.drawLineSegment(g, new Vector2(0.5, 0), mouse);
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouse = new Vector2(helper.getX(e.getX()), helper.getY(e.getY()));
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
}
