package chess;

import java.util.ArrayList;

/**
 * The Parent class of all chess pieces.
 * @author Jing Tai Wu
 *
 */
public abstract class ChessPiece {
	public Color color;
	public String name;
	public String location;
	
	public ChessPiece(String location, Color color) {
		this.location = location;
		this.color = color;
	}
	
	public void setLocation(String newLocation) {
		this.location = newLocation;
	}
	
	public abstract ArrayList<Move> possibleMoves(ChessBoard board);
}
