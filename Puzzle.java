import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Puzzle {

	private static List<Piece> pieces;
	private static Boolean[][] playBoard;
	private static Map<String, List<Piece>> solutions;

	public static View myView;

	private static boolean runApp;
	private static boolean runAlgorithm;
	private static int gamemode;

	private static long startTime;
	private static int moveCount;

	public static void main(String[] args) {

		gamemode = 0;
		runApp = true;
		runAlgorithm = false;

		KeyAdapter mainKeyListener = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					myView.switchPalette();
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					runAlgorithm = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (runAlgorithm) {
						gamemode = 0;
						runAlgorithm = false;
					} else {
						runApp = false;
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_1) {
					gamemode = 1;
				}
				if (e.getKeyCode() == KeyEvent.VK_2) {
					gamemode = 2;
				}
				if (e.getKeyCode() == KeyEvent.VK_3) {
					gamemode = 3;
				}
			}
		};

		myView = new View();
		myView.addKeyListener(mainKeyListener);
		
		while (runApp) {
			if(gamemode == 0) {
				myView.showMenu(true);
			} else {
				myView.showMenu(false);
				runAlgorithm(gamemode);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		System.exit(0);
	}

	public static void initialiseGame() {

		pieces = PieceFactory.puzzlePieces1();
		solutions = new HashMap<String, List<Piece>>();

		Collections.shuffle(pieces);

		playBoard = new Boolean[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				playBoard[i][j] = false;
			}
		}
	}

	private static void runAlgorithm(int algorithm) {

		initialiseGame();
		List<Piece> remainingPieces = new ArrayList<Piece>(pieces);
		List<Piece> placedPieces = new ArrayList<Piece>();
		runAlgorithm(algorithm, placedPieces, remainingPieces);
	}

	public static void runAlgorithm(int algorithm, List<Piece> placedPieces, List<Piece> remainingPieces) {

		runAlgorithm = true;
		startTime = System.currentTimeMillis();
		moveCount = 0;

		if (algorithm == 1) {

			while (runAlgorithm) {
				makeMove(placedPieces, remainingPieces, false);
				moveCount++;

				if (moveCount % 1000000 == 0) {
					System.out.println(moveCount + " moves so far.");
				}
			}

		} else if (algorithm == 2) {

			while (runAlgorithm) {
				makeMove(placedPieces, remainingPieces, true);
				moveCount++;

				if (moveCount % 1000000 == 0) {
					System.out.println(moveCount + " moves so far.");
				}
			}

		} else if (algorithm == 3) {

			makeIntelligentMove(placedPieces, remainingPieces);
		}

		System.out.println("Solve time " + ((System.currentTimeMillis() - startTime) / 1000));
		System.out.println("Total move count " + moveCount);
	}

	private static boolean checkEndOfGame(List<Piece> newPlacedPieces, List<Piece> newRemainingPieces) {

		if (!runAlgorithm) {
			return true;
		} else if (newRemainingPieces.size() != 0) {
			return false;
		}

		boolean newResult = addSolution(newPlacedPieces);
		if (newResult) {
			System.out.print(printPieces(newPlacedPieces));
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private static void makeMove(List<Piece> placedPieces,
			List<Piece> remainingPieces, boolean performAnalysis) {

		Piece curPiece = remainingPieces.get(0);
		boolean moreOptions = true;
		boolean testResult = false;

		while (moreOptions && !testResult) {

			testResult = testPiece(curPiece);
			if (!testResult) {
				moreOptions = shufflePiece(curPiece);

			} else if (performAnalysis) {

				// for debugging temporarily draw the piece being tested
				placedPieces.add(curPiece);
				myView.updatePieces(placedPieces);
				placedPieces.remove(curPiece);

				// check empty spaces surrounding the piece
				// do a quick (shallow) test of surrounding squares to see if
				// there is any immediate bad squares
				List<Piece> tempList = new ArrayList<Piece>(remainingPieces);
				tempList.remove(curPiece);
				placePiece(curPiece);
				List<Integer[]> resultsArray = getCountMapForSurroundingSquares(
						curPiece, tempList);
				removePiece(curPiece);

				if (resultsArray == null) {
					testResult = false;
					moreOptions = shufflePiece(curPiece);
				}
			}
		}

		// if we found a legitimate move
		if (testResult) {

			placePiece(curPiece);
			remainingPieces.remove(curPiece);
			placedPieces.add(curPiece);

			myView.updatePieces(placedPieces);

			// check for finish state
			checkEndOfGame(placedPieces, remainingPieces);

			// else we have reached a bad end and need to backtrack
		} else {

			if (!moreOptions) {
				// reset the start position of curPiece that we were unable to
				// place and add it back onto the remaining pieces
				resetPiece(curPiece);

				// remove the last placed piece from the board and then shuffle
				// it
				curPiece = placedPieces.get(placedPieces.size() - 1);
				removePiece(curPiece);
				shufflePiece(curPiece);

				placedPieces.remove(curPiece);
				remainingPieces.add(0, curPiece);
			}

			myView.updatePieces(placedPieces);
		}
	}

	private static void makeIntelligentMove(List<Piece> placedPieces,
			List<Piece> remainingPieces) {

		Piece piece = remainingPieces.get(0);
		piece.curX = 0;
		piece.curY = 0;

		boolean moreOptions = true;
		boolean testResult = false;

		while (moreOptions && !testResult) {
			testResult = testPiece(piece)
					&& doFocusedPieceTest(piece, placedPieces, remainingPieces,
							true);
			if (!testResult) {
				moreOptions = shufflePiece(piece);
			}
		}
	}

	private static boolean doFocusedPieceTest(Piece piece,
			List<Piece> placedPieces, List<Piece> remainingPieces,
			boolean deepTest) {

		boolean result = true;

		// temporarily place piece to make it easy to check for empty spaces
		List<Piece> newRemainingPieces = new ArrayList<Piece>(remainingPieces);
		removePieceByName(newRemainingPieces, piece);

		List<Piece> newPlacedPieces = new ArrayList<Piece>(placedPieces);
		newPlacedPieces.add(piece);

		placePiece(piece);
		myView.updatePieces(newPlacedPieces);
		// myView.updateBoard(playBoard);

		// check for end of game
		if (checkEndOfGame(newPlacedPieces, newRemainingPieces)) {
			removePiece(piece);
			return false;
		}

		// check empty spaces surrounding the piece
		// do a quick (shallow) test of surrounding squares to see if there is
		// any immediate bad squares
		List<Integer[]> resultsArray = getCountMapForSurroundingSquares(piece,
				newRemainingPieces);
		result = resultsArray != null;

		// if all immediate squares are ok, start doing deep tests on each of
		// the possible options
		// starting with the piece that had the fewest possible solutions
		if (deepTest && result == true) {

			for (Integer[] coords : resultsArray) {
				result = deepscanSolutionsForPoint(coords[0], coords[1],
						newPlacedPieces, newRemainingPieces);
				if (!result) {
					break;
				}
			}

			// if result is true we have either finished or found an isolated
			// piece
			// either way make a new intelligent move
			if (result == true) {
				makeIntelligentMove(newPlacedPieces, newRemainingPieces);
			}
		}

		removePiece(piece);
		return result;
	}

	private static List<Integer[]> getCountMapForSurroundingSquares(
			Piece piece, List<Piece> remainingPieces) {

		PieceMap map = piece.getCurMap();
		int size = map.getSize();
		List<Integer> countArray = new ArrayList<Integer>();
		List<Integer[]> resultsArray = new ArrayList<Integer[]>();

		int x = isInBounds(piece.curX - 1, piece.curY) ? piece.curX - 1
				: piece.curX;
		int y = isInBounds(piece.curX, piece.curY - 1) ? piece.curY - 1
				: piece.curY;
		int width = isInBounds(piece.curX - 1, piece.curY)
				&& isInBounds(piece.curX + size, piece.curY) ? size + 2
				: size + 1;
		int height = isInBounds(piece.curX, piece.curY - 1)
				&& isInBounds(piece.curX, piece.curY + size) ? size + 2
				: size + 1;

		// test each square on a superficial level (ie: one deep)
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				if (isPlaceableSquare(i + x, j + y)) {
					int immediateSolutions = countSolutionsForPoint(i + x, j
							+ y, remainingPieces);

					// if any one point has no solutions then we can cancel this
					// path entirely
					if (immediateSolutions == 0) {
						return null;

					} else {

						// put solutions with a low count at the start of the
						// list..
						int index = Collections.binarySearch(countArray,
								immediateSolutions);
						if (index < 0) {
							resultsArray.add(-index - 1, new Integer[] { i + x,
									j + y });
							countArray.add(-index - 1, immediateSolutions);
						} else {
							resultsArray.add(index, new Integer[] { i + x,
									j + y });
							countArray.add(index, immediateSolutions);
						}
					}
				}
			}
		}
		return resultsArray;
	}

	private static int countSolutionsForPoint(int x, int y,
			List<Piece> remainingPieces) {

		int resultCount = 0;

		// for each piece yet to be placed, see if we can get that piece to
		// cover the test point
		for (Piece nextPiece : remainingPieces) {

			// clone each piece so we can mess with its coordinates
			Piece tempPiece = clonePiece(nextPiece);

			int pieceSize = tempPiece.getCurMap().getSize();

			// for each mapping that this piece contains
			for (int curPos = 0; curPos < tempPiece.positions.length; curPos++) {
				tempPiece.curPosition = curPos;
				for (int xOffset = 0; xOffset < pieceSize; xOffset++) {
					tempPiece.curX = x - xOffset;
					for (int yOffset = 0; yOffset < pieceSize; yOffset++) {
						tempPiece.curY = y - yOffset;

						// if tempPiece covers the required area in this
						// position
						// then test to see if it can be placed successfully
						if (tempPiece.getCurMap().isHit(xOffset, yOffset)
								&& testPiece(tempPiece)) {
							resultCount++;
						}

					}
				}
			}
		}

		return resultCount;
	}

	private static boolean deepscanSolutionsForPoint(int x, int y,
			List<Piece> placedPieces, List<Piece> remainingPieces) {

		boolean result = true;

		// for each piece yet to be placed, see if we can get that piece to
		// cover the test point
		for (Piece nextPiece : remainingPieces) {

			// clone each piece so we can mess with its coordinates
			Piece tempPiece = clonePiece(nextPiece);

			int pieceSize = tempPiece.getCurMap().getSize();

			// for each mapping that this piece contains
			for (int curPos = 0; curPos < tempPiece.positions.length; curPos++) {
				tempPiece.curPosition = curPos;
				for (int xOffset = 0; xOffset < pieceSize; xOffset++) {
					tempPiece.curX = x - xOffset;
					for (int yOffset = 0; yOffset < pieceSize; yOffset++) {
						tempPiece.curY = y - yOffset;

						// if tempPiece covers the required area in this
						// position
						// then test to see if it can be placed successfully
						if (tempPiece.getCurMap().isHit(xOffset, yOffset)
								&& testPiece(tempPiece)) {

							// recursively try to solve for this piece
							result = doFocusedPieceTest(tempPiece,
									placedPieces, remainingPieces, true);
						}

					}
				}
			}
		}

		return result;
	}

	private static boolean testPiece(Piece piece) {

		boolean result = true;
		PieceMap map = piece.getCurMap();
		int size = map.getSize();

		// check to see if the piece will fit at its current location and
		// position
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				// if the piece occupies this square of the map then check to
				// see if this block is safe to place in
				if (map.isHit(i, j)
						&& !isPlaceableSquare(i + piece.curX, j + piece.curY)) {
					return false;
				}
			}
		}
		return result;
	}

	// test if there is already a square in this position or this position is
	// out of bounds
	private static boolean isPlaceableSquare(int x, int y) {

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

	private static void removePieceByName(List<Piece> pieces,
			Piece pieceToRemove) {
		Piece matchingPiece = null;
		for (Piece curPiece : pieces) {
			if (curPiece.pieceName.equals(pieceToRemove.pieceName)) {
				matchingPiece = curPiece;
			}
		}
		pieces.remove(matchingPiece);
	}

	public static Piece clonePiece(Piece pieceToClone) {
		Piece newPiece = new Piece(pieceToClone.positions,
				pieceToClone.pieceName);
		newPiece.curPosition = pieceToClone.curPosition;
		newPiece.curX = pieceToClone.curX;
		newPiece.curY = pieceToClone.curY;
		return newPiece;
	}

	// test if there is already a square in this position or this position is
	// out of bounds
	private static boolean isInBounds(int x, int y) {

		boolean result = true;
		try {
			boolean test = playBoard[x][y];
		} catch (ArrayIndexOutOfBoundsException ex) {
			result = false;
		}
		return result;
	}

	public static void placePiece(Piece piece) {

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

		piece.curPosition = 0;
		piece.curX = 0;
		piece.curY = 0;
	}

	private static void printPlayBoard() {
		for (int x = 0; x < playBoard.length; x++) {
			for (int y = 0; y < playBoard.length; y++) {

				System.out.print(playBoard[y][x] ? "x" : "`");
			}
			System.out.print("\n");
		}
	}

	private static String printPieces(List<Piece> playedPieces) {

		String piecesStr = "";
		for (int x = 0; x < playBoard.length; x++) {
			for (int y = 0; y < playBoard.length; y++) {
				for (Piece curPiece : playedPieces) {
					if (curPiece.getCurMap().isHit(x - curPiece.curX,
							y - curPiece.curY)) {
						piecesStr += curPiece.pieceName;
					}
				}
			}
			piecesStr += "\n";
		}
		piecesStr += "\n";
		return piecesStr;
	}

	private static boolean addSolution(List<Piece> playedPieces) {

		String key = printPieces(playedPieces);
		if (solutions.get(key) == null) {
			List<Piece> newSolution = new LinkedList<Piece>();
			for (Piece curPiece : playedPieces) {
				newSolution.add(clonePiece(curPiece));
			}
			solutions.put(key, newSolution);
			return true;
		}
		return false;
	}

}
