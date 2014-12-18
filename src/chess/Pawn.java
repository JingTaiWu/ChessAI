package chess;

import java.util.ArrayList;

public class Pawn extends ChessPiece {

	public Pawn(String location, Color color) {
		super(location, color);
	}

	@Override
	public ArrayList<Move> possibleMoves(ChessBoard board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		int file = (int) this.location.charAt(0);
		int rank = Integer.parseInt(this.location.charAt(1) + "");
		
		// If the pawn is at its initial square, it can move 2 blocks forward
		if((rank == 2 && board.get(this.location.charAt(0) + "3") == null) || (rank == 7 && board.get(this.location.charAt(0) + "6") == null)) {
			int newRank = (this.color == Color.White) ? rank + 2 : rank - 2;
			String newLocation = "" + (char) file + newRank;
			if(board.isOnBoard(newLocation) && board.get(newLocation) == null) {
				moves.add(new Move(this.toString() + newLocation));
			}
		}
		
		// Other cases, one block forward
		int forwardRank = (this.color == Color.White) ? rank + 1 : rank - 1;
		String forwardLocation = "" + (char) file + forwardRank;
		
		if(board.isOnBoard(forwardLocation) && board.get(forwardLocation) == null) {
			if((this.color == Color.White && rank != 7) || (this.color == Color.Black && rank != 2)) {
				moves.add(new Move(this.toString() + forwardLocation));
			}			
		}
		
		// Pawns can attack pieces diagonally if there is an enemy piece
		int[] diagonalOffsets = {1, -1};
		
		for (int i = 0; i < diagonalOffsets.length; i++) {
			String diagonalFile = "" + (char) (file + diagonalOffsets[i]);
			String diagonalLocation = diagonalFile + forwardRank;
			if(board.isOnBoard(diagonalLocation) && board.get(diagonalLocation) != null) {
				if(board.get(diagonalLocation).color != this.color) {
					if((this.color == Color.White && rank == 7) || (this.color == Color.Black && rank == 2)) {
						moves.add(new Move(this.toString() + diagonalLocation + "Q"));
					} else {
						moves.add(new Move(this.toString() + diagonalLocation));
					}
				}
			}
		}
		
		if(this.color == Color.Black && rank == 2) {
			moves.add(new Move(this.toString() + forwardLocation + "Q"));
		} else if(this.color == Color.White && rank == 7) {
			moves.add(new Move(this.toString() + forwardLocation + "Q"));
		}

		return moves;
	}
	
	public String toString() {
		return "P" + this.location;
	}
}
