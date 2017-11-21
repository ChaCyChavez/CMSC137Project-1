import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.Toolkit;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class PlayingField extends Canvas implements Runnable {
  private boolean running = false;
  private Thread thread;

  public LinkedList<GameObject> objects = new LinkedList<GameObject>();
  private GameObject tempObject;

  private String server;
  private String playerName;
  private String dataFromServer;
  private DatagramSocket socket;
  private boolean isConnected = false;

  private ArrayList playerCoordinates = new ArrayList();

  public PlayingField(String playerName) {
    this.playerName = playerName;
    try {
      socket = new DatagramSocket();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init() {

		// this.objects.add(new Circle(50, 50, playerName));

    // this.objects.add(new Block(100, 100, 50, 50));

    // this.objects.add(new Block(5, 0, 1014, 5));
    // this.objects.add(new Block(1014, 5, 5, 610));
    // this.objects.add(new Block(0, 610, 1014, 5));
    // this.objects.add(new Block(0, 0, 5, 610));

    //add objects or players here

    this.addKeyListener(new KeyInput(objects, playerName));
  }

  public synchronized void start() {
    if(running) return;
    running = true;
    thread = new Thread(this);
    thread.start();
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

  public void run() {
    init();
    this.requestFocus();

    long lastTime = System.nanoTime();
    double amountOfTicks = 60.0;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;
    long timer = System.currentTimeMillis();
    int updates = 0;
    int frames = 0;
    while(running) {
      try{
        Thread.sleep(1);
      }catch(Exception ioe){}
            
      byte[] buf = new byte[256]; //Get the data from players
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      try{
          socket.receive(packet);
      } catch(Exception ioe){/*lazy exception handling :)*/}
      
      dataFromServer = new String(buf);
      dataFromServer = dataFromServer.trim();

      if (!isConnected && dataFromServer.startsWith("CONNECTED")){
				isConnected = true;
				System.out.println("Connected.");
			} else if (!isConnected){
				System.out.println("Connecting..");				
				sendMessage("CONNECT " + playerName);
			} else if (isConnected){
				if (dataFromServer.startsWith("PLAYER_LIST")) {
          String[] playerNames = dataFromServer.split(" ");
					for (int i = 1; i < playerNames.length; i++){
            System.out.println("PlayingField player: " + playerNames[i]);
          }
        }
        
        if (dataFromServer.startsWith("PLAYER")){
          long now = System.nanoTime();
          delta += (now - lastTime) / ns;
          lastTime = now;
          while(delta >= 1) {     
            tick(); //adjusts the position of each object
            updates++;
            delta--;
          }

          render(); //renders the background, renders each object
          frames++;

          if(System.currentTimeMillis() - timer > 1000) {
            timer += 1000;
            // fps = frames;
            // ticks = updates;
            System.out.println("FPS: " + frames + " TICKS: " + updates);
            frames = 0;
            updates = 0;
          }
				}			
			}
    }
  }

  private void tick() { //adjusts the position of each object
		for(int i = 0; i < objects.size(); i++) {
      tempObject = objects.get(i);
      tempObject.tick(objects);
    }
  }

  private void render() { //renders the background, renders each object
    BufferStrategy bs = this.getBufferStrategy();

    if(bs == null) {
      this.createBufferStrategy(1);
      bs = this.getBufferStrategy();
    }

    Graphics g = bs.getDrawGraphics();
    while(g == null) {
      g = bs.getDrawGraphics();
    }

    g.setColor(Color.black);
    g.fillRect(0,0, getWidth(), getHeight());

    for(int i = 0; i < objects.size(); i++) {
      tempObject = objects.get(i);
      tempObject.render(g);
    }

    g.dispose();
    bs.show();
    
    Toolkit.getDefaultToolkit().sync();
  }
}