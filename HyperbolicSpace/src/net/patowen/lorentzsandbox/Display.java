package net.patowen.lorentzsandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	private DrawHelper helper;
	
	private ArrayList<Node> nodes;
	private Node activeNode;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Lorentz Sandbox");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Display display = new Display();
		frame.add(display);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public Display() {
		setPreferredSize(new Dimension(480, 480));
		helper = new DrawHelper();
		
		nodes = new ArrayList<Node>();
		nodes.add(new Node(new Vector21(-1, 0, 2)));
		nodes.add(new Node(new Vector21(0, 1, 2)));
		activeNode = null;
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		
		helper.setBounds(getWidth(), getHeight());
		draw(g);
	}
	
	public void draw(Graphics2D g) {
		helper.drawEuclideanCircle(g, new Vector2(), 1, false);
		
		for (int i=0; i<nodes.size(); i++)
			helper.drawEuclideanCircle(g, nodes.get(i).v.poincare(), 0.05, false);
		
		helper.drawLineSegment(g, nodes.get(0).v.poincare(), nodes.get(1).v.poincare());
		helper.drawLineSegment(g, new Vector2(0, 0), nodes.get(1).v.reverseTranslate(nodes.get(0).v).poincare());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (activeNode != null) {
			Vector2 v = new Vector2(helper.getX(e.getX()), helper.getY(e.getY()));
			double magnitude = v.magnitude();
			if (magnitude > 0.999)
				v = v.times(0.999/magnitude);
			activeNode.v = Vector21.makePoincare(v);
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		Vector2 mouse = new Vector2(helper.getX(e.getX()), helper.getY(e.getY()));
		
		double maxDist = 0.05;
		activeNode = null;
		
		for (Node node : nodes) {
			double newDist = node.v.poincare().minus(mouse).magnitude();
			if (newDist < maxDist) {
				maxDist = newDist;
				activeNode = node;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		activeNode = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
