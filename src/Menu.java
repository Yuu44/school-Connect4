import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.json.*;


public class Menu {
	
	static Scanner sc = new Scanner(System.in);
	
	static Token[] tokenTab = {new Token('X'), new Token('O'), new Token('Z'), new Token('N')};
	
	// Asked for type + nickname player 
	// according to the number of player (<4) set in configuration.txt
	protected static Player SetPlayer(int i) throws SetPlayerException {
		
		String input, pseudo = "";
		String[] result;
		int y = 0;
		
		System.out.println("Player " + (i+1) + " ?");
		input = sc.nextLine();
		result = input.split("\\s");
		
		// Error : no nickname has been input 
		if (result.length == 1)
			throw new SetPlayerException(i);
		
		// Read through the scanner
		for (int x = 0; x<result.length; x++) {
			if (x == 0) {
				if (result[x].equals("ia"))
					y = 1;
				else if (result[x].equals("humain"))
					y = 2;
				else
					throw new SetPlayerException(i+1);
			}
			else
				pseudo = pseudo.concat(result[x] + " ");
		}
		if (y == 1)
			return new Ai(pseudo, tokenTab[i]);
		else
			return new Human(pseudo, tokenTab[i]);
	}
	
	// Find on http://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
	static String readFile(String path, Charset encoding)  throws IOException {
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
	}
	
	
	public static void main(String[] args) {
		
		System.out.println("*****************\n** Connect  4  **\n*****************\n Welcome to the game !");
		// Default configuration
		int nbPlayer = 0, gridWidth = 0, gridLenght = 0;
		
		
		
		// Read data from configuration.txt
		try {
			String str = readFile("configuration.txt", StandardCharsets.UTF_8);
			JSONObject obj = new JSONObject(str);
			nbPlayer = obj.getInt("nbPlayer");  
			gridWidth = obj.getInt("gridWidth");  
			gridLenght = obj.getInt("gridLength");  
		} catch(JSONException err) {
			System.out.println(err);
		} catch(IOException err) {
			System.out.println(err);
		}
		
		int i = 0;
		Player[] pTable = new Player[nbPlayer];
		
		// Create Player
		while (i < nbPlayer) {
			try {
				pTable[i] = SetPlayer(i);
				System.out.println(pTable[i].GetNickname());
				i++;
			} catch(SetPlayerException err) {
				System.out.println(err);
			}
		}
		// Create Grid
		Grid mygrid = new Grid(gridWidth, gridLenght);
		mygrid.initGrid();
		
		
		// Create and play Game
		Game myGame = new Game(pTable, mygrid);
		int playerwin = myGame.PlayGame();
		

		
	}

}
