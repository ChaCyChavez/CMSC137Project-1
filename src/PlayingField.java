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
import java.awt.Point;
import java.awt.Color;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayingField extends JPanel implements Runnable {
	private String text;
	private int dx;
	private int dy;

	private boolean isUp, isDown, isLeft, isRight;
	private boolean running = false;

	private Thread thread;
	private BufferedImage offscreen;

	private String server;
	private String playerName;
	private String dataFromServer;
	private DatagramSocket socket;
	private boolean isConnected = false;
	private Thread t = new Thread(this);

	private ArrayList playerCoordinates = new ArrayList();

	public PlayingField(String server, String playerName) {
		super();
		System.out.println("playingfield player name: " + playerName);
		System.out.println("playingfield server name: " + server);

		this.server = server;
		this.playerName = playerName;
		try {
			socket = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}

    this.setFocusable(true);
		this.requestFocusInWindow();

		dx = 50;
		dy = 50;

		Action up = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				isUp = true; isDown = false; isRight = false; isLeft = false;
			}
		};

		Action down = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				isUp = false; isDown = true; isRight = false; isLeft = false;
			}
		};

		Action right = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				isUp = false; isDown = false; isRight = true; isLeft = false;
			}
		};

		Action left = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				isUp = false; isDown = false; isRight = false; isLeft = true;
			}
		};

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "upPressed");
    this.getActionMap().put("upPressed", up);
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "downPressed");
    this.getActionMap().put("downPressed", down);
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "leftPressed");
    this.getActionMap().put("leftPressed", left);
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "rightPressed");
    this.getActionMap().put("rightPressed", right);

		t.start();
	}

	public void sendMessage(String message) {
		try {
			byte[] buffer = message.getBytes();
			InetAddress address = InetAddress.getByName(server);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 4444);
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		// g2D.fillOval(dx, dy, 25, 25);
		int limit = playerCoordinates.size();
		for(int i = 0; i < limit; i++) {
			Point player = (Point) playerCoordinates.remove(0);
			g2D.fillOval(player.x, player.y, 25, 25);
		}
		g2D.dispose();
		// for(Iterator i = playerCoordinates.keySet().iterator(); i.hasNext();){
		// 	String playerName = (String) i.next();
		// 	Point point = (Point) playerCoordinates.remove(playerName);
			// g2D.setColor(new Color((int)(Math.random() * 0x1000000)));	
			// g2D.fillOval(point.x, point.y, 25, 25);
			// sendMessage(player, message);	
		// }
	}

	public synchronized void start() {
		if(running) return;

		running = true;

		thread = new Thread(this);
		thread.start();
	}

	private synchronized void stop() {
		if(!running) return;

		running = false;
		try {
			thread.join();
		} catch(InterruptedException ie) {
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
			//to check FPS

			// long now = System.nanoTime();
			// delta += (now - lastTime) / ns;
			// lastTime = now;
			// if(delta >= 1) {
			// 	delta--;
			// 	updates++;
			// }	
			// frames++;

			// if(System.currentTimeMillis() - timer > 1000) {
			// 	timer += 1000;
			// 	System.out.println(updates + " Ticks, fps " + frames);
			// 	updates = 0;
			// 	frames = 0;
			// }
			/**/ 
			// try{
			// 	Thread.sleep(1);
			// }catch(Exception ioe){}

			// if(!isConnected) {
			// 	isConnected = true;
			// 	System.out.println("Connected.");
			// } else 
			if (!isConnected){
				isConnected = true;
				sendMessage("CONNECT "+ playerName);
			} else if(isConnected) {
				if(isUp) dy -= 1;
				else if(isDown) dy += 1;
				else if(isLeft) dx -= 1;
				else if(isRight) dx += 1;
				// playerCoordinates.add(new Point(dx, dy));				
				sendMessage("PLAYER " + playerName + " "+ dx + " "+ dy);		
				
				byte[] buffer = new byte[256];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(packet);
				} catch (Exception e) {
					e.printStackTrace();
				}

				dataFromServer = new String(buffer);
				dataFromServer = dataFromServer.trim();

				String[] playersInfo = dataFromServer.split(":");
					for (int i=0;i<playersInfo.length;i++){
						String[] playerInfo = playersInfo[i].split(" ");
						try {
							String pname = playerInfo[1];
							int x = Integer.parseInt(playerInfo[2]);
							int y = Integer.parseInt(playerInfo[3]);
							playerCoordinates.add(new Point(x, y));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				// !!!catch outofbounds exception!!!
				repaint();
			}

			try {
				Thread.sleep(10);
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
}