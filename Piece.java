
public class Piece {

	PieceMap[] positions;
	int curPosition;
	int curX;
	int curY;
	String pieceName;
	int matchCount;
	
	public Piece(PieceMap[] positions, String pieceName) {
		
		this.positions = positions;
		this.curPosition = 0;
		this.curX = 0;
		this.curY = 0;
		this.pieceName = pieceName;
		this.matchCount = 0;
	}
	
	public PieceMap getCurMap() {
		return positions[this.curPosition];
	}
	
	public int getTotalPositions() {
		return positions.length;
	}
	
	public String toString() {
		return "n"+pieceName+"p"+curPosition+"x"+curX+"y"+curY;
	}
	
}
