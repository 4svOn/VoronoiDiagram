package hse.edu.cs.fortuneAlg;

import java.util.ArrayList;
import java.util.LinkedList;

public class VoronoiDiagram {
    private final ArrayList<InitPoint> initPoints = new ArrayList<>();
    private final ArrayList<Cell> cells = new ArrayList<>();
    private final LinkedList<CellPoint> cellPoints = new LinkedList<>();
    private final LinkedList<HalfEdge> halfEdges = new LinkedList<>();

    VoronoiDiagram(ArrayList<Point> points) {
        initPoints.ensureCapacity(points.size());
        cells.ensureCapacity(points.size());
        for(int i = 0; i < points.size(); ++i) {
            initPoints.add(i, new InitPoint(i, points.get(i)));
            cells.add(i, new Cell());
            initPoints.get(i).setCell(cells.get(i));
            cells.get(i).setInitPoint(initPoints.get(i));
        }
    }

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
}
