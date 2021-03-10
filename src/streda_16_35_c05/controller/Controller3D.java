package streda_16_35_c05.controller;

import streda_16_35_c05.model.Element;
import streda_16_35_c05.model.TopologyType;
import streda_16_35_c05.model.Vertex;
import streda_16_35_c05.rasterize.Raster;
import streda_16_35_c05.renderer.GPURenderer;
import streda_16_35_c05.renderer.RendererZBuffer;
import streda_16_35_c05.shader.BasicColorShader;
import streda_16_35_c05.view.Panel;
import transforms.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
        vertexBuffer.add(new Vertex(new Point3D(.5, .0, .9), new Col(255, 0, 0))); // 0 // nejvíce vlevo
        vertexBuffer.add(new Vertex(new Point3D(.7, .7, .9), new Col(255, 120, 0))); // 1 // nejvíce dole
        vertexBuffer.add(new Vertex(new Point3D(.0, .5, .3), new Col(255, 255, 0))); // 2 // společný
        vertexBuffer.add(new Vertex(new Point3D(.3, .8, .5), new Col(0, 255, 0))); // 3 // nejvíce vpravo
        vertexBuffer.add(new Vertex(new Point3D(.1, .2, 1), new Col(0, 255, 120))); // 4 // nejvíce nahoře
        vertexBuffer.add(new Vertex(new Point3D(.7, .3, .2), new Col(0, 255, 255))); // 4 // nejvíce nahoře

        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(1);

        indexBuffer.add(3);
        indexBuffer.add(4);
        indexBuffer.add(5);

        elementBuffer.add(new Element(TopologyType.TRIANGLE, 0, 6));

//        renderer.setShader(new Shader<Vertex, Col>() {
//            @Override
//            public Col shade(Vertex c) {
//                return c.getColor();
//            }
//        });
//        renderer.setShader(Vertex::getColor);
//
//        renderer.setShader(new Shader<Vertex, Col>() {
//            @Override
//            public Col shade(Vertex c) {
//                return new Col(0, 255, 0);
//            }
//        });
//        renderer.setShader(v -> new Col(0, 255, 0));
//
//        final Shader<Vertex, Col> shader = v -> new Col(255, 255, 0);
//        final Shader<Vertex, Col> shader = Vertex::getColor;
//        renderer.setShader(shader);
//
//        renderer.setShader(v -> {
//            double remainder = Math.round(v.getY()) % 2;
//            return remainder == 0 ? new Col(255, 0, 0) : new Col(255, 255, 255);
//            return new Col(v.getZ(), v.getZ(), v.getZ());
//        });

//        renderer.setShader(Vertex::getColor);
        renderer.setShader(new BasicColorShader());
        renderer.draw(elementBuffer, indexBuffer, vertexBuffer);
        panel.repaint(); // pouze pro debug
    }

//    private void createScene() {
//        // 5 červených vrcholů
//        vertexBuffer.add(new Vertex(new Point3D(200, 200, 0), new Col(255, 0, 0))); // 0 // nejvíce vlevo
//        vertexBuffer.add(new Vertex(new Point3D(600, 350, 0), new Col(255, 0, 0))); // 1 // nejvíce dole
//        vertexBuffer.add(new Vertex(new Point3D(1, 5, 3), new Col(255, 0, 0))); // 2 // společný
//        vertexBuffer.add(new Vertex(new Point3D(3, 4, -5), new Col(255, 0, 0))); // 3 // nejvíce vpravo
//        vertexBuffer.add(new Vertex(new Point3D(-1, -2, -10), new Col(255, 0, 0))); // 4 // nejvíce nahoře
//        // 2 modré vrcholy
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 0, 255))); // 5 // nahoře
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 0, 255))); // 6 // dole
//        // 2 zelené vrcholy
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 255, 0))); // 7 // vpravo
//        vertexBuffer.add(new Vertex(new Point3D(), new Col(0, 255, 0))); // 8 // vlevo
//
//        // dolní trojúhleník
//        indexBuffer.add(0);
//        indexBuffer.add(2);
//        indexBuffer.add(1);
//        // horní trojúhleník
//        indexBuffer.add(2);
//        indexBuffer.add(4);
//        indexBuffer.add(3);
//
//        // modrá úsečka
//        indexBuffer.add(5);
//        indexBuffer.add(6);
//        // zelená úsečka
//        indexBuffer.add(7);
//        indexBuffer.add(8);
//
//        elementBuffer.add(new Element(TopologyType.TRIANGLE, 0, 6));
//        elementBuffer.add(new Element(TopologyType.LINE, 6, 4));
//
//        renderer.draw(elementBuffer, indexBuffer, vertexBuffer);
//
//        panel.repaint(); // pouze pro debug
//    }
//
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

    private synchronized void display() {
        renderer.clear();
        // TODO draw

        // set model, view, projection, ... shader
        // draw(..)
        panel.repaint();
    }

}
