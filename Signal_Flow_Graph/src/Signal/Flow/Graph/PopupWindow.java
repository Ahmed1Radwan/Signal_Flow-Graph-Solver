package Signal.Flow.Graph;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class PopupWindow extends JFrame {
	
		
		private MasonAlgorithm mason = MasonAlgorithm.getInstance();
		
		PopupWindow(){
			initialize();
		}

		private void initialize() {
			// TODO Auto-generated method stub
			JLabel forwardPath, individualLoops, tfLabel, nonTouchingLoop;
			TextArea forwardPathContent,individualLoopsContent,nonTouchingLoopContent;
			JLabel tfLabelContent;
			
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screenSize.getWidth() - 400;
			int height = (int) screenSize.getHeight() - 380;
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 50, width, height);
			setTitle("Signal FLow Graph Solver - Results");
			setLayout(null);
			setResizable(true);
			
			forwardPath = new JLabel("ForwardPaths");
			forwardPathContent=new TextArea();
			
			individualLoops = new JLabel("Loops");
			individualLoopsContent =new TextArea();
			
			nonTouchingLoop = new JLabel("NonTouching loops");
			nonTouchingLoopContent =new TextArea();
			
			tfLabel = new JLabel("overall T.F = ");
			tfLabelContent = new JLabel();
			
			forwardPath.setBounds(100, 50, 250, 40);
			forwardPathContent.setBounds(100, 90, 250, 250);
			
			individualLoops.setBounds(400, 50, 250, 40);
			individualLoopsContent.setBounds(400, 90, 250, 250);
			
			nonTouchingLoop.setBounds(700, 50, 250, 40);
			nonTouchingLoopContent.setBounds(700, 90, 250, 250);
			
			
			tfLabel.setBounds(100,400,250,40);
			tfLabelContent.setBounds(300, 320, 1000,200 );
			
			
			Font font = new Font("Serif", Font.PLAIN, 32);
			tfLabel.setFont(font);
			tfLabelContent.setFont(font);
			
			font = new Font("Serif", Font.BOLD, 16);

			forwardPath.setFont(font);
			individualLoops.setFont(font);
			nonTouchingLoop.setFont(font);
			forwardPathContent.setFont(font);
			individualLoopsContent.setFont(font);
			nonTouchingLoopContent.setFont(font);
			
			
			getContentPane().add(forwardPath);
			getContentPane().add(individualLoops);
			getContentPane().add(tfLabel);
			getContentPane().add(nonTouchingLoop);
			getContentPane().add(forwardPathContent);
			getContentPane().add(individualLoopsContent);
			getContentPane().add(nonTouchingLoopContent);
			getContentPane().add(tfLabelContent);
			
			
			String sb = "";
			String[] tempArr = mason.getForwardPaths();
			for (int i = 0; i < tempArr.length; i++) {
				String n = Integer.toString(i+1);
				sb += "P";
				sb+=  n ;
				sb += ") ";
				sb+=  tempArr[i] ;
				sb+= "\n";
			}
				
			
			forwardPathContent.setText(sb);
			forwardPathContent.setEditable(false);
			forwardPathContent.setForeground(Color.blue);
			forwardPathContent.setBackground(Color.WHITE);
			
			sb = "";
			tempArr = mason.getLoops();
			for (int i = 0; i < tempArr.length; i++) 
			{
				String n = Integer.toString(i+1);
				sb += "L";
				sb+=  n ;
				sb += ") ";
				sb+=  tempArr[i] ;
				sb+= "\n";
			}
			
			individualLoopsContent.setText(sb);
			individualLoopsContent.setEditable(false);
			individualLoopsContent.setForeground(Color.blue);
			individualLoopsContent.setBackground(Color.WHITE);
			
			sb = "";
			tempArr = mason.getNonTouchingloops();
			for (int i = 0; i < tempArr.length; i++) {
				String n = Integer.toString(i+1);
				sb += "NL";
				sb+=  n ;
				sb += ") ";
				sb+=  tempArr[i] ;
				sb+= "\n";
			}
			
			nonTouchingLoopContent.setText(sb);
			nonTouchingLoopContent.setEditable(false);
			nonTouchingLoopContent.setForeground(Color.blue);
			nonTouchingLoopContent.setBackground(Color.WHITE);
			
			
			tfLabelContent.setText(mason.getOverAllTF()+"");
			
		}
		
		
}
