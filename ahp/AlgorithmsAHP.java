import org.jblas.ComplexDouble;
import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class AlgorithmsAHP {
    static LinkedList<Double> rankMethod(LinkedList<LinkedList<Double>> matrix, Integer number) {
        switch (number) {
            case 1:
                return normalize(geometricMeanMethod(matrix));
            case 2:
                return normalize(eigenvalueMethod(matrix));
        }
        return null; // Error
    }

    private static LinkedList<Double> normalize(LinkedList<Double> vector) {
        Double normalizationTerm = 0.0;
        for(Double val : vector) {
            normalizationTerm += val;
        }
        LinkedList<Double> result = new LinkedList<>();
        for(Double val : vector) {
            result.add(val/normalizationTerm);
        }
        return result;
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


    private static LinkedList<Double> eigenvalueMethod(LinkedList<LinkedList<Double>> matrix){
        double[][] array = new double[matrix.size()][matrix.size()];
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.size(); j++) {
                array[i][j] = matrix.get(i).get(j);
            }
        }
        DoubleMatrix doubleMatrix = new DoubleMatrix(array);
        List<Double> principalEigenvector = getPrincipalEigenvector(doubleMatrix);
        LinkedList<Double> result = new LinkedList<>();
        result.addAll(principalEigenvector);
        return result;
    }

    private static List<Double> getPrincipalEigenvector(DoubleMatrix matrix) {
        int maxIndex = getMaxIndex(matrix);
        ComplexDoubleMatrix eigenVectors = Eigen.eigenvectors(matrix)[0];
        return getEigenVector(eigenVectors, maxIndex);
    }

    private static int getMaxIndex(DoubleMatrix matrix) {
        ComplexDouble[] doubleMatrix = Eigen.eigenvalues(matrix).toArray();
        int maxIndex = 0;
        for (int i = 0; i < doubleMatrix.length; i++){
            double newnumber = doubleMatrix[i].abs();
            if ((newnumber > doubleMatrix[maxIndex].abs())){
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static List<Double> getEigenVector(ComplexDoubleMatrix eigenvector, int columnId) {
        ComplexDoubleMatrix column = eigenvector.getColumn(columnId);

        List<Double> values = new ArrayList<Double>();
        for (ComplexDouble value : column.toArray()) {
            values.add(value.abs()  );
        }
        return values;
    }

}
