# Maze
A Java based Maze Creator and Solver

Created as a homework project by Tom Kowalski and Leandro Taveras 

Instructions:

Run Maze.jar
- The inital type of world shown is a manual search world that is unbiased.
	- Use arrow keys to move to the finish.
		- Only works if the Search type is Manual, if not the arrow keys do not affect the program
	- Use R to make a new maze with the same properties 
		- Same type of search
		- Same type of biasedness
	- Use B to change search to a Breadth First Search 
		- biasedness stays the same
		- Maze does not change, only the search type does
	- Use D to change search to a Depth First Search
		- biasedness stays the same
		- Maze does not change, only the search type does
	- Use M to change search to a Manual Search
		- biasedness stays the same
		- Maze does not change, only the search type does
	- Use V to create Maze with the same type of search, but with a vertical bias
		- search type does not change
		- A new maze is created
	- Use H to create Maze with the same type of search, but with a horizontal bias
		- search type does not change
		- A new maze is created
	- Use N to create Maze with the same type of search, but with no bias
		- search type does not change
		- A new maze is created
	- Use S to toggele showing the path from the start to the currently affected node in the maze
		- This works in all 3 types of searchs
- Changing NODE_SIZE in MazeWorld changes the displayed size maze Cells
- Changing WORLD_HEIGHT in MazeWorld changes the number of cells in the maze horizontally
- Changing WORLD_WIDTH in MazeWorld changes the number of cells in the maze vertically

To run source use javalib.jar and run main from main.java in src.
