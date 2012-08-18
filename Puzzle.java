import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Puzzle {

	private static Puzzle puzzle;
	private static List<Piece> pieces;
	private static List<Map<String, Color>> palettes;
	private static View myView;
	private static Boolean[][] playBoard;

	private static int curPieceNumber;
	private static boolean finished;
	private static int forceShuffleIndex;
	private static boolean forceReset;
	private static boolean switchPalette;

	public static void main(String[] args) {

		puzzle = new Puzzle();
		myView = new View();
		pieces = PieceFactory.puzzlePieces1();
		palettes = PaletteFactory.generatePalettes();

		Collections.shuffle(pieces);
		Collections.shuffle(palettes);

		initialiseBoard();

		curPieceNumber = 0;
		finished = false;
		forceShuffleIndex = -1;
		forceReset = false;
		switchPalette = false;

		KeyAdapter mainKeyListener = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				/*
				 * if (e.getKeyCode() == KeyEvent.VK_SPACE) { makeMove();
				 * myView.drawPieces(pieces); } if (e.getKeyCode() ==
				 * KeyEvent.VK_ENTER) { (new Thread(puzzle.new
				 * AutoMove())).start(); }
				 */
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					switchPalette = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					forceReset = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_1) {
					forceShuffleIndex = 0;
				}
				if (e.getKeyCode() == KeyEvent.VK_2) {
					forceShuffleIndex = 1;
				}
				if (e.getKeyCode() == KeyEvent.VK_3) {
					forceShuffleIndex = 2;
				}
				if (e.getKeyCode() == KeyEvent.VK_4) {
					forceShuffleIndex = 3;
				}
				if (e.getKeyCode() == KeyEvent.VK_5) {
					forceShuffleIndex = 4;
				}
			}
		};

		myView.addKeyListener(mainKeyListener);
		myView.curPalette = palettes.get(0);

		(new Thread(puzzle.new AutoMove())).start();
	}

	private static void initialiseBoard() {

		playBoard = new Boolean[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				playBoard[i][j] = false;
			}
		}
	}

	public class AutoMove implements Runnable {

		public void run() {
			long startTime = System.currentTimeMillis();
			int moveCount = 0;
			while (!finished) {
				// check for force shuffle
				if (forceShuffleIndex >= 0) {
					forceShuffle(forceShuffleIndex);
					forceShuffleIndex = -1;
				}
				// check for switch palette
				if (switchPalette) {
					Collections.shuffle(palettes);
					myView.curPalette = palettes.get(0);
					switchPalette = false;
				}
				// check for reset
				if (forceReset) {
					Collections.shuffle(pieces);
					initialiseBoard();
					resetAllPieces();
					curPieceNumber = 0;
					forceReset = false;
					startTime = System.nanoTime();
				}
				makeMove();
				moveCount++;
				myView.drawPieces(pieces);
				if (moveCount % 1000000 == 0) {
					System.out.println(moveCount + " moves so far.");
				}
			}
			System.out.println("Solve time "
					+ ((System.currentTimeMillis() - startTime) / 1000));
			System.out.println("Total move count " + moveCount);
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

		// if we found a legitimate move
		if (testResult) {
			placePiece(curPiece);

			// check for finish state
			if (curPieceNumber == 10) {
				finished = true;
				return;
			}

			curPieceNumber++;

			// otherwise we have reached a bad end and need to backtrack
		} else {

			// reset the start position of curPiece that we were unable to place
			resetPiece(curPiece);

			// remove the last placed piece from the board and then shuffle it
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

		//check to see if the piece will fit at its current location and position
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				
				//if the piece occupies this square of the map then check to see if this block is safe to place in
				if (map.isHit(i, j) && !checkBlock(i + piece.curX, j + piece.curY)) {
					return false;
				}
			}
		}
		
		return result;
	}

	//test if there is already a square in this position or this position is out of bounds
	private static boolean checkBlock(int x, int y) {
		
		boolean result = true;
		try {
			if (playBoard[x][y]) {
				result = false;
			}

		} catch (ArrayIndexOutOfBoundsException ex) {
			result = false;
		}
		return result;
	}

	//test if there is already a square in this position or this position is out of bounds
	private static boolean isInBounds(int x, int y) {
		
		boolean result = true;
		try {
			boolean test = playBoard[x][y];
		} catch (ArrayIndexOutOfBoundsException ex) {
			result = false;
		}
		return result;
	}

	private static void placePiece(Piece piece) {

		piece.inPlay = true;
		PieceMap map = piece.getCurMap();
		int size = map.getSize();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (map.isHit(i, j)) {
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

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (map.isHit(i, j)) {
						playBoard[i + piece.curX][j + piece.curY] = false;
					}
				}
			}
		}

	}

	// returns true if it successfully found a new position
	// and false if it has run out of options
	private static boolean shufflePiece(Piece piece) {

		boolean result = true;

		// if we are not in the final position (rotation and mirror combination)
		if (piece.curPosition + 1 < piece.getTotalPositions()) {
			piece.curPosition++;

			// otherwise try moving its position
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

		// reset all pieces after this piece
		for (int i = pieces.size() - 1; i > pieceNumber; i--) {
			Piece curPiece = pieces.get(i);
			removePiece(curPiece);
			resetPiece(curPiece);
		}

		// shuffle requested piece
		Piece curPiece = pieces.get(pieceNumber);
		removePiece(curPiece);
		shufflePiece(curPiece);

		curPieceNumber = pieceNumber;
	}
}
