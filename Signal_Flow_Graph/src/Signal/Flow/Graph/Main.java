package Signal.Flow.Graph;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main extends JFrame {

	private JLabel main_label;
	private JTextField input_field;
	private JButton enter;
	private MasonAlgorithm mason = MasonAlgorithm.getInstance();
	
	
	Main() {
		initialize();
	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 200, 600, 300);
		setTitle("Signal FLow Graph Solver");
		setLayout(null);
		setResizable(false);

		main_label = new JLabel("Enter Total Number Of Nodes");
		main_label.setBounds(50, 20, 500, 40);
		input_field = new JTextField();
		input_field.setBounds(100, 80, 400, 50);
		enter = new JButton("Enter");
		enter.setBounds(200, 180, 200, 50);

		enter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (isValidInt(input_field.getText())) {
					int n = Integer.parseInt(input_field.getText());
					mason.setNumOfNodes(n);
					mason.setAdjacencyMatrix(new double[n][n]);
					GUI view = new GUI();
					view.setVisible(true);
					dispose();
				} else {
					ErrorMessage errorMessage = new ErrorMessage("Invalid Numeric Value!");
					errorMessage.setVisible(true);
				}
			}
		});

		Font font = new Font("Serif", Font.PLAIN, 32);
		main_label.setFont(font);
		input_field.setFont(font);
		enter.setFont(font);

		getContentPane().add(main_label);
		getContentPane().add(input_field);
		getContentPane().add(enter);

	}
	private boolean isValidInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main view = new Main();
					view.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	class ErrorMessage extends JFrame {

		private JLabel label;

		public ErrorMessage(String str) { // constructor
			initialize(str);
		}

		private void initialize(String str) {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(400, 200, 600, 200);
			setTitle("Error Message!");
			setLayout(null);
			setResizable(false);

			label = new JLabel(str);
			label.setBounds(50, 0, 500, 200);
	        Font font = new Font("Serif", Font.PLAIN, 24);
	        label.setFont(font);
			getContentPane().add(label);

		}
	}

}