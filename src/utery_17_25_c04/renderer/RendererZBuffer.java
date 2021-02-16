package utery_17_25_c04.renderer;

import transforms.Mat4;
import utery_17_25_c04.model.Element;
import utery_17_25_c04.model.ElementType;
import utery_17_25_c04.model.Vertex;
import utery_17_25_c04.rasterize.Raster;

import java.util.List;

public class RendererZBuffer implements GPURenderer {

    private final Raster<Integer> raster;

    public RendererZBuffer(Raster<Integer> raster) {
        this.raster = raster;
    }

    @Override
    public void draw(List<Element> elements, List<Integer> ib, List<Vertex> vb) {
        for (Element element : elements) {
            final ElementType type = element.getElementType();
            final int start = element.getStart();
            final int count = element.getCount();
            if (type == ElementType.TRIANGLE) {
                for (int i = start; i < start + count; i += 3) {
                    final Integer i1 = ib.get(i);
                    final Integer i2 = ib.get(i + 1);
                    final Integer i3 = ib.get(i + 2);
                    final Vertex v1 = vb.get(i1);
                    final Vertex v2 = vb.get(i2);
                    final Vertex v3 = vb.get(i3);
//                    prepareTriangle(v1, v2, v3);
                }
            } else if (type == ElementType.LINE) {
                // dobrovolný domácí úkol, příště ukážu
                // dopsat for cyklus pro vykreslní úsečky
            }
//            switch (type) {
//                case TRIANGLE:
//                    System.out.println("triangle");
//                    break;
//                case LINE:
//                    System.out.println("line");
//                    break;
//                case POINT:
//                    System.out.println("point");
//                    break;
//            //default UnsupportedOperationException
//            }
//
//            switch (type) {
//                case TRIANGLE -> System.out.println("triangle");
//                case LINE -> System.out.println("line");
//                case POINT -> System.out.println("point");
//            }
        }
    }

//    private void prepareTriangle(Vertex v1, Vertex v2, Vertex v3) {
//        LineRasterizer lineRasterizer = new LineRasterizerGraphics(raster);
//        lineRasterizer.rasterize((int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y, v1.getColor());
//        lineRasterizer.rasterize((int)v1.x, (int)v1.y, (int)v3.x, (int)v3.y, v1.getColor());
//        lineRasterizer.rasterize((int)v2.x, (int)v2.y, (int)v3.x, (int)v3.y, v2.getColor());
//    }

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
