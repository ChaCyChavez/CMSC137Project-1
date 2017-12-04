import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.LinkedList;
import java.io.File;

public class Bomb extends GameObject {

  private float width, height;

  private BufferedImage bomb;

  public Bomb(float x, float y) {
    super(x, y, "bomb", null, 0, "bomb");
    this.width = 7;
    this.height = 7;
    try {
      bomb = ImageIO.read(new File("assets/img/dynamite.png"));
    } catch(Exception e) {}
  }

  public void tick(LinkedList<GameObject> objects) {
    setX(getX()+getVelX());
    setY(getY()+getVelY());

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
    g.drawImage(bomb, (int)getX() - 5, (int)getY() - 7, null);
  }

  public Rectangle getBounds() {
    return new Rectangle((int)getX(), (int)getY(), (int)width, (int)height);
  }

}