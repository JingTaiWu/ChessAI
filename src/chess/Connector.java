package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class to connect game AI to web server
 * @author Jing Tai Wu and Mike McElligott
 *
 */
public class Connector {
	String gameID = "";	//
	String teamNumber = "";	//208
	String teamSecret = ""; //9b70ebdc
	boolean myTurn = false;
	ChessPlayer player;
	ChessBoard board;

	public Connector(String gameID, String teamNumber, String teamSecret, Color color) {
		this.gameID = gameID;
		this.teamNumber = teamNumber;
		this.teamSecret = teamSecret;
		this.player = new ChessPlayer(color);
		this.board = new ChessBoard();
		this.board.initialize();
		myTurn = false;
	}
	
	/**
	 * Game Loop to talk to sever give a move
	 * @param move
	 * @return
	 * @throws ParseException
	 */
	public void play() throws ParseException {
		String lastmove = "";
		while (true) {
			lastmove = (String) this.poll().get("lastmove");
			if (myTurn) {
				this.board.update(lastmove);
				this.board.print();
				Node node = new Node(this.board);
				String newMove = this.player.AlphaBetaSearch(node);
				System.out.println(newMove);
				this.move(newMove);
				this.board.update(newMove);
				myTurn = false;
			}
			//poll server every 5 seconds
			try {
				Thread.sleep(5000); //milliseconds
			} catch (InterruptedException ex) {
				System.out.println(ex);
			}
		}
	}

	/**
	 * Checks the status of the game.
	 * @return data, a HashMap
	 * @throws ParseException
	 */
	public HashMap<String, Object> poll() throws ParseException {
		HashMap<String, Object> data = new HashMap<String, Object>();
		String url = "http://www.bencarle.com/chess/poll/" +
				gameID + "/" + 
				teamNumber + "/" + 
				teamSecret + "/";
		System.out.println("Polling...");
		System.out.println("Trying to establish a connection...");
		try {
			URL myURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");

			if (connection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ connection.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(connection.getInputStream())));

			String output;
			System.out.print("Output from Server .... \n");
			JSONParser parser=new JSONParser();
			while ((output = br.readLine()) != null) {
				JSONObject obj = (JSONObject) parser.parse(output);
				//System.out.println(obj);
				data.put("ready", (Boolean) obj.get("ready"));
				data.put("secondsleft", (Double) obj.get("secondsleft"));
				data.put("lastmovenumber", (Long) obj.get("lastmovenumber"));
				//if it is now our turn
				if ((String) obj.get("lastmove") != null) {
					data.put("lastmove", (String) obj.get("lastmove"));
				}
				System.out.println(data);
			}
			connection.disconnect();
		} 
		catch (MalformedURLException e) { 
			// new URL() failed
			// ...
			System.out.println(e.toString());
		} 
		catch (IOException e) {   
			// openConnection() failed
			// ...
			System.out.println(e.toString());
		}
		myTurn = (Boolean) data.get("ready");
		return data;
	}//end method

	 /**
	  * Takes in a move and returns a HashMap
	  * @param move
	  * @throws ParseException
	  */
	public HashMap<String, Object> move(String move) throws ParseException {
		HashMap<String, Object> data = new HashMap<String, Object>();
		String url = "http://www.bencarle.com/chess/move/" +
				gameID + "/" + 
				teamNumber + "/" + 
				teamSecret + "/" + 
				move + "/";
		System.out.println("Sending...");
		System.out.println("Trying to establish a connection...");
		try {
			URL myURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");

			if (connection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ connection.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(connection.getInputStream())));

			String output;
			System.out.print("Output from Server .... \n");
			JSONParser parser=new JSONParser();
			while ((output = br.readLine()) != null) {
				JSONObject obj = (JSONObject) parser.parse(output);
				//System.out.println(obj);
				//{"result": true, "message": ""}
				data.put("result", (Boolean) obj.get("result"));
				data.put("message", (String) obj.get("message"));
				System.out.println(data);
			}
			connection.disconnect();
		} 
		catch (MalformedURLException e) { 
			// new URL() failed
			// ...
			System.out.println(e.toString());
		} 
		catch (IOException e) {   
			// openConnection() failed
			// ...
			System.out.println(e.toString());
		}
		return data;
	}//end method

}//end class