package minimization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MinimizationImpl {

    private final double EPS = 0.00001;

    private final Function<Double, Double> fun;

    public MinimizationImpl(Function<Double, Double> fun) {
        this.fun = fun;
    }

    /*
    Метод дихотомии.
    Алгоритм работает корректно на интервале [l, r], если
    функция унимодальная на этом отрезке.
    Дельта берется EPS / 2.
    Значение для ответа берется из середины отрезка [l; r], когда границы
    не различимы первыми 4 значащими цифрами после запятой.
     */
    public double methodDichotomy(double l, double r) {
        double delta = EPS / 2, x1, x2;
        while ((r - l) / 2 > EPS) {
            x1 = (l + r) / 2 - delta;
            x2 = (l + r) / 2 + delta;
            if (fun.apply(x1) <= fun.apply(x2)) {
                r = x2;
            } else {
                l = x1;
            }
        }
        return fun.apply((l + r) / 2);
    }

    /*
    Метод золотого сечения.
    Алгоритм работает корректно на интервале [l, r], если
    функция унимодальная на этом отрезке.
    За константу Ф берется пропорция золотого сечения.
    Граница высчитывается один раз.
    Значение для ответа берется из середины отрезка [l; r], когда границы
    не различимы первыми 4 значащими цифрами после запятой.
     */
    public double methodGoldenRatio(double l, double r) {
        final double phi = (1 + Math.sqrt(5)) / 2;
        final double resPhi = 2 - phi;
        double x1 = l + resPhi * (r - l),
                x2 = r - resPhi * (r - l),
        f1 = fun.apply(x1), f2 = fun.apply(x2);
        while((r - l) > EPS) {
            if (f1 < f2) {
                r = x2;
                x2 = x1;
                f2 = f1;
                x1 = l + resPhi * (r - l);
                f1 = fun.apply(x1);
            } else {
                l = x1;
                x1 = x2;
                f1 = f2;
                x2 = r - resPhi * (r - l);
                f2 = fun.apply(x2);
            }
        }
        return fun.apply((x1 + x2) / 2);
    }

    /*

     */
    public double methodFibonacciNumbers(double l, double r) {
        ArrayList<Double> fibonacciCount = new ArrayList<>();
        fibonacciCount.add(0.0);
        fibonacciCount.add(1.0);
        return 0;
    }

    public static void main(String[] args) {
        MinimizationImpl minimization = new MinimizationImpl((x) -> x * x + 2 * x + 1);
        System.out.printf("%.4f%n", minimization.methodGoldenRatio(-5, -2));
    }
}
