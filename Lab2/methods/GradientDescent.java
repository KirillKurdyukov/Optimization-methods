package methods;

import engine.Mode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GradientDescent {

    public static ArrayList<VectorNumbers> vectors = new ArrayList<>();

    public static Double run(double eps,
                             double alpha,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function, Mode mode) {
        int countIteration = 0;
        VectorNumbers y;
        double module = gradient.module(x);
        while (module > eps) {
            vectors.add(x);
            VectorNumbers gradientVal = gradient.evaluate(x);
            countIteration++;
            y = gradientVal.multiplyConst(-1 * alpha / module).add(x);
            while (function.apply(y) >= function.apply(x)) {
                if (countIteration == 1000) {
                    System.out.println("more than 1000 iterations!");
                    return function.apply(x);
                }
                alpha = alpha / 2;
                y = gradientVal.multiplyConst(-1 * alpha / module).add(x);
                countIteration++;
            }
            x = y;
            module = gradient.module(x);
        }
        vectors.add(x);
        System.out.println(countIteration);
        return function.apply(x);
    }

    public static void run(Function<VectorNumbers, Double> function, Gradient gradient, Mode mode, Double eps) {
        vectors.clear();
        run(eps, 10, gradient, new VectorNumbers(List.of(0d, 0d)), function, mode);
    }
}