package hesse;

import research.Test;

import java.util.List;
import java.util.function.Function;

public class MHesse extends Hesse {
    public MHesse(List<List<Function<double[], Double>>> matrix) {
        super(matrix);
    }

    /**
     * Для него не нужна матрица, т.к. есть формула
     */
    public MHesse() {}

    @Override
    public double[][] evaluate(double[] v) {
        final int n = v.length;
        double[][] res = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = Test.rozdidj.apply(new Test.Pair(i, j), v);
            }
        }
        return res;
    }
}
