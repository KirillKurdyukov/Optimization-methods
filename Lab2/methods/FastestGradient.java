package methods;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FastestGradient {
    public static ArrayList<VectorNumbers> vectors = new ArrayList<>();

    private static Double methodDichotomy(Function<Double, Double> function,
                                          double l,
                                          double r,
                                          double eps) {
        double delta = eps / 2, x1, x2;
        while ((r - l) / 2 > eps) {
            x1 = (l + r) / 2 - delta;
            x2 = (l + r) / 2 + delta;
            if (function.apply(x1) <= function.apply(x2)) {
                r = x2;
            } else {
                l = x1;
            }
        }
        return (r + l) / 2;
    }

    public static Double run(double eps,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function) {
        VectorNumbers y;
        double alpha = 1;
        while (gradient.module(x) >= eps) {
            final VectorNumbers X = x;
            vectors.add(x);
            alpha = methodDichotomy(a -> function.apply(gradient.evaluate(X).multiplyConst(-1 * a).add(X)), 0, 1, eps);
            y = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
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

        System.out.println(run(0.001, gradient, x, function));
    }
}
