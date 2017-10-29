import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

public class InstructionPanel extends JPanel {
	private JLabel header;
	private JButton backButton;

	public InstructionPanel() {
		super();
		header = new JLabel("Instruction");
		backButton = new JButton("Back to Main Menu");

		this.add(header);
		this.add(backButton);

	}

	public JButton getBackButton() {
		return this.backButton;
	}
}