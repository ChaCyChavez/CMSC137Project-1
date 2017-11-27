import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.util.LinkedList;

public class Bomb extends GameObject {

  private float width, height;

  public Bomb(float x, float y) {
    super(x, y, "bomb" + x + "" + y, null, 0);
    this.width = 10;
    this.height = 10;
  }

  public void tick(LinkedList<GameObject> objects) {
    x += velX;
    y += velY;

    collision(objects);
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
    g.setColor(Color.red);
    g.fillOval((int)x, (int)y, (int)width, (int)height);

    Graphics2D g2d = (Graphics2D) g;
  }

  public Rectangle getBounds() {
    return new Rectangle((int)x , (int) y, (int)width, (int)height);
  }

}