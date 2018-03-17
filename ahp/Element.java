import java.util.LinkedList;

public class Element {
    String name;
    LinkedList<LinkedList<Double>> matrix;
    Element parent;
    LinkedList<Element> children;
    Integer level;
}
