import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Triangle {
    private List<Point> points;

    public Triangle(Point a, Point b, Point c) {
        points = new ArrayList<>();
        points.addAll(Point.getPointsofLine(a, b));
        points.addAll(Point.getPointsofLine(b, c));
        points.addAll(Point.getPointsofLine(c, a));
    }

    public Stream<PairOfPoints> getIntDists() {
        return IntStream.range(0, points.size())
                .boxed()
                .flatMap(i -> IntStream.range(i+1, points.size()).boxed().map(j -> new PairOfPoints(points.get(i), points.get(j))))
                .filter(pop -> pop.getDist()%1 == 0);
    }

    public Optional<Point> getFarthestPoint() {
        Optional<Point> mPoint = points.stream().max(Point::compareTo);

        if (points.stream().filter(x -> x.getRadVector() == mPoint.get().getRadVector()).count() == 1)
            return mPoint;
        else return Optional.empty();

    }


}

class PairOfPoints{
    private Point a, b;

    PairOfPoints(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    double getDist() {
        return Math.sqrt((this.a.getX() - this.b.getX())*(this.a.getX() - this.b.getX())
                + (this.a.getY() - this.b.getY()) *(this.a.getY() - this.b.getY()));
    }

    public String toString() {
        return this.a.toString() + " -- " + this.b.toString() + " " + getDist();
    }
}

class Point implements Comparable<Point>{
    private int x, y;
    private static int gcd(int x, int y) {
        return y == 0 ? x : gcd(y, x%y);
    }

    Point(int x, int y) {
        this.x = x;
        this.y = y;

    }

    static List<Point> getPointsofLine(Point a, Point b) {
        int gcd = gcd(Math.abs(a.x - b.x), Math.abs(a.y - b.y));
        int stepX = (a.x - b.x)/gcd;
        int stepY = (a.y - b.y)/gcd;

        return setPoints(gcd, b, stepX, stepY);
    }

    private static List<Point> setPoints(int gcd, Point b, int stepX, int stepY) {
        return Stream.iterate(1, n -> n + 1).limit(gcd)
                .map(i -> new Point(b.x + stepX*i, b.y + stepY*i)).collect(Collectors.toList());
    }

    double getRadVector() { return Math.sqrt(x*x + y*y); }
    int getX() { return x; }
    int getY() { return y; }

    public String toString() {
        return "(" + x + "." + y + ")";
    }

    public int compareTo(Point o) {
        return Double.compare(this.getRadVector(), o.getRadVector());
    }
}
