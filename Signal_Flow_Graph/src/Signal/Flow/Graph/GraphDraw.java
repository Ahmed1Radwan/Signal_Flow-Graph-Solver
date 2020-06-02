package Signal.Flow.Graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.JFrame;

public class GraphDraw extends JFrame{
	
	MasonAlgorithm mason = MasonAlgorithm.getInstance();
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		int numOfNodes = mason.getNumOfNodes();
		double[][] gains = mason.getAdjacencyMatrix();
		int xDisplace = mason.getWidth() / (numOfNodes + 1);
		int yCenter = (mason.getHeight() - 120) / 2;
		int nodeRadius = 25;

		int tanBaseUp = (int) (yCenter - nodeRadius);
		int tanBaseDown = (int) (yCenter + nodeRadius);
		float selfLoopC1 = yCenter - 4 * nodeRadius;
		float selfLoopC2 = yCenter + 4 * nodeRadius;
		Path2D.Double path = new Path2D.Double();
		Graphics2D g2 = (Graphics2D) g;

		Font font = new Font("Serif", Font.PLAIN, 24);
		g.setFont(font);
		
		g.setColor(Color.white); // draw nodes itself
		for (int i = 0; i < numOfNodes ; i++)
			g.fillOval(xDisplace * (i + 1) - nodeRadius, yCenter - nodeRadius,nodeRadius * 2, nodeRadius * 2);
		
		if(numOfNodes > 0) {
			g.setColor(Color.black);
			g.drawString("R(s)", xDisplace - nodeRadius, yCenter - 2 * nodeRadius);
			if (numOfNodes > 1)
				g.drawString("C(s)", xDisplace * (numOfNodes) - nodeRadius, yCenter - 2 * nodeRadius);
		}
		
		g.setColor(Color.black); // draw nodes number
		for (int i = 0; i < numOfNodes; i++)
			g.drawString("" + (i + 1), xDisplace * (i + 1) - nodeRadius + 18,yCenter + 8);
		
		int x;
		for (int sNode = 0; sNode < numOfNodes; sNode++) {
			for (int eNode = 0; eNode < numOfNodes; eNode++) {
				if (gains[sNode][eNode] != 0) {
					// drawing self loops
					if (sNode == eNode) {
						// drawing arc
						g.setColor(Color.black);
						path = new Path2D.Double();
						path.moveTo(xDisplace * (sNode + 1), tanBaseUp);
						x = xDisplace * (sNode + 1) - 3 * nodeRadius;
						path.curveTo(x, selfLoopC1, x, selfLoopC2, xDisplace * (sNode + 1), tanBaseDown);
						g2.draw(path);

						x = x + nodeRadius - 5;
						// drawing arrow
						path = new Path2D.Double();
						path.moveTo(x + 12, yCenter - 10);
						path.lineTo(x, yCenter + 12);
						path.lineTo(x - 12, yCenter - 10);
						g2.fill(path);
						// drawing gain text
						g.setColor(Color.black);
						g.drawString(gains[sNode][eNode] + "", xDisplace * (sNode + 1) - nodeRadius, yCenter - 2 * nodeRadius);

					} else if (eNode - sNode == 1) {
						// drawing arc
						g.setColor(Color.black);
						g.drawLine((sNode + 1) * xDisplace + nodeRadius,yCenter, (eNode + 1) * xDisplace - nodeRadius,yCenter);
						
						// drawing arrow
						x = (eNode + sNode + 2) * xDisplace / 2;
						path = new Path2D.Double();
						path.moveTo(x, yCenter - 12);
						path.lineTo(x, yCenter + 12);
						path.lineTo(x + 24, yCenter);
						g2.fill(path);
						// drawing gain text
						g.setColor(Color.black);
						g.drawString(gains[sNode][eNode] + "", x, yCenter - 20);

						// feedback
					} else if (sNode > eNode) {
						// drawing arc
						g.setColor(Color.black);
						path = new Path2D.Double();
						path.moveTo(xDisplace * (sNode + 1), tanBaseDown);
						x = xDisplace * (eNode + sNode + 2) / 2;
						path.quadTo(x, yCenter + (sNode - eNode) * xDisplace / 2, xDisplace * (eNode + 1), tanBaseDown);
						g2.draw(path);

						// drawing arrow
						path = new Path2D.Double();
						path.moveTo(x - 12, yCenter + (sNode - eNode) * xDisplace / 4 + 12);
						path.lineTo(x + 12, yCenter + (sNode - eNode) * xDisplace / 4);
						path.lineTo(x + 12, yCenter + (sNode - eNode) * xDisplace / 4 + 24);
						g2.fill(path);

						// drawing gain text
						g.setColor(Color.black);
						g.drawString(gains[sNode][eNode] + "", x - 12, yCenter + (sNode - eNode) * xDisplace / 4 - 6);

					} else {
						// drawing arc
						g.setColor(Color.black);
						path = new Path2D.Double();
						path.moveTo(xDisplace * (sNode + 1), tanBaseUp);
						x = xDisplace * (eNode + sNode + 2) / 2;
						path.quadTo(x, yCenter - (eNode - sNode) * xDisplace / 2, xDisplace * (eNode + 1), tanBaseUp);
						g2.draw(path);

						// drawing arrow
						path = new Path2D.Double();
						path.moveTo(x + 12, yCenter - (eNode - sNode) * xDisplace / 4 - 12);
						path.lineTo(x - 12, yCenter - (eNode - sNode) * xDisplace / 4);
						path.lineTo(x - 12, yCenter - (eNode - sNode) * xDisplace / 4 - 24);
						g2.fill(path);
						// drawing gain text
						g.setColor(Color.black);
						g.drawString(gains[sNode][eNode] + "", x - 12, yCenter - (eNode - sNode) * xDisplace / 4 + 24);
					}
				}
			}

		}
	}

}