package streda_16_35_c05.renderer;

import streda_16_35_c05.model.Element;
import streda_16_35_c05.model.TopologyType;
import streda_16_35_c05.model.Vertex;
import streda_16_35_c05.rasterize.DepthBuffer;
import streda_16_35_c05.rasterize.Raster;
import streda_16_35_c05.shader.Shader;
import transforms.*;

import java.util.List;
import java.util.Optional;

public class RendererZBuffer implements GPURenderer {

    private final Raster<Integer> imageRaster;
    private final Raster<Double> depthBuffer;

    private Mat4 model, view, projection;
    private Shader<Vertex, Col> shader;

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
            double t1 = (0 - b.getZ()) / (c.getZ() - b.getZ());
            Vertex bc = b.mul(1 - t1).add(c.mul(t1));
            drawTriangle(a, b, bc);

            double t2 = (0 - a.getZ()) / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));
            drawTriangle(a, bc, ac);

        } else {
            // vidíme celý trojúhelník (podle Z)
            drawTriangle(a, b, c);
        }
    }

    private void drawTriangle(Vertex a, Vertex b, Vertex c) {
        // 1. dehomogenizace
        Optional<Vertex> oA = a.dehomog();
        Optional<Vertex> oB = b.dehomog();
        Optional<Vertex> oC = c.dehomog();

        // zahodit trojúhelník, pokud některý vrchol má w==0
        if (oA.isEmpty() || oB.isEmpty() || oC.isEmpty()) return;

        a = oA.get();
        b = oB.get();
        c = oC.get();

        // 2. transformace do okna
        a = transformToWindow(a);
        b = transformToWindow(b);
        c = transformToWindow(c);

        // 3. seřazení podle Y
        // cíl: a.y < b.y < c.y
        if (a.getY() > b.getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getY() > c.getY()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }
        if (a.getY() > b.getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        // 4. interpolace podle Y
        // slide 124
        // A -> B
        long start = (long) Math.max(Math.ceil(a.getY()), 0);
        double end = Math.min(b.getY(), imageRaster.getHeight() - 1);
        for (long y = start; y <= end; y++) {
            double t1 = (y - a.getY()) / (b.getY() - a.getY());
            double t2 = (y - a.getY()) / (c.getY() - a.getY());

            Vertex ab = a.mul(1 - t1).add(b.mul(t1));
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            fillLine(y, ab, ac);
        }

        // B -> C
        // TODO
    }

    private Vertex transformToWindow(Vertex vertex) {
        Vec3D vec3D = new Vec3D(vertex.getPoint())
                .mul(new Vec3D(1, -1, 1)) // Y jde nahoru a my chceme, aby šlo dolů
                .add(new Vec3D(1, 1, 0)) // (0,0) je uprostřed a my chceme, aby bylo vlevo nahoře
                // máme <0;2> -> vynásobíme polovinou velikosti plátna
                .mul(new Vec3D(imageRaster.getWidth() / 2f, imageRaster.getHeight() / 2f, 1));

        return new Vertex(new Point3D(vec3D), vertex.getColor());
    }

    private void fillLine(long y, Vertex a, Vertex b) {
        if (a.getX() > b.getX()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        long start = (long) Math.max(Math.ceil(a.getX()), 0);
        double end = Math.min(b.getX(), imageRaster.getWidth() - 1);
        for (long x = start; x <= end; x++) {
            double t = (x - a.getX()) / (b.getX() - a.getX());
            Vertex finalVertex = a.mul(1 - t).add(b.mul(t));

            final Col finalColor = shader.shade(finalVertex);
            drawPixel((int) x, (int) y, finalVertex.getZ(), finalColor);
        }
    }

    private void drawPixel(int x, int y, double z, Col color) {
        Optional<Double> zOptional = depthBuffer.getElement(x, y);
        if (zOptional.isPresent() && z < zOptional.get()) {
            depthBuffer.setElement(x, y, z);
            imageRaster.setElement(x, y, color.getRGB());
        }
    }

    @Override
    public void clear() {
        // TODO clear zb ib
    }

    @Override
    public void setShader(Shader<Vertex, Col> shader) {
        this.shader = shader;
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
