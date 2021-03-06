import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Frame extends JFrame {
	private CardLayout cardLayout;
	private GamePanel gamePanel;
	private InstructionPanel instructionPanel;
	private MenuPanel menuPanel;
	private JPanel mainPanel;

	private static final String GAME = "GAME";
	private static final String INSTRUCTION = "INSTRUCTION";
	private static final String MENU = "MENU";

	private String server;
	private int tcpPortNumber;
	private int udpPortNumber;

	public Frame(String server, int tcpPortNumber, int udpPortNumber) {
		super("Knock Out v.0.2");
		this.server = server;
		this.tcpPortNumber = tcpPortNumber;
		this.udpPortNumber = udpPortNumber;
		this.setPreferredSize(new Dimension(1280, 720));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setContentPane(createMainPanel());
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private JPanel createMainPanel() {
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);

		menuPanel = new MenuPanel();
		instructionPanel = new InstructionPanel();

		mainPanel.add(menuPanel, MENU);
		mainPanel.add(instructionPanel, INSTRUCTION);

		menuPanel.getStartButton().addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent ae) { 
				startGame();
			}
		});

		menuPanel.getField().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				startGame();
			}
		});

		menuPanel.getInstructionButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				cardLayout.show(mainPanel, INSTRUCTION);
			}
		});

		menuPanel.getExitButton().addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});	

		instructionPanel.getBackButton().addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent ae) {
				cardLayout.show(mainPanel, MENU);
			}
		});

		return mainPanel;
	}

	public void startGame() {
		if(!menuPanel.getNameField().equals("")) { //create client only if name is not empty
			gamePanel = new GamePanel(menuPanel.getNameField(), server, udpPortNumber);
			Client client = new Client(menuPanel.getNameField(), server, tcpPortNumber, gamePanel); //create and start a new client
			mainPanel.add(gamePanel, GAME);
			cardLayout.show(mainPanel, GAME);
			new Thread(new Runnable() { //run new thread so gui wont freeze
				@Override
				public void run() {
					client.startClient();
				}
			}).start();
		}
	}
}