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

	public Frame() {
		super("Knock Out v.0.2");
		this.setPreferredSize(new Dimension(800, 600));
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
		gamePanel = new GamePanel();
		instructionPanel = new InstructionPanel();

		mainPanel.add(menuPanel, MENU);
		mainPanel.add(gamePanel, GAME);
		mainPanel.add(instructionPanel, INSTRUCTION);

		menuPanel.getStartButton().addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent ae) { 
				if(!menuPanel.getNameField().equals("")) { //create client only if name is not empty
					cardLayout.show(mainPanel, GAME);
					Client client = new Client(menuPanel.getNameField(), "localhost", 4444, gamePanel); //create and start a new client
					new Thread(new Runnable() { //run new thread so gui wont freeze
						@Override
						public void run() {
							client.startClient();
						}
					}).start();
				}
			}
		});

		menuPanel.getInstructionButton().addActionListener(new ActionListener () {
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

		gamePanel.getBackButton().addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent ae) {
				cardLayout.show(mainPanel, MENU);
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
}