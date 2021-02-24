package streda_16_35_c05.model;

import transforms.Col;
import transforms.Point3D;

public class Vertex {

    private final Point3D point;
    private final Col color;
//    public final double x, y, z, w;

    public Vertex(Point3D point, Col color) {
        this.point = point;
        this.color = color;
//        this.x = point.getX();
//        this.y = point.getY();
//        this.z = point.getZ();
//        this.w = point.getW();
    }

    public Vertex mul(double t) {
        return new Vertex(point.mul(t), color.mul(t));
    }

    public Vertex add(Vertex v) {
        return new Vertex(point.add(v.getPoint()), color.add(v.getColor()));
    }

    public Point3D getPoint() {
        return point;
    }

    public Col getColor() {
        return color;
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public double getZ() {
        return point.getZ();
    }

    public double getW() {
        return point.getW();
    }

}
