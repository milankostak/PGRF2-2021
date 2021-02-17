package streda_16_35_c05.controller;

import streda_16_35_c05.model.Element;
import streda_16_35_c05.model.ElementType;
import streda_16_35_c05.model.Vertex;
import streda_16_35_c05.rasterize.Raster;
import streda_16_35_c05.renderer.GPURenderer;
import streda_16_35_c05.renderer.RendererZBuffer;
import streda_16_35_c05.view.Panel;
import transforms.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final Panel panel;
    private final Raster<Integer> imageRaster;
    private final GPURenderer renderer;

    private final List<Element> elementBuffer;
    private final List<Integer> indexBuffer;
    private final List<Vertex> vertexBuffer;

    private Mat4 model, projection;
    private Camera camera;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.imageRaster = panel.getRaster();
        this.renderer = new RendererZBuffer(imageRaster);

        elementBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        vertexBuffer = new ArrayList<>();

        initMatrices();
        initListeners(panel);
        createScene();
    }

    private void createScene() {
        // 5 červených vrcholů
        vertexBuffer.add(new Vertex(new Point3D(200, 200, 0), new Col(255, 0, 0))); // 0 // nejvíce vlevo
        vertexBuffer.add(new Vertex(new Point3D(600, 350, 0), new Col(255, 0, 0))); // 1 // nejvíce dole
        vertexBuffer.add(new Vertex(new Point3D(450, 50, 0), new Col(255, 0, 0))); // 2 // společný
        vertexBuffer.add(new Vertex(new Point3D(), new Col(255, 0, 0))); // 3 // nejvíce vpravo
        vertexBuffer.add(new Vertex(new Point3D(), new Col(255, 0, 0))); // 4 // nejvíce nahoře
        // 2 modré vrcholy
        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 0, 255))); // 5 // nahoře
        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 0, 255))); // 6 // dole
        // 2 zelené vrcholy
        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 255, 0))); // 7 // vpravo
        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 255, 0))); // 8 // vlevo

        // dolní trojúhleník
        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(1);
        // horní trojúhleník
        indexBuffer.add(2);
        indexBuffer.add(4);
        indexBuffer.add(3);

        // modrá úsečka
        indexBuffer.add(5);
        indexBuffer.add(6);
        // zelená úsečka
        indexBuffer.add(7);
        indexBuffer.add(8);

        elementBuffer.add(new Element(ElementType.TRIANGLE, 0, 6));
        elementBuffer.add(new Element(ElementType.LINE, 6, 4));

        renderer.draw(elementBuffer, indexBuffer, vertexBuffer);

        panel.repaint(); // pouze pro debug
    }

//    private void createScene2() {
//        // 2 modré vrcholy
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 0, 255))); // 0 // nahoře
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 0, 255))); // 1 // dole
//        // 5 červených vrcholů
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(255, 0, 0))); // 2 // nejvíce vlevo
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(255, 0, 0))); // 3 // nejvíce dole
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(255, 0, 0))); // 4 // nejvíce vpravo
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(255, 0, 0))); // 5 // nejvíce nahoře
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(255, 0, 0))); // 6 // společný
//        // 2 zelené vrcholy
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 255, 0))); // 7 // vpravo
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 255, 0))); // 8 // vlevo
//
//        // dolní trojúhleník
//        indexBuffer.add(2);
//        indexBuffer.add(3);
//        indexBuffer.add(6);
//
//        // modrá úsečka
//        indexBuffer.add(0);
//        indexBuffer.add(1);
//        // zelená úsečka
//        indexBuffer.add(7);
//        indexBuffer.add(8);
//
//        // horní trojúhleník
//        indexBuffer.add(4);
//        indexBuffer.add(5);
//        indexBuffer.add(6);
//
//        elementBuffer.add(new Element(ElementType.LINE, 3, 4));
//        elementBuffer.add(new Element(ElementType.TRIANGLE, 0, 3));
//        elementBuffer.add(new Element(ElementType.TRIANGLE, 7, 3));
//    }

    private void initMatrices() {
        model = new Mat4Identity();

        Vec3D e = new Vec3D(0, -5, 2);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-20));

        projection = new Mat4PerspRH(
                Math.PI / 3,
                imageRaster.getHeight() / (float) imageRaster.getWidth(),
                0.5,
                50
        );
    }

    private void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getX());
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println(e.isControlDown());
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode() == KeyEvent.VK_ENTER);
            }
        });
    }

    private void display() {
        imageRaster.clear();

        panel.repaint();
    }

}
