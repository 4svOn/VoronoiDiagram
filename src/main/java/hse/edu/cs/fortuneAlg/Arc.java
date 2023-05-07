package hse.edu.cs.fortuneAlg;

class Arc {

    // Hierarchy in tree
    Arc parent, left, right;

    // Voronoi diagram information
    InitPoint initPoint;
    HalfEdge leftHalfEdge;
    HalfEdge rightHalfEdge;
    Event event;

    // For iterating over elements
    Arc next, prev;

    // For tree balancing
    enum Color{
        RED,
        BLACK
    }
    Color color;

    Arc() {
        this.color = Color.BLACK;
    }

    Arc(InitPoint initPoint, Arc nullNode) {
        this.initPoint = initPoint;
        this.parent = this.left = this.right = this.next = this.prev = nullNode;
        this.color = Color.RED;
    }

    void makeEventInvalid() {
        if (this.event != null) {
            this.event.makeInvalid();
            this.event = null;
        }
    }
}
