package hse.edu.cs.fortuneAlg;

public class Point implements Comparable<Point> {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point getOrthogonal() {
        return new Point(-this.y, this.x);
    }

    public void mul(double k) {
        this.x *= k;
        this.y *= k;
    }

    public void add(Point oth) {
        this.x += oth.x;
        this.y += oth.y;
    }

    public double getDet(Point oth) {
        return this.x * oth.y - this.y * oth.x;
    }

    public double getNorm() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double getDistance(Point oth) {
        return Point.sub(this, oth).getNorm();
    }

    public static Point sub(Point p1, Point p2) {
        return new Point(p1.x - p2.x, p1.y - p2.y);
    }

    public static Point sum(Point p1, Point p2) {
        return new Point(p1.x + p2.x, p1.y + p2.y);
    }

    public static Point mul(Point p1, double t) {
        return new Point(p1.x * t, p1.y * t);
    }

    @Override
    public String toString() {
        return this.x + " " + this.y;
    }

    @Override
    public int compareTo(Point other) {
        return Double.compare(this.y, other.y);
    }
}
