package streda_16_35_c05.model;

import transforms.Col;
import transforms.Point3D;

public class Vertex {

    private final Point3D point;
    private final Col color;
    public final double x, y, z, w;

    public Vertex(Point3D point, Col color) {
        this.point = point;
        this.color = color;
        this.x = point.getX();
        this.y = point.getY();
        this.z = point.getZ();
        this.w = point.getW();
    }

    public Point3D getPoint() {
        return point;
    }

    public Col getColor() {
        return color;
    }

}
