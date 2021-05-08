package methods;


import engine.Mode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FastestGradient {
    public static ArrayList<VectorNumbers> vectors = new ArrayList<>();
    private static int numMethod = 3;

    public static void setNumMethod(int i) {
        numMethod = i;
    }

    public static List<Method> methods = Arrays
            .stream(OneDimensionalOptimization
                    .class.getMethods())
            .filter(a -> Modifier
                    .isStatic(a.getModifiers()))
            .collect(Collectors.toList());

    public static Double run(double eps,
                             Gradient gradient,
                             VectorNumbers x,
                             Function<VectorNumbers, Double> function,
                             double L, Mode mode) throws InvocationTargetException, IllegalAccessException {
        vectors.clear();
        int countIteration = 0;
        double alpha;
        while (gradient.module(x) >= eps) {
            final VectorNumbers X = x;
            vectors.add(x);
            alpha = OneDimensionalOptimization.methodByMod(a -> function.apply(gradient.evaluate(X).
                    multiplyConst(-1 * a).add(X)), 0, L / 2, eps, mode);
            if (countIteration == 1000) {
                System.out.println("more than 1000 iterations!");
                return function.apply(X);
            }
            countIteration++;
            x = gradient.evaluate(x).multiplyConst(-1 * alpha).add(x);
        }
        vectors.add(x);
        System.out.println(countIteration);
        return function.apply(x);
    }

    public static void run(Function<VectorNumbers, Double> function, Gradient gradient, double L, Mode mode, Double eps) throws InvocationTargetException, IllegalAccessException {
        vectors.clear();
        run(eps, gradient, new VectorNumbers(List.of(0d, 0d)), function, L, mode);

    }

}