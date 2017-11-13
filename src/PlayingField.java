import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import java.awt.Graphics;
import java.awt.Graphics2D;


public class PlayingField extends JPanel implements Runnable {
	private String text;
	private int dx;
	private int dy;

	public PlayingField() {
		super();

    this.setFocusable(true);
		this.requestFocusInWindow();

		dx = 50;
		dy = 50;

		Action up = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dy -= 5;
				repaint();
			}
		};

		Action down = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dy += 5;
				repaint();
			}
		};

		Action right = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dx += 5;
				repaint();
			}
		};

		Action left = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dx -= 5;
				repaint();
			}
		};

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        KeyStroke.getKeyStroke("UP"), "upPressed");
    this.getActionMap().put("upPressed", up);
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        KeyStroke.getKeyStroke("DOWN"), "downPressed");
    this.getActionMap().put("downPressed", down);
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        KeyStroke.getKeyStroke("LEFT"), "leftPressed");
    this.getActionMap().put("leftPressed", left);
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        KeyStroke.getKeyStroke("RIGHT"), "rightPressed");
    this.getActionMap().put("rightPressed", right);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D)g;

		g2D.fillOval(dx, dy, 50, 50);
	}

	@Override
	public void run() {
		
	}
	 
}