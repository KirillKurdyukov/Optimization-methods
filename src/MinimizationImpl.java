import processing.Trie;

import java.util.ArrayList;
import java.util.function.Function;

public class MinimizationImpl {

    private final double EPS = 0.00001;
    private final double left = -6;
    private final double right = -4;
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
    public Trie methodDichotomy(Trie input) {
        double delta = EPS / 2, x1, x2;
        double l = input.getX1();
        double r = input.getX2();
        if ((r - l) / 2 > EPS) {
            x1 = (l + r) / 2 - delta;
            x2 = (l + r) / 2 + delta;
            if (fun.apply(x1) <= fun.apply(x2)) {
                r = x2;
            } else {
                l = x1;
            }
            return new Trie(false, l, r, fun.apply((l + r) / 2));
        }
        return new Trie(true, l, r, fun.apply((l + r) / 2));
    }

    /*
    Метод золотого сечения.
    Алгоритм работает корректно на интервале [l, r], если
    функция унимодальная на этом отрезке.
    За константу Ф берется пропорция золотого сечения.
    Граница высчитывается один раз.
    Значение для ответа берется из середины отрезка [l < x1; x2 < r], когда границы l, r
    не различимы первыми 4 значащими цифрами после запятой.
     */
    public double methodGoldenRatio(double l, double r) {
        final double phi = (1 + Math.sqrt(5)) / 2;
        final double resPhi = 2 - phi;
        double x1 = l + resPhi * (r - l),
                x2 = r - resPhi * (r - l),
                f1 = fun.apply(x1), f2 = fun.apply(x2);
        while ((r - l) > EPS) {
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
    Метод Фибоначчи.
    Улучшение реализации "поиска с помощью золотого сечения", так
    как высчитывание коэффициентов происходит в типах Long, а Double.
    Вычисления вспомогательного множителя происходит с помощью чисел Фибоначчи.
    Алгоритм работает корректно на интервале [l, r], если
    функция унимодальная на этом отрезке.
    Значение для ответа берется из середины отрезка [l < x1; x2 < r], когда границы l, r
    не различимы первыми 4 значащими цифрами после запятой.
     */
    public double methodFibonacciNumbers(double l, double r) {
        long n = (long) ((r - l) / EPS);
        ArrayList<Long> fib = new ArrayList<>();
        fib.add(0L);
        fib.add(1L);
        int s = -1;
        while (n > fib.get(fib.size() - 1)) {
            fib.add(fib.get(fib.size() - 1) + fib.get(fib.size() - 2));
            s++;
        }
        double cur = (double) fib.get(s - 1) / fib.get(s) * (r - l);
        double x2 = l + cur;
        double x1 = r - cur;
        double f1 = fun.apply(x1), f2 = fun.apply(x2);
        for (int i = s - 1; i > 2; i--) {
            if (f1 < f2) {
                r = x2;
                x2 = x1;
                f2 = f1;
                x1 = l + (double) fib.get(i - 2) / fib.get(i) * (r - l);
                f1 = fun.apply(x1);
            } else {
                l = x1;
                x1 = x2;
                f1 = f2;
                x2 = l + (double) fib.get(i - 1) / fib.get(i) * (r - l);
                f2 = fun.apply(x2);
            }
        }
        return fun.apply((x1 + x2) / 2);
    }

    public double getEPS() {
        return EPS;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public Function<Double, Double> getFun() {
        return fun;
    }

    public static void main(String[] args) {

        //System.out.printf("%.4f%n", minimization.methodFibonacciNumbers(-6, -4));
    }

}
