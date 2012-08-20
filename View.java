import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class View extends JFrame {

	JPanel mainContentPane;
	Boolean[][] playBoard;
	String[] menu;
	List<Piece> pieces;
	int squareWidth;
	List<Map<String, Color>> palettes;
	Map<String, Color> curPalette;
	
	public View() {

		setTitle("PUZZLE!");
		setLocation(new Point(5,5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        this.squareWidth = 100;

        initialisePalettes();
        
		this.getContentPane().add(getMainContentPane());
		this.pack();
		this.setResizable(false);
        this.setVisible(true);
        
        startTimer();
	}
	
	public void showMenu(boolean show) {

		if (show) {
			String[] menuStr = {
					"Choose your algorithm:", 
					"1: BASIC", 
					"2: INTERMEDIATE", 
					"3: ADVANCED"};
			
			menu = menuStr;
		} else {
			menu = null;
		}
	}

	public void initialisePalettes() {
		
		palettes = PaletteFactory.generatePalettes();
		Collections.shuffle(palettes);
		curPalette = palettes.get(0);
	}
	
	public void switchPalette() {

		Collections.shuffle(palettes);
		curPalette = palettes.get(0);
	}

	private String[] getMenuTitle() {
		String[] menuTitleStr = {
			"                               ___           __  ",
			"                              /\\_ \\         /\\ \\   ", 
			" _____   __  __  ____    ____ \\//\\ \\      __\\ \\ \\   ",
			"/\\  __`\\/\\ \\/\\ \\/\\_ ,`\\ /\\_ ,`\\ \\ \\ \\   /'__`\\ \\ \\  ",
			"\\ \\ \\_\\ \\ \\ \\_\\ \\/_/  /_\\/_/  /_ \\_\\ \\_/\\  __/\\ \\_\\ ",
			" \\ \\  __/\\ \\____/ /\\____\\ /\\____\\/\\____\\ \\____\\\\/\\_\\",
			"  \\ \\ \\/  \\/___/  \\/____/ \\/____/\\/____/\\/____/ \\/_/",
			"   \\ \\_\\                                            ",
			"    \\/_/                                            "
		};
		return menuTitleStr;
	}

	public void startTimer() {
		
	  int delay = 40; //milliseconds
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

	public void updatePieces(List<Piece> pieces) {
		
		List<Piece> tempList = new LinkedList<Piece>();
		for (Piece curPiece : pieces) {
			tempList.add(Puzzle.clonePiece(curPiece));
		}
		this.pieces = tempList;
	}

	public void updateBoard(Boolean[][] playBoard) {
		
		this.playBoard = playBoard;
	}
	
	private void paintPuzzle(Graphics g) {
    	
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, 1000, 1000);
		
		if (menu != null) {

	    	g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
			g.setColor(Color.WHITE);

			int yPos = 150;
			int xPos = 80;
			for (String curString : getMenuTitle()) {
		        g.drawString( curString, xPos, yPos);
		        yPos += 20;
			}
			
	    	g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));
	    	yPos += 150;
			xPos += 10;
			for (String curString : menu) {
		        g.drawString( curString, xPos, yPos);
		        yPos += 50;
			}
		} else {
			
			if (pieces != null) {

				for(Piece nextPiece : pieces) {
					drawPiece(nextPiece, g);
				}
			}
			
			if (playBoard != null) {

				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if (!playBoard[i][j]) {
							drawSpace(i, j, g);
						}
					}
				}
			}
		}
	}
	
	private void drawPiece(Piece piece, Graphics g) {
		
		PieceMap map = piece.getCurMap();
		int size = map.getSize();
		
		g.setColor(getPieceColour(piece.pieceName));
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
	
	private void drawSpace(int x, int y, Graphics g) {
		
		g.setColor(Color.GREEN);
        g.drawRect(
        		this.squareWidth * x,
        		this.squareWidth * y,
        		this.squareWidth,
        		this.squareWidth
		);
	}
	
	private Color getPieceColour(String name) {
		
		if (name.equals("a") || name.equals("k")) {
			return curPalette.get(PaletteFactory.yellow);
			
		} else if (name.equals("b")) {
			return curPalette.get(PaletteFactory.white);
			
		}if (name.equals("c") || name.equals("h")) {
			return curPalette.get(PaletteFactory.dark_blue);
			
		}if (name.equals("d") || name.equals("e")) {
			return curPalette.get(PaletteFactory.red);
			
		}if (name.equals("f") || name.equals("i")) {
			return curPalette.get(PaletteFactory.blue);
			
		}if (name.equals("g") || name.equals("j")) {
			return curPalette.get(PaletteFactory.green);
			
		}
		
		return Color.BLACK;
	}
	
	
}
