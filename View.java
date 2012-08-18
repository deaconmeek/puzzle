import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class View extends JFrame {

	JPanel mainContentPane;
	List<Piece> pieces;
	int squareWidth;
	Map<String, Color> curPalette;
	
	public View() {

		setTitle("PUZZLE!");
		setLocation(new Point(50,50));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        this.squareWidth = 100;
        
		this.getContentPane().add(getMainContentPane());
		this.pack();
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
			int puzzleWidth = this.squareWidth * 8;
			mainContentPane.setPreferredSize(new Dimension(puzzleWidth,puzzleWidth));
		}
		return mainContentPane;
	}
	
	public void drawPieces(List<Piece> pieces) {
		
		this.pieces = pieces;
		getMainContentPane().repaint();
	}
	
	private void paintPuzzle(Graphics g) {
    	
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, 1000, 1000);
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
		
		g.setColor(getPieceColour(piece.pieceNumber));
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
	
	private Color getPieceColour(int pieceNumber) {
		
		if (pieceNumber == 1 || pieceNumber == 11) {
			return curPalette.get(PaletteFactory.yellow);
			
		} else if (pieceNumber == 2) {
			return curPalette.get(PaletteFactory.white);
			
		}if (pieceNumber == 3 || pieceNumber == 8) {
			return curPalette.get(PaletteFactory.dark_blue);
			
		}if (pieceNumber == 4 || pieceNumber == 5) {
			return curPalette.get(PaletteFactory.red);
			
		}if (pieceNumber == 6 || pieceNumber == 9) {
			return curPalette.get(PaletteFactory.blue);
			
		}if (pieceNumber == 7 || pieceNumber == 10) {
			return curPalette.get(PaletteFactory.green);
			
		}
		
		return Color.BLACK;
	}
	
}
