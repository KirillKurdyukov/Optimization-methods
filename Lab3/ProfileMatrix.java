import methods.VectorNumbers;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileMatrix implements Matrix {
    private final double[] diag;
    private final double[] al;
    private final double[] au;
    private final int[] ial;
    private final int[] iau;

    public ProfileMatrix(double[][] matrix) {
        diag = new double[matrix.length];
        ArrayList<Double> al1 = new ArrayList<>();
        ArrayList<Integer> ial1 = new ArrayList<>();
        ArrayList<Double> au1 = new ArrayList<>();
        ArrayList<Integer> iau1 = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            diag[i] = matrix[i][i];
            for (int j = 0; j < i; j++) {
                al1.add(matrix[i][j]);
                au1.add(matrix[j][i]);
            }
            ial1.add(al1.size());
            iau1.add(au1.size());
        }
        al = al1.stream().mapToDouble(i -> i).toArray();
        au = au1.stream().mapToDouble(i -> i).toArray();
        ial = ial1.stream().mapToInt(i -> i).toArray();
        iau = iau1.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public int size() {
        return diag.length;
    }

    private double getElement(int i, int j) {
        return 0;
    }

    @Override
    public double get(int i, int j) {
        return 0;
    }

    public static void main(String[] args) {
        ProfileMatrix m = new ProfileMatrix(new double[][]{{1, 1, 1}, {0, 2, 2}, {3, 3, 3}});
        System.out.println(Arrays.toString(m.diag));
        System.out.println(Arrays.toString(m.al));
        System.out.println(Arrays.toString(m.au));
        System.out.println(Arrays.toString(m.ial));
        System.out.println(Arrays.toString(m.iau));

    }

    @Override
    public void set(int i, int j, double element) {
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
