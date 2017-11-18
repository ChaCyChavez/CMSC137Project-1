import java.awt.Graphics;
import java.awt.Rectangle;

import java.util.LinkedList;

public abstract class GameObject {
  public float x, y;
  public String objectName;
  public float velX = 0, velY = 0;

  public GameObject(float x, float y, String name) {
    this.x = x;
    this.y = y;
    this.objectName = name; 
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
}