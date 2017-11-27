import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.util.LinkedList;

public class Circle extends GameObject {

  private float width = 40, height = 40;
  private DatagramSocket socket;
  private float prevX;
  private float prevY;

  public Circle(float x, float y, String name, InetAddress inetAddress, int portNumber) {
    super(x, y, name, inetAddress, portNumber, "circle");
    try {
      socket = new DatagramSocket();      
    } catch (Exception e) {
    }
  }

  public void tick(LinkedList<GameObject> objects) {
    prevX = x; 
    prevY = y;
    x += velX;
    y += velY;

    if (prevX != x || prevY != y){
      String message = "PLAYER " + objectName + " " + x + " " + y + " " + score;

      try {
        byte[] buffer = message.getBytes();
        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 4444);
        socket.send(packet);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    collision(objects);
  }

  public void collision(LinkedList<GameObject> objects){
    for(int i = 0; i < objects.size(); i++) {
      GameObject tempObject = objects.get(i);
      //check if other players
      //name can be used for identity
      if(tempObject.getType().equals("block")) {
        if(getBounds().intersects(tempObject.getBounds())) {
          y = tempObject.getY() - height;
          velY = 0;
        }

        if(getBoundsTop().intersects(tempObject.getBounds())) {
          y = tempObject.getY() + 5;
          velY = 0;
        }

        if(getBoundsLeft().intersects(tempObject.getBounds())) {
          x = tempObject.getX() + 5;
          velX = 0;
        }

        if(getBoundsRight().intersects(tempObject.getBounds())) {
          x = tempObject.getX() - width;
          velX = 0;
        }
      } else if(tempObject.getName().startsWith("food")) {
        if(getBounds().intersects(tempObject.getBounds())) {
          
          width += 2;
          height += 2;
          objects.remove(tempObject);
        }

        if(getBoundsTop().intersects(tempObject.getBounds())) {
          width += 2;
          height += 2;
          objects.remove(tempObject);
        }

        if(getBoundsLeft().intersects(tempObject.getBounds())) {
          width += 2;
          height += 2;
          objects.remove(tempObject);
        }

        if(getBoundsRight().intersects(tempObject.getBounds())) {
          objects.remove(tempObject);
        }
      } else if(tempObject.getName().startsWith("bomb")) {
        if(getBounds().intersects(tempObject.getBounds())) {
          objects.remove(this);
          objects.remove(tempObject);
        }

        if(getBoundsTop().intersects(tempObject.getBounds())) {
          objects.remove(this);
          objects.remove(tempObject);
        }

        if(getBoundsLeft().intersects(tempObject.getBounds())) {

          objects.remove(this);
          objects.remove(tempObject);
        }

        if(getBoundsRight().intersects(tempObject.getBounds())) {
          objects.remove(this);
          objects.remove(tempObject);
      } else if(tempObject.getType().equals("circle")) { //collided with other players
        Circle temp = (Circle) tempObject;
        if(getBounds().intersects(tempObject.getBounds()) && 
          !tempObject.getName().equals(objectName) &&
          isAlive() && tempObject.isAlive() &&
          temp.getWidth() < getWidth()) {
            System.out.println("width" + width + "height" + height);
            System.out.println("width" + temp.getWidth() + "height" + temp.getHeight());
            width += temp.getWidth()/2;
            height += temp.getHeight()/2;
            score += 15;
            tempObject.nowDead();
        }
      }
    }
  }
}

  public void render(Graphics g) {
    if(this.isAlive()) {
      g.setColor(playerColor);
      g.fillOval((int)x, (int)y, (int)width, (int)height);

      Graphics2D g2d = (Graphics2D) g;
      g.setColor(Color.white);
      g2d.drawString(this.objectName, (int)(x+(width/2)), (int)(y+height));
      g2d.drawString(Integer.toString(this.score), (int)(x+(width/2)), (int)y);
      g.setColor(Color.yellow);
      g2d.draw(getBounds());
      g2d.draw(getBoundsRight());
      g2d.draw(getBoundsLeft());
      g2d.draw(getBoundsTop());
    }
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

  public float getHeight() {
    return this.height;
  }

  public float getWidth() {
    return this.width;
  }
}