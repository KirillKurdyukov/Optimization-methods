package methods;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static methods.OneDimensionalOptimization.methodGoldenRatio;
import static methods.Tester.*;

public class FastestGradient {
    public static ArrayList<VectorNumbers> vectors = new ArrayList<>();

    public static Double run(double eps,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function,
                             double L) {
        vectors.clear();
        int countIteration = 0;
        double alpha;
        while (gradient.module(x) >= eps) {
            final VectorNumbers X = x;
            vectors.add(x);
            alpha = methodGoldenRatio(a -> function.apply(gradient.evaluate(X).multiplyConst(-1 * a).add(X)), 0, L / 2, eps);
            if (countIteration == 1000) {
                System.out.println("more than 1000 iterations!");
                return function.apply(X);
            }
            countIteration++;
            x = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
        }
        vectors.add(x);
        return function.apply(x);
    }

    public static void run(Function<VectorNumbers, Double> function, Gradient gradient, double L) {
        vectors.clear();
        run(eps, gradient, new VectorNumbers(List.of(0d, 0d)), function, L);

    }

}