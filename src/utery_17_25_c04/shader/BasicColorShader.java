package utery_17_25_c04.shader;

import transforms.Col;
import utery_17_25_c04.model.Vertex;

public class BasicColorShader implements Shader<Vertex, Col> {

    @Override
    public Col shade(Vertex vertex) {
        return vertex.getColor();
    }

}
