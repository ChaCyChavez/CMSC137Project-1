import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.net.InetAddress;

import java.util.LinkedList;

public abstract class GameObject {
  private InetAddress inetAddress;
  private int portNumber;
  public float x, y;
  public String objectName;
  public float velX = 0, velY = 0;
  public Color playerColor;
  public String objectType;
  private Boolean isAlive = true;
  public int score = 0;

  public GameObject(float x, float y, String name, InetAddress inetAddress, int portNumber, String objectType) {
    this.x = x;
    this.y = y;
    this.objectName = name; 
    this.inetAddress = inetAddress;
    this.portNumber = portNumber;
    this.objectType = objectType;
    this.playerColor = new Color((int)(Math.random() * 0x1000000));
  }

  public abstract void tick(LinkedList<GameObject> object);
  public abstract void render(Graphics g);
  public abstract Rectangle getBounds();

  public float getX() {
    return this.x;
  }
  public float getY() {
    return this.y;
  }
  public void setX(float x) {
    this.x = x;
  }
  public void setY(float y) {
    this.y = y;
  }

  public float getVelX() {
    return this.velX;
  }
  public float getVelY() {
    return this.velY;
  }
  public void setVelX(float x) {
    this.velX = x;
  }
  public void setVelY(float y) {
    this.velY = y;
  }

  public String getName() {
      return this.objectName;
  }

  public String getType() {
      return this.objectType;
  }

  public String playerToString(){ /*** String representation. used for transfer over the network */
		return ("PLAYER " + objectName + " " + x + " " + y + " " + Integer.toString(playerColor.getRGB()));
	}	

  public InetAddress getInetAddress(){
		return inetAddress;
	}

	public int getPortNumber(){
		return portNumber;
	}

  public void nowDead() {
    this.isAlive = false;
  }

  public Boolean isAlive() {
    return isAlive;
  }

  public void setScore(int s) {
    this.score += s;
  }
}