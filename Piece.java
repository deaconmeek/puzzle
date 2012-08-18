import java.awt.Color;


public class Piece {

	PieceMap[] positions;
	int curPosition;
	int curX;
	int curY;
	int pieceNumber;
	boolean inPlay;
	
	public Piece(PieceMap[] positions, int pieceNumber) {
		
		this.positions = positions;
		this.curPosition = 0;
		this.curX = 0;
		this.curY = 0;
		this.pieceNumber = pieceNumber;
		this.inPlay = false;
	}
	
	public PieceMap getCurMap() {
		return positions[this.curPosition];
	}
	
	public int getTotalPositions() {
		return positions.length;
	}
	
	
}
