import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.io.IOException;
import java.io.File;

public class MenuPanel extends JPanel {

	private JPanel buttonContainer;
	private JButton startButton;
	private JButton exitButton;
	private JButton instructionButton;
	private JTextField nameField;
	private JLabel nameLabel;
	private JLabel title;
	private JLabel version;
	private Font font;

	public MenuPanel() {
		super(new BorderLayout());

		try {
		  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		  font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/FjallaOne-Regular.otf"));
		  
		  ge.registerFont(font);
		} catch (IOException|FontFormatException e) {
		  e.printStackTrace();
		}

		buttonContainer = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		buttonContainer.setOpaque(true);
		buttonContainer.setBackground(Color.BLACK);

		font = font.deriveFont(Font.PLAIN, 100);
		title = new JLabel("Knock Out");
		title.setFont(font);
		title.setForeground(Color.WHITE);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

		font = font.deriveFont(Font.PLAIN, 15);
		version = new JLabel("v.0.2");
		version.setFont(font);
		version.setForeground(Color.WHITE);
		version.setAlignmentX(Component.RIGHT_ALIGNMENT);

		font = font.deriveFont(Font.PLAIN, 20);
		nameLabel = new JLabel("Enter Username:");
		nameLabel.setFont(font);
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		nameField = new JTextField();
		nameField.setFont(font);
		nameField.setBackground(Color.BLACK);
		nameField.setForeground(Color.WHITE);
		nameField.setHorizontalAlignment(JTextField.CENTER);

		
		startButton = new JButton("Join Game");
		setMenuButtonFont(startButton);
		addHoverEffect(startButton);

		exitButton = new JButton("Exit");
		setMenuButtonFont(exitButton);
		addHoverEffect(exitButton);

		instructionButton = new JButton("Instruction");
		setMenuButtonFont(instructionButton);
		addHoverEffect(instructionButton);


		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 5;
		buttonContainer.add(title, gbc);

		gbc.insets = new Insets(-20, 350, 0, 0);
		gbc.fill = GridBagConstraints.PAGE_END;
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		buttonContainer.add(version, gbc);

		gbc.insets = new Insets(10, 0, -10, 0);
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		buttonContainer.add(nameLabel, gbc);

		gbc.insets = new Insets(10, 0, 10, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipady = 10;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		buttonContainer.add(nameField, gbc);


		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		buttonContainer.add(startButton, gbc);

		gbc.gridy = 5;
		buttonContainer.add(instructionButton, gbc);

		gbc.gridy = 6;
		buttonContainer.add(exitButton, gbc);

		this.add(buttonContainer, BorderLayout.CENTER);

	}

	public JButton getStartButton() {
		return this.startButton;
	}

	public JButton getInstructionButton() {
		return this.instructionButton;
	}

	public JButton getExitButton() {
		return this.exitButton;
	}

	public String getNameField() {
		return this.nameField.getText();
	}

	private void setMenuButtonFont(JButton button)  {
		font = font.deriveFont(Font.PLAIN, 40);
		button.setFont(font);
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	private void addHoverEffect(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent me) {
				button.setBackground(Color.WHITE);
				button.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent me) {
				button.setBackground(Color.BLACK);
				button.setForeground(Color.WHITE);
			}

			@Override
			public void mouseReleased(MouseEvent me) {}

		});
	}
}