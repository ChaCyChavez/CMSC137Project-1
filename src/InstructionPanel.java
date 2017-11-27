import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.FontFormatException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;

import java.io.IOException;
import java.io.File;

public class InstructionPanel extends JPanel {
	private JLabel header;
	private JButton backButton;
	private JPanel topPanel;
	private JPanel centerPanel;

	private Font font;
	private JLabel objective;

	public InstructionPanel() {
		super(new BorderLayout());

		try {
		  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		  font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/FjallaOne-Regular.otf"));
		  
		  ge.registerFont(font);
		} catch (IOException|FontFormatException e) {
		  e.printStackTrace();
		}

		this.add(createTopPanel(), BorderLayout.NORTH);
		this.add(createCenterPanel(), BorderLayout.CENTER);
	}

	public JPanel createCenterPanel() {
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);

		font = font.deriveFont(Font.PLAIN, 18);
		objective = new JLabel("<html><div style=\"text-align: center;\"><br><br><h2 style=\"font-size: 20px;\">GOAL</h2>Earn more points by obtaining food tokens while avoiding bombs <br>and the border of the game arena.<br><br><br><h2 style=\"font-size: 20px;\">POINT SYSTEM</h2>Here are the corresponding points for each action in the game: <br><br> 5 points - acquiring a food token<br>15 points - bumping and eliminating ann opponent<br><br><br><h2 style=\"font-size: 20px;\">ADDITIONAL RULES</h2>Detonating a bomb will automatically eliminate <br>a player from the game. <br> Once the time limit (3 minutes) ends, the player who has retained its position in the playing field <br>or has the most number of points wins.</div></html>"); 
		objective.setFont(font);
		objective.setForeground(Color.WHITE);

		centerPanel.add(objective);

		return centerPanel;
	}

	public JPanel createTopPanel() {
		topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.setBackground(Color.BLACK);

		font = font.deriveFont(Font.PLAIN, 50);
		header = new JLabel("Instruction");
		header.setFont(font);
		header.setForeground(Color.WHITE);

		backButton = new JButton(" < ");
		backButton.setFont(font);
		backButton.setBorder(null);
		backButton.setBackground(Color.BLACK);
		backButton.setForeground(Color.WHITE);

		topPanel.add(backButton);
		topPanel.add(header);

		return topPanel;
	}

	public JButton getBackButton() {
		return this.backButton;
	}
}