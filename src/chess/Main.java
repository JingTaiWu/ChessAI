package chess;

import org.json.simple.parser.ParseException;

public class Main {
	
	public static void main(String[] args) throws ParseException {
		Connector p1Conn = new Connector ("843", "208", "9b70ebdc", Color.White);	
		p1Conn.play();
	}

}
