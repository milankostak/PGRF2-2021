package streda_16_35_c05.renderer;

import streda_16_35_c05.model.Element;
import streda_16_35_c05.model.ElementType;
import streda_16_35_c05.model.Vertex;
import streda_16_35_c05.rasterize.LineRasterizerGraphics;
import streda_16_35_c05.rasterize.Raster;
import transforms.Mat4;

import java.awt.*;
import java.util.List;

public class RendererZBuffer implements GPURenderer {

    private final Raster<Integer> imageRaster;

    public RendererZBuffer(Raster<Integer> imageRaster) {
        this.imageRaster = imageRaster;
    }

    @Override
    public void draw(List<Element> elements, List<Integer> ib, List<Vertex> vb) {
        for (Element element : elements) {
            final ElementType elementType = element.getElementType();
            final int start = element.getStart();
            final int count = element.getCount();

            if (elementType == ElementType.TRIANGLE) {
                for (int i = start; i < start + count; i += 3) {
                    final Integer i1 = ib.get(i);
                    final Integer i2 = ib.get(i + 1);
                    final Integer i3 = ib.get(i + 2);
                    final Vertex v1 = vb.get(i1);
                    final Vertex v2 = vb.get(i2);
                    final Vertex v3 = vb.get(i3);
                    prepareTriangle(v1, v2, v3);
                }

            } else if (elementType == ElementType.LINE) {
                // TODO
            }
        }
    }

    private void prepareTriangle(Vertex v1, Vertex v2, Vertex v3) {
        LineRasterizerGraphics lineRasterizer = new LineRasterizerGraphics(imageRaster);
        lineRasterizer.rasterize((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y, Color.RED);
        lineRasterizer.rasterize((int) v1.x, (int) v1.y, (int) v3.x, (int) v3.y, Color.CYAN);
        lineRasterizer.rasterize((int) v3.x, (int) v3.y, (int) v2.x, (int) v2.y, Color.WHITE);
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
