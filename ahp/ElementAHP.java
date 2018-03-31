import java.util.LinkedList;

class ElementAHP {
    String name;
    LinkedList<LinkedList<Double>> matrix;
    ElementAHP parent;
    LinkedList<ElementAHP> children;
    Integer level;
    LinkedList<Double> rank;
}
