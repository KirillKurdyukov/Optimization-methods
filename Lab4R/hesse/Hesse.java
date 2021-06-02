package hesse;

import java.util.List;
import java.util.function.Function;

public class Hesse {
    protected List<List<Function<double[], Double>>> matrix;
    public Hesse() {}
    public Hesse(List<List<Function<double[], Double>>> matrix) {
        this.matrix = matrix;
    }
    protected double evalEl(int i, int j, double[] v) {
        return matrix.get(i).get(j).apply(v);
    }
    public double[][] evaluate(double[] v) {
        final int n = v.length;
        double[][] res = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = evalEl(i, j, v);
            }
        }
        return res;
    }
}
