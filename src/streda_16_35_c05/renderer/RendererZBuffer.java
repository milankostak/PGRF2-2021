package streda_16_35_c05.renderer;

import streda_16_35_c05.model.Element;
import streda_16_35_c05.model.TopologyType;
import streda_16_35_c05.model.Vertex;
import streda_16_35_c05.rasterize.DepthBuffer;
import streda_16_35_c05.rasterize.LineRasterizerGraphics;
import streda_16_35_c05.rasterize.Raster;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;
import java.util.List;

public class RendererZBuffer implements GPURenderer {

    private final Raster<Integer> imageRaster;
    private final Raster<Double> depthBuffer;

    private Mat4 model, view, projection;

    public RendererZBuffer(Raster<Integer> imageRaster) {
        this.imageRaster = imageRaster;
        this.depthBuffer = new DepthBuffer(imageRaster);

        model = new Mat4Identity();
        view = new Mat4Identity();
        projection = new Mat4Identity();
    }

    @Override
    public void draw(List<Element> elements, List<Integer> ib, List<Vertex> vb) {
        for (Element element : elements) {
            final TopologyType topologyType = element.getTopologyType();
            final int start = element.getStart();
            final int count = element.getCount();

            if (topologyType == TopologyType.TRIANGLE) {
                for (int i = start; i < start + count; i += 3) {
                    final Integer i1 = ib.get(i);
                    final Integer i2 = ib.get(i + 1);
                    final Integer i3 = ib.get(i + 2);
                    final Vertex v1 = vb.get(i1);
                    final Vertex v2 = vb.get(i2);
                    final Vertex v3 = vb.get(i3);
                    prepareTriangle(v1, v2, v3);
                }

            } else if (topologyType == TopologyType.LINE) {
                // TODO
            }
        }
    }

    private void prepareTriangle(Vertex v1, Vertex v2, Vertex v3) {
//        LineRasterizerGraphics lineRasterizer = new LineRasterizerGraphics(imageRaster);
//        lineRasterizer.rasterize((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y, Color.RED);
//        lineRasterizer.rasterize((int) v1.x, (int) v1.y, (int) v3.x, (int) v3.y, Color.CYAN);
//        lineRasterizer.rasterize((int) v3.x, (int) v3.y, (int) v2.x, (int) v2.y, Color.WHITE);

        // 1. transformace vrcholů
        Vertex a = new Vertex(v1.getPoint().mul(model).mul(view).mul(projection), v1.getColor());
        Vertex b = new Vertex(v2.getPoint().mul(model).mul(view).mul(projection), v2.getColor());
        Vertex c = new Vertex(v3.getPoint().mul(model).mul(view).mul(projection), v3.getColor());

        // 2. ořezání
        // ořezat trojúhelníky, které jsou CELÉ mimo zobrazovací objem
        // slide 93
        // TODO

        // 3. seřazení vrcholů podle Z (a.z > b.z > c.z)
        // slide 97
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getZ() < c.getZ()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }
        // teď je v C vrchol, jehož Z je k nám nejblíže (je nejmenší, může být i za námi)
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        // teď máme seřazeno - Z od největšího po nejmenší: A,B,C

        // 4. ořezání podle hrany Z
        if (a.getZ() < 0) {
            // A.Z je menší než nula => všehcny Z jsou menší než nula => není co zobrazit (vše je za námi)
            return;
        } else if (b.getZ() < 0) {
            // vrchol A je vidět, vrcholy B,C nejsou

            // odečíst minimum, dělit rozsahem
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());
            // 0 -> protože nový vrchol má mít Z souřadnici 0
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));
//            System.out.println(t1);

            double t2 = (0 - a.getZ()) / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            drawTriangle(a, ab, ac);

        } else if (c.getZ() < 0) {

        } else {
            // vidíme celý trojúhelník (podle Z)
            drawTriangle(a, b, c);
        }
    }

    private void drawTriangle(Vertex a, Vertex b, Vertex c) {
        LineRasterizerGraphics lineRasterizer = new LineRasterizerGraphics(imageRaster);
        lineRasterizer.rasterize((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY(), Color.RED);
        lineRasterizer.rasterize((int) a.getX(), (int) a.getY(), (int) c.getX(), (int) c.getY(), Color.CYAN);
        lineRasterizer.rasterize((int) c.getX(), (int) c.getY(), (int) b.getX(), (int) b.getY(), Color.WHITE);
    }

    private Vec3D transformToWindow(Point3D vec) {
        return new Vec3D(vec)
                .mul(new Vec3D(1, -1, 1)) // Y jde nahoru a my chceme, aby šlo dolů
                .add(new Vec3D(1, 1, 0)) // (0,0) je uprostřed a my chceme, aby bylo vlevo nahoře
                // máme <0;2> -> vynásobíme polovinou velikosti plátna
                .mul(new Vec3D(imageRaster.getWidth() / 2f, imageRaster.getHeight() / 2f, 1));
    }

    @Override
    public void clear() {

    }

    @Override
    public void setModel(Mat4 model) {
        // TODO
    }

    @Override
    public void setView(Mat4 view) {
        // TODO
    }

    @Override
    public void setProjection(Mat4 projection) {
        // TODO
    }

}
