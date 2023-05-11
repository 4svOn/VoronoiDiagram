package hse.edu.cs.fortuneAlg;

public class HalfEdge {
    private CellPoint origin;
    private CellPoint destination;
    private HalfEdge twin;
    private final Cell cell;
    private HalfEdge next;
    private HalfEdge prev;

    HalfEdge(Cell cell) {
        this.cell = cell;
    }

    public CellPoint getDestination() {
        return destination;
    }

    public CellPoint getOrigin() {
        return origin;
    }

    public HalfEdge getNext() {
        return next;
    }

    public HalfEdge getPrev() {
        return prev;
    }

    public HalfEdge getTwin() {
        return twin;
    }

    void setOrigin(CellPoint origin) { this.origin = origin; }

    void setDestination(CellPoint destination) { this.destination = destination; }

    void setTwin(HalfEdge twin) { this.twin = twin; }

    void setNext(HalfEdge next) {
        this.next = next;
    }

    void setPrev(HalfEdge prev) {
        this.prev = prev;
    }
}
