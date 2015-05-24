//import tester.Tester;
abstract class ANode<T> {
    ANode<T> next;
    ANode<T> prev;
    ANode() {
        this.next = null;
        this.prev = null;
    }
    // Returns the size of the list
    abstract int size();
    // Finds the first node that satisfies the given predicate, else returns the header
    abstract ANode<T> find(IPred<T> pred);
    // Removes this from the list
    // If it's a sentinel, does nothing
    abstract DNode<T> remove();
    // Removes this node from the list
    // If it's a sentinel, does nothing
    abstract void removeNode();
}

class Sentinel<T> extends ANode<T> { 
    /*Template
     * Fields:
     * this.next -> ANode<T>
     * this.prev -> ANode<T>
     * 
     * Methods:
     * size() -> int
     * find(IPred) -> ANode<T>
     * remove() -> node<T>
     * removenode() -> void
     */
    
    Sentinel() {
        super();
        this.next = this;
        this.prev = this;
    }
    // Returns the size of the list
    int size() {
        return 0;
    }
    // Finds the given node that satisfies the predicate,
    // Else returns the sentinel
    ANode<T> find(IPred<T> pred) {
        return this;
    }
    // Removes this node from the list
    // If it's a sentinel, throws an exception
    DNode<T> remove() {
        throw new RuntimeException("List is Empty");

    }
    void removeNode() {
        // Method does nothing if called on a sent
    }
}
// A class to represent a node
class DNode<T> extends ANode<T> {
    /*Template
     * Fields:
     * this.data -> T
     * this.next -> ANode<T>
     * this.prev -> ANode<T>
     * 
     * Methods:
     * size() -> int
     * find(IPred) -> ANode<T>
     * remove() -> node<T>
     * removenode() -> void
     */
    T data;
    DNode(T data) {
        super();
        this.data = data;
    }
    DNode(T data, ANode<T> next, ANode<T> prev) {
        this(data);
        if (next == null || prev == null) {
            throw new IllegalArgumentException("Nodes cannot be null");
        }
        this.next = next;
        this.prev = prev;
        next.prev = this;
        prev.next = this;
    }
    // Returns the size of the list
    int size() {
        return 1 + this.next.size();
    }
    // Finds the given node that satisfies the predicate,
    // Else returns the sentinel
    ANode<T> find(IPred<T> pred) {
        if (pred.apply(this.data)) {
            return this;
        }
        else {
            return this.next.find(pred);
        }
    }
    // Removes this node from the list
    DNode<T> remove() {
        //ANode<T> next = this.next;
        //ANode<T> prev = this.prev;
        this.next.prev = this.prev;
        this.prev.next = this.next;   
        return this;
    }
    // Removes this node from the list
    void removeNode() {
        this.remove();
    }
}
// A class to represent a DEQue
class Deque<T> {
    /*Template
     * Fields:
     * this.next -> ANode<T>
     * this.prev -> ANode<T>
     * 
     * Methods:
     * size() -> int
     * find(IPred) -> ANode<T>
     * remove() -> node<T>
     * removenode() -> void
     */
    Sentinel<T> header;
    Deque(Sentinel<T> header) {
        this.header = header;
    }
    Deque() {
        this.header = new Sentinel<T>();
    }
    // Determines the size of the list
    int size() {
        return this.header.next.size();
    }
    // Adds a new node at the head of the list
    void addAtHead(T data) {
        new DNode<T>(data, this.header.next, this.header);
    }
    // Adds a new node at the tail of the list
    void addAtTail(T data) {
        new DNode<T>(data, this.header, this.header.prev);
    }
    // Removes the node from the head of the list
    T removeFromHead() {
        return this.header.next.remove().data;
    }
    // Removes the node from the tail of the list
    T removeFromTail() {
        return this.header.prev.remove().data;
    }
    // Finds the first node (if any) that satisfies the given pred
    ANode<T> find(IPred<T> pred) {
        return this.header.next.find(pred);
    }
    // Removes the given node from the list
    void removeNode(ANode<T> node) {
        node.removeNode();
    }
}

//Represents a boolean-valued question over values of type T
interface IPred<T> {
    boolean apply(T t);
}

// Determines if the given string is longer than 2 characters
class LongerThanTwo implements IPred<String> {
    public boolean apply(String string) {
        return string.length() > 2;
    }
}

// Determines if the given string is equal to CDE
class IsCDE implements IPred<String> {
    // Determines if the given string is equal to CDE
    public boolean apply(String string) {
        return string.equals("cde");
    }
}

