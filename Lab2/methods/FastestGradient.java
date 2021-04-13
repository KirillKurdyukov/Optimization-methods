package methods;


import java.util.ArrayList;
import java.util.function.Function;

import static methods.OneDimensionalOptimization.*;

public class FastestGradient {
    public static ArrayList<VectorNumbers> vectors = new ArrayList<>();
    public static Double run(double eps,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function) {
        double alpha;
        while(gradient.module(x) >= eps) {
            final VectorNumbers X = x;
            vectors.add(x);
            alpha = methodGoldenRatio(a -> function.apply(gradient.evaluate(X).multiplyConst(-1 * a).add(X)), eps, 1, eps);
            if (alpha * gradient.module(X) < eps) {
                return function.apply(X);
            }
            x = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
        }
        return function.apply(x);
    }

}