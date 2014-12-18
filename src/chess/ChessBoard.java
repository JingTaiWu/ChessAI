package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the chess board and 
 * @author Jing Tai Wu
 *
 */
public class ChessBoard {
	public final String[] files = {"a","b", "c", "d", "e", "f", "g", "h"};
	public final int ranks = 8;
	private HashMap<String, ChessPiece> board;
	public boolean isGoalState = false;
	
	public void initialize() {
		this.board = new HashMap<String, ChessPiece>();
		// Initialize the whole board
		for (int i = 0; i < this.files.length; i++) {
			for (int j = 1; j <= ranks; j++) {
				String location = "" + this.files[i] + j;
				this.board.put(location, null);
			}
		}
		
		// Add pawns
		for (int j = 0; j < files.length; j++) {
			ChessPiece whitePawn = new Pawn(this.files[j] + "2", Color.White);
			ChessPiece blackPawn = new Pawn(this.files[j] + "7", Color.Black);
			this.board.put(this.files[j] + "2", whitePawn);
			this.board.put(this.files[j] + "7", blackPawn);
		}
		
		// 2 knights, 2 rooks, 2 bishops
		this.board.put(this.files[1] + "1", new Knight(this.files[1] + "1", Color.White));
		this.board.put(this.files[1] + "8", new Knight(this.files[1] + "8", Color.Black));
		this.board.put(this.files[6] + "1", new Knight(this.files[6] + "1", Color.White));
		this.board.put(this.files[6] + "8", new Knight(this.files[6] + "8", Color.Black));
		
		this.board.put(this.files[2] + "1", new Bishop(this.files[2] + "1", Color.White));
		this.board.put(this.files[2] + "8", new Bishop(this.files[2] + "8", Color.Black));
		this.board.put(this.files[5] + "1", new Bishop(this.files[5] + "1", Color.White));
		this.board.put(this.files[5] + "8", new Bishop(this.files[5] + "8", Color.Black));
		
		this.board.put(this.files[0] + "1", new Rook(this.files[0] + "1", Color.White));
		this.board.put(this.files[0] + "8", new Rook(this.files[0] + "8", Color.Black));
		this.board.put(this.files[7] + "1", new Rook(this.files[7] + "1", Color.White));
		this.board.put(this.files[7] + "8", new Rook(this.files[7] + "8", Color.Black));
		
		this.board.put(this.files[4] + "1", new King(this.files[4] + "1", Color.White));
		this.board.put(this.files[4] + "8", new King(this.files[4] + "8", Color.Black));
		this.board.put(this.files[3] + "1", new Queen(this.files[3] + "1", Color.White));
		this.board.put(this.files[3] + "8", new Queen(this.files[3] + "8", Color.Black));
		
	}
	
	public boolean isOnBoard(String location) {
		if(this.board.containsKey(location)) {
			return true;
		} else {
			return false;
		}
	}
	
	public ChessPiece get(String location) {
		return this.board.get(location);
	}
	
	public void set(ChessPiece piece, String location) {
		if(this.isOnBoard(location)) {
			this.board.put(location, piece);
			piece.setLocation(location);
		}
	}
	
	public void update(String move) {
		if(move == null || move.equals("")) return;
		String oldPosition = move.substring(1, 3);
		String newPosition = move.substring(3, 5);
		ChessPiece piece = this.get(oldPosition);
		
		if(piece != null && move.subSequence(0, 1) == "P") {
			if(move.length() >= 6) {
				this.board.put(newPosition, new Queen(newPosition, piece.color));
				this.board.put(oldPosition, null);
			}
		}
		else if(piece != null && this.isOnBoard(newPosition)) {
			this.board.put(newPosition, piece);
			this.board.put(oldPosition, null);
		}
		
		for(Map.Entry<String, ChessPiece> e : this.board.entrySet()) {
			if(e.getValue() != null) {
				e.getValue().setLocation(e.getKey());
			}
			
			if(e.getValue() instanceof King) {
				this.isGoalState = false;
			} else {
				this.isGoalState = true;
			}
		}
	}
	
	public ArrayList<ChessPiece> getPieces(Color color) {
		ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();
		for(Map.Entry<String, ChessPiece> e : this.board.entrySet()) {
			if(e.getValue() != null) {
				if(e.getValue().color == color) {
					pieces.add(e.getValue());
				}
			}
		}
		
		return pieces;
	}
	
	public ArrayList<Move> getAllPossibleMoves(Color color) {
		ArrayList<Move> totalmoves = new ArrayList<Move>();
		ArrayList<ChessPiece> pieces = this.getPieces(color);
		for(ChessPiece piece : pieces) {
			ArrayList<Move> moves = piece.possibleMoves(this);
			if(moves.size() > 0) {
				for(Move move : moves) {
					totalmoves.add(move);
				}
			}
		}
		
		return totalmoves;
	}
	
	public HashMap<String, ChessPiece> getBoard() {
		return this.board;
	}
	
	public void setBoard(HashMap<String, ChessPiece> newBoard) {
		this.board = newBoard;
	}
	
	/**
	 * Pretty print the board
	 */
	public void print() {
		System.out.print("   ");
		for(int z = 0; z < this.files.length; z++) {
			System.out.print(" " + this.files[z]);
		}
		System.out.println();
		for(int i = this.ranks; i > 0; i--) {
			System.out.print(i + " |");
			for(int j = 0; j < this.files.length; j++) {
				String location = "" + this.files[j] + i;
				ChessPiece piece = this.get(location);
				if(piece == null) {
					System.out.print("  ");
				} else {
					System.out.print(" " + piece.toString().substring(0, 1));
				}
			}
			System.out.print("| " + i);
			System.out.println();
		}
		System.out.print("   ");
		for(int z = 0; z < this.files.length; z++) {
			System.out.print(" " + this.files[z]);
		}
		System.out.println();
	}
}
