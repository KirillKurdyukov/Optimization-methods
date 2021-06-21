package method;

import derivative.Gradient;

import java.util.function.Function;

public interface NewtoneMethod {
    double[] run(Gradient gradient, Function<double[], Double> function, double[] point);
}
