// Assignment 10
// Kowalski Tom
// kowalski
// Taveras Leandro
// Taverasl

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
//import tester.*;
import javalib.impworld.*;
import javalib.colors.*;
import javalib.worldimages.*;

//A List of T
interface IList<T> extends Iterable<T> {
    // Returns the size of the list
    int size();
    Iterator<T> iterator();
    // Returns the current item as a Cons, else throws an exceptions
    Cons<T> asCons();
}
//Represents an empty list of T
class Empty<T> implements IList<T> {
    /* Template for Empty<T>
     *Fields: 
     *n/a 
     *
     *Methods:
     *accept(IListVisitor<T, R>) -> R
     *
     *Methods on Fields:
     *n/a
     */
    Empty(){
        //Empty has no fields to initialize 
    }
    // Returns the size of the list
    public int size() {
        return 0;
    }
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }
    // Returns the current item as a Cons, else throws an exceptions
    public Cons<T> asCons() {
        throw new IllegalArgumentException("Not a cons");
    }
}


//Represents a non-empty list of T
class Cons<T> implements IList<T> {
    /* Template for Cons<T>
     * Fields:
     * first = T
     * first = IList<T>
     * 
     * Methods:
     * accept(IListVisitor<T, R>) -> R
     * 
     * Methods on Fields:
     * accept(IListVisitor<T, R>) -> R
     */
    T first;
    IList<T> rest;
    Cons(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }
    // Returns the size of the list
    public int size() {
        return 1 + this.rest.size();
    }
    // Returns an ilist iterator
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }
    // Returns the current item as a Cons, else throws an exceptions
    public Cons<T> asCons() {
        return this;
    }
}
// An iterator to iterate through ILists
class IListIterator<T> implements Iterator<T> {
    IList<T> list;
    IListIterator(IList<T> list) {
        this.list = list;
    }
    // Does the list have a next
    public boolean hasNext() {
        return this.list.size() > 0;
    }
    // Returns the next item in the list
    public T next() {
        T next = this.list.asCons().first;
        this.list = this.list.asCons().rest;
        return next;
    }
    // Removes the first item in the list
    public void remove() {
        throw new IllegalArgumentException("Not supported");        
    }
}
// A class to represent a node in the maze
class Node {
    int x;
    int y;
    IList<Edge> edges;
    boolean front;
    boolean visited;
    Node(int x, int y) {
        this(x, y, new Empty<Edge>());
    }
    Node(int x, int y, IList<Edge> edges) {
        this.x = x; 
        this.y = y;
        this.front = false;
        this.edges = edges;
    }
    // Draws the node
    WorldImage drawNode() {
        int size = MazeWorld.NODE_SIZE;
        int width = MazeWorld.WORLD_WIDTH;
        int height = MazeWorld.WORLD_HEIGHT;
        WorldImage back = new FrameImage(this.getPosn(), size , size, new Black());
        Color color = Color.white;
        if (this.front 
            || this.visited 
            || (this.x == 0 && this.y == 0)
            || (this.x == width - 1 && this.y == height - 1)) {
            if (this.front) {
                color = Color.BLUE;
            }
            else if (this.x == width - 1 && this.y == height - 1) {
                color = Color.MAGENTA;
            }
            else if (this.x == 0 && this.y == 0) {
                color = Color.green;
            }
            else if (this.visited) {
                color = Color.cyan;
            }
            return new OverlayImages(
                this.drawEdges(back),
                new RectangleImage(this.getPosn(), size - 1, size - 1, color));
        }
        return this.drawEdges(back);
    }

