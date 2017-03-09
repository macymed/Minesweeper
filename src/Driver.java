import java.util.Scanner;

public class Driver {
	private static int rows;
	private static int cols;
	private static int bombs;
	
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to Minesweeper!");
		System.out.println("Please enter 3 numbers between 1 and 20 for how many rows, columns, and bombs you would like.");
		getDimensions(scanner);
		Game game = new Game(rows, cols, bombs);
		game.initialize();
		game.start();
		
	}
	
	private static void getDimensions(Scanner scanner){		
		rows = scanner.nextInt();
		cols = scanner.nextInt();
		bombs = scanner.nextInt();
		while(!numCheck(rows) | !numCheck(cols) | !numCheck(bombs)){
			System.out.println("Make sure all numbers are between 1 and 20 and try again!");
			getDimensions(scanner);
		}
		double ratio = (double) bombs / (rows*cols);
		while (ratio > 0.8){
			System.out.println("You've got too many bombs, pick less bombs and try again!" );
			getDimensions(scanner);
		}
	}
	
	private static boolean numCheck(int n){
		return n > 0 && n <= 20;
	}
}
