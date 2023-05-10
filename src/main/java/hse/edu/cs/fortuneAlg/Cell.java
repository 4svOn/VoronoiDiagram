package hse.edu.cs.fortuneAlg;

public class Cell {
    private HalfEdge halfEdge;

    void setHalfEdge(HalfEdge halfEdge) {
        if (this.halfEdge == null)
            this.halfEdge = halfEdge;
    }

    public HalfEdge getHalfEdge() {
        return halfEdge;
    }
}
