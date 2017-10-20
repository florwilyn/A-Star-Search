import java.io.*;
import java.util.*;

class MazePathSearch{
	public Square[][] Maze;
	public List<Square> openList;
	public List<Square> closedList;
	public List<Square> parentList;
	Square goal;

	public MazePathSearch(){
		openList = new ArrayList<Square>();
		closedList = new ArrayList<Square>();
		parentList = new ArrayList<Square>();
	}


	public void Load_Maze(){
		FileRead mazefile = new FileRead("smallMaze.lay.txt");
		Maze = new Square[mazefile.content.size()][];
		for (int i=0; i<mazefile.content.size(); i++){
			Maze[i] = new Square[mazefile.content.get(i).length()];
			for (int j=0; j<mazefile.content.get(i).length(); j++){
				 Maze[i][j] = new Square(mazefile.content.get(i).charAt(j), j, i);
				 if (Maze[i][j].state == '.')
				 	goal = Maze[i][j];
			}
		}
	}

	public void Display_Maze(){
		for (int i=0; i<Maze.length; i++) {
			for (int j=0; j<Maze[i].length; j++){
				System.out.print(Maze[i][j].state);
			}
			System.out.print("\n");
		}
	}

	public void findAdjacent (Square square){
		for (int i = square.yPos - 1; i <= square.yPos + 1; i++){
			for (int j = square.xPos - 1; j <= square.xPos + 1; j++){
				if (!(i == square.yPos && j == square.xPos)){
					if (i >= 0 && i < Maze.length && j >= 0 && j < Maze[0].length){
						if (Maze[i][j].state == ' '){
							Maze[i][j].parent = square;
							openList.add(Maze[i][j]);
						}
					}
				}
			}
		}
	}

	public int ManhattanDistance(Square square){
		return Math.abs(square.xPos - goal.xPos) + Math.abs(square.yPos - goal.yPos);
	} 

	public int StraightLineDistance(Square square){
		return Math.max(Math.abs(square.xPos - goal.xPos),Math.abs(square.yPos - goal.yPos));
	}



	public static void main(String[] args) {
		MazePathSearch search = new MazePathSearch();
		search.Load_Maze();
		search.Display_Maze();
	}
}

class FileRead {
	public List<String> content;
	public FileRead(String filename){
		content = new ArrayList<String>();
	  	try
	  	{
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
		    String line;
		    while ((line = reader.readLine()) != null)
		    {
		      content.add(line);
		    }
	  	}
	  	catch (Exception e)
	  	{
		    System.err.format("Exception occurred trying to read '%s'.", filename);
		    e.printStackTrace();
	  	}
	}
}

class Square{
	public int xPos;
	public int yPos;
	public int g;
	public int h;
	public int f;
	public char state;
	public boolean start;
	public boolean goal;
	public Square parent;

	public Square(char state, int x, int y){
		this.state = state;
		xPos = x;
		yPos = y;
		g = 0;
		h = 0;
		f = 0;
		if (state == 'P'){
			start = true;
			goal = false;
		}
		else if (state == '.'){
			start = false;
			goal = true;
		}
		else{
			start = false;
			goal = true;
		}
	}
}