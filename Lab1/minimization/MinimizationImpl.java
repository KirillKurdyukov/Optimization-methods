package minimization;

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
    Дельта берется из интервала (0; (r - l) / 2).
    Значение для ответа берется из середины отрезка [l; r], когда границы
    не различимы первыми 4 значащими цифрами после запятой.
     */
    public double methodDichotomy(double l, double r) {
        double delta, x1, x2;
        while ((r - l) / 2 > EPS) {
            delta = (r - l) / 4;
            x1 = (l + r) / 2 - delta;
            x2 = (l + r) / 2 + delta;
            if (fun.apply(x1) <= fun.apply(x2))
                r = x2;
            else
                l = x1;
        }
        return fun.apply((l + r) / 2);
    }

    /*
    Метод золотого сечения.
    Алгоритм работает корректно на интервале [l, r], если
    функция унимодальная на этом отрезке.
    За константу Ф берется пропорция золотого сечения.
    Значение для ответа берется из середины отрезка [l; r], когда границы
    не различимы первыми 4 значащими цифрами после запятой.
     */
    public double methodGoldenRatio(double l, double r) {
        double ratioConst = (1 + Math.sqrt(5)) / 2;
        double x1, x2;
        while ((r - l) / 2 > EPS) {
            x1 = r - (r - l) / ratioConst;
            x2 = l + (r - l) / ratioConst;
            if (fun.apply(x1) <= fun.apply(x2))
                r = x2;
            else
                l = x1;
        }
        return fun.apply((l + r) / 2);
    }

    public double methodFibonacciNumbers(double l, double r) {
        return 0;
    }

    public static void main(String[] args) {
        MinimizationImpl minimization = new MinimizationImpl((x) -> x * x + 2 * x + 1);
        System.out.printf("%.4f%n", minimization.methodDichotomy(-5, -2));
    }
}
