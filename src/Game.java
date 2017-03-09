import java.util.Scanner;

public class Game {
	public enum GameState {WON, LOST, PLAYING}
	
	private Board board;
	private int rows;
	private int cols;
	private int bombs;
	private GameState state;
	
	public Game(int r, int c, int b){
		rows = r;
		cols = c;
		bombs = b;
		state = GameState.PLAYING;
	}
	
	public boolean initialize(){
		if(board == null){
			board = new Board(rows, cols, bombs);
			board.printBoard(true);
			return true;
		}else{
			System.out.println("Game has already been initialized.");
			return false;
		}
	}
	
	public boolean start(){
		if(board == null){
			initialize();
		}
		return playGame();
	}
	
	public void printGameState(){
		if(state == GameState.LOST){
			board.printBoard(true);
			System.out.println("You Lost");
		}else if(state == GameState.WON){
			board.printBoard(true);
			System.out.println("You Won");
		}else{
			System.out.println("Number remaining: " + board.getNumRemaining());
			board.printBoard(false);
		}
	}
	
	private boolean playGame(){
		Scanner scanner = new Scanner(System.in);
		printGameState();
		
		while(state == GameState.PLAYING){
			System.out.println("Enter a guess or pick a cell.");
			String input = scanner.nextLine();

			if(input.equals("exit")){
				scanner.close();
				return false;
			}
			UserPlay play = UserPlay.fromString(input);
			if(play == null){
				continue;
			}
			
			UserPlayResult result = board.playFlip(play);
			if(result.successfulMove()){
				state = result.getResultingState();
			}else{
				System.out.println("Could not flip cell (" + play.getRow() +
						" , " + play.getCol() + ").");	
			}
			printGameState();
		}
		scanner.close();
		return true;
	}
	

}
