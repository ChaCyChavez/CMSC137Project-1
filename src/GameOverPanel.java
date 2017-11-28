import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.FontFormatException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;

import java.io.IOException;
import java.io.File;

public class GameOverPanel extends JPanel {
	private JLabel header;
	private JButton playAgainButton;
	private JPanel centerPanel;

	private Font font;
	private JLabel gameOver;

	public GameOverPanel() {
		super(new BorderLayout());

		try {
		  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		  font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/FjallaOne-Regular.otf"));
		  
		  ge.registerFont(font);
		} catch (IOException|FontFormatException e) {
		  e.printStackTrace();
		}

		this.add(createCenterPanel(), BorderLayout.CENTER);
		this.add(playAgainButton);
	}

	public JPanel createCenterPanel() {
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);

		font = font.deriveFont(Font.PLAIN, 18);
		gameOver = new JLabel("GAME OVER"); 
		gameOver.setFont(font);
		gameOver.setForeground(Color.WHITE);

		centerPanel.add(gameOver);

		return centerPanel;
	}

	public JButton playButton() {
		return this.playAgainButton;
	}
}
