package methods;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static methods.OneDimensionalOptimization.methodGoldenRatio;
import static methods.Tester.eps;
import static methods.Tester.gradient1;

public class FastestGradient {
    public static ArrayList<VectorNumbers> vectors = new ArrayList<>();

    public static Double run(double eps,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function) {
        vectors.clear();
        double alpha;
        while (gradient.module(x) >= eps) {
            final VectorNumbers X = x;vectors.add(x);
            alpha = methodGoldenRatio(a -> function.apply(gradient.evaluate(X).multiplyConst(-1 * a).add(X)), eps, 1, eps);
            if (alpha * gradient.module(X) < eps) {
                return function.apply(X);
            }
            x = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
        }
        return function.apply(x);
    }

    public static void run(Function<VectorNumbers, Double> function) {
        vectors.clear();
        run(eps, gradient1, new VectorNumbers(List.of(0d, 0d)), function);
    }
}