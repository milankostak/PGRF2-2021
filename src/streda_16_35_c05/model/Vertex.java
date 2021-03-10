package streda_16_35_c05.model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

import java.util.Optional;

public class Vertex {

    private final Point3D point;
    private final Col color;
//    private final Vec2D texCoord;
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

    public Optional<Vertex> dehomog() {
//        Optional<Vec3D> dehomog = point.dehomog();
//        if (dehomog.isPresent()) {
//            return Optional.of(new Vertex(new Point3D(dehomog.get()), color));
//        } else {
//            return Optional.empty();
//        }

        return point.dehomog().map(vec3D -> new Vertex(new Point3D(vec3D), color));
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
