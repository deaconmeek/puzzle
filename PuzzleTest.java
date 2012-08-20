import java.util.LinkedList;
import java.util.List;


public class PuzzleTest {

	private static Puzzle puzzle;

	public static void main(String[] args) {

		puzzle = new Puzzle();
		puzzle.initialiseGame();
/*
		KeyAdapter mainKeyListener = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					forceReset = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		};

		puzzle.myView.addKeyListener(mainKeyListener);
*/
		testDoFocusedPieceTest();
	}
	
	private static void testDoFocusedPieceTest() {
		
		List<Piece> placedPieces = new LinkedList<Piece>();
		List<Piece> remainingPieces = new LinkedList<Piece>(PieceFactory.puzzlePieces1());
		
		
		Piece p = getPieceByName("d", 3, 0, 0);
		//assertTrue("Piece4 doFocusedPieceTest", puzzle.doFocusedPieceTest(p));
		puzzle.placePiece(p);
		placedPieces.add(p);
		remainingPieces.remove(p);
		

		p = getPieceByName("a", 1, 1, 0);
		//assertTrue("Piece1 doFocusedPieceTest", puzzle.doFocusedPieceTest(p1));
		puzzle.placePiece(p);
		placedPieces.add(p);
		remainingPieces.remove(p);
		

		p = getPieceByName("b", 3, 4, 0);
		//assertTrue("Piece2 doFocusedPieceTest", puzzle.doFocusedPieceTest(p2));
		puzzle.placePiece(p);
		placedPieces.add(p);
		remainingPieces.remove(p);
		

		puzzle.myView.menu = null;
		puzzle.myView.updatePieces(placedPieces);
		puzzle.runAlgorithm(2, placedPieces, remainingPieces);

		/*
		p = getPieceByName("k", 4, 5, 0);
		//assertTrue("Piece11 doFocusedPieceTest", puzzle.doFocusedPieceTest(p));
		puzzle.placePiece(p);
		placedPieces.add(p);
		remainingPieces.remove(p);
		

		p = getPieceByName("g", 4, 0, 3);
		//assertTrue("Piece7 doFocusedPieceTest", puzzle.doFocusedPieceTest(p7));
		puzzle.placePiece(p);
		placedPieces.add(p);
		remainingPieces.remove(p);
		

		p = getPieceByName("f", 4, 3, 2);
		//assertTrue("Piece6 doFocusedPieceTest", puzzle.doFocusedPieceTest(p6));
		puzzle.placePiece(p);
		placedPieces.add(p);
		remainingPieces.remove(p);
		

		p = getPieceByName("j", 1, 6, 4);
		//assertTrue("Piece10 doFocusedPieceTest", puzzle.doFocusedPieceTest(p10));
		puzzle.placePiece(p);
		placedPieces.add(p);
		remainingPieces.remove(p);
		

		p = getPieceByName("i", 3, 0, 4);
		//assertTrue("Piece9 doFocusedPieceTest", puzzle.doFocusedPieceTest(p9));
		puzzle.placePiece(p);
		placedPieces.add(p);
		remainingPieces.remove(p);
		

		p = getPieceByName("e", 0, 3, 4);
		//assertTrue("Piece5 doFocusedPieceTest", puzzle.doFocusedPieceTest(p5));
		puzzle.placePiece(p);
		placedPieces.add(p);
		remainingPieces.remove(p);
		

		p = getPieceByName("c", 0, 1, 5);
		//assertTrue("Piece3 doFocusedPieceTest", puzzle.doFocusedPieceTest(p3));
		puzzle.placePiece(p);
		placedPieces.add(p);
		remainingPieces.remove(p);
		

		Piece p8 = getPieceByName("h", 3, 4, 5);
		//assertTrue("Piece8 doFocusedPieceTest", puzzle.doFocusedPieceTest(p8));
		puzzle.placePiece(p8);*/

	}
	
	private static Piece getPieceByName(String pieceName, int pos, int x, int y) {
		
		for (Piece curPiece : PieceFactory.puzzlePieces1()) {
			if (curPiece.pieceName == pieceName) {

				curPiece.curPosition = pos;
				curPiece.curX = x;
				curPiece.curY = y;
				return curPiece;
			}
		}
		return null;
	}
	
	private static void assertTrue(String test, boolean result) {
		if (result) {
			System.out.println("Testing " + test + ": PASSED");
		} else {
			System.out.println("Testing " + test + ": FAILED");
		}
	}
}
