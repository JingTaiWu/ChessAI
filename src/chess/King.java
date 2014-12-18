package chess;

import java.util.ArrayList;

public class King extends ChessPiece {

	public King(String location, Color color) {
		super(location, color);
	}

	@Override
	public ArrayList<Move> possibleMoves(ChessBoard board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		int file = (int) this.location.charAt(0);
		int rank = Integer.parseInt("" + this.location.charAt(1));
		int[][] offsets = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
		
		for (int i = 0; i < offsets.length; i++) {
			char newFile = (char) (file + offsets[i][0]);
			int newRank = rank + offsets[i][1];
			String newLocation = "" + newFile + newRank;
			ChessPiece piece = board.get(newLocation);
			
			// If the search hits something, check the color
			if(board.isOnBoard(newLocation)) {
				// If the search hits something, check the color
				if(piece != null) {
					if(piece.color != this.color) {
						moves.add(new Move(this.toString() + newLocation));
					}
				} else {
					moves.add(new Move(this.toString() + newLocation));
				}
			}
		}
		
		return moves;
	}

	public String toString() {
		return "K" + this.location;
	}
}
