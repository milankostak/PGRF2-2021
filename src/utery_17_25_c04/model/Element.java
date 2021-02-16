package utery_17_25_c04.model;

public class Element {

    private final ElementType elementType;
    private final int start; // kde v index bufferu začít indexovat
    private final int count; // kolik indexů od startu se má použít

    public Element(ElementType elementType, int start, int count) {
        this.elementType = elementType;
        this.start = start;
        this.count = count;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public int getStart() {
        return start;
    }

    public int getCount() {
        return count;
    }
}
