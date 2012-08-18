public class PieceMap {
		
	Boolean[][] pieceMap;
	
	public PieceMap(String stringMap) {

		int width = stringMap.indexOf(":");
		String[] rows = stringMap.split(":");
		
		pieceMap = new Boolean[width][width];
		for (int i=0; i<width; i++) {
			for (int j=0; j<width; j++) {
				boolean value = rows[i].charAt(j) == 'x';
				pieceMap[i][j] = value;
			}
		}
	}
	
	public int getSize() {
		return pieceMap.length;
	}
	public boolean isHit(int x, int y) {
		
		boolean result = false;
		try {
			if (pieceMap[x][y]) {
				result = true;
			}

		} catch (ArrayIndexOutOfBoundsException ex) {
			result = false;
		}
		return result;
	}
}