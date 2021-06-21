package derivative;

import java.util.function.BiFunction;

public class MarkwardtGradient extends AbstractGradient {
    public MarkwardtGradient(BiFunction<Integer, double[], Double> derivative) {
        super(derivative);
    }

    @Override
    public double[] getGradient(double[] point) {
        final int n = point.length;
        double[] gradient = new double[n];
        gradient[0] = derivative.apply(0, new double[]{point[0], point[1], point[2]});
        gradient[n - 1] = derivative.apply(2, new double[]{point[n - 3], point[n - 2], point[n - 1]});
        for (int i = 1; i < n - 1; i++) {
            gradient[i] = derivative.apply(1, new double[]{point[i - 1], point[i], point[i + 1]});
        }
        return gradient;
    }
}
