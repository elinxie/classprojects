
import static org.junit.Assert.*;

import org.junit.Test;


public class TestFile{



	//testfile drawboard
	private static void drawBoard(int N, Board board) {
		StdDrawPlus.setXscale(0,8);
		StdDrawPlus.setYscale(0,8);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ((i + j) % 2 == 0) StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
                else                  StdDrawPlus.setPenColor(StdDrawPlus.RED);
                StdDrawPlus.filledSquare(i + .5, j + .5, .5);
                StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
                
                if (board.pieceAt( i, j) != null) {
                    StdDrawPlus.picture(i + .5, j + .5, returnTypePic(board.pieceAt(i, j)), 1, 1);

                }
            }
        }
    }

    //testfile get picture
    private static String returnTypePic(Piece p){
    	if (p.isFire()){
	    	if (p.isKing()){
	    		if (p.isBomb())
	    		return "img/bomb-fire-crowned.png";
	    	if (p.isShield())
	    		return "img/shield-fire-crowned.png";
	    	else 
	    		return "img/pawn-fire-crowned.png";
	    	}
	    		
	    	if (p.isBomb())
	    		return "img/bomb-fire.png";
	    	if (p.isShield())
	    		return "img/shield-fire.png";
	    	else 
	    		return "img/pawn-fire.png";
	    }
	    else {
	    	if (p.isKing()){
	    		if (p.isBomb())
	    		return "img/bomb-water-crowned.png";
	    	if (p.isShield())
	    		return "img/shield-water-crowned.png";
	    	else 
	    		return "img/pawn-water-crowned.png";
	    	}
	    	if (p.isBomb())
	    		return "img/bomb-water.png";
	    	if (p.isShield())
	    		return "img/shield-water.png";
	    	else 
	    		return "img/pawn-water.png";
	    }

    }



	// @Test
	public void testBoardStuff(){
		Board b = new Board(false);
		drawBoard(8, b);
		Piece p3 = new Piece(true, b, 2, 2, "bomb");
		Piece p4 = new Piece(false, b, 2, 6, "shield");
		Piece p5 = new Piece(true, b, 3, 1, "pawn");
		Piece p6 = new Piece(true, b, 4, 6, "shield");
		Piece p7 = new Piece(false, b, 3, 4, "bomb");
		Piece p8 = new Piece(true, b, 5, 2, "pawn");
		assertEquals(p3.isFire(), b.pieceAt(2,2).isFire());
		assertEquals(p3.isBomb(), b.pieceAt(2,2).isBomb());
		assertEquals(p4.isFire(), b.pieceAt(2,6).isFire());
		assertEquals(p4.isShield(), b.pieceAt(2,6).isShield());
		assertNotEquals(p5.isShield(), b.pieceAt(3,1).isShield());
		assertNotEquals(null, b.pieceAt(4,6));
		assertNotEquals(p6.isFire(), b.pieceAt(4,6).isFire());
		assertEquals(null, b.pieceAt(3,4));
		assertNotEquals(p8, b.pieceAt(0,0));

		//testing the place method
		// b.pieces[2][2] = new Piece(true, b, 2, 2, "pawn");
		b.place(new Piece(true, b, 2, 2, "pawn"), 2, 2);
		b.place(b.pieceAt(2,2), 3, 3);
		assertNotEquals(null, b.pieceAt(3,3));
		b.place(b.pieceAt(3,5), 2, 4);
		b.place(b.pieceAt(3,3), 2, 4);
		assertNotEquals(null, b.pieceAt(2,4));
		assertEquals(true, b.pieceAt(2,4).isFire());
		assertEquals(false, b.pieceAt(2,4).isBomb());
		assertEquals(0, b.pieceAt(2,4).side());

		//testing the canSelect method
		assertEquals(true, b.canSelect(2, 4)); //selects initial piece
		// assertEquals(true, b.selected);
		// assertEquals(2, b.selected_piece_x);
		// assertEquals(4, b.selected_piece_y);
		b.select(2,4);
		assertEquals(false, b.canSelect(9, 3));
		assertEquals(false, b.canSelect(3, 11));
		assertEquals(false, b.canSelect(3, 3)); 

		// b.pieces[0][6] = null;
		b.remove(0,6);
		assertEquals(false, b.canSelect(1, 5)); //piece moving but enemy blocking
		assertEquals(null, b.pieceAt(0,6));
		assertEquals(true, b.canSelect(0,6)); //checks if it  can capture enemy piece
		b.select(0,6);

		assertEquals(true, b.pieceAt(2, 4).hasCaptured());

		// assertEquals(true, b.canSelect(0, 6));
		// assertEquals(false, )


	}

	// @Test 
	public void endingduhturn(){
		Board b = new Board(false);
		drawBoard( 8, b);
		assertEquals(false, b.canEndTurn());
		b.select(4,2);
		assertEquals(false, b.canEndTurn());
		b.select(3,3);
		assertEquals(true, b.canEndTurn());
		b.endTurn();						//fire is done
		assertEquals(false, b.canEndTurn());
		b.select(1,5);
		assertEquals(false, b.canEndTurn());
		b.select(2,4);
		assertEquals(true, b.canEndTurn());
		b.endTurn();						//water is done
		b.select(3,3);
		b.select(1,5);						//fire captures water
		assertEquals(true, b.canEndTurn());
		assertEquals(null, b.pieceAt(1,5));
		b.endTurn();

		// assertNotEquals(null, b.pieceAt(1,5));	
		// assertEquals(true, b.pieceAt(1,5).isFire());				

		
	}

	@Test 
	public void emptyBoardTestOne(){
		Board empty = new Board(true);
		assertEquals("No one", empty.winner());
		drawBoard(8,empty);
		Piece p9 = new Piece(true,   empty, 2, 0, "pawn");
		Piece p10 = new Piece(false, empty, 1, 1, "shield");
		Piece p11 = new Piece(false, empty, 1, 3, "pawn");
		Piece p12 = new Piece(false, empty, 1, 3, "shield");
		Piece p13 = new Piece(false, empty, 2, 6, "bomb");
		Piece p14 = new Piece(true,  empty, 5, 2, "pawn");
		empty.place(p9, 2, 0);
		empty.place(p10, 1, 1);
		empty.place(p11, 1, 3);
		empty.place(p12, 1, 5);
		// empty.endTurn();
		assertEquals(true, empty.canSelect( 2, 0));
		empty.select(2,0);
		assertEquals(true, empty.canSelect(0,2));
		empty.select(0,2);
		assertEquals(true, p9.hasCaptured());
		assertEquals(true, empty.canSelect(2, 4));
		empty.select(2,4);
		assertEquals(true, empty.canSelect(0, 6));
		empty.select(0,6);
		empty.select(1,7);
		assertEquals(true, empty.pieceAt(1,7).isKing());
		empty.select(2,6);
		empty.select(3,5);
		empty.place(p13, 2, 6);
		empty.select(4,4);
		assertEquals("No one", empty.winner());
		



	}	



	// @Test
	public void testPieceStuff(){
		Board b = new Board(false);
		Piece p1 = new Piece(false, b, 3, 4, "bomb");
		Piece p2 = new Piece(true, b, 5, 2, "shield");
		drawBoard(8, b);


		assertEquals(1, p1.side());
		assertEquals(0, p2.side());
		assertEquals(false, p1.isKing());
		assertEquals(true, p1.isBomb());
		assertEquals(false, p1.isShield());
		assertEquals(false, p2.isKing());
		assertEquals(false, p2.isBomb());
		assertEquals(true, p2.isShield());


	}


	public static void main(String[] args) {
    jh61b.junit.textui.runClasses(TestFile.class);
    }

}