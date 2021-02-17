package streda_16_35_c05.model;

public class Element {

    private final ElementType elementType;
    private final int start; // na jakém indexu v IB začít
    private final int count; // kolik indexů z IB celkem použít

    /**
     * @param elementType type of the element
     * @param start       index of the starting item in index buffer
     * @param count       how many indices to take from index buffer
     */
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
