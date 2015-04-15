public class Piece{
	private boolean isFire;
	private Board board;
	private int x;
	private int y;
	private String type;
	private boolean isKing = false;
	private boolean hasCaptured;
	private Captured[][] captured_pieces = new Captured[8][8];
	private Captured_graveyard graveyard = new Captured_graveyard();

	public Piece(boolean isFire, Board b, int x, int y, String type){
		this.isFire = isFire;
		board = b;
		this.x = x;
		this.y = y;
		this.type = type;

	}
	public boolean isFire(){
		return isFire;
	}
	public int side(){
		if (this.isFire == true)
			return 0;
		else 
			return 1;
	}

	public boolean isKing(){
		return this.isKing;
	}
	public boolean isBomb(){
		if (this.type == "bomb"){
			return true;
		}
		return false;
	}
	public boolean isShield(){
		if (this.type == "shield"){
			return true;
		}
		return false;
	}
	public boolean hasCaptured(){
			return hasCaptured;
		}

	public void doneCapturing(){
			hasCaptured = false;
		}

	public void move(int x, int y){ //assumes validmove method works
	
			
		capture(x, y);

		this.x = x;
		this.y = y;
		if (this.side() == 0 && this.y == 7){
			this.isKing = true;
			}
		if (this.side() == 1 && this.y == 0){
			this.isKing = true;
			}
		explodethebomb();
		// if (isBomb() && hasCaptured()){
		// 	board.remove(this.x, this.y);
		// 	}
		// if (hasCaptured() == true) {

		// 	captured_pieces[xi + i][yi + j] = new Captured(xi + i, yi + j); //captures a piece
		// }


	}

	private void capture(int x, int y){
		if (Math.abs(this.x - x) == 2){
			int mid_x = (this.x + x)/2;
			int mid_y = (this.y + y)/2;
			board.remove(mid_x, mid_y);
			hasCaptured = true;
			}
		}

	// private void validmove(int xi, int yi, int xf, int yf){
	// 	if (board.pieceAt(xi, yi) == null){
	// 		return false;
	// 	}

	// 	if (board.pieceAt(xi, yi).isKing() == true){

	// 		if (xi != xf && yi != yf && xf <= xi + 1 && xf >= xi - 1 && yf <= yi + 1 && yf >= yi - 1  && board.pieceAt(xi,yi).hasCaptured() == false){
	// 			if (board.pieceAt(xf, yf) == null){
	// 				return true;
	// 				}	
				
	// 			}
	// 		for (int i = -1; i <= 1; i += 2){
	// 			for (int j = -1; j <= 1; j += 2){
	// 				if (board.pieceAt(xi + i, yi + j) != null){
	// 					if (xi + 2 * i == xf && yi + 2 * j == yf){
	// 						board.remove(xi + i, yi + j);
	// 						// captured_pieces[xi + i][yi + j] = new Captured(xi + i, yi + j); //captures a piece
	// 						return true;
	// 					}
						

	// 				}
	// 			}
	// 		}
	// 		}
	// 	if (board.pieceAt(xi, yi).side() == 0){

	// 		if (xi != xf && yi != yf && xf <= xi + 1 && xf >= xi - 1 && yf <= yi + 1 && yf > yi && board.pieceAt(xi,yi).hasCaptured() == false){
	// 			if (board.pieceAt(xf, yf) == null){
	// 				return true;
	// 				}
				
	// 			}
	// 		for (int i = -1; i <= 1; i += 2){
	// 			int j = 1;
	// 			if (board.pieceAt(xi + i, yi + 1) != null){
	// 				if (xi + 2 * i == xf && yi + 2 == yf){
	// 					board.remove(xi + i, yi + j);
	// 					// captured_pieces[xi + i][yi + j] = new Captured(xi + i, yi + j); //captures a piece
	// 					return true;
	// 					}
					

	// 				}
					
	// 			}				
	// 		}
	// 	if (board.pieceAt(xi, yi).side() == 1){

	// 		if (xi != xf && yi != yf && xf <= xi + 1 && xf >= xi - 1 && yf < yi && yf >= yi - 1  && board.pieceAt(xi,yi).hasCaptured() == false){
	// 			if (board.pieceAt(xf, yf) == null){
	// 				return true;
	// 				}
				
	// 			}
	// 		for (int i = -1; i <= 1; i += 2){
	// 			int j = -1;
	// 			if (board.pieceAt(xi + i, yi - 1) != null){
	// 				if (xi + 2 * i == xf && yi - 2 == yf){
	// 					board.remove(xi + i, yi + j);
	// 					// captured_pieces[xi + i][yi + j] = new Captured(xi + i, yi + j); //captures a piece
	// 					return true;
	// 					}
					

	// 				}
					
	// 			}
	// 		}
	// 	return false;
	// 	}

		private void explodethebomb(){

			if (isBomb() == true && hasCaptured() == true){ //explodes the bomb
				int a = 1;
				int b = 1;
				int c = 1;
				int d = 1;
				if (this.x == 0){
					a = 0;
				}
				if (this.x == 7){
					b = 0;
				}
				if (this.y == 0){
					c = 0;
				}
				if (this.y == 7){
					d = 0;
				}
				for (int i = -a; i <= b; i += 1){
					for (int j = -c; j <= d; j += 1){


						if (board.pieceAt(this.x + i, this.y + j) != null && board.pieceAt(this.x + i, this.y + j).isShield() == false){
							board.remove(this.x + i, this.y + j);
							}
						}
					}
				}
			}

private void makeking(){
	if (board.pieceAt(this.x, this.y).isKing() == true){ //turns piece to king
				if (board.pieceAt(this.x, this.y).side() == 0){
					if (board.pieceAt(this.x, this.y).isBomb() ){
						type = "img/bomb-fire-crowned.png";
						}
					if (board.pieceAt(this.x, this.y).isShield()){
						type = "img/shield-fire-crowned.png";
						}
					else {
						type = "img/pawn-fire-crowned";
						}
					}
				if (board.pieceAt(this.x, this.y).side() == 1){
					if (board.pieceAt(this.x, this.y).isBomb() ){
						type = "img/bomb-water-crowned.png";
						}
					if (board.pieceAt(this.x, this.y).isShield()){
						type = "img/shield-water-crowned.png";
						}
					else {
						type = "img/pawn-water-crowned";
						}
					}
				}
}






}