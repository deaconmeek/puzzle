import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;


public class Puzzle {

	
	private static Puzzle puzzle;
	private static List<Piece> pieces;
	private static View myView;
	private static Boolean[][] playBoard;
	
	private static int curPieceNumber;
	private static boolean finished;
	private static int forceShuffleIndex;
	private static boolean forceReset;
	
	public static void main(String[] args) {

		puzzle = new Puzzle();
		myView = new View();
		pieces = PieceFactory.puzzlePieces1();

		Collections.shuffle(pieces);
		
		initialiseBoard();
		
		curPieceNumber = 0;
		finished = false;
		forceShuffleIndex = -1;
		forceReset = false;
		
		KeyAdapter mainKeyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
            	/*if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            		makeMove();
        			myView.drawPieces(pieces);
            	}
            	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            		(new Thread(puzzle.new AutoMove())).start();
            	}*/
            	if (e.getKeyCode() == KeyEvent.VK_SPACE) { forceReset = true; }
            	if (e.getKeyCode() == KeyEvent.VK_1) { forceShuffleIndex = 0; }
            	if (e.getKeyCode() == KeyEvent.VK_2) { forceShuffleIndex = 1; }
            	if (e.getKeyCode() == KeyEvent.VK_3) { forceShuffleIndex = 2; }
            	if (e.getKeyCode() == KeyEvent.VK_4) { forceShuffleIndex = 3; }
            	if (e.getKeyCode() == KeyEvent.VK_5) { forceShuffleIndex = 4; }
            }
		};

		myView.addKeyListener(mainKeyListener);
		
		(new Thread(puzzle.new AutoMove())).start();
	}
	
	private static void initialiseBoard() {

		playBoard = new Boolean[8][8];
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				playBoard[i][j] = false;
			}
		}
	}
	
	public class AutoMove implements Runnable {
		
		 public void run() {
			 while (!finished) {
				 //check for force shuffle
				 if (forceShuffleIndex >= 0) {
					 forceShuffle(forceShuffleIndex);
					 forceShuffleIndex = -1;
				 }
				 //check for reset
				 if (forceReset) {
					Collections.shuffle(pieces);
            		initialiseBoard();
            		resetAllPieces();
            		curPieceNumber = 0;
            		forceReset = false;
				 }
	     		 makeMove();
    			 myView.drawPieces(pieces);
	 		 }
		 }

	}
	
	
	private static void makeMove() {
		
		Piece curPiece = pieces.get(curPieceNumber);
		boolean moreOptions = true;
		boolean testResult = false;
		
		while (moreOptions && !testResult) {
			testResult = testPiece(curPiece);
    			if (!testResult) {
				moreOptions = shufflePiece(curPiece);
			}
		}
		
		//if we found a legitimate move
		if (testResult) {
			placePiece(curPiece);
			
			//check for finish state
			if (curPieceNumber  == 10) {
				finished = true;
				return;
			}

			curPieceNumber++;

		//otherwise we have reached a bad end and need to backtrack
		} else {
			
			//reset the start position of curPiece that we were unable to place
			resetPiece(curPiece);
			
			//remove the last placed piece from the board and then shuffle it
			curPieceNumber--;
			curPiece = pieces.get(curPieceNumber);
			removePiece(curPiece);
			shufflePiece(curPiece);
		}
		
		
		
	}
	
	private static boolean testPiece(Piece piece) {
		
		PieceMap map = piece.getCurMap();
		int size = map.getSize();
		boolean result = true;
		
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				if (map.isHit(i,j)) {

					try {
						if (playBoard[i + piece.curX][j + piece.curY]) {
							result = false;
							break;
						}
					} catch (ArrayIndexOutOfBoundsException ex) {
						result = false;
						break;
					}
				}
			}
		}
		return result;
	}

	private static void placePiece(Piece piece) {
		
		piece.inPlay = true;
		PieceMap map = piece.getCurMap();
		int size = map.getSize();
		
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				if (map.isHit(i,j)) {
					playBoard[i + piece.curX][j + piece.curY] = true;
				}
			}
		}
	}

	private static void removePiece(Piece piece) {
		
		if (piece.inPlay) {
			
			piece.inPlay = false;
			PieceMap map = piece.getCurMap();
			int size = map.getSize();
			
			for (int i=0; i<size; i++) {
				for (int j=0; j<size; j++) {
					if (map.isHit(i,j)) {
						playBoard[i + piece.curX][j + piece.curY] = false;
					}
				}
			}
		}
		
	}

	//returns true if it successfully found a new position
	//and false if it has run out of options
	private static boolean shufflePiece(Piece piece) {
		
		boolean result = true;
		
		//if we are not in the final position (rotation and mirror combination)
		if (piece.curPosition+1 < piece.getTotalPositions()) {
			piece.curPosition++;
		
		//otherwise try moving its position
		} else {
			piece.curPosition = 0;
			
			if (piece.curX < 7) {
				piece.curX++;
			} else if (piece.curY < 7) {
				piece.curX = 0;
				piece.curY++;
			} else {
				result = false;
			}
		}
		return result;
	}

	private static void resetPiece(Piece piece) {
		
		piece.inPlay = false;
		piece.curPosition = 0;
		piece.curX = 0;
		piece.curY = 0;
	}
	
	private static void resetAllPieces() {
		
		for (Piece piece : pieces) {
			piece.inPlay = false;
			piece.curPosition = 0;
			piece.curX = 0;
			piece.curY = 0;
		}
	}

	
	private static void forceShuffle(int pieceNumber) {
		
		//reset all pieces after this piece
		for (int i = pieces.size()-1; i > pieceNumber; i--) {
			Piece curPiece = pieces.get(i);
			removePiece(curPiece);
			resetPiece(curPiece);
		}
		
		//shuffle requested piece
		Piece curPiece = pieces.get(pieceNumber);
		removePiece(curPiece);
		shufflePiece(curPiece);
		
		curPieceNumber = pieceNumber;
	}
}

