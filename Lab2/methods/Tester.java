package methods;

import java.util.List;
import java.util.function.Function;

public class Tester {
    private static final double ansForFunction1 = -23799.0 / 127;
    private static final double ansForFunction2 = -331103.0 / 394;

    public static double eps = 0.00001d;
    public static Function<VectorNumbers, Double> function1 = v -> {
        double x = v.get(0);
        double y = v.get(1);
        return 64 * x * x +
                126 * x * y +
                64 * y * y -
                10 * x +
                30 * y +
                13;
    };

    public static Function<VectorNumbers, Double> function2 = v -> {
        double x = v.get(0);
        double y = v.get(1);
        return 99 * x * x +
                196 * x * y +
                99 * y * y -
                95 * x -
                9 * y +
                91;
    };

    public static Gradient gradient1 = new Gradient(List
            .of(v -> {
                double x = v.get(0);
                double y = v.get(1);
                return 128 * x + 126 * y - 10;
            }, v -> {
                double x = v.get(0);
                double y = v.get(1);
                return 128 * y + 126 * x + 30;
            })
    );

    public static Gradient gradient2 = new Gradient(List
            .of(v -> {
                double x = v.get(0);
                double y = v.get(1);
                return 198 * x + 196 * y - 95;
            }, v -> {
                double x = v.get(0);
                double y = v.get(1);
                return 196 * x + 198 * y - 9;
            })
    );

    private void test1() {
        for (double eps = 0.0001; eps >= 0.000000001; eps /= 10) {
            for (int alpha = 10; alpha <= 100; alpha += 10) {
                for (double x = 0; x < 3; x++)
                    for (double y = 0; y < 3; y++)
                        System.out.println(eps + " | " + alpha + " | " + ansForFunction1 + " == " + GradientDescent.run(eps,
                                alpha,
                                gradient1,
                                new VectorNumbers(List.of(x, y)),
                                function1));
            }
        }
    }


    public static void main(String[] args) {
        Tester tester = new Tester();
        tester.test1();
    }

}