    // Returns the posn that the cell is located at
    Posn getPosn() {
        int size = MazeWorld.NODE_SIZE;
        return new Posn((size / 2) + (this.x * size), (size / 2) + (this.y * size));
    }
    // Draws all the edges
    WorldImage drawEdges(WorldImage bg) {
        for (Edge e: this.edges) {
            bg = new OverlayImages(bg, e.drawEdge());
        }
        return bg;
    }
    // If possible, returns the node reached by moving x units horizontally and y units vertically
    // Else returns this node
    Node move(int x, int y) {
        int nextX = this.x + x;
        int nextY = this.y + y;
        for (Edge e: this.edges) {
            if (e.start.equals(this)) {
                if (e.end.x == nextX && e.end.y == nextY) {
                    e.visited = true;
                    return e.end;
                }
            }
            else {
                if (e.start.x == nextX && e.start.y == nextY) {
                    e.visited = true;
                    return e.start;
                }
            }
        }
        return this;
    }
}
// A class to represent the edge between nodes
class Edge implements Comparable<Edge> {
    int weight; 
    Node start;
    Node end;
    boolean front;
    boolean visited;
    Edge(int weight, Node start, Node end) {
        this.front = false;
        this.weight = weight;
        this.start = start;
        this.end = end;
        this.visited = false;
    }
    // Compares this edge to the given edge
    // Returns a negative number if this edge comes before that edge
    // Returns 0 if they're tied
    // Returns 1 if this edge comes after that edge
    public int compareTo(Edge that) {
        return this.weight - that.weight;
    }   
    // Draws the edge
    WorldImage drawEdge() {
        int size = MazeWorld.NODE_SIZE;
        Posn start = this.start.getPosn();
        Posn end = this.end.getPosn();
        Posn mid = new Posn((start.x + end.x) / 2, (start.y + end.y) / 2);
        int vSize = size - 1;
        int hSize = size - 1;
        Color color = Color.white;
        if (start.y  == end.y) {
            hSize = 2;
        }
        else {
            vSize = 2;
        }
        if (this.visited) {
            color = Color.cyan;
        }
        if (this.front) {
            color = Color.BLUE;
        }

        return  new RectangleImage(mid, hSize, vSize, color);
    }
}

