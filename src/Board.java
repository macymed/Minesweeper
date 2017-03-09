import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class Board {
	private Cell[][] cells;
	private int rows;
	private int cols;
	private int numBombs;
	private Cell[] bombLocs;
	private int numUnexposedRemaining;
	//offsets of cells around a cell
	private int[][] deltas = { {-1,-1}, {-1, 0}, {-1, 1},
			{0,-1}, {0,1}, {1, -1}, {1, 0}, {1,1}};
	
	public Board(int r, int c, int b){
		rows = r;
		cols = c;
		numBombs = b;
		
		initializeBoard();
		shuffleBoard();
		setNumberedCells();
		
		numUnexposedRemaining = rows * cols - numBombs;
	}
	
	public int getNumRemaining(){
		return numUnexposedRemaining;
	}
	
	private void initializeBoard(){
		//make blank board
		cells = new Cell[rows][cols];
		bombLocs = new Cell[numBombs];
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < cols; c++){
				cells[r][c] = new Cell(r, c);
			}
		}
		
		//set bombs in first cells
		for(int i = 0; i < numBombs; i++){
			int r = i / cols;
			int c = (i - r * cols) % cols;
			bombLocs[i] = cells[r][c];
			bombLocs[i].setBomb(true);
			
		}
	}
	
	//iterate i = 0 through n-1. 
	//for each i pick a random index and swap it with that index. 
	private void shuffleBoard(){
		int numCells = rows * cols;
		Random random = new Random();
		for(int index1 = 0; index1 < numCells; index1++){
			int index2 = index1 + random.nextInt(numCells - index1);
			if(index1 != index2){
				int row1 = index1 / cols;
				int col1 = (index1 - row1 * cols) % cols;
				Cell cell1 = cells[row1][col1];
				
				int row2 = index2 / cols;
				int col2 = (index2 - row2 * cols) % cols;
				Cell cell2 = cells[row2][col2];
				
				cells[row1][col1] = cell2;
				cell2.setRowAndColumn(row1, col1);
				cells[row2][col2] = cell1;
				cell1.setRowAndColumn(row2, col2);					
			}
		}					
	}
	
	private boolean inBounds(int row, int col){
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}
	
	//sets the values of cells around the bombs
	private void setNumberedCells(){
		for(Cell bomb: bombLocs){
			System.out.println(bomb.getRow() + " " + bomb.getCol());
			int row = bomb.getRow();
			int col = bomb.getCol();
			for(int[] delta: deltas){
				int r = row + delta[0];
				int c = col + delta[1];
				if(inBounds(r,c)){
					cells[r][c].incrementValue();
				}
			}
		}
	}
	
	public void printBoard(boolean showUnderside){
		System.out.println();
		System.out.print("   ");
		for(int i = 0; i < cols; i++){
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.print("  ");
		for(int i = 0; i < cols; i++){
			System.out.print("--");
		}
		System.out.println();
		for(int r = 0; r < rows; r++){
			System.out.print(r + "| ");
			for(int c = 0; c < cols; c++){
				if(showUnderside){
					System.out.print(cells[r][c].getUnderState());
				}else{
					System.out.print(cells[r][c].getTopState());
				}
			}
			System.out.println();
		}
	}
	
	private boolean flipCell(Cell cell){
		if(!cell.isExposed() && !cell.isGuess()){
			cell.flip();
			numUnexposedRemaining--;
			return true;
		}
		return false;
	}
	
	public void expandBlank(Cell cell){
		Queue<Cell> toExplore = new LinkedList<Cell>();
		toExplore.add(cell);
		
		while(!toExplore.isEmpty()){
			Cell current = toExplore.remove();
			for(int[] delta : deltas){
				int r = current.getRow() + delta[0];
				int c = current.getCol() + delta[1];
				if(inBounds(r,c)){
					Cell neighbor = cells[r][c];
					if(flipCell(neighbor) && neighbor.isBlank()){
						toExplore.add(neighbor);
					}
				}
			}
		}
	}
	
	public UserPlayResult playFlip(UserPlay play){
		Cell cell = getCellAtLocation(play);
		if(cell == null){
			return new UserPlayResult(false, Game.GameState.PLAYING);
		}
		
		if(play.isGuess()){
			boolean guessResult = cell.toggleGuess();
			return new UserPlayResult(guessResult, Game.GameState.PLAYING);
		}
		boolean result = flipCell(cell);
		if(cell.isBomb()){
			return new UserPlayResult(result, Game.GameState.LOST);
		}
		
		if(cell.isBlank()){
			expandBlank(cell);
		}
		if(numUnexposedRemaining == 0){
			return new UserPlayResult(result, Game.GameState.WON);
		}
		
		return new UserPlayResult(result, Game.GameState.PLAYING);
	}
	
	public Cell getCellAtLocation(UserPlay play){
		int row = play.getRow();
		int col = play.getCol();
		if(!inBounds(row, col)){
			return null;
		}
		return cells[row][col];
	}

	

}
