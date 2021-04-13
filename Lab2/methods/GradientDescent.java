package methods;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GradientDescent {
    public static ArrayList<VectorNumbers> vectors = new ArrayList<>();

    public static Double run(double eps,
                             double alpha,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function) {
        VectorNumbers y;
        while (gradient.module(x) >= eps) {
            vectors.add(x);
            y = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
            while (function.apply(y) >= function.apply(x)) {
                alpha = alpha / 2;
                y = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
            }
            x = y;
        }
        return function.apply(x);
    }

    public static void main(String[] args) {
        Function<VectorNumbers, Double> function = (VectorNumbers x) ->
                (x.get(0) - 1) * (x.get(0) - 1) + x.get(1) * x.get(1);
        Function<VectorNumbers, Double> dx = (VectorNumbers x) ->
                x.get(0) * 2 - 2;
        Function<VectorNumbers, Double> dy = (VectorNumbers x) ->
                x.get(1) * 2;
        Gradient gradient = new Gradient(List.of(dx, dy));
        VectorNumbers x = new VectorNumbers(List.of(200.0, 200.0));

        System.out.println(run(0.00000001, 16, gradient, x, function));
    }
}
