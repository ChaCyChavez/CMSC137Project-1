import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.Toolkit;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.FontFormatException;

import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.Random;

public class PlayingField extends Canvas implements Runnable {
  private Font font;
  private boolean running = false;
  private Thread thread;

  public LinkedList<GameObject> objects = new LinkedList<GameObject>();
  private GameObject tempObject;

  private String server;
  private int portNumber;
  private String playerName;
  private String dataFromServer;
  private DatagramSocket socket;
  private boolean isConnected = false;
  private boolean completedPlayers = false;

  private ArrayList playerCoordinates = new ArrayList();
  private Random rand = new Random();

  private float prevX = 0;
  private float prevY = 0;
  private String time;

  private BufferedImage food_big;
  private BufferedImage bomb_big;
  private BufferedImage power_big;

  public PlayingField(String playerName, String server, int portNumber) {
    this.playerName = playerName;
    this.server = server;
		this.portNumber = portNumber;

    try {
		  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		  font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/FjallaOne-Regular.otf"));
		  
		  ge.registerFont(font);
		} catch (IOException|FontFormatException e) {
		  e.printStackTrace();
		}

    try {
      socket = new DatagramSocket();
      socket.setSoTimeout(100);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      food_big = ImageIO.read(new File("assets/img/food-big.png"));
      bomb_big = ImageIO.read(new File("assets/img/dynamite-big.png"));
      power_big = ImageIO.read(new File("assets/img/power-big.png"));
    } catch(Exception e) {}
  }

  private void init() {
    this.objects.add(new Block(70, 60, 879, 5));
    this.objects.add(new Block(944, 65, 5, 490));
    this.objects.add(new Block(65, 550, 879, 5));
    this.objects.add(new Block(65, 60, 5, 490));
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
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, portNumber);
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
      byte[] buf = new byte[2048]; //Get the data from players
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      try{
        socket.receive(packet);
      } catch(Exception e){
        // e.printStackTrace();
      }
      
      dataFromServer = new String(buf);
      dataFromServer = dataFromServer.trim();

