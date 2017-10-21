import java.io.*;
import java.util.*;

class MazePathSearch{
	public Square[][] Maze;
	public List<Square> openList;
	public List<Square> closedList;
	public List<Square> parentList;
	Square goal;
	Square start;
	public static final int DCOST = 1;
	public static final int SCOST = 1;

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
				//System.out.println(i + " --> " + j + " --> " + Maze.length + "; " + Maze[0].length);
				if (!(i == square.yPos && j == square.xPos)){
					if (i >= 0 && i < Maze.length && j >= 0 && j < Maze[0].length){
						if ((Maze[i][j].state == ' ' || Maze[i][j].state == '.') && exists2(Maze[i][j]) == null){
							Maze[i][j].parent = square;
							//openList.add(Maze[i][j]);
							//System.out.println("------------> " + Maze[i][j].parent.xPos + ", " + Maze[i][j].parent.yPos);
							Maze[i][j].g = computeG(Maze[i][j]);
							adjacent.add(new Square(Maze[i][j]));
						}
					}
				}
			}
		}
		return adjacent;
	}

	//for up, down, left right movements h
	public int ManhattanDistance(Square square){
		return (Math.abs(square.xPos - goal.xPos) + Math.abs(square.yPos - goal.yPos)) * SCOST;
	} 

	//for diagonal h
	public int StraightLineDistance(Square square){
		return Math.max(Math.abs(square.xPos - goal.xPos),Math.abs(square.yPos - goal.yPos));
	}

	public Square findMinf(){
		Square min = openList.get(0);
		for(Square square: openList){

			System.out.println("x: "+square.xPos +  "y: "+square.yPos + " >>> " + square.f + " ------->> " + min.xPos + ", " + min.yPos + " >>> " + min.f);
			if (min.f > square.f){
				min = square;
			}

			else if (min.f == square.f){
				if (min.yPos > square.yPos){
					min = square;
				}
				else if(min.yPos == square.yPos){
					if (min.xPos > square.xPos)
						min = square;
				}
				
			}
		}
		System.out.println("finding nemo");
		System.out.println("x: "+min.xPos +  "y: "+min.yPos);
		return min;
	}

	public void search(){
		System.out.println("GOOOOOOOOOOOOAAAALL: " + goal.xPos + ", " + goal.yPos);
		boolean goal_found = false;
		start.h = ManhattanDistance(start);
		start.f = start.g + start.h;
		openList.add(new Square(start));

		while (openList.size() > 0){
				System.out.println("-------------------------------------------------");
			Square q = findMinf();
			openList.remove(q);
			List<Square> adjacent = findAdjacent(q);
			System.out.println("Current Square: " + q.xPos + ", " + q.yPos + "; g: " + q.g + "; h: " + q.h + "; f: " + q.f);
			closedList.add(new Square(q));
			if (q.samePosition(goal)){
					System.out.println("-------------------------------------------------->>>>>>>>>>>> ");
					goal_found = true;
					break;
				}
			for (int i=0; i<adjacent.size(); i++){
				Square successor = adjacent.get(i);
				Square tmp = exists(successor);
				if (tmp == null){
					//successor.g = computeG(successor);
					//if (!diagonal(successor)) //if successor is diagonal; code
						successor.h = ManhattanDistance(successor);
					//else
					//	successor.h = StraightLineDistance(successor);
					successor.f = successor.g + successor.h;
					openList.add(new Square(successor));
					parentList.add(new Square(successor));
				}
				else{
					//successor.g = computeG(successor);
					successor.h = ManhattanDistance(successor);
					successor.f = successor.g + successor.h;
					//System.out.println("======================== " + tmp.g + ">" + successor.g + " " + tmp.xPos + ", " + tmp.yPos);
					if (tmp.g > successor.g){
					//	System.out.println("======================== " + tmp.g + ">" + successor.g + " " + tmp.xPos + ", " + tmp.yPos);
						//Square tmp2 = exists2(tmp);
						//parentList.remove(tmp2);
						//openList.remove(tmp);
					//	successor.g = computeG(successor);
					//if (!diagonal(successor)) //if successor is diagonal; code
					//	successor.h = ManhattanDistance(successor);
					//else
					//	successor.h = StraightLineDistance(successor);
					// successor.f = successor.g + successor.h;
						//openList.add(new Square(successor));
						//System.out.println("last nga g add: " + openList.get(openList.size()-1).f);
						//parentList.add(new Square(successor));
					}
					openList.remove(tmp);
					openList.add(new Square(successor));
						System.out.println("last nga g add: " + openList.get(openList.size()-1).f);
				}
				//System.out.println("----->>> " + "x: " + q.xPos + ", y: " + q.yPos + " ---->>> " + openList.size());
				//System.out.println(goal.xPos + ", " + goal.yPos);
				//System.out.println(q.xPos + "," + q.yPos);
				System.out.println("Adjacent Square: " + successor.xPos + ", " + successor.yPos+ "; g: " + successor.g + "; h: " + successor.h + "; f: " + successor.f);
			}
			/*
			System.out.println("__________________________________________________________");
				for (Square sq: openList){
					System.out.println("Adjacent Square: " + sq.xPos + ", " + sq.yPos+ "; g: " + sq.g + "; h: " + sq.h + "; f: " + sq.f);
				}
				*/
			//if (goal_found)
			//	break;
		}
		
		for (Square sq: closedList){
			if (sq.parent!=null)
				System.out.println("Square: " + sq.xPos + ", " + sq.yPos + "	Parent: " + sq.parent.xPos + ", " + sq.parent.yPos);
			else
				System.out.println("Square: " + sq.xPos + ", " + sq.yPos);
		}
		
		System.out.println("----------------------------------------------");
		
		for (Square sq: parentList){
			if (sq.parent!=null)
				System.out.println("Square: " + sq.xPos + ", " + sq.yPos + "	Parent: " + sq.parent.xPos + ", " + sq.parent.yPos);
			else
				System.out.println("Square: " + sq.xPos + ", " + sq.yPos);
		}
		
		
	}

	public int computeG(Square square){
		if(diagonal(square))
			return square.parent.g + DCOST;
		else
			return square.parent.g + SCOST;
	}

	public Square exists(Square square){
		for (Square sq : openList){
			if (sq.samePosition(square))
				return sq;
		}
		return null;
	}

	public Square exists2(Square square){
		for (Square sq : closedList){
			if (sq.samePosition(square))
				return sq;
		}
		return null;
	}

	public boolean diagonal(Square square){
		if ((square.parent.yPos == square.yPos - 1 || square.parent.yPos == square.yPos + 1) && square.xPos != square.parent.xPos){
			//System.out.println("=============================>>>>>>>>>> diagonal: " + square.xPos + ", " + square.yPos);
			return true;
		}
		return false;
	}



	public static void main(String[] args) {
		MazePathSearch search = new MazePathSearch();
		search.Load_Maze();
		search.Display_Maze();
		search.search();
		/*
		for (Square sq : search.parentList){
			search.Maze[sq.parent.yPos][sq.parent.xPos].state = '.';
		}*/
		List<Square> path = new ArrayList<Square>();
		Square tmp;
		tmp = search.parentList.get(search.parentList.size() - 1);
		for (int i= search.parentList.size() - 1; i >= 0; i--){
			if (tmp.parent!=null){
			path.add(tmp.parent);
			tmp = tmp.parent;
		}
		}
		for (Square sq: path){
			search.Maze[sq.yPos][sq.xPos].state = '.';
		}
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
		state = square.state;
		if (square.parent != null)
			parent = new Square(square.parent);
		else{
			parent = null;
		} 
	}

	public boolean samePosition(Square square){
		return square.xPos == this.xPos && square.yPos == this.yPos;
	}
}