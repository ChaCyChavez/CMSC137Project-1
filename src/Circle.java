import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.util.LinkedList;

public class Circle extends GameObject {

  private float width = 40, height = 40;

  public Circle(float x, float y, String name) {
    super(x, y, name);
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
    g.setColor(Color.blue);
    g.fillOval((int)x, (int)y, (int)width, (int)height);

    Graphics2D g2d = (Graphics2D) g;
    g.setColor(Color.yellow);
    g2d.draw(getBounds());
    g2d.draw(getBoundsRight());
    g2d.draw(getBoundsLeft());
    g2d.draw(getBoundsTop());
  }

  public Rectangle getBounds() {
    return new Rectangle((int) ((int)x + (width/2) - ((width/2)/2)), (int) ((int)y+(height/2)), (int)width/2, (int)height/2);
  }
  public Rectangle getBoundsTop() {
    return new Rectangle((int) ((int)x + (width/2) - ((width/2)/2)), (int)y, (int)width/2, (int)height/2);
  }
  public Rectangle getBoundsRight() {
    return new Rectangle((int)((int)x + width - 5), (int)y, (int)5, (int)height);
  }
  public Rectangle getBoundsLeft() {
    return new Rectangle((int)x, (int)y, (int)5, (int)height);
  }
}