      if (!isConnected && dataFromServer.startsWith("CONNECTED")){
				isConnected = true;
				System.out.println("Connected.");
			} else if (!isConnected){
				System.out.println("Connecting..");		
        printWaiting();		
				sendMessage("CONNECT " + playerName);
			} else if(isConnected && dataFromServer.equals("END")) {
        running = false;
        System.out.println("GAME OVER");
        printGameOver();
      } else if (isConnected && completedPlayers){      
        long now = System.nanoTime();
        delta += (now - lastTime) / ns;
        lastTime = now;
        while(delta >= 1) {     
          tick(); //adjusts the position of each object
          updates++;
          delta--;
        }

        if(dataFromServer.startsWith("POWERUP")) {
          String[] token = dataFromServer.split(" ");
          String[] coord = token[1].split(":");
          float x = Float.parseFloat(coord[0]);
          float y = Float.parseFloat(coord[1]);
          while(alreadyExist(x, y)) {
            x = (float)Math.abs(rand.nextInt() % 800) + 80;
            y = (float)Math.abs(rand.nextInt() % 425) + 80;
          }
          this.objects.add(new PowerUp(x, y));
        } if(dataFromServer.startsWith("FOOD")) {
          String[] token = dataFromServer.split(" ");
          String[] coord = token[1].split(":");
          if(!alreadyExist(Float.parseFloat(coord[0]), Float.parseFloat(coord[1]))) {
            this.objects.add(new Food(Float.parseFloat(coord[0]), Float.parseFloat(coord[1])));
          }
        } else if (dataFromServer.startsWith("PLAYER")) {
          String[] playersInfo = dataFromServer.split(":");

					for (int i = 0; i < playersInfo.length; i++){
						String[] playerInfo = playersInfo[i].split(" ");
						String pname = playerInfo[1];
						float x = Float.parseFloat(playerInfo[2]);
						float y = Float.parseFloat(playerInfo[3]);
            Boolean alive = Boolean.parseBoolean(playerInfo[5]);
            for(int j = 0; j < this.objects.size(); j++) {
              GameObject tempObject = this.objects.get(j);
              if(pname.equals(tempObject.getName()) && !pname.equals(playerName)) {
                if(!alive) tempObject.isDead();
                if (prevX != x || prevY != y){
                  tempObject.setX(x);
                  tempObject.setY(y);    
                }
                prevX = x;
                prevY = y;            
              }
            }					
					}
        } else if(dataFromServer.startsWith("TIME")) {
          time = dataFromServer;
        }

        render(); //renders the background, renders each object
        frames++;

        if(System.currentTimeMillis() - timer > 1000) {
          timer += 1000;
          // fps = frames;
          // ticks = updates;
          // System.out.println("FPS: " + frames + " TICKS: " + updates);
          frames = 0;
          updates = 0;
        }			
			} else if(isConnected) {
        if (dataFromServer.startsWith("PLAYER_LIST")) {
          String[] playerNames = dataFromServer.split(" ");
					for (int i = 1; i < playerNames.length; i++){
            String[] player_coord = playerNames[i].split(":");

            if(!player_coord[0].startsWith("food") &&
                !player_coord[0].startsWith("powerup") && 
                !player_coord[0].startsWith("bomb")) {

              float x = Float.parseFloat(player_coord[1]);
              float y = Float.parseFloat(player_coord[1]);
              while(alreadyExist(x, y)) {
                x = (float)Math.abs(rand.nextInt() % 800) + 80;
                y = (float)Math.abs(rand.nextInt() % 425) + 80;
                
              }
              GameObject newCircle = new Circle(x, y,
                                          player_coord[0], packet.getAddress(),
                                          packet.getPort(), server);
              this.objects.add(newCircle);
              sendMessage(newCircle.playerToString());
            } else if(player_coord[0].startsWith("food")) {
              this.objects.add(new Food(Float.parseFloat(player_coord[1]),
                                          Float.parseFloat(player_coord[2])));
            } else if(player_coord[0].startsWith("powerup")) {
              this.objects.add(new PowerUp(Float.parseFloat(player_coord[1]),
                                          Float.parseFloat(player_coord[2])));
            } else {
              this.objects.add(new Bomb(Float.parseFloat(player_coord[1]),
                                          Float.parseFloat(player_coord[2])));
            }
          }
          completedPlayers = true;
          this.addKeyListener(new KeyInput(objects, playerName));
        }
      } 
    }
  }

  private boolean alreadyExist(float x, float y) {
    for(int i = 0; i < this.objects.size(); i++) {
      GameObject tempObject = (GameObject) this.objects.get(i);
      if(tempObject.getX() == x && tempObject.getY() == y) {
        return true;
      }
    }
    return false;
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
    font = font.deriveFont(Font.PLAIN, 30);
    Graphics2D g2d = (Graphics2D) g;
    g.setColor(Color.white);
    g2d.setFont(font);
    if(time != null) g2d.drawString(time, 25, 40);
    g2d.drawImage(bomb_big, 200, 5, null);
    g2d.drawImage(food_big, 400, 5, null);
    g2d.drawImage(power_big, 600, 5, null);
    g.setColor(Color.white);
    g2d.drawString("Bomb", 250, 40);
    g2d.drawString("Food", 450, 40);
    g2d.drawString("Power Up", 650, 40);
    for (GameObject obj: objects) {
      if(obj.getName().equals(this.playerName)) {
        String printScore = "Points: " + Integer.toString(obj.getScore());
        g2d.drawString(printScore, 700, 600);
      }
    }
    for(int i = 0; i < objects.size(); i++) {
      tempObject = objects.get(i);
      if(tempObject.isAlive()) tempObject.render(g);
    }
    g.dispose();
    bs.show();
    Toolkit.getDefaultToolkit().sync();
  }

  private void printGameOver() {
    //print game over screen
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
    font = font.deriveFont(Font.PLAIN, 100);
    Graphics2D g2d = (Graphics2D) g;
    g.setColor(Color.white);
    g2d.setFont(font);
    g2d.drawString("GAME OVER", 300, 240);
  }

  private void printWaiting() {
    //print waiting for other players screen
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
    Graphics2D g2d = (Graphics2D) g;
    g.setColor(Color.white);
    font = font.deriveFont(Font.PLAIN, 55);
    g2d.setFont(font);
    g2d.drawString("Waiting for other players", 215, 240);
    g.drawOval(300, 300, 40, 40);
    font = font.deriveFont(Font.PLAIN, 20);
    g2d.setFont(font);
    g2d.drawString(playerName, 350, 328);
  }
}