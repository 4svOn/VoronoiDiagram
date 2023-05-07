package hse.edu.cs.fortuneAlg;

public class HalfEdge {
    CellPoint origin;
    CellPoint destination;
    HalfEdge twin;
    Cell cell;
    HalfEdge next;
    HalfEdge prev;

    HalfEdge(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
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
}
