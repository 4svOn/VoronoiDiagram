package hse.edu.cs.fortuneAlg;

import java.util.ArrayList;
import java.util.LinkedList;

public class VoronoiDiagram {
    private final ArrayList<InitPoint> initPoints = new ArrayList<>();
    private final LinkedList<CellPoint> cellPoints = new LinkedList<>();
    private final LinkedList<HalfEdge> halfEdges = new LinkedList<>();

    private ArrayList<Point> points;

    VoronoiDiagram(ArrayList<Point> points) {
        this.points = points;
        initPoints.ensureCapacity(points.size());
        for(int i = 0; i < points.size(); ++i) {
            initPoints.add(i, new InitPoint(points.get(i)));
            initPoints.get(i).setCell(new Cell());
        }
    }

    public ArrayList<Point> getPoints() { return points; }

    public ArrayList<InitPoint> getInitPoints() {
        return this.initPoints;
    }

    HalfEdge createHalfEdge(Cell cell) {
        HalfEdge halfEdge = new HalfEdge(cell);
        halfEdges.add(halfEdge);
        cell.setHalfEdge(halfEdge);
        return halfEdge;
    }

    CellPoint createCellPoint(Point point) {
        CellPoint cellPoint = new CellPoint(point);
        cellPoints.add(cellPoint);
        return cellPoint;
    }

    LinkedList<CellPoint> getCellPoints() {
        return cellPoints;
    }
}
