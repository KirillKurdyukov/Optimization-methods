package derivative;

import java.util.function.BiFunction;
import static derivative.MatrixUtilities.*;

public abstract class AbstractGradient implements Gradient {
    protected BiFunction<Integer, double[], Double> derivative;
    public AbstractGradient(BiFunction<Integer, double[], Double> derivative) {
        this.derivative = derivative;
    }
    @Override
    public double[] getAntiGradient(double[] v) {
        return multVector(getGradient(v), -1);
    }
}
