import java.io.*;
import java.util.*;

class MazePathSearch{
	public Square[][] Maze;
	public List<Square> openList;
	public List<Square> closedList;
	public List<Square> parentList;
	Square goal;
	Square start;

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
				 else if (Maze[i][j].state == 'P')
				 	start = Maze[i][j];
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

	public List<Square> findAdjacent (Square square){
		List<Square> adjacent = new ArrayList<Square>();
		for (int i = square.yPos - 1; i <= square.yPos + 1; i++){
			for (int j = square.xPos - 1; j <= square.xPos + 1; j++){
				if (!(i == square.yPos && j == square.xPos)){
					if (i >= 0 && i < Maze.length && j >= 0 && j < Maze[0].length){
						if (Maze[i][j].state == ' '){
							Maze[i][j].parent = square;
							//openList.add(Maze[i][j]);
							adjacent.add(Maze[i][j]);
						}
					}
				}
			}
		}
		return adjacent;
	}

	//for up, down, left right movements h
	public int ManhattanDistance(Square square){
		return Math.abs(square.xPos - goal.xPos) + Math.abs(square.yPos - goal.yPos);
	} 

	//for diagonal h
	public int StraightLineDistance(Square square){
		return Math.max(Math.abs(square.xPos - goal.xPos),Math.abs(square.yPos - goal.yPos));
	}

	public Square findMinf(){
		Square min = openList.get(0);
		for(Square square: openList){
			if (min.f > square.f)
				min = square;
		}
		return min;
	}

	public Square findMinG(){
		Square min = openList.get(0);
		for(Square square: openList){
			if (min.g > square.g)
				min = square;
		}
		return min;
	}

	public void search(){
		openList.add(start);
		while (openList.size() > 0){
			Square q = findMinf();
			openList.remove(q);
			List<Square> adjacent = findAdjacent(q);
			for (int i=0; i<adjacent.size(); i++){
				Square successor = adjacent.get(i);
				if (!openList.contains(successor)){
					successor.g = q.g + 1;
					if (!diagonal(successor)) //if successor is diagonal; code
						successor.h = ManhattanDistance(successor);
					else
						successor.h = StraightLineDistance(successor);
					successor.f = successor.g + successor.h;
					openList.add(new Square(successor));
				}
				else{
					
				}
			}
		}

	}

	public boolean diagonal(Square square){
		//if ()
		return true;
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

	public Square(Square square){
		xPos = square.xPos;
		yPos = square.yPos;
		g = square.g;
		h = square.h;
		f = square.f;
	}
}