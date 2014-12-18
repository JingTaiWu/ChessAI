package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the main AI for the chess game
 * @author Jing Tai Wu
 *
 */
public class ChessPlayer {
	private Color playerColor;
	private Color opponentColor;
	private HashMap<Character, Integer> pieceValue;
	private Move bestMove = null;
	private Node root;
	
	public ChessPlayer(Color playerColor) {
		this.playerColor = playerColor;
		this.opponentColor = (this.playerColor == Color.White) ? Color.Black : Color.White;
		this.pieceValue = new HashMap<Character, Integer>();
		this.pieceValue.put('P', 15);
		this.pieceValue.put('N', 30);
		this.pieceValue.put('B', 40);
		this.pieceValue.put('R', 50);
		this.pieceValue.put('Q', 100);
		this.pieceValue.put('K', 100000);
	}
	
	public int eval(ChessBoard board) {
		int evalNum = 0;
		
		for(ChessPiece player : board.getPieces(playerColor)) {
			if(player != null) {
				evalNum +=  this.pieceValue.get(player.toString().charAt(0));
				for(Move attacks : player.possibleMoves(board)) {
					if(attacks.toString().substring(0, 1).equals("K")) {
						evalNum += 12;
					} else if(attacks.toString().substring(0, 1).equals("Q")) {
						evalNum += 5;
					}
				}
			}
		}
		
		for(ChessPiece opp : board.getPieces(this.opponentColor)) {
			if(opp != null) {
				evalNum -=  this.pieceValue.get(opp.toString().charAt(0));
				for(Move attacks : opp.possibleMoves(board)) {
					if(attacks.toString().substring(0, 1).equals("K")) {
						evalNum -= 12;
					} else if(attacks.toString().substring(0, 1).equals("Q")) {
						evalNum -= 5;
					}
				}
			}
		}
		
		return evalNum;
	}
	
	/**
	 * Takes an action and applies the action to the board
	 * @param action
	 * @param board
	 * @return a new board with a new reference
	 */
	public ChessBoard move(String action, ChessBoard board) {
		// Need to create a deep copy to avoid conflict
		ChessBoard newBoard = new ChessBoard();
		HashMap<String, ChessPiece> copyBoard = new HashMap<String, ChessPiece>();
		for(Map.Entry<String, ChessPiece> e : board.getBoard().entrySet()) {
			if(e.getValue() != null) {
				copyBoard.put(e.getKey(), e.getValue());
			}
			copyBoard.put(e.getKey(), e.getValue());
		}
		
		newBoard.setBoard(copyBoard);
		newBoard.update(action);
		return newBoard;
	}
	
	/**
	 * The core of the AI
	 * @param node
	 * @return a move
	 */
	public String AlphaBetaSearch(Node node) {
		this.root = node;
		int v = this.miniMax(root, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true, null);
		
		System.out.println("Finished searching v is " + v);
		System.out.println("Best move is " + this.bestMove);

		return this.bestMove.toString();
	}
	
	public int miniMax(Node node, int depth, int alpha, int beta, boolean maximizingPlayer, Move move) {
		if(depth == 0) {
			return this.eval(node.state);
		}
		
		if(maximizingPlayer) {
			for(Move newmove : node.state.getAllPossibleMoves(this.playerColor)) {
				ChessBoard newboard = this.move(newmove.toString(), node.state);
				
				// Check to see if there is a king
				ArrayList<ChessPiece> playerPieces = newboard.getPieces(playerColor);
				newboard.isGoalState = true;
				for(ChessPiece piece : playerPieces) {
					if(piece instanceof King) {
						newboard.isGoalState = false;
					}
				}
				
				Node newNode = new Node(newboard);
				
				int v = this.miniMax(newNode, depth - 1, alpha, beta, false, newmove);
				if(v > alpha) {
					alpha = v;
					if(this.root == node) {
						this.bestMove = newmove;
					}
				}
				if(alpha >= beta) {
					return alpha;
				}
			}
			return alpha;
		} else {
			for(Move newmove : node.state.getAllPossibleMoves(this.opponentColor)) {
				ChessBoard newboard = this.move(newmove.toString(), node.state);
				// Check for opponent king
				ArrayList<ChessPiece> playerPieces = newboard.getPieces(this.opponentColor);
				newboard.isGoalState = true;
				for(ChessPiece piece : playerPieces) {
					if(piece instanceof King) {
						newboard.isGoalState = false;
					}
				}
				Node newNode = new Node(newboard);
				int result = this.miniMax(newNode, depth - 1, alpha, beta, true, newmove);
				if(result < beta) {
					beta = result;
					if(this.root == node) {
						this.bestMove = newmove;
					}
				}
				if(beta <= alpha) {
					return beta;
				}
			}
			return beta;
		}
	}
}
