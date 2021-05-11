import methods.VectorNumbers;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileMatrix implements Matrix {
    private final double[] matrix;
    private final int[] pointers;

    public ProfileMatrix(double[][] matrix) {
        ArrayList<Double> elements = new ArrayList<>();
        ArrayList<Integer> pointers = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (matrix[i][j] != 0) {
                    elements.add(matrix[i][j]);
                    if (pointers.size() == i) {
                        pointers.add(j);
                    }
                }
            }
        }
        this.matrix = elements.stream().mapToDouble(i -> i).toArray();
        this.pointers = pointers.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public int size() {
        return pointers.length;
    }

    private int getPointer(int i, int j) {
        int pos = 0;
        for (int k = 0; k < i; k++) {
            pos += (k - pointers[k]) + 1;
        }
        return pos + j;
    }

    private double getElement(int i, int j) {
        return matrix[getPointer(i, j)];
    }

    @Override
    public double get(int i, int j) {
        if (pointers[i] <= j && j <= i) {
            return getElement(i, j);
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        ProfileMatrix m = new ProfileMatrix(new double[][]{{1, 1, 1}, {0, 2, 2}, {3, 3, 3}});
        System.out.println(Arrays.toString(m.matrix) + "  " + Arrays.toString(m.pointers));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m.set(i, j, 6);
            }
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(m.get(i, j) + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void set(int i, int j, double element) {
        if (pointers[i] > j || j > i) {
            return;
        }
        matrix[getPointer(i, j)] = element;
    }

    @Override
    public void setFreeVector(VectorNumbers b) {

    }

    @Override
    public double getFreeVector(int i) {
        return 0;
    }

    @Override
    public void setFreeVectorNum(int i, double el) {

    }

    @Override
    public void swapRow(int i, int j) {

    }
}
