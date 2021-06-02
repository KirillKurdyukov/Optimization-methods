package research;

import fourthLab.derivative.Gradient;
import fourthLab.derivative.MarkwardtGradient;
import fourthLab.derivative.NormalGradient;
import fourthLab.hesse.Hesse;
import fourthLab.hesse.MHesse;
import fourthLab.method.*;
import method.NewtoneMethod;
import thirdLab.exception.NoExactSolutionException;
import thirdLab.exception.NoSolutionException;
import thirdLab.matrix.Matrix;
import thirdLab.matrix.StandardMatrix;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Test {
    //copy-pasted from secondLab.Tester
    //need to be refactored
    private static double[] test(NewtoneMethod method, Function<double[], Double> function, Gradient gradient, double[] point) {
        return method.run(gradient, function, point);
    }

    //f(x) = 100(y − x^2 )^2 + (1 − x)^2
    public static Function<double[], Double> function1 = x -> Math.pow(100 * (x[1] - x[0]*x[0]), 2) + Math.pow(1 - x[0], 2);
    public static BiFunction<Integer, double[], Double> derivative1 = (a, b) -> switch (a) {
        case 0 -> 2 * (200 * Math.pow(b[0], 3) - 200 * b[0] * b[1] + b[0] - 1);
        case 1 -> 200 * (b[1] - Math.pow(b[0], 2));
        default -> throw new RuntimeException("No such coord in derivative");
    };

    public static Matrix gese1 = new StandardMatrix(new double[][]{
            {1, 2},
            {3, 4} });
    public static final Function<double[], Double> func2 = a -> a[0] * a[0] + 0.5 * a[1] * a[1] - a[0] * a[1];
    public static final Map<Integer, Function<double[], Double>> derivative2 = Map.of(0, x->2 * x[0] - x[1], 1, x->x[1] - x[0]);
    public static final String func2String = "x^2+0.5*y^2-x*y";
    public static final double[][] matrix2 = new double[][]{{2, -1},{-1, 1}};
    public static final double[] bVector2 = new double[]{0, 0};

    // Начальная точка 1 5
    public static final Function<double[], Double> func4 = a -> 64 * a[0] * a[0] + 126 * a[0] * a[1] + 64 * a[1] * a[1] + 10 * a[0] + 10 * a[1] - 187;
    public static final String func4String = "64*x*x + 126*x*y + 64*y*y + 10*x + 10*y - 187";
    public static final Map<Integer, Function<double[], Double>> derivative4 = Map.of(0, x -> 128 * x[0] + 126 * x[1] + 10, 1, x -> 126 * x[0] + 128 * x[1] + 10);
    public static final BiFunction<Integer, double[], Double> derivative4bf = (i, x) -> switch (i) {
        case 0 -> 128 * x[0] + 126 * x[1] + 10;
        case 1 -> 126 * x[0] + 128 * x[1] + 10;
        default -> throw new RuntimeException("No such coord in derivative");
    };
    public static final double[][] matrix4 = new double[][]{{128, 126},{126, 128}};
    public static final double[] bVector4 = new double[]{10, 10};
    public static final Function<double[], Double> gesseFunc41 = a -> 128d;
    public static final Function<double[], Double> gesseFunc42 = a -> 126d;
    public static final List<List<Function<double[], Double>>> gesseMatrix4 = List.of(
            List.of(gesseFunc41, gesseFunc42),
            List.of(gesseFunc42, gesseFunc41)
    );
    public static final Hesse HESSE_4 = new Hesse(gesseMatrix4);

    // Начальная точка4 5
    public static final Function<double[], Double> func5 = a -> 13 * a[0] * a[0] - 37 * a[0] * a[1] + 70 * a[1] * a[1] + 13 * a[1] - 56;
    public static final String func5String = "13*x*x - 37*x*y + 70*y*y + 13*y - 56";
    public static final Map<Integer, Function<double[], Double>> derivative5 = Map.of(0, x -> 26 * x[0] - 37 * x[1], 1, x -> -37 * x[0] + 140 * x[1] + 13);
    public static final double[][] matrix5 = new double[][]{{26, -37},{-37, 140}};
    public static final double[] bVector5 = new double[]{0, 13};

    // Начальная точка -1 2
    public static final Function<double[], Double> func6 = a -> 5 * a[0] * a[0] - 12 * a[0] * a[1] + 11 * a[1] * a[1] + 6 * a[0] - 13 * a[1] - 131;
    public static final String func6String = "5*x*x - 12*x*y + 11*y*y + 6*x - 13*y - 131";
    public static final Map<Integer, Function<double[], Double>> derivative6 = Map.of(0, x -> 10 * x[0] - 12 * x[1] + 6, 1, x -> -12 * x[0] + 22 * x[1] - 13);
    public static final double[][] matrix6 = new double[][]{{10, -12},{-12, 22}};
    public static final double[] bVector6 = new double[]{6, -13};



    public static final Function<double[], Double> func3 = a -> 1 - Math.exp(-(10 * a[0] * a[0] + a[1] * a[1]));
    public static final Map<Integer, Function<double[], Double>> derivative3 = Map.of(
            0, x -> 20 * x[0] * Math.exp(-10 * x[0] * x[0] - x[1] * x[1]),
            1, x -> 2 * x[1] * Math.exp(-10 * x[0] * x[0] - x[1] * x[1]));
    public static final String func3String = "1-2.7^(-1*(10*x^2+y^2))";

    public static final Function<double[], Double> func7 = a -> -a[1] * Math.pow(a[0], 1d/5) + 2 * a[1] * a[1] + a[0] - 14 * a[1];
    public static final String func7String = "-x2*x1^(1/5) + 2*x2^2 + x1 - 14*x2";
    public static final BiFunction<Integer, double[], Double> derivative7 = (i, x) -> switch(i) {
        case 0 -> 1 - x[1] / (5 * Math.pow(x[0], 4d/5));
        case 1 -> -Math.pow(x[0], 1d/5) + 4 * x[1] - 14;
        default -> 0d;
    };
    public static final Function<double[], Double> gesseFunc7dxdx = a -> (4 * a[1]) / (25 * Math.pow(a[0], 9d/5));
    public static final Function<double[], Double> gesseFunc7dxdy = a -> -1 / (5 * Math.pow(a[0], 4d/5));
    public static final Function<double[], Double> gesseFunc7dydy = a -> 4d;
    public static final List<List<Function<double[], Double>>> gesseMatrix7 = List.of(
            List.of(gesseFunc7dxdx, gesseFunc7dxdy),
            List.of(gesseFunc7dxdy, gesseFunc7dydy)
    );
    public static final Hesse HESSE_7 = new Hesse(gesseMatrix7);

    /**
     * Данную функцию по теоретическим причинам не может решить базовый метод Ньютона
     */
    public static final Function<double[], Double> func8 = a -> 4 + Math.pow(a[0] * a[0] + a[1] * a[1], 1d/3);
    public static final String func8String = "4 + (x1^2 + x2^2)^(1/3)";
    public static final BiFunction<Integer, double[], Double> derivative8 = (i, x) -> switch(i) {
        case 0 ->
                (2 * x[0]) / (3 * Math.pow(x[0] * x[0] + x[1] * x[1], 2d/3));
        case 1 ->
            (2 * x[1]) / (3 * Math.pow(x[0] * x[0] + x[1] * x[1], 2d/3));
        default -> 0.0;
    };
    public static final Function<double[], Double> gesseFunc8dxdx = a -> -(2 * (a[0] * a[0] - 3 * a[1] * a[1])) / (9 * Math.pow(a[0] * a[0] + a[1] * a[1], 5d/3));
    public static final Function<double[], Double> gesseFunc8dxdy = a -> -(8 * a[0] * a[1]) / (9 * Math.pow(a[0] * a[0] + a[1] * a[1], 5d/3));
    public static final Function<double[], Double> gesseFunc8dydx = a -> -(8 * a[0] * a[1]) / (9 * Math.pow(a[0] * a[0] + a[1] * a[1], 5d/3));
    public static final Function<double[], Double> gesseFunc8dydy = a -> -(2 * (a[1] * a[1] - 3 * a[0] * a[0])) / (9 * Math.pow(a[0] * a[0] + a[1] * a[1], 5d/3));
    public static final List<List<Function<double[], Double>>> gesseMatrix8 = List.of(
            List.of(gesseFunc8dxdx, gesseFunc8dxdy),
            List.of(gesseFunc8dydx, gesseFunc8dydy)
    );
    public static final Hesse HESSE_8 = new Hesse(gesseMatrix8);

    private static double sqr(double a) {
        return a * a;
    }
    private static final Function<double[], Double> rozenbrok = v -> {
        double res = 0;
        for (int i = 0; i < v.length - 1; i++) {
            res += 100*sqr(v[i] - sqr(v[i + 1])) + sqr(1 - v[i]);
        }
        return res;
    };

    /*public static final BiFunction<Integer, double[], Double> rozDerivative = (i, v) -> switch(i) {
        case 0 -> 202 * v[0] - 200 * sqr(v[1]) - 2;
        case 1 -> (202 - 400 * v[0]) * v[1] + 400 * v[1] * sqr(v[1]) - 200 * sqr(v[2]) - 2;
        case 2 -> 400 * v[2] * (sqr(v[2]) - v[1]);
        default -> throw new IllegalStateException("Unexpected value: " + i);
    };*/

    /**
     * Производная будет зависеть от длины x
     */
    public static final BiFunction<Integer, double[], Double> rozDerivative = (i, x) -> {
        if (i < 0 || i >= x.length) {
            throw new IllegalStateException("Unexpected value: " + i);
        }
        if (i == 0) {
            return -400 * x[i + 1] * x[i] + 400 * x[i] * sqr(x[i]) - 2 + 2 * x[i];
        } else if (i == x.length - 1) {
            return 200 * x[i] - 200 * sqr(x[i - 1]);
        }
        return -400 * x[i + 1] * x[i] + 400 * x[i] * sqr(x[i]) - 2 + 202 * x[i] - 200 * sqr(x[i - 1]);
    };

    public static final Function<double[], Double> rozdada = v -> 202d;
    public static final Function<double[], Double> rozdadb = v -> -400 * v[1];
    public static final Function<double[], Double> rozdadc = v -> 0d;
    public static final Function<double[], Double> rozdbdb = v -> 400 * (3 * sqr(v[1]) - v[0]) + 202;
    public static final Function<double[], Double> rozdbdc = v -> -400 * v[2];
    public static final Function<double[], Double> rozdcdc = v -> 400 * (3*sqr(v[2]) - v[1]);
    public static final MHesse rozgesse = new MHesse(List.of(
            List.of(rozdada, rozdadb, rozdadc),
            List.of(rozdbdb, rozdbdc),
            List.of(rozdcdc)
    ));

    /**
     * Вместо тысячи функций
     */
    public static final BiFunction<Pair, double[], Double> rozdidj = (index, x) -> {
        int i = index.i;
        int j = index.j;
        if (i < 0 || i >= x.length || j < 0 || j >= x.length) {
            throw new IllegalStateException("Unexpected value: " + i);
        }
        double res = 0;
        if (i == j) {
            if (i < x.length - 1) {
                res += -400 * x[i + 1] + 1200 * sqr(x[i]) + 2;
            }
            if (i > 0) {
                res += 200;
            }
        } else if (j == i + 1) {
            res += -400 * x[i];
        } else if (j == i - 1) {
            res += -400 * x[i - 1];
        }
        return res;
    };

    private static void testFunction(Function<double[], Double> function, Gradient gradient, Hesse H, double[] point) {
        List<NewtoneMethod> methods = List.of(
        new Markwardt(H),
        new KvasiNewton(),
        new BaseNewtonMethod(H),
        new OneDimMinNewtonMethod(H),
        new CoolNewtonMethod(H)
        );
        double[][] res = new double[methods.size()][];
        for (int i = 0; i < methods.size(); i++) {
            res[i] = new double[]{0};
            try {
                res[i] = test(methods.get(i), function, gradient, point);
            } catch (NoSolutionException | NoExactSolutionException e) {
                System.out.println(methods.get(i).getClass().getSimpleName() + " can't solve this");
            }
        }
        for (int i = 0; i < res.length; i++) {
            System.out.print(methods.get(i).getClass().getSimpleName() + ": ");
            for (double y : res[i]) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        //testFunction(func4, new NormalGradient(derivative4bf), HESSE_4, new double[]{2, 3});
        //testFunction(func8, new NormalGradient(derivative8), HESSE_8, new double[]{2, 3});
        testFunction(func7, new NormalGradient(derivative7), HESSE_7, new double[]{2, 3});
        /*double[] rozpoint = new double[100];
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            rozpoint[i] = r.nextFloat() % 15;
        }
        testFunction(rozenbrok, new MarkwardtGradient(rozDerivative), rozgesse, rozpoint);*/
    }

    public static class Pair {
        public final int i, j;

        public Pair(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
}
