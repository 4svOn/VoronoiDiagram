package hse.edu.cs.fortuneAlg;

public class InitPoint {
    int index;
    private final Point point;
    Cell cell;

    InitPoint(int index, Point point) {
        this.index = index;
        this.point = point;
    }

    void setCell(Cell cell) {
        this.cell = cell;
    }

    public Point getPoint() {
        return point;
    }

    public Cell getCell() {
        return cell;
    }
}