// A class to represent the maze world
class MazeWorld extends World {
    //Constants 
    public static int WORLD_HEIGHT = 50;
    public static int WORLD_WIDTH = 50;
    public static int NODE_SIZE =  16;
    Random ran;
    ArrayList<Node> frontN;
    ArrayList<Edge> frontE;
    ArrayList<ArrayList<Node>> nodes;
    ASearch search;
    //-1 depth, 0 manual, 1 breadth
    int searchType; 
    //-1 vertical bias, 0 no bias, 1 horizontal bias
    int bias;
    MazeWorld(int searchType, int bias) {
        this.searchType = searchType;
        this.bias = bias;
        this.ran = new Random();
        initWorld();
    }
    // EFFECT: Initializes the edges, creates the minimum spanning tree
    // Creates the search heads, and initializes the type of search used
    void initWorld() {
        this.mst(this.getEdges());
        this.frontN = new ArrayList<Node>();
        this.frontE = new ArrayList<Edge>();
        this.initSearch();
    }
    // EFFECT: Determines the type of search used based on the searchtype field
    void initSearch() {
        if (this.searchType < 0) {
            this.search = new DepthFirst(this);
        }
        else if (this.searchType > 0) {
            this.search = new BreadthFirst(this);
        }
        else {
            this.search = new ManualSearch(this);
        }
    }
    // EFFECT: Creates the minimum spanning tree from the input list of edges
    void mst(ArrayList<Edge> edges) {
        Collections.sort(edges);
        int index = 0;
        int numEdges = 0;
        UnionFind map = new UnionFind(this.nodes);
        while (numEdges < (MazeWorld.WORLD_HEIGHT * MazeWorld.WORLD_WIDTH) - 1) {
            Edge tempEdge = edges.get(index);
            if (map.union(tempEdge.start, tempEdge.end)) {
                tempEdge.start.edges = new Cons<Edge>(tempEdge, tempEdge.start.edges);
                tempEdge.end.edges = new Cons<Edge>(tempEdge, tempEdge.end.edges);
                numEdges += 1;
            }
            index += 1;
        }
    }
    // Returns the initial complete list of edges, biased according to the bias field
    ArrayList<Edge> getEdges() {
        initNodes();
        int height = MazeWorld.WORLD_HEIGHT; 
        int width = MazeWorld.WORLD_WIDTH;
        ArrayList<Edge> edges = new ArrayList<Edge>();
        int x = 1;
        int y = 1;
        if (this.bias < 0) {
            x = 2;
        }
        if (this.bias > 0) {
            y = 2;
        }

        for (ArrayList<Node> ar: this.nodes) {
            for (Node n: ar) {
                if (n.x < width - 1) {
                    edges.add(new Edge(this.ran.nextInt(1000 * x),
                        n, this.nodes.get(n.x + 1).get(n.y))); 
                }
                if (n.y < height - 1) {
                    edges.add(new Edge(this.ran.nextInt(1000 * y),
                        n, this.nodes.get(n.x).get(n.y + 1))); 
                }
            }

        }
        return edges;
    }
    //EFFECT: Initialzes the ArrayList<ArrayList<Node>> to contain 
    //the correct number of rows/columns
    void initNodes() {
        int height = MazeWorld.WORLD_HEIGHT; 
        int width = MazeWorld.WORLD_WIDTH;
        this.nodes = new ArrayList<ArrayList<Node>>();
        for (int col = 0; col < width; col += 1) {
            this.nodes.add(new ArrayList<Node>());
            for (int row = 0; row < height; row += 1) {
                this.nodes.get(col).add(new Node(col, row));
            }
        }
    }
    // Draws each node and returns the final image
    WorldImage drawNodes() {
        int height = MazeWorld.WORLD_HEIGHT;
        int width = MazeWorld.WORLD_WIDTH;
        int nodeSize = MazeWorld.NODE_SIZE;
        WorldImage bg = new RectangleImage(
            new Posn(nodeSize / 2 + ((width / 2) * nodeSize), 
                nodeSize / 2 + ((height / 2) * nodeSize)),
                width * nodeSize, height * nodeSize, Color.white);
        for (int col = 0; col < this.nodes.size(); col += 1) {
            WorldImage temp = this.nodes.get(col).get(0).drawNode();
            for (int row = 1; row < this.nodes.get(col).size(); row += 1) {
                temp = new OverlayImages(temp, this.nodes.get(col).get(row).drawNode());
            }
            bg = new OverlayImages(bg, temp);
        }
        return bg;
    }
    // EFFECT: Adds search heads
    void drawSearch(ArrayList<Node> nodes) {
        if (nodes.size() > 0) {
            Node last = nodes.get(0);
            for (Node n: nodes) {
                n.front = true;
                this.frontN.add(n);
                for (Edge e: n.edges) {
                    if (e.start.equals(last) || e.end.equals(last)) {
                        e.front = true;
                        this.frontE.add(e);
                    }
                }
                last = n;
            }
        }
    }
    // Searches the graph depth first
    ArrayList<Node> depthFirst(int x, int y) {
        return this.genSearch(new Stack<Node>(), x, y);
    }
    //Searches the graph breadth first
    ArrayList<Node> breadthFirst(int x, int y) {
        return this.genSearch(new Queue<Node>(), x, y);
    }
    // Searches the graph breadth or depth first, depending on the type of icollection entered
    ArrayList<Node> genSearch(ICollection<Node> struct, int x, int y) {
        HashMap<Node, Node> cameFromEdge = new HashMap<Node, Node>();
        struct.add(this.nodes.get(0).get(0)); 
        ArrayList<Node> end = new ArrayList<Node>();
        while (struct.size() > 0) {
            Node next = struct.remove();
            if (cameFromEdge.containsValue(next)) {
                //If previously visited do nothing
            }
            else if (next.equals(this.nodes.get(x).get(y))) { 
                return reconstruct(cameFromEdge, next); 
            }
            else {
                for (Edge e: next.edges) {
                    if (!e.end.equals(next)) {
                        struct.add(e.end);
                        if (!cameFromEdge.containsKey(e.end)) {
                            cameFromEdge.put(e.end, next);
                        }
                    }
                    else {
                        struct.add(e.start);
                        if (!cameFromEdge.containsKey(e.start)) {
                            cameFromEdge.put(e.start, next);
                        }
                    }
                }
            }
        }


        return end;

    }
    // Reconstructs the final path based on the given hashmap
    ArrayList<Node> reconstruct(HashMap<Node, Node> map, Node end) {
        ArrayList<Node>  nodes = new ArrayList<Node>();
        Node next = end;
        while (!next.equals(this.nodes.get(0).get(0))) {
            nodes.add(next);
            next = map.get(next);
        }
        return nodes;
    }
    // EFFECT: Resets all the nodes to their original state
    void reset() {
        for (ArrayList<Node> arr: this.nodes) {
            for (Node n: arr) {
                n.front = false;
                n.visited = false;
                for (Edge e: n.edges) {
                    e.visited = false;
                    e.front = false;
                }
            }
        }

    }
    // EFFECT: Resets it so no nodes are in the front
    void resetFront() {
        for (Node n: this.frontN) {
            n.front = false;
        }
        for (Edge e: this.frontE) {
            e.front = false;
        }
    }
    // Returns the image of the world
    public WorldImage makeImage() {
        this.drawSearch(this.search.makeWorld());
        if (this.search.finished) {
            return new OverlayImages(
                this.drawNodes(), 
                new TextImage(
                    new Posn((MazeWorld.WORLD_WIDTH / 2) * MazeWorld.NODE_SIZE, 
                        (MazeWorld.WORLD_HEIGHT / 2)  * MazeWorld.NODE_SIZE), 
                        "Solved", MazeWorld.NODE_SIZE * 4, 1, Color.orange));
        }
        else {
            return this.drawNodes();

        }
    }
    // What the world performs on every tick
    public void onTick() {
        this.search.onTick();
        this.resetFront();
    }
    // Handles key events
    public void onKeyEvent(String str) {
        // Moves the head of the search if on a manual search
        if (str.equals("up") 
            || str.equals("down")
            || str.equals("left")
            || str.equals("right")) {
            this.search.onKey(str);
        }
        // Shows the current path
        else if (str.equals("s")) {
            this.search.showPath = !this.search.showPath; 
        }
        else {
            // Specifies the type of search
            // B -> breadth first
            // M -> manual search
            // D -> Depth first
            if (str.equals("b")
                || str.equals("d")
                || str.equals("m")) {
                if (str.equals("b")) {
                    this.searchType = 1;
                }
                if (str.equals("d")) {
                    this.searchType = -1;
                }
                if (str.equals("m")) {
                    this.searchType = 0;
                }
                this.reset();
                this.initSearch();
            }
            else if (str.equals("r")) {
                this.initWorld();
            }
            // Specifies the type of bias
            // H -> horizontal bias
            // N -> no bias
            // V -> vertical bias
            else if (str.equals("h")
                || str.equals("n")
                || str.equals("v")) {
                if (str.equals("h")) {
                    this.bias = 1;
                }
                if (str.equals("n")) {
                    this.bias = 0;
                }
                if (str.equals("v")) {
                    this.bias = -1;
                }
                this.initWorld();
            }

        }
    }
}
// A class to represent the union find structure
class UnionFind {
    HashMap<Integer, Node> map;
    UnionFind(ArrayList<ArrayList<Node>> nodes) {
        this.map = new HashMap<Integer, Node>();
        for (ArrayList<Node> ar: nodes) {
            for (Node n: ar) {
                map.put(n.hashCode(), n);
            }
        }
    }
    // Unions two nodes together in the hashmap
    boolean union(Node node1, Node node2) {
        Node fNode1 = this.find(node1);
        Node fNode2 = this.find(node2);
        if (fNode1.equals(fNode2)) {
            return false;
        }
        else {
            this.map.put(fNode1.hashCode(), node2);
            return true;
        }
    }
    // Finds the end result node
    Node find(Node n) {
        while (!this.map.get(n.hashCode()).equals(n)) {
            n = map.get(n.hashCode());
        }
        return n;
    }
}
// An abstract class to represent the different types of search
abstract class ASearch {
    GenSearch gen;
    boolean finished;
    boolean showPath;
    ASearch(ICollection<Node> struct, MazeWorld world) {
        this.gen = new GenSearch(struct, world);
        this.finished = false;
        this.showPath = false;

    }
    // EFFECT: Modifies the state of the search on every tick
    void onTick() {
        this.gen.showPath = this.showPath;
        this.finished = this.gen.finished;
        this.gen.onTick();
    }
    // EFFECT: Handles key events (Arrow Keys)
    void onKey(String str) {
        //Arrow Keys are not supported for generic ASearch Objects
    }
    // Returns the current/final path
    ArrayList<Node> makeWorld() {
        return this.gen.makeWorld();
    }

}
// A class to represent manual searches
class ManualSearch extends ASearch {
    Node cur;
    MazeWorld world;
    ManualSearch(MazeWorld world) {
        super(new Stack<Node>(), world);
        this.world = world;
        this.cur = world.nodes.get(0).get(0);
    }
    // EFFECT: Modifies the state of the search on every tick
    public void onTick() {
        int width = MazeWorld.WORLD_WIDTH - 1;
        int height = MazeWorld.WORLD_HEIGHT - 1;
        if (this.cur.equals(world.nodes.get(width).get(height))) {
            this.finished = true;
            this.gen.finished = true;
        }
        cur.front = true;
    }
    // EFFECT: Handles keys
    public void onKey(String str) {
        int x = 0;
        int y = 0;
        if (!this.finished) {
            if (str.equals("up")) {
                y = -1;
            }
            if (str.equals("down")) {
                y = 1;
            }
            if (str.equals("left")) {
                x = -1;    
            }
            if (str.equals("right")) {
                x = 1;
            }
            this.cur.front = false;
            this.cur.visited = true;
            cur = cur.move(x, y);
            this.cur.front = true;
        }
    }
    // Shows the final or current path;
    ArrayList<Node> makeWorld() {
        if (this.finished || this.showPath) {
            return this.world.breadthFirst(cur.x, cur.y);
        }
        else {
            this.cur.front = true;
            return new ArrayList<Node>();
        }
    }

}
// A class to represent depth first search;
class DepthFirst extends ASearch {
    DepthFirst(MazeWorld world) {
        super(new Stack<Node>(), world);
    }
}
// A class to represent breadth first search
class BreadthFirst extends ASearch {
    BreadthFirst(MazeWorld world) {
        super(new Queue<Node>(), world);
    }
}

