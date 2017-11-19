import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.LinkedList;

public class KeyInput extends KeyAdapter {
  LinkedList<GameObject> objects;

  public KeyInput(LinkedList<GameObject> objects) {
    this.objects = objects;
  }

  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();

    for(int i = 0; i < this.objects.size(); i++) {
      GameObject tempObject = this.objects.get(i);

      if(tempObject.getName() == "paula") {

        if(key == KeyEvent.VK_D) tempObject.setVelX(1);
        if(key == KeyEvent.VK_A) tempObject.setVelX(-1);
        if(key == KeyEvent.VK_W) tempObject.setVelY(-1);
        if(key == KeyEvent.VK_S) tempObject.setVelY(1);
      }
    }

    if(key == KeyEvent.VK_ESCAPE) System.exit(1);
  }

  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    
    for(int i = 0; i < this.objects.size(); i++) {
      GameObject tempObject = this.objects.get(i);

      if(key == KeyEvent.VK_D) tempObject.setVelX(0);
      if(key == KeyEvent.VK_A) tempObject.setVelX(0);
      if(key == KeyEvent.VK_W) tempObject.setVelY(0);
      if(key == KeyEvent.VK_S) tempObject.setVelY(0);
    }
  }
}