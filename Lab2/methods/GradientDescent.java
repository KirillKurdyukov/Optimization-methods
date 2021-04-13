package methods;

import java.util.ArrayList;
import java.util.function.Function;

public class GradientDescent {

    public static ArrayList<VectorNumbers> vectors = new ArrayList<>();

    public static Double run(double eps,
                             double alpha,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function) {
        int countIteration = 0;
        VectorNumbers y;
        double module = gradient.module(x);
        while (module > eps) {
            vectors.add(x);
            VectorNumbers gradientVal = gradient.evaluate(x);
            y = gradientVal.multiplyConst(-1 * alpha / module).add(x);
            while (function.apply(y) >= function.apply(x)) {
                if (countIteration == 1000)
                    return function.apply(x);
                alpha = alpha / 2;
                y = gradientVal.multiplyConst(-1 * alpha / module).add(x);
                countIteration++;
            }
            x = y;
            module = gradient.module(x);
        }
        return function.apply(x);
    }
}