import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.util.LinkedList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class PowerUp extends GameObject {

  private float width, height;
  private BufferedImage power;

  public PowerUp(float x, float y) {
    super(x, y, "powerup", null, 0, "powerup");
    this.width = 15;
    this.height = 15;
    try {
      power = ImageIO.read(new File("assets/img/power.png"));
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
      if(getBounds().intersects(tempObject.getBounds())) {}
    }
  }

  public void render(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(power, (int)getX(), (int)getY(), null);

    
  }

  public Rectangle getBounds() {
    return new Rectangle((int)getX(), (int)getY(), (int)width, (int)height);
  }

}