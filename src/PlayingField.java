import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;


public class PlayingField extends JPanel implements Runnable {
	private String text;
	private int dx;
	private int dy;

	private boolean isUp, isDown, isLeft, isRight;
	private boolean running = false;

	private Thread thread;
	private BufferedImage offscreen;

	public PlayingField() {
		super();

    this.setFocusable(true);
		this.requestFocusInWindow();

		dx = 50;
		dy = 50;

		Action up = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				isUp = true;
				isDown = false;
				isRight = false;
				isLeft = false;
			}
		};

		Action down = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				isUp = false;
				isDown = true;
				isRight = false;
				isLeft = false;
			}
		};

		Action right = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				isUp = false;
				isDown = false;
				isRight = true;
				isLeft = false;
			}
		};

		Action left = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				isUp = false;
				isDown = false;
				isRight = false;
				isLeft = true;
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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D)g;

		g2D.fillOval(dx, dy, 25, 25);

	}

	public synchronized void start() {
		if(running) {
			return;
		}

		running = true;

		thread = new Thread(this);
		thread.start();
	}

	private synchronized void stop() {
		if(!running) {
			return;
		}

		running = false;
		try {
			thread.join();
		}
		catch(InterruptedException ie) {
			ie.printStackTrace();
		}

		System.exit(1);
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();


		while(true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				delta--;
				updates++;
			}	
			frames++;

			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " Ticks, fps " + frames);
				updates = 0;
				frames = 0;
			}

			if(isUp) {
				dy -= 1;
			}
			else if(isDown) {
				dy += 1;
			}
			else if(isLeft) {
				dx -= 1;
			}
			else if(isRight) {
				dx += 1;
			}

			repaint();

			try {
				Thread.sleep(10);
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
}