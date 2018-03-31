import java.util.LinkedList;

public class Test {
    public static void main(String[] args) {
        LinkedList<LinkedList<Double>> matrix = new LinkedList<>();
        matrix.add(new LinkedList<>());
        matrix.add(new LinkedList<>());
        matrix.add(new LinkedList<>());
        matrix.get(0).add(1.0);
        matrix.get(0).add(0.5);
        matrix.get(0).add(0.25);
        matrix.get(1).add(2.0);
        matrix.get(1).add(1.0);
        matrix.get(1).add(0.5);
        matrix.get(2).add(4.0);
        matrix.get(2).add(2.0);
        matrix.get(2).add(1.0);
        LinkedList<Double> rank = AlgorithmsAHP.rankMethod(matrix,1);
        for(Double value : rank){
            System.out.println(value);
        }
    }
}
