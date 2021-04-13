package methods;

import java.util.List;
import java.util.function.Function;

public class GradientDescent {

    public static Double run(double eps,
                             double alpha,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function) {
        VectorNumbers y;
        while(gradient.module(x) > eps) {
            y = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
            while(function.apply(y) >= function.apply(x)) {
                Double module = gradient.module(x);
                alpha = alpha / 2;
                if (alpha * module < eps) {
                    return function.apply(x);
                }
                y = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
            }
            x = y;
        }
        return function.apply(x);
    }

}
