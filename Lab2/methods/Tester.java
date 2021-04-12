package methods;

import java.util.List;
import java.util.function.Function;

public class Tester {
    private void test1() {
        double ansForTest1 = -23799.0 / 127;
        Function<VectorNumbers, Double> function = v -> {
            double x = v.get(0);
            double y = v.get(1);
            return 64 * x * x +
                    126 * x * y +
                    64 * y * y -
                    10 * x +
                    30 * y +
                    13;
        };

        Gradient gradient = new Gradient(List
                .of(v -> {
                    double x = v.get(0);
                    double y = v.get(1);
                    return 128 * x + 126 * y - 10;
                }, v -> {
                    double x = v.get(0);
                    double y = v.get(1);
                    return 128 * y + 126 * x + 30;
                }));
        GradientDescent.run(0.00001, 10, gradient, new VectorNumbers(List.of(0.0, 0.0)),function);
        for (double eps = 0.0001; eps >= 0.000000001; eps /= 10) {
            for (int alpha = 10; alpha <= 100; alpha+= 10) {
                for (double x = 0; x < 3; x++)
                    for (double y = 0; y < 3; y++)
                        System.out.println(ansForTest1 + " == " + GradientDescent.run(eps,
                                alpha,
                                gradient,
                                new VectorNumbers(List.of(x, y)),
                                function));
            }
        }
    }


    public static void main(String[] args) {
        Tester tester = new Tester();
        tester.test1();
    }

}
