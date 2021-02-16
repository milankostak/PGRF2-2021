package utery_17_25_c04.renderer;

import transforms.Mat4;
import utery_17_25_c04.model.Element;
import utery_17_25_c04.model.Vertex;
import utery_17_25_c04.rasterize.Raster;

import java.util.List;

public class RendererZBuffer implements GPURenderer {

    private final Raster<Integer> raster;

    public RendererZBuffer(Raster<Integer> raster) {
        this.raster = raster;
    }

    @Override
    public void draw(List<Element> elements, List<Vertex> vb, List<Integer> ib) {

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
