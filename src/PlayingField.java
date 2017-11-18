import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.Toolkit;

import java.util.LinkedList;

public class PlayingField extends Canvas implements Runnable {
  private boolean running = false;
  private Thread thread;

  public LinkedList<GameObject> objects = new LinkedList<GameObject>();
  private GameObject tempObject;

  private void init() {

		this.objects.add(new Circle(50, 50, "paula"));

    //add objects or players here
    this.addKeyListener(new KeyInput(objects));
  }

  public synchronized void start() {
    if(running) return;
    running = true;
    thread = new Thread(this);
    thread.start();
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
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;
      while(delta >= 1) {
        tick(); //adjusts the position of each object
        updates++;
        delta--;
      }
      render(); //renders the background, renders each object
      frames++;

      if(System.currentTimeMillis() - timer > 1000) {
        timer += 1000;
        // fps = frames;
        // ticks = updates;
        System.out.println("FPS: " + frames + " TICKS: " + updates);
        frames = 0;
        updates = 0;
      }
    }
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
      this.createBufferStrategy(1);
      bs = this.getBufferStrategy();
      g = bs.getDrawGraphics();
    }

    g.setColor(Color.black);
    g.fillRect(0,0, getWidth(), getHeight());

    for(int i = 0; i < objects.size(); i++) {
      tempObject = objects.get(i);
      tempObject.render(g);
    }

    g.dispose();
    bs.show();
    
    Toolkit.getDefaultToolkit().sync();
  }
}