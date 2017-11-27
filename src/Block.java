import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.util.LinkedList;

public class Block extends GameObject {

  private float width, height;
  private boolean canBlock;

  public Block(float x, float y, float width, float height) {
    super(x, y, "block", null, 0, "block");
    this.width = width;
    this.height = height;
  }

  public void tick(LinkedList<GameObject> objects) {
    //x += velX;
    //y += velY;
    setX(getX()+getVelX());
    setY(getY()+getVelY());

    collision(objects);
  }

  public boolean canBlock() {
    return this.canBlock;
  }

  public void collision(LinkedList<GameObject> objects){
    for(int i = 0; i < objects.size(); i++) {
      GameObject tempObject = objects.get(i);
      //check if other players
      //name can be used for identity
      if(getBounds().intersects(tempObject.getBounds())) {
        // System.out.println("collided");
      }
    }
  }

  public void render(Graphics g) {
    g.setColor(Color.white);
    g.drawRect((int)getX(), (int)getY(), (int)width, (int)height);

    Graphics2D g2d = (Graphics2D) g;
  }

  public Rectangle getBounds() {
    return new Rectangle((int)getX(), (int)getY(), (int)width, (int)height);
  }

}