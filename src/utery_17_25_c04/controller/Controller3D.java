package utery_17_25_c04.controller;

import transforms.*;
import utery_17_25_c04.model.Element;
import utery_17_25_c04.model.Vertex;
import utery_17_25_c04.rasterize.Raster;
import utery_17_25_c04.renderer.GPURenderer;
import utery_17_25_c04.renderer.RendererZBuffer;
import utery_17_25_c04.view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final GPURenderer renderer;
    private final Panel panel;
    private final Raster raster;

    private final List<Element> elements;
    private final List<Vertex> vb; // vertex buffer
    private final List<Integer> ib; // index buffer

    private Mat4 model, projection;
    private Camera camera;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        this.renderer = new RendererZBuffer(raster);

        elements = new ArrayList<>();
        vb = new ArrayList<>();
        ib = new ArrayList<>();

        initMatrices();
        initListeners(panel);
    }

    private void initMatrices() {
        model = new Mat4Identity();

        var e = new Vec3D(0, -5, 2);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-20));

        projection = new Mat4PerspRH(
                Math.PI / 3,
                raster.getHeight() / (float) raster.getWidth(),
                0.5,
                50
        );
    }

    private void initListeners(Panel panel) {
        // TODO
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.isControlDown());
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("drag");
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode() == KeyEvent.VK_H);
            }
        });
//        MouseAdapter drag = new MouseAdapter() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                System.out.println("drag");
//            }
//        };
//
//        panel.addMouseMotionListener(drag);
//        panel.removeMouseMotionListener(drag);
    }

    private void display() {
        raster.clear();
        renderer.clear();

        renderer.setModel(model);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);

        // musíme nakonec říci, že panel má nový obsah zobrazit
        panel.repaint();
    }

}
