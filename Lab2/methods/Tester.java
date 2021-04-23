package methods;

import engine.Mode;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tester {
    private static final double ansForFunction1 = -23799.0 / 127;
    private static final double ansForFunction2 = -331103.0 / 394;
    private static final double ansForFunction3 = 212.0 / 39;

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

    public static Function<VectorNumbers, Double> function3 = v -> {
        double x = v.get(0);
        double y = v.get(1);
        return 10 * x * x -
                x * y +
                y * y -
                5 * x +
                3 * y +
                8;
    };

    public static SquareMatrix matrix1 = new SquareMatrix(List.of(new VectorNumbers(List.of(128d, 126d)),
            new VectorNumbers(List.of(126d, 128d)))
    );

    public static double L1 = 254;

    public static VectorNumbers b1 = new VectorNumbers(List.of(-10d, 30d));

    public static SquareMatrix matrix2 = new SquareMatrix(List.of(new VectorNumbers(List.of(198d, 196d)),
            new VectorNumbers(List.of(196d, 198d))));

    public static double L2 = 394;

    public static VectorNumbers b2 = new VectorNumbers(List.of(-95d, -9d));

    public static SquareMatrix matrix3 = new SquareMatrix(List.of(new VectorNumbers(List.of(20d, -1d)),
            new VectorNumbers(List.of(-1d, 2d))));

    public static double L3 = Math.sqrt(82) + 11;

    public static VectorNumbers b3 = new VectorNumbers(List.of(-5d, 3d));

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

    public static Gradient gradient3 = new Gradient(List
            .of(v -> {
                double x = v.get(0);
                double y = v.get(1);
                return 20 * x - y - 5;
            }, v -> {
                double x = v.get(0);
                double y = v.get(1);
                return -x + 2 * y + 3;
            })
    );

    private void testingGradientDescent(Gradient g, Function<VectorNumbers, Double> f, double ans) {
        for (double eps = 0.0001; eps >= 0.000000001; eps /= 10) {
            for (int alpha = 10; alpha <= 100; alpha += 10) {
                for (double x = 0; x < 3; x++)
                    for (double y = 0; y < 3; y++)
                        System.out.println(eps + " | " + alpha + " | " + ans + " == " + GradientDescent.run(eps,
                                alpha,
                                g,
                                new VectorNumbers(List.of(x, y)),
                                f, Mode.GOLDEN_RATIO)
                        );
            }
        }
    }

    private void testingFastestGradient(Gradient g, Function<VectorNumbers, Double> f, double ans, double L) throws InvocationTargetException, IllegalAccessException {
        for (double eps = 0.0001; eps >= 0.000000001; eps /= 10) {
            for (double x = 0; x < 3; x++)
                for (double y = 0; y < 3; y++)
                    System.out.println(eps + " | " + ans + " == " + FastestGradient.run(eps,
                            g,
                            new VectorNumbers(List.of(x, y)),
                            f,
                            L, Mode.GOLDEN_RATIO)
                    );
        }
    }

    private void testingConjugateGradient(SquareMatrix A, double ans, VectorNumbers b, Function<VectorNumbers, Double> f) {
        for (double eps = 0.0001; eps >= 0.000000001; eps /= 10) {
            for (double x = 0; x < 3; x++)
                for (double y = 0; y < 3; y++)
                    System.out.println(eps + " | " + ans + " == " + f.apply(ConjugateGradient.run(eps,
                            A,
                            new VectorNumbers(List.of(x, y)),
                            b, Mode.GOLDEN_RATIO))
                    );
        }
    }

    private void test1() {
        System.out.println("Testing method gradient descent.");
        System.out.println("Testing function one: f(x, y) = 64 * x * x + 126 * x * y + 64 * y * y - 10 * x + 30 * y + 13");
        testingGradientDescent(gradient1, function1, ansForFunction1);
        System.out.println("===========================================================================================");
        System.out.println("Testing function one: f(x, y) = 99 * x * x + 196 * x * y + 99 * y * y - 95 * x - 9 * y + 91");
        testingGradientDescent(gradient2, function2, ansForFunction2);
        System.out.println("===========================================================================================");
        System.out.println("Testing function one: f(x, y) = 10 * x * x +  y * y - 5 * x + 3 * y + 8");
        testingGradientDescent(gradient3, function3, ansForFunction3);
        System.out.println("===========================================================================================");
    }

    private void test2() throws InvocationTargetException, IllegalAccessException {
        System.out.println("Testing method fastest gradient.");
        System.out.println("Testing function one: f(x, y) = 64 * x * x + 126 * x * y + 64 * y * y - 10 * x + 30 * y + 13");
        testingFastestGradient(gradient1, function1, ansForFunction1, L1);
        System.out.println("===========================================================================================");
        System.out.println("Testing function one: f(x, y) = 99 * x * x + 196 * x * y + 99 * y * y - 95 * x - 9 * y + 91");
        testingFastestGradient(gradient2, function2, ansForFunction2, L2);
        System.out.println("===========================================================================================");
        System.out.println("Testing function one: f(x, y) = 10 * x * x +  y * y - 5 * x + 3 * y + 8");
        testingFastestGradient(gradient3, function3, ansForFunction3, L3);
        System.out.println("===========================================================================================");
    }

    private void test3() {
        System.out.println("Testing function one: f(x, y) = 64 * x * x + 126 * x * y + 64 * y * y - 10 * x + 30 * y + 13");
        testingConjugateGradient(matrix1, ansForFunction1, b1, function1);
        System.out.println("===========================================================================================");
        System.out.println("Testing function one: f(x, y) = 99 * x * x + 196 * x * y + 99 * y * y - 95 * x - 9 * y + 91");
        testingConjugateGradient(matrix2, ansForFunction2, b2, function2);
        System.out.println("===========================================================================================");
        System.out.println("Testing function one: f(x, y) = 10 * x * x +  y * y - 5 * x + 3 * y + 8");
        testingConjugateGradient(matrix3, ansForFunction3, b3, function3);
        System.out.println("===========================================================================================");
    }

    private void generation() throws InvocationTargetException, IllegalAccessException {
        for (int i = 10; i <= 10000; i *= 10) {
            System.out.println("i == " + i);
            Gradient gradientN = new Gradient(IntStream
                    .range(0, i)
                    .mapToObj(index ->
                            (Function<VectorNumbers, Double>) v -> 2 * v.get(index)
                    ).collect(Collectors.toList()));
            Function<VectorNumbers, Double> functionN = v ->
                    v.stream()
                            .map(element -> element * element)
                            .reduce(Double::sum)
                            .orElseThrow();
            System.out.println(FastestGradient.run(eps, gradientN, new VectorNumbers(IntStream.range(0, i)
                            .mapToObj(ind -> 1d)
                            .collect(Collectors.toList())),
                    functionN, 0.5d, Mode.GOLDEN_RATIO));
        }
    }

    public void test4() throws InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < 5; i++) {
            FastestGradient.setNumMethod(i);
            FastestGradient.run(eps, gradient1, new VectorNumbers(List.of(0d, 0d)), function1, L1, Mode.GOLDEN_RATIO);
        }
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Tester tester = new Tester();

        //        tester.test1();
//        System.out.println("===========================================================================================");
//        System.out.println("===========================================================================================");
//        System.out.println("===========================================================================================");
//        System.out.println("===========================================================================================");
//        tester.test2();
//        System.out.println("===========================================================================================");
//        System.out.println("===========================================================================================");
//        System.out.println("===========================================================================================");
//        System.out.println("===========================================================================================");
//        tester.test3();
        tester.generation();
    }

}
