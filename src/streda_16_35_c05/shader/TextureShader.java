package streda_16_35_c05.shader;

import streda_16_35_c05.model.Vertex;
import transforms.Col;

import java.awt.image.BufferedImage;

public class TextureShader implements Shader<Vertex, Col> {

    public TextureShader() {
        // load image, use BufferImage
    }

    @Override
    public Col shade(Vertex vertex) {
//        vertex.texCoord;
//
//        return new Col(image.getRGB());
        return new Col();
    }

}
