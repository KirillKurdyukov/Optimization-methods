import engine.Mode;
import methods.FastestGradient;
import methods.Gradient;
import methods.VectorNumbers;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Tester {
    public static Gradient gradient1 = methods.Tester.gradient3;
    public static double[][] hessian1 = new double[][]{{20d, -1d}, {-1d, 2d}};
    public static Function<VectorNumbers, Double> function1 = methods.Tester.function3;
    public static double ans1 = 212.0 / 39;

    private void testingFastestGradient() {
        for (double eps = 0.0001; eps >= 0.000000001; eps /= 10) {
            for (double x = 0; x < 3; x++)
                for (double y = 0; y < 3; y++)
                    System.out.println(eps + " | " + ans1 + " == " + function1.apply(new VectorNumbers(ClassicNewton.method(new HessianQuadratic(hessian1), gradient1, new double[]{x, y}, eps))));
        }
    }

    public static void main(String[] args) {
        Tester tester = new Tester();
        tester.testingFastestGradient();
    }
}
