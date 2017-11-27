import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;

public class InstructionPanel extends JPanel {
	private JLabel header;
	private JButton backButton;
	private JLabel objective;

	public InstructionPanel() {
		super();
		header = new JLabel("Instruction");
		backButton = new JButton("Back to Main Menu");
		objective = new JLabel("<html>GOAL<br><br><br>Earn more points by obtaining food tokens while avoiding bombs <br>and the border of the game arena.<br><br><br> POINT SYSTEM<br><br><br>Here are the corresponding points for each action in the game: <br><br> 5 points - acquiring a food token<br><br>15 points - bumping and eliminating ann opponent<br><br><br>ADDITIONAL RULES<br><br><br>Detonating a bomb will automatically eliminate <br>a player from the game. <br> Once the time limit (3 minutes) ends, the player who has retained its position in the playing field <br>or has the most number of points wins.</html>"); 
		
		

		this.add(header);
		this.add(backButton);
	    this.add(objective);

	}
	

	public JButton getBackButton() {
		return this.backButton;
	}
}
