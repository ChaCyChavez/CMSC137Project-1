import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import java.awt.Graphics;


public class PlayingField extends JPanel {
	private String text;

	public PlayingField() {
		super();

    this.setFocusable(true);
		this.requestFocusInWindow();

		Action up = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("UP");
			}
		};

		Action down = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("DOWN");
			}
		};

		Action right = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("RIGHT");
			}
		};

		Action left = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("LEFT");
			}
		};

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        KeyStroke.getKeyStroke("UP"), "upPressed");
    this.getActionMap().put("upPressed", up);
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        KeyStroke.getKeyStroke("DOWN"), "downPressed");
    this.getActionMap().put("downPressed", down);
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        KeyStroke.getKeyStroke("LEFT"), "leftPressed");
    this.getActionMap().put("leftPressed", left);
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        KeyStroke.getKeyStroke("RIGHT"), "rightPressed");
    this.getActionMap().put("rightPressed", right);
	}

	
}