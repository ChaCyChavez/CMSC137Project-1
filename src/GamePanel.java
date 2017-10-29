import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class GamePanel extends JPanel {

	private JPanel topPanel;
	private JLabel header;
	private JButton backButton;

	private JPanel leftPanel;
	private GridBagConstraints gbc;
	private JTextPane conversation;
	private JScrollPane scroll;
	private JTextField input;
	private JButton chatButton;

	private JPanel centerPanel;

	final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;

	public GamePanel() {
		super(new BorderLayout());

		this.add(createTopPanel(), BorderLayout.NORTH);
		this.add(createLeftPanel(), BorderLayout.WEST);
	}


	public JPanel createTopPanel() {
		topPanel = new JPanel();

		header = new JLabel("Game");
		backButton = new JButton("Back to Main Menu");

		topPanel.add(header);
		topPanel.add(backButton);

		return topPanel;
	}

	public JPanel createLeftPanel() {
		leftPanel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();

		leftPanel.setPreferredSize(new Dimension(300, 600));

    conversation = new JTextPane();
		scroll = new JScrollPane(conversation);
    gbc.weightx = 0.2;
    gbc.weighty = 0.97;
    gbc.gridwidth = 5;
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    leftPanel.add(scroll, gbc);

    input = new JTextField();
    gbc.weighty = 0.03;
    gbc.weightx = 0.99;
    gbc.anchor = GridBagConstraints.PAGE_END;
    gbc.insets = new Insets(1,0,0,0);
    gbc.gridx = 0;
    gbc.gridwidth = 3;
    gbc.gridy = 5;
    leftPanel.add(input, gbc);

    chatButton = new JButton("Enter");
    gbc.ipady = 0;
    gbc.weightx = 0.01;
    gbc.anchor = GridBagConstraints.PAGE_END;
    gbc.insets = new Insets(1,0,0,0);
    gbc.gridx = 4;
    gbc.gridwidth = 1;
    gbc.gridy = 5;
    leftPanel.add(chatButton, gbc);

    chatButton.addActionListener(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		String curr = conversation.getText();
    		conversation.setText(curr + input.getText() + "\n");
    		input.setText("");
    	}
    });

		return leftPanel;
	}


	public JButton getBackButton() {
		return this.backButton;
	}
}