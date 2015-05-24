//Class to start program
public class Main {
    public static void main(String[] args) {
        MazeWorld maze = new MazeWorld(0, 0); 
        maze.bigBang(MazeWorld.WORLD_WIDTH * MazeWorld.NODE_SIZE, 
            MazeWorld.WORLD_HEIGHT * MazeWorld.NODE_SIZE, 0.001);
    }
}