// A class to represent the general search algorithm
class GenSearch {
    HashMap<Node, Node> cameFromEdge; 
    ICollection<Node> struct;
    MazeWorld world;
    Node next;
    boolean finished;
    boolean showPath;

    GenSearch(ICollection<Node> struct, MazeWorld world) {
        this(struct, world, false);
    } 
    GenSearch(ICollection<Node> struct, MazeWorld world, boolean showPath) {
        this.cameFromEdge = new HashMap<Node, Node>();
        this.struct = struct;
        this.world = world;
        struct.add(this.world.nodes.get(0).get(0));
        this.next = struct.remove();
        this.next.visited = true;
        this.showPath = showPath;
        this.finished = false;
    }
    // Effect: Modifies the state of all the components of the search algorithm
    void onTick() {
        this.next.visited = true;
        int width = MazeWorld.WORLD_WIDTH - 1;
        int height = MazeWorld.WORLD_HEIGHT - 1;
        if (this.next.equals(world.nodes.get(width).get(height))) {
            this.finished = true;
        }
        else if (cameFromEdge.containsValue(next)) { 
            //If the Node has been visited it should do nothing
        }
        else {
            for (Edge e: next.edges) {
                if (!e.end.equals(next)) {
                    struct.add(e.end);
                    if (!e.visited) {
                        cameFromEdge.put(e.end, next);
                        e.visited = true;
                    }
                }
                else {
                    struct.add(e.start);
                    if (!e.visited) {
                        cameFromEdge.put(e.start, next);
                        e.visited = true;
                    }
                }
            }
        }
        if (this.struct.size() > 0 && !this.finished) {

            this.next = this.struct.remove();
        }

    }
    // Returns the current path if the search is complete or showpath is enabled
    ArrayList<Node> makeWorld() {
        if (this.finished) { 
            return world.reconstruct(cameFromEdge, next); 
        }
        if (showPath) {
            return world.reconstruct(cameFromEdge, next); 
        }
        else {
            return new ArrayList<Node>();
        }

    }
}
/* Testing that uses Tester.jar

class ExamplesMaze {
    void testBigBang(Tester t) {
        MazeWorld maze = new MazeWorld(0, 0); 
        maze.bigBang(MazeWorld.WORLD_WIDTH * MazeWorld.NODE_SIZE, 
            MazeWorld.WORLD_HEIGHT * MazeWorld.NODE_SIZE, 0.001);
    }
    /*
     * Tests assume that
     * WORLD_HEIGHT = 20;
     * WORLD_WIDTH = 20;
     */
