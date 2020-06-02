package Signal.Flow.Graph;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class GUI extends JFrame{
 
	
	MasonAlgorithm mason = MasonAlgorithm.getInstance();
	
	private JLabel startLabel, endLabel, gainLabel,startClearLabel, endClearLabel;
	private JTextField startText, endText, gainText,startClearText, endClearText;
	private JButton solveButton, addButton, clearButton;
	private JButton newProblem;

	private JLabel Note;
	
	private TextArea userPaths;
	private int width, height;
	
	
	GUI(){
		initialize();
	}
	
	private boolean isValidInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	private boolean isValidDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void initialize() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth() - 500;
		height = (int) screenSize.getHeight() - 400;
		
		mason.setWidth(width);
		mason.setHeight(height);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(300,200,width,height);
		setTitle("Signal Flow Graph Solver");
		setLayout(null);
		setResizable(true);
		
		startLabel = new JLabel("Start Edge");
		startLabel.setBounds(60, 40,160,50);
		startText = new JTextField();
		startText.setBounds(60,90,160,50);
		
		endLabel = new JLabel("End Edge");
		endLabel.setBounds(220, 40, 160, 50);
		endText = new JTextField();
		endText.setBounds(220, 90, 160, 50);
		
		gainLabel = new JLabel("gain Edge");
		gainLabel.setBounds(380, 40, 160, 50);
		gainText = new JTextField();
		gainText.setBounds(380, 90, 160, 50);
		
		addButton = new JButton("Add");
		addButton.setBounds(60, 150, 100, 50);
		solveButton = new JButton("Solve");
		solveButton.setBounds(170, 150, 100, 50);
		
		
		startClearLabel = new JLabel("Start  Edge");
		startClearLabel.setBounds(580, 40, 160, 50);
		startClearText = new JTextField();
		startClearText.setBounds(580,90,160,50);
		
		endClearLabel = new JLabel("End Edge");
		endClearLabel.setBounds(740, 40, 160, 50);
		endClearText = new JTextField();
		endClearText.setBounds(740, 90, 160, 50);
		
		
		clearButton = new JButton("Delete");
		clearButton.setBounds(580, 150, 100, 50);
		
		
		userPaths = new TextArea();
		userPaths.setBounds(60,220,200,200);
		
		newProblem = new JButton("New Graph");
		newProblem.setBounds(580, 300, 150, 70);
		
		Note = new JLabel("Start and End Edges must be a number between 1 to Number of Nodes");
		Note.setBounds(300, 300, 1000, 200);
		Note.setForeground(Color.red);
		
		solveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mason.SFG();
				
				GraphDraw Draw = new GraphDraw();
				Draw.setBounds(300, 300, 1100, 350);
				Draw.setVisible(true);
				Draw.repaint();
				
				PopupWindow result = new PopupWindow();
				result.setVisible(true);
			}
			
		});
		
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (!isValidInt(startText.getText())) {
					ErrorMessage errorMessage = new ErrorMessage("from node, invalid numeric value!");
					errorMessage.setVisible(true);
				} else if (!isValidInt(endText.getText())) {
					ErrorMessage errorMessage = new ErrorMessage("to node, invalid numeric value!");
					errorMessage.setVisible(true);
				} else if (!isValidDouble(gainText.getText())) {
					ErrorMessage errorMessage = new ErrorMessage("segment gain, invalid numeric value!");
					errorMessage.setVisible(true);
				} else {
					int n1 = Integer.parseInt(startText.getText()), n2 = Integer
							.parseInt(endText.getText());
					if (n1 > mason.getNumOfNodes() || n2 > mason.getNumOfNodes()) {
						ErrorMessage errorMessage = new ErrorMessage("node number exceeded max number of nodes!");
						errorMessage.setVisible(true);
					} else if (n1 < 1 || n2 < 1) {
						ErrorMessage errorMessage = new ErrorMessage("invalid node number! nodes must be from 1 to n as n is the numberr of nodes");
						errorMessage.setVisible(true);
					} else if (n1 == mason.getNumOfNodes()) {
						ErrorMessage errorMessage = new ErrorMessage("no feedback allowded from node  "+ mason.getNumOfNodes());
						errorMessage.setVisible(true);
					} else if (n2 == 1) {
						ErrorMessage errorMessage = new ErrorMessage("no feedback allowded to node  1");
						errorMessage.setVisible(true);
					} else {
						double g = Double.parseDouble(gainText.getText());
						mason.getAdjacencyMatrix()[n1 - 1][n2 - 1] = g;
						String startEdge = startText.getText();
						String endEdge = endText.getText();
						String gain = gainText.getText();
						String path = userPaths.getText();
						path +="(" + startEdge + " , " + endEdge + ")" + " = " + gain;
						path += "\n";
						endText.setText("");
						startText.setText("");
						gainText.setText("");
						userPaths.setText(path);
					}
				}
			}
			
		});
		
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (!isValidInt(startClearText.getText())) {
					ErrorMessage errorMessage = new ErrorMessage("Start node, invalid numeric value!");
					errorMessage.setVisible(true);
				} else if (!isValidInt(endClearText.getText())) {
					ErrorMessage errorMessage = new ErrorMessage("End node, invalid numeric value!");
					errorMessage.setVisible(true);
				} else {
					int n1 = Integer.parseInt(startClearText.getText()), n2 = Integer
							.parseInt(endClearText.getText());
					if (n1 > mason.getNumOfNodes() || n2 > mason.getNumOfNodes()) {
						ErrorMessage errorMessage = new ErrorMessage("node exceeded max number of nodes!");
						errorMessage.setVisible(true);
					} else if (n1 < 1 || n2 < 1) {
						ErrorMessage errorMessage = new ErrorMessage("invalid node number!");
						errorMessage.setVisible(true);
					} else if (mason.getAdjacencyMatrix()[n1 - 1][n2 - 1] == 0) {
						ErrorMessage errorMessage = new ErrorMessage("segment doesnot exist!");
						errorMessage.setVisible(true);
					} else {
						String gain = Double.toString(mason.getAdjacencyMatrix()[n1-1][n2-1]);
						mason.getAdjacencyMatrix()[n1 - 1][n2 - 1] = 0;
						String paths = userPaths.getText();
						String startEdge = startClearText.getText();
						String endEdge = endClearText.getText();
						String clearedPath = "(" + startEdge + " , " + endEdge + ")";
												
						int i = paths.indexOf(clearedPath);
						int j =paths.indexOf('(', i+1);
						if( j == -1) j = paths.length();
						String x = paths.substring(0, i);
						String y = paths.substring(j, paths.length());
						String res = x + y;
						startClearText.setText("");
						endClearText.setText("");
						userPaths.setText(res);
					}
				}
			}
			
		});
		
		newProblem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Main main = new Main();
				main.setVisible(true);
			}
			
		});
		
		Font font = new Font("Serif", Font.PLAIN, 24);
		startLabel.setFont(font);
		endLabel.setFont(font);
		gainLabel.setFont(font);
		Note.setFont(font);
		startText.setFont(font);
		endText.setFont(font);
		gainText.setFont(font);
		addButton.setFont(font);
		solveButton.setFont(font);
		
		startClearLabel.setFont(font);
		startClearText.setFont(font);
		endClearLabel.setFont(font);
		endClearText.setFont(font);
		clearButton.setFont(font);
		newProblem.setFont(font);
		
		getContentPane().add(Note);
		getContentPane().add(startLabel);
		getContentPane().add(endLabel);
		getContentPane().add(gainLabel);
		getContentPane().add(startText);
		getContentPane().add(endText);
		getContentPane().add(gainText);
		getContentPane().add(solveButton);
		getContentPane().add(addButton);

		getContentPane().add(startClearLabel);
		getContentPane().add(startClearText);
		getContentPane().add(endClearLabel);
		getContentPane().add(endClearText);
		getContentPane().add(clearButton);
		
		getContentPane().add(userPaths);
		getContentPane().add(newProblem);
		
	}
	class ErrorMessage extends JFrame {

		private JLabel label;

		public ErrorMessage(String str) {
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

