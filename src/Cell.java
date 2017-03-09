
public class Cell {
	private boolean isBomb;
	private int value;
	private int row;
	private int col;
	private boolean isExposed = false;
	private boolean isGuess = false;
	
	public Cell(int r, int c){
		isBomb = false;
		value = 0;
		row = r;
		col = c;	
	}
	
	public void setVal(int n){
		value = n;
	}
	
	public int getVal(){
		return value;
	}
	
	public void incrementValue(){
		value++;
	}
	
	public boolean isBlank(){
		return value == 0;
	}
	
	public void setBomb(boolean b){
		isBomb = b;
		value = -1;
	}
	
	public boolean isBomb(){
		return isBomb;
	}
	
	public void setRowAndColumn(int r, int c){
		row = r;
		col = c;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public boolean isExposed(){
		return isExposed;
	}
	
	public boolean isGuess(){
		return isGuess;
	}
	
	public boolean flip(){
		isExposed = true;
		return !isBomb;
	}
	
	public boolean toggleGuess(){
		if(!isExposed){
			isGuess = !isGuess;
		}
		return isGuess;
	}
	
	public String getUnderState(){
		if(isBomb){
			return "* ";
		}else if(value > 0){
			return Integer.toString(value) + " ";
		}else{
			return "  ";
		}
	}
	
	public String getTopState(){
		if(isExposed){
			return getUnderState();
		}else if(isGuess){
			return "B ";
		}else{
			return "? ";
		}
	}
	
	@Override
	public String toString(){
		return getUnderState();
	}
}