interface ICollection<T> {
    void add(T data);
    T remove();
    int size();
}
class Stack<T> implements ICollection<T> {
    Deque<T> deque;
    Stack() {
        this.deque = new Deque<T>();
    }
    public void add(T data) {
        this.deque.addAtHead(data);
    }
    public T remove() {
        return this.deque.removeFromHead();
    }
    public int size() {
        return this.deque.size();
    }
}
class Queue<T> implements ICollection<T> {
    Deque<T> deque;
    Queue() {
        this.deque = new Deque<T>();
    }
    public void add(T data) {
        this.deque.addAtTail(data);
    }
    public T remove() {
        return this.deque.removeFromHead();
    }
    public int size() {
        return this.deque.size();
    }
}
/*
  Tests that use Tester.jar
class ExamplesDeque {
    void initialize() {
        sent1 = new Sentinel<String>();
        node1 = new DNode<String>("abc", sent1, sent1);
        node2 = new DNode<String>("bcd", node1, sent1);
        node3 = new DNode<String>("cde", node2, sent1);
        node4 = new DNode<String>("def", node3, sent1);
        deque1 = new Deque<String>(sent1);
        sent2 = new Sentinel<String>();
        node5 = new DNode<String>("gat", sent2, sent2);
        node6 = new DNode<String>("lcd", node5, sent2);
        node7 = new DNode<String>("eat", node6, sent2);
        node8 = new DNode<String>("pop", node7, sent2);
        deque2 = new Deque<String>(sent2);
        sent3 = new Sentinel<String>();
        deque3 = new Deque<String>(sent3);
        IPred<String> longerThanTwo = new LongerThanTwo();
        IPred<String> isCDE = new IsCDE();
    }
    ANode<String> constructNullNode() {
        return new DNode<String>("Oops", null, null);
    }
    Sentinel<String> sent1 = new Sentinel<String>();
    DNode<String> node1 = new DNode<String>("abc", sent1, sent1);
    DNode<String> node2 = new DNode<String>("bcd", node1, sent1);
    DNode<String> node3 = new DNode<String>("cde", node2, sent1);
    DNode<String> node4 = new DNode<String>("def", node3, sent1);
    Deque<String> deque1 = new Deque<String>(sent1);
    Sentinel<String> sent2 = new Sentinel<String>();
    DNode<String> node5 = new DNode<String>("gat", sent2, sent2);
    DNode<String> node6 = new DNode<String>("lcd", node5, sent2);
    DNode<String> node7 = new DNode<String>("eat", node6, sent2);
    DNode<String> node8 = new DNode<String>("pop", node7, sent2);
    Deque<String> deque2 = new Deque<String>(sent2);
    Sentinel<String> sent3 = new Sentinel<String>();
    Deque<String> deque3 = new Deque<String>(sent3);
    IPred<String> longerThanTwo = new LongerThanTwo();
    IPred<String> longerThanFour = new LongerThanTwo();
    IPred<String> isCDE = new IsCDE();


    void testSize(Tester t) {
        this.initialize();
        Deque dq5 = new Deque();
        t.checkExpect(this.deque1.size(), 4);
        t.checkExpect(this.deque2.size(), 4);
        t.checkExpect(this.deque3.size(), 0);
        t.checkExpect(dq5.size(), 0);
    }
    void testHead(Tester t) {
        this.initialize();
        this.deque1.addAtHead("x");
        this.deque2.addAtHead("x");
        this.deque3.addAtHead("x");
        t.checkExpect(this.deque1.size(), 5);
        t.checkExpect(this.deque2.size(), 5);
        t.checkExpect(this.deque3.size(), 1);
        this.deque1.addAtHead("y");
        t.checkExpect(this.deque1.size(), 6);
        this.deque1.addAtHead("q");
        t.checkExpect(this.deque1.size(), 7);
        this.deque1.removeFromHead();
        t.checkExpect(this.deque1.size(), 6);
        this.deque1.removeFromTail();
        t.checkExpect(this.deque1.size(), 5);
    }
    void testTail(Tester t) {
        this.initialize();
        this.deque1.addAtTail("x");
        this.deque2.addAtTail("x");
        this.deque3.addAtTail("x");
        t.checkExpect(this.deque1.size(), 5);
        t.checkExpect(this.deque2.size(), 5);
        t.checkExpect(this.deque3.size(), 1);
        this.deque1.addAtTail("y");
        t.checkExpect(this.deque1.size(), 6);
        this.deque1.addAtTail("q");
        t.checkExpect(this.deque1.size(), 7);
    }
    void testFind(Tester t) {
        this.initialize();
        Sentinel<String> sentTest = new Sentinel<String>();
        DNode<String> node500 = new DNode<String>("a", sentTest, sentTest);
        DNode<String> node600 = new DNode<String>("b", node500, sentTest);
        Deque<String> deque500 = new Deque<String>(sentTest);
        t.checkExpect(this.deque1.find(isCDE), node3);
        t.checkExpect(this.deque2.find(isCDE), sent2);
        t.checkExpect(this.deque3.find(isCDE), sent3);
        t.checkExpect(this.deque3.find(longerThanTwo), sent3);
        t.checkExpect(deque500.find(isCDE), sentTest);
    }
    void testRemove(Tester t) {
        this.initialize();
        this.deque1.removeNode(node3);
        t.checkExpect(this.deque1.find(isCDE), sent1);
        t.checkExpect(this.deque1.size(), 3);
        this.initialize();
        this.node3.remove();
        t.checkExpect(this.deque1.find(isCDE), sent1);
        t.checkException(new RuntimeException("List is Empty"), this.sent3, "remove");
    }
    void testConstructNullNode(Tester t) {
        t.checkException(new IllegalArgumentException("Nodes cannot be null"), 
                this, "constructNullNode");
    }
}
*/