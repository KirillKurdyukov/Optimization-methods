package methods;

import java.util.List;
import java.util.function.Function;

import static methods.OneDimensionalOptimization.methodDichotomy;

public class FastestGradient {

    public static Double run(double eps,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function) {
        VectorNumbers y;
        double alpha = 1;
        while(gradient.module(x) >= eps) {
            final VectorNumbers X = x;
            alpha = methodDichotomy(a -> function.apply(gradient.evaluate(X).multiplyConst(-1 * a).add(X)), 0, 1, eps);
            y = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
            x = y;
        }
        return function.apply(x);
    }

}
