import java.awt.Color;


public class Piece {

	PieceMap[] positions;
	int curPosition;
	int curX;
	int curY;
	Color colour;
	boolean inPlay;
	
	public Piece(PieceMap[] positions, Color colour) {
		
		this.positions = positions;
		this.curPosition = 0;
		this.curX = 0;
		this.curY = 0;
		this.colour = colour;
		this.inPlay = false;
	}
	
	public PieceMap getCurMap() {
		return positions[this.curPosition];
	}
	
	public int getTotalPositions() {
		return positions.length;
	}
	
	
}
