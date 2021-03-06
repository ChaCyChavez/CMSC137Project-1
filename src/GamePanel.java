import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Canvas;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GamePanel extends JPanel {

	private JPanel topPanel;
	private JLabel header;

	private PlayingField playingField;

	private JPanel leftPanel;
	private GridBagConstraints gbc;
	private JTextPane conversation;
	private JScrollPane scroll;
	public JTextField input;
	public JButton chatButton;

	private JPanel centerPanel;
	private Font font;
	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;

	public String playerName;
	public String server;
	public int portNumber;
	

	public GamePanel(String playerName, String server, int portNumber) {
		super(new BorderLayout());
		this.setBackground(Color.BLACK);
		this.playerName = playerName;
		this.server = server;
		this.portNumber = portNumber;

		try {
		  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		  font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/FjallaOne-Regular.otf"));
		  
		  ge.registerFont(font);
		} catch (IOException|FontFormatException e) {
		  e.printStackTrace();
		}

		this.add(createTopPanel(), BorderLayout.NORTH);
		this.add(createLeftPanel(), BorderLayout.WEST);
		this.add(createCenterPanel(), BorderLayout.CENTER);
		playingField.start();
	}

	public Canvas createCenterPanel() {
		playingField = new PlayingField(playerName, server, portNumber);

		return playingField;
	}

	public JPanel createTopPanel() {
		topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.setBackground(Color.BLACK);

		font = font.deriveFont(Font.PLAIN, 50);
		header = new JLabel("Knock Out");
		header.setFont(font);
		header.setForeground(Color.WHITE);
		topPanel.add(header);

		return topPanel;
	}

	public JPanel createLeftPanel() {
		leftPanel = new JPanel(new GridBagLayout());
		leftPanel.setBackground(Color.BLACK);

		gbc = new GridBagConstraints();

		leftPanel.setPreferredSize(new Dimension(250, 600));

		font = font.deriveFont(Font.PLAIN, 20);
		conversation = new JTextPane();
		conversation.setBackground(Color.BLACK);
		conversation.setForeground(Color.WHITE);
		conversation.setFont(font);
		conversation.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

		scroll = new JScrollPane(conversation);
		gbc.weightx = 0.2;
		gbc.weighty = 0.97;
		gbc.gridwidth = 5;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		leftPanel.add(scroll, gbc);

		font = font.deriveFont(Font.PLAIN, 18);
		input = new JTextField();
		input.setBackground(Color.BLACK);
		input.setForeground(Color.WHITE);
		input.setFont(font);
		input.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

		gbc.weighty = 0.03;
		gbc.weightx = 0.90;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.insets = new Insets(1,0,0,0);
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		gbc.gridy = 5;
		leftPanel.add(input, gbc);

		chatButton = new JButton("Enter");
		chatButton.setBackground(Color.BLACK);
		chatButton.setForeground(Color.WHITE);
		chatButton.setFont(font);
		chatButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		addHoverEffect(chatButton);

		gbc.ipady = 0;
		gbc.weightx = 0.10;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.insets = new Insets(1,0,0,0);
		gbc.gridx = 4;
		gbc.gridwidth = 1;
		gbc.gridy = 5;
		leftPanel.add(chatButton, gbc);

		chatButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				appendConversationPane(input.getText());
				input.setText("");
			}
		});

		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ke) {
				appendConversationPane(input.getText());
				input.setText("");
			}
		});

		return leftPanel;
	}

	public void appendConversationPane (String message) {
		String curr = conversation.getText();
		conversation.setText(curr + message);
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