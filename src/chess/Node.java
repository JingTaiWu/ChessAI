package chess;

public class Node {
	public String move;
	public ChessBoard state;
	public int value;
	
	public Node(ChessBoard state, String move) {
		this.move = move;
		this.state = state;
	}
	
	public Node(ChessBoard state) {
		this.state = state;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
