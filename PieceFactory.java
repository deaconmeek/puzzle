import java.util.LinkedList;
import java.util.List;


public class PieceFactory {

	public static List<Piece> puzzlePieces1() {

		List<Piece> pieces = new LinkedList<Piece>();
		
		//piece 1
		PieceMap[] pieceMaps1 = new PieceMap[4];
		pieceMaps1[0] = new PieceMap(
				"xxx:" +
				"x  :" +
				"x  :"
			);
		pieceMaps1[1] = new PieceMap(
				"xxx:" +
				"  x:" +
				"  x:"
			);
		pieceMaps1[2] = new PieceMap(
				"  x:" +
				"  x:" +
				"xxx:"
			);
		pieceMaps1[3] = new PieceMap(
				"x  :" +
				"x  :" +
				"xxx:"
			);
		
		//piece2
		PieceMap[] pieceMaps2 = new PieceMap[4];
		pieceMaps2[0] = new PieceMap(
				"xxx:" +
				" x :" +
				" x :"
			);
		pieceMaps2[1] = new PieceMap(
				"  x:" +
				"xxx:" +
				"  x:"
			);
		pieceMaps2[2] = new PieceMap(
				" x :" +
				" x :" +
				"xxx:"
			);
		pieceMaps2[3] = new PieceMap(
				"x  :" +
				"xxx:" +
				"x  :"
			);
		
		//piece3
		PieceMap[] pieceMaps3 = new PieceMap[2];
		pieceMaps3[0] = new PieceMap(
				"xx :" +
				"xxx:" +
				" xx:"
			);
		pieceMaps3[1] = new PieceMap(
				" xx:" +
				"xxx:" +
				"xx :"
			);
		

		//piece4
		PieceMap[] pieceMaps4 = new PieceMap[8];
		pieceMaps4[0] = new PieceMap(
				"xxx:" +
				"xx :" +
				"xx :"
			);
		pieceMaps4[1] = new PieceMap(
				"xxx:" +
				"xxx:" +
				"  x:"
			);
		pieceMaps4[2] = new PieceMap(
				" xx:" +
				" xx:" +
				"xxx:"
			);
		pieceMaps4[3] = new PieceMap(
				"x  :" +
				"xxx:" +
				"xxx:"
			);
		pieceMaps4[4] = new PieceMap(
				"xxx:" +
				" xx:" +
				" xx:"
			);
		pieceMaps4[5] = new PieceMap(
				"xxx:" +
				"xxx:" +
				"x  :"
			);
		pieceMaps4[6] = new PieceMap(
				"xx :" +
				"xx :" +
				"xxx:"
			);
		pieceMaps4[7] = new PieceMap(
				"  x:" +
				"xxx:" +
				"xxx:"
			);
		

		//piece5
		PieceMap[] pieceMaps5 = new PieceMap[1];
		pieceMaps5[0] = new PieceMap(
				" x :" +
				"xxx:" +
				" x :"
			);
		


		//piece6
		PieceMap[] pieceMaps6 = new PieceMap[8];
		pieceMaps6[0] = new PieceMap(
				"x  :" +
				"xxx:" +
				"x x:"
			);
		pieceMaps6[1] = new PieceMap(
				"xxx:" +
				" x :" +
				"xx :"
			);
		pieceMaps6[2] = new PieceMap(
				"x x:" +
				"xxx:" +
				"  x:"
			);
		pieceMaps6[3] = new PieceMap(
				"xx :" +
				" x :" +
				"xxx:"
			);
		pieceMaps6[4] = new PieceMap(
				"  x:" +
				"xxx:" +
				"x x:"
			);
		pieceMaps6[5] = new PieceMap(
				"xx :" +
				" x :" +
				"xxx:"
			);
		pieceMaps6[6] = new PieceMap(
				"x x:" +
				"xxx:" +
				"x  :"
			);
		pieceMaps6[7] = new PieceMap(
				"xxx:" +
				" x :" +
				"xx :"
			);
		
		
		//piece7
		PieceMap[] pieceMaps7 = new PieceMap[8];
		pieceMaps7[0] = new PieceMap(
				"xxx:" +
				"xx :" +
				"   :"
			);
		pieceMaps7[1] = new PieceMap(
				"xx :" +
				"xx :" +
				" x :"
			);
		pieceMaps7[2] = new PieceMap(
				" xx:" +
				"xxx:" +
				"   :"
			);
		pieceMaps7[3] = new PieceMap(
				"x  :" +
				"xx :" +
				"xx :"
			);
		pieceMaps7[4] = new PieceMap(
				"xxx:" +
				" xx:" +
				"   :"
			);
		pieceMaps7[5] = new PieceMap(
				" x :" +
				"xx :" +
				"xx :"
			);
		pieceMaps7[6] = new PieceMap(
				"xx :" +
				"xxx:" +
				"   :"
			);
		pieceMaps7[7] = new PieceMap(
				"xx :" +
				"xx :" +
				"x  :"
			);		

		
		//piece8
		PieceMap[] pieceMaps8 = new PieceMap[4];
		pieceMaps8[0] = new PieceMap(
				"x  :" +
				"xx :" +
				" xx:"
			);
		pieceMaps8[1] = new PieceMap(
				" xx:" +
				"xx :" +
				"x  :"
			);
		pieceMaps8[2] = new PieceMap(
				"xx :" +
				" xx:" +
				"  x:"
			);
		pieceMaps8[3] = new PieceMap(
				"  x:" +
				" xx:" +
				"xx :"
			);
		

		
		//piece9
		PieceMap[] pieceMaps9 = new PieceMap[4];
		pieceMaps9[0] = new PieceMap(
				"xxxx:" +
				"x   :" +
				"    :" +
				"    :"
			);
		pieceMaps9[1] = new PieceMap(
				"xx  :" +
				" x  :" +
				" x  :" +
				" x  :"
			);
		pieceMaps9[2] = new PieceMap(
				"x   :" +
				"xxxx:" +
				"    :" +
				"    :"
			);
		pieceMaps9[3] = new PieceMap(
				"xx  :" +
				"x   :" +
				"x   :" +
				"x   :"
			);


		
		//piece10
		PieceMap[] pieceMaps10 = new PieceMap[4];
		pieceMaps10[0] = new PieceMap(
				"xxxx:" +
				"x  x:" +
				"    :" +
				"    :"
			);
		pieceMaps10[1] = new PieceMap(
				"xx  :" +
				" x  :" +
				" x  :" +
				"xx  :"
			);
		pieceMaps10[2] = new PieceMap(
				"x  x:" +
				"xxxx:" +
				"    :" +
				"    :"
			);
		pieceMaps10[3] = new PieceMap(
				"xx  :" +
				"x   :" +
				"x   :" +
				"xx  :"
			);
		


		
		//piece11
		PieceMap[] pieceMaps11 = new PieceMap[8];
		pieceMaps11[0] = new PieceMap(
				"xxx :" +
				"x   :" +
				"xx  :" +
				"xx  :"
			);
		pieceMaps11[1] = new PieceMap(
				"xxxx:" +
				"xx x:" +
				"   x:" +
				"    :"
			);
		pieceMaps11[2] = new PieceMap(
				" xx :" +
				" xx :" +
				"  x :" +
				"xxx :"
			);
		pieceMaps11[3] = new PieceMap(
				"x   :" +
				"x xx:" +
				"xxxx:" +
				"    :"
			);
		pieceMaps11[4] = new PieceMap(
				"xxx :" +
				"  x :" +
				" xx :" +
				" xx :"
			);
		pieceMaps11[5] = new PieceMap(
				"xxxx:" +
				"x xx:" +
				"x   :" +
				"    :"
			);
		pieceMaps11[6] = new PieceMap(
				"xx  :" +
				"xx  :" +
				"x   :" +
				"xxx :"
			);
		pieceMaps11[7] = new PieceMap(
				"   x:" +
				"xx x:" +
				"xxxx:" +
				"    :"
			);
		
		
		pieces.add(new Piece(pieceMaps1,1));
		pieces.add(new Piece(pieceMaps2, 2));	
		pieces.add(new Piece(pieceMaps3, 3));			
		pieces.add(new Piece(pieceMaps4, 4));
		pieces.add(new Piece(pieceMaps5, 5));
		pieces.add(new Piece(pieceMaps6, 6));
		pieces.add(new Piece(pieceMaps7, 7));		
		pieces.add(new Piece(pieceMaps8, 8));
		pieces.add(new Piece(pieceMaps9, 9));
		pieces.add(new Piece(pieceMaps10, 10));
		pieces.add(new Piece(pieceMaps11, 11));
		
		return pieces;
	}
}
