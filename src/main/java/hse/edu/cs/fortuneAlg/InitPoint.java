package hse.edu.cs.fortuneAlg;

public class InitPoint {
    private final Point point;
    private Cell cell;

    InitPoint(Point point) {
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
