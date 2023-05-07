package hse.edu.cs.fortuneAlg;

public class Cell {
    private InitPoint initPoint;
    private HalfEdge halfEdge;

    void setInitPoint(InitPoint initPoint) {
        this.initPoint = initPoint;
    }

    void setHalfEdge(HalfEdge halfEdge) {
        if (this.halfEdge == null)
            this.halfEdge = halfEdge;
    }

    public HalfEdge getHalfEdge() {
        return halfEdge;
    }
}
