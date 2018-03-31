import java.util.LinkedList;

class AlgorithmsAHP {
    static LinkedList<Double> rankMethod(LinkedList<LinkedList<Double>> matrix, Integer number) {
        switch (number) {
            case 1:
                return geometricMeanMethod(matrix);
            case 2:
                return new LinkedList<Double>(); // Other method in future
        }
        return null; // Error
    }

    private static LinkedList<Double> geometricMeanMethod(LinkedList<LinkedList<Double>> matrix) {
        LinkedList<Double> rank = new LinkedList<>();
        for(int i = 0; i < matrix.size(); i++) {
            Double iloczyn = 1.0;
            for(int j = 0; j < matrix.getFirst().size(); j++) {
                iloczyn *= matrix.get(i).get(j);
            }
            rank.add(Math.pow(iloczyn, 1.0/matrix.size()));
        }
        // Normalization
        Double normalizationTerm = 0.0;

        LinkedList<Double> result = new LinkedList<>();
        for(Double element : rank) {
            normalizationTerm += element;
        }
        for(Double value : rank) {
            result.add(value/normalizationTerm);
        }
        return result;
    }
}
