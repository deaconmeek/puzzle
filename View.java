import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class View extends JFrame {

	JPanel mainContentPane;
	List<Piece> pieces;
	int squareWidth;
	
	public View() {

		setTitle("PUZZLE!");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.squareWidth = 50;
        int puzzleWidth = this.squareWidth * 8;

		this.setMinimumSize(new Dimension(420,440));
		this.getContentPane().add(getMainContentPane());
        this.setVisible(true);
	}
	
	public void startTimer() {
		
	  int delay = 100; //milliseconds
	  ActionListener taskPerformer = new ActionListener() {
	      public void actionPerformed(ActionEvent evt) {
	    	  getMainContentPane().repaint();
	      }
	  };
	  new Timer(delay, taskPerformer).start();
	}
	
	private JPanel getMainContentPane() {
		
		if (mainContentPane == null) {
			
			mainContentPane = new JPanel() {
				public void paintComponent(Graphics g) { 
					paintPuzzle(g);
	            }
			};
		}
		return mainContentPane;
	}
	
	public void drawPieces(List<Piece> pieces) {
		
		this.pieces = pieces;
		getMainContentPane().repaint();
	}
	
	private void paintPuzzle(Graphics g) {
    	
		g.clearRect(0, 0, 1000, 1000);
		if (pieces != null) {

			for(Piece nextPiece : pieces) {
				
				if (nextPiece.inPlay) {
					drawPiece(nextPiece, g);
				}
			}
		}
	}
	
	private void drawPiece(Piece piece, Graphics g) {
		
		PieceMap map = piece.getCurMap();
		int size = map.getSize();
		
		g.setColor(piece.colour);
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				if (map.isHit(i,j)) {

			        g.fillRect(
			        		this.squareWidth * (piece.curX + i),
			        		this.squareWidth * (piece.curY + j),
			        		this.squareWidth,
			        		this.squareWidth
	        		);
				}
			}
		}
	}
	
}
