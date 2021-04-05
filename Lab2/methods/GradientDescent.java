package methods;

import java.util.function.Function;

public class GradientDescent {

    public static Double run(double eps,
                             double alpha,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function) {
        VectorNumbers y;
        while (gradient.module(x) >= eps) {
            if (function.apply(x) < eps)
                break;
            y = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
            if (function.apply(x) < function.apply(y))
                alpha /= 2;
            else
                x = y;
        }
        return function.apply(x);
    }

}
