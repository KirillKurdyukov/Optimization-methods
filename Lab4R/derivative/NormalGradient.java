package derivative;

import java.util.function.BiFunction;

public class NormalGradient extends AbstractGradient {
    public NormalGradient(BiFunction<Integer, double[], Double> derivative) {
        super(derivative);
    }
    @Override
    public double[] getGradient(double[] point) {
        double[] gradient = new double[point.length];
        for (int i = 0; i < gradient.length; i++) {
            gradient[i] = derivative.apply(i, point);
        }
        return gradient;
    }
}
