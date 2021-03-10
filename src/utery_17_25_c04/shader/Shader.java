package utery_17_25_c04.shader;

@FunctionalInterface
public interface Shader<V, C> {

    C shade(V v);

}