/*
    void testLists(Tester t) {
        Empty<String> empty = new Empty<String>();
        Cons<String> cons = new Cons<String>("h", 
            new Cons<String>("i", empty));
        t.checkExpect(empty.size(), 0);
        t.checkExpect(cons.size(), 2);
        t.checkExpect(cons.asCons(), cons);
        t.checkException(new IllegalArgumentException("Not a cons"), empty, "asCons");
    }
    IList<Edge> empty = new Empty<Edge>();        
    Node node1 = new Node(5, 5);
    Node node2 = new Node(4, 5);
    Node node3 = new Node(5, 4);
    Edge edge12 = new Edge(1, node1, node2);
    Edge edge32 = new Edge(2, node3, node2);
    Edge edge21 = new Edge(1, node2, node1);
    IList<Edge> edges1 = new Cons<Edge>(edge12, empty);
    IList<Edge> edges2 = new Cons<Edge>(edge21, empty);
    IList<Edge> edges3 = new Cons<Edge>(edge32, empty);
    void init() {
        IList<Edge> empty = new Empty<Edge>();        
        Node node1 = new Node(5, 5);
        Node node2 = new Node(4, 5);
        Node node3 = new Node(5, 4);
        Edge edge12 = new Edge(1, node1, node2);
        Edge edge32 = new Edge(2, node3, node2);
        Edge edge21 = new Edge(1, node2, node1);
        IList<Edge> edges1 = new Cons<Edge>(edge12, empty);
        IList<Edge> edges2 = new Cons<Edge>(edge21, empty);
        IList<Edge> edges3 = new Cons<Edge>(edge32, empty);
        node1.edges = edges1;
        node2.edges = edges2;
        node3.edges = edges3;

    }
    void testEdges(Tester t) {
        this.init(); 
        node1.edges = edges1;
        node2.edges = edges2;
        node3.edges = edges3;
        t.checkExpect(edge12.compareTo(edge32), -1);
        t.checkExpect(edge12.compareTo(edge21), 0);
        t.checkExpect(edge32.compareTo(edge12), 1);
        ArrayList<Node> lon1 = new ArrayList<Node>();
        lon1.add(node1);
        lon1.add(node2);
        ArrayList<Node> lon2 = new ArrayList<Node>();
        lon2.add(node3);
        ArrayList<ArrayList<Node>> lolon = new ArrayList<ArrayList<Node>>();
        lolon.add(lon1);
        lolon.add(lon2);
        UnionFind uf = new UnionFind(lolon);
        t.checkExpect(uf.find(node1), node1);
        uf.union(node1, node2);
        t.checkExpect(uf.find(node1), node2);
    }
    void testNodes(Tester t) {
        this.init();
        node1.edges = edges1;
        node2.edges = edges2;
        node3.edges = edges3;
        t.checkExpect(node1.move(0, 0), node1);
        t.checkExpect(node1.move(-1, 0), node2);
        t.checkExpect(node1.move(0, -1), node1);
        t.checkExpect(node3.move(-1, 1), node2);    
    }
    void testWorld(Tester t) {
        MazeWorld maze = new MazeWorld(0, 0);
        t.checkExpect(maze.searchType, 0);
        t.checkExpect(maze.search instanceof ManualSearch, true);
        t.checkExpect(maze.bias, 0);
        maze.onKeyEvent("h");
        t.checkExpect(maze.bias, 1);
        t.checkExpect(maze.search instanceof ManualSearch, true);
        maze.onKeyEvent("n");
        t.checkExpect(maze.bias, 0);
        maze.onKeyEvent("v");
        t.checkExpect(maze.bias, -1);
        maze.onKeyEvent("b");
        t.checkExpect(maze.search instanceof BreadthFirst, true);
        t.checkExpect(maze.searchType, 1);
        maze.onKeyEvent("d");
        t.checkExpect(maze.search instanceof DepthFirst, true);
        t.checkExpect(maze.searchType, -1);
        maze.onKeyEvent("m");
        t.checkExpect(maze.search instanceof ManualSearch, true);
        t.checkExpect(maze.searchType, 0);
        ManualSearch manSearch = (ManualSearch) maze.search;
        t.checkExpect(manSearch.finished, false);
        t.checkExpect(manSearch.cur, maze.nodes.get(0).get(0));
        maze.search.onKey("up");
        t.checkExpect(manSearch.cur.front, true);
        t.checkExpect(manSearch.cur, maze.nodes.get(0).get(0));
        maze.search.onKey("left");
        t.checkExpect(manSearch.cur, maze.nodes.get(0).get(0));
        t.checkExpect(manSearch.cur.x, 0);
        t.checkExpect(manSearch.cur.y, 0);
        manSearch.onKey("right");
        t.checkExpect(manSearch.cur.x, 0);
        t.checkExpect(manSearch.cur.y, 0);
        manSearch.onKey("left");
        t.checkExpect(manSearch.cur.x, 0);
        t.checkExpect(manSearch.cur.y, 0);
        manSearch.onKey("down");
        t.checkExpect(manSearch.cur.x, 0);
        t.checkExpect(manSearch.cur.y, 1);
        manSearch.onKey("up");
        t.checkExpect(manSearch.cur.x, 0);
        t.checkExpect(manSearch.cur.y, 0);
        // Maze key handler
        maze.onKeyEvent("up");
        t.checkExpect(manSearch.cur.front, true);
        t.checkExpect(manSearch.cur, maze.nodes.get(0).get(0));
        maze.onKeyEvent("left");
        t.checkExpect(manSearch.cur, maze.nodes.get(0).get(0));
        t.checkExpect(manSearch.cur.x, 0);
        t.checkExpect(manSearch.cur.y, 0);
        maze.onKeyEvent("right");
        t.checkExpect(manSearch.cur.x, 0);
        t.checkExpect(manSearch.cur.y, 0);
        maze.onKeyEvent("left");
        t.checkExpect(manSearch.cur.x, 0);
        t.checkExpect(manSearch.cur.y, 0);
        maze.onKeyEvent("down");
        t.checkExpect(manSearch.cur.x, 0);
        t.checkExpect(manSearch.cur.y, 1);
        maze.onKeyEvent("up");
        t.checkExpect(manSearch.cur.x, 0);
        t.checkExpect(manSearch.cur.y, 0);

    } 
    void testSearch(Tester t) {
        //Breadth First
        MazeWorld world = new MazeWorld(1, 0);
        //OnKey events
        world.search.onKey("up");
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 0);
        world.search.onKey("down");
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 0);
        world.search.onKey("left");
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 0);
        world.search.onKey("right");
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 0);
        //On Tick
        world.search.onTick();
        t.checkExpect(world.search.gen.next.x, 1);
        t.checkExpect(world.search.gen.next.y, 0);
        world.search.onTick();
        t.checkExpect(world.search.gen.next.x, 1);
        t.checkExpect(world.search.gen.next.y, 1);
        world.search.onTick();
        world.search.onTick();
        world.search.onTick();
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 1);
        world.search.onTick();
        world.search.onTick();
        world.search.onTick();
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 3);
        for (int i = 0; i < 1000; i += 1) {
            world.search.onTick();
        }
        t.checkExpect(world.search.finished);
        //Depth First
        world = new MazeWorld(-1, 0);
        //OnKey events
        world.search.onKey("up");
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 0);
        world.search.onKey("down");
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 0);
        world.search.onKey("left");
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 0);
        world.search.onKey("right");
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 0);
        //On Tick
        world.search.onTick();
        t.checkExpect(world.search.gen.next.x, 1);
        t.checkExpect(world.search.gen.next.y, 0);
        world.search.onTick();
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 0);
        world.search.onTick();
        world.search.onTick();
        world.search.onTick();
        t.checkExpect(world.search.gen.next.x, 0);
        t.checkExpect(world.search.gen.next.y, 2);
        world.search.onTick();
        world.search.onTick();
        world.search.onTick();
        t.checkExpect(world.search.gen.next.x, 1);
        t.checkExpect(world.search.gen.next.y, 2);
        for (int i = 0; i < 1000; i += 1) {
            world.search.onTick();
        }
        t.checkExpect(world.search.finished);
        //Depth First
        world = new MazeWorld(0, 0);
        //OnKey events
        world.search.onKey("right");
        t.checkExpect(world.nodes.get(0).get(0).visited, true);
        world.search.onKey("down");
        t.checkExpect(world.nodes.get(1).get(0).visited, true);
        world.search.onKey("left");
        t.checkExpect(world.nodes.get(1).get(1).visited, true);
        world.search.onKey("up");
        t.checkExpect(world.nodes.get(0).get(1).visited);
        world.search.onKey("down");
        t.checkExpect(world.nodes.get(1).get(1).visited);
        //On Tick

        for (int i = 0; i < 100; i += 1) {
            world.search.onTick();
        }
        t.checkExpect(world.search.finished, false);
    }
    void testMST(Tester t) {
        MazeWorld maze = new MazeWorld(0, 0); 
        ArrayList<Edge> loe = maze.getEdges();
        maze.mst(loe);
        Collections.sort(loe);
        int index = 0;
        int numEdges = 0;
        UnionFind map = new UnionFind(maze.nodes);
        while (numEdges < (MazeWorld.WORLD_HEIGHT * MazeWorld.WORLD_WIDTH) - 1) {
            Edge tempEdge = loe.get(index);
            if (map.union(tempEdge.start, tempEdge.end)) {
                tempEdge.start.edges = new Cons<Edge>(tempEdge, tempEdge.start.edges);
                tempEdge.end.edges = new Cons<Edge>(tempEdge, tempEdge.end.edges);
                numEdges += 1;
            }
            index += 1;
        }
        for (Edge e : loe) {
            t.checkExpect(map.union(e.start, e.end), false);
        }
    }
    void testReset(Tester t) {
        MazeWorld maze = new MazeWorld(0, 0); 
        Node one = new Node(3, 2);
        one.front = true;
        Node two = new Node(2, 2);
        two.front = true;
        Edge three = new Edge(2, one, two);
        three.front = true;
        Edge four = new Edge(1, two, one);
        four.front = true;
        one.edges = new Cons<Edge>(three, one.edges);
        two.edges = new Cons<Edge>(four, two.edges);
        maze.frontN.add(one);
        maze.frontN.add(two);
        maze.frontE.add(three);
        maze.frontE.add(four);
        maze.resetFront();
        t.checkExpect(one.front, false);
        t.checkExpect(two.front, false);
        t.checkExpect(three.front, false);
        t.checkExpect(four.front, false);
        ArrayList<ArrayList<Node>> lolon = maze.nodes;
        for (ArrayList<Node> lon : lolon) {
            for (Node n : lon) {
                n.visited = true;
                n.front = true;
                for (Edge e: n.edges) {
                    e.front = true;
                    e.visited = true;
                }
            }
        }
        maze.reset();
        for (ArrayList<Node> lon : lolon) {
            for (Node n : lon) {
                t.checkExpect(n.visited, false);
                t.checkExpect(n.front, false);
                for (Edge e: n.edges) {
                    t.checkExpect(e.front, false);
                    t.checkExpect(e.visited, false);
                }
            }
        }
    }
}
*/
