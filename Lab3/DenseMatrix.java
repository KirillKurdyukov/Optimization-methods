import methods.Vector;
import methods.VectorNumbers;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DenseMatrix implements Matrix {

    private final int size;
    private final double[][] matrix;
    private VectorNumbers freeVector;

    public DenseMatrix(int size) {
        this.size = size;
        this.matrix = new double[size][size];
    }

    public void setFreeVector(VectorNumbers b) {
        this.freeVector = b;
    }

    @Override
    public double getFreeVector(int i) {
        return freeVector.get(i);
    }

    @Override
    public void setFreeVectorNum(int i, double el) {
        freeVector.set(i ,el);
    }

    @Override
    public void swapRow(int i, int j) {
        double [] temp = matrix[i];
        matrix[i] = matrix[j];
        matrix[j] = temp;
    }


    public double get(int i, int j) {
        return matrix[i][j];
    }

    public void set(int i, int j, double element) {
        matrix[i][j] = element;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return Arrays.stream(matrix)
                .map(row -> Arrays.stream(row)
                        .mapToObj(Double::toString)
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));
    }

}
