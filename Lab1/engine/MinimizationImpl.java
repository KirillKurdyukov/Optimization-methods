package engine;

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
    public ArrayList<Trie> methodDichotomy(Trie input) {
        double delta = EPS / 2, x1, x2;
        double l = input.getX1();
        double r = input.getX2();
        ArrayList<Trie> result = new ArrayList<>();
        while ((r - l) / 2 > EPS) {
            x1 = (l + r) / 2 - delta;
            x2 = (l + r) / 2 + delta;
            double d1 = r - l;
            if (fun.apply(x1) <= fun.apply(x2)) {
                r = x2;
            } else {
                l = x1;
            }
            double d2 = r - l;
            result.add(new Trie(l, r, fun.apply((l + r) / 2)));
        }
        result.add(new Trie(l, r, fun.apply((l + r) / 2)));
        return result;
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
    public ArrayList<Trie> methodGoldenRatio(Trie input) {
        final double phi = (1 + Math.sqrt(5)) / 2;
        final double resPhi = 2 - phi;
        double l = input.getX1();
        double r = input.getX2();
        double x1 = l + resPhi * (r - l),
                x2 = r - resPhi * (r - l),
                f1 = fun.apply(x1), f2 = fun.apply(x2);
        ArrayList<Trie> result = new ArrayList<>();
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
            result.add(new Trie(l, r, fun.apply((l + r) / 2)));
        }
        result.add(new Trie(l, r, fun.apply((l + r) / 2)));
        return result;
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
    public ArrayList<Trie> methodFibonacciNumbers(Trie input) {
        double l = input.getX1();
        double r = input.getX2();
        long n = (long) ((r - l) / EPS);
        ArrayList<Trie> result = new ArrayList<>();
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
            result.add(new Trie(l, r, fun.apply((l + r) / 2)));
        }
        result.add(new Trie(l, r, fun.apply((l + r) / 2)));
        return result;
    }

    /*
    Метод парабол
     */
    public ArrayList<Trie> methodParabolas(Trie input) {
        double l = input.getX1();
        double r = input.getX2();
        double fl = fun.apply(l);
        double fr = fun.apply(r);
        double x = (l + r) / 2;
        double fx = fun.apply(x);
        ArrayList<Trie> result = new ArrayList<>();
        while (r - l > EPS) {
            double u = x - (Math.pow(x - l, 2) * (fx - fr) - Math.pow(x - r, 2) * (fx - fl)) / (2 * ((x - l) * (fx - fr) - (x - r) * (fx - fl)));
            double fu = fun.apply(u);
            if (fu > fx) {
                if (u > x) {
                    r = u;
                    fr = fu;
                } else {
                    l = u;
                    fl = fu;
                }
            } else {
                if (x > u) {
                    r = x;
                    fr = fx;
                } else {
                    l = x;
                    fl = fx;
                }
                x = u;
                fx = fu;
            }
            result.add(new Trie(l, r, fun.apply((l + r) / 2)));
        }
        result.add(new Trie(l, r, fun.apply((l + r) / 2)));
        return result;
    }

    /*
    метод Брента.
     */
    public ArrayList<Trie> brent(Trie input) {
        double k = (3 - Math.sqrt(5)) / 2;
        double x, w, v;
        double a = input.getX1();
        double c = input.getX2();
        x = w = v = (a + c) / 2;
        double fx, fw, fv;
        fx = fw = fv = fun.apply(x);
        double d, e;
        d = e = c - a;
        ArrayList<Trie> result = new ArrayList<>();
        while (d > EPS) {
            double g;
            g = e;
            e = d;
            double u;
            if (!(fx == fw || fx == fv || fv == fw)) {
                u = x - (Math.pow((x - w), 2) * (fx - fv) - Math.pow((x - v), 2) * (fx - fw)) / (2 * ((x - w) * (fx - fv) - (x - v) * (fx - fw)));
                if (u >= a + EPS && u <= c - EPS && Math.abs(u - x) < g / 2) {
                    d = Math.abs(u - x);
                } else {
                    if (x < (c - a) / 2) {
                        u = a + k * (c - x);
                        d = c - x;
                    } else {
                        u = c - k * (x - a);
                        d = x - a;
                    }
                }
            } else {
                if (x < (c - a) / 2) {
                    u = x + k * (c - x);
                    d = c - x;
                } else {
                    u = x - k * (x - a);
                    d = x - a;
                }
                if (Math.abs(u - x) < EPS) {
                    u = x + Math.signum(u - x) * EPS;
                }
            }
            double fu = fun.apply(u);
            if (fu <= fx) {
                if (u >= x) {
                    a = x;
                } else {
                    c = x;
                }
                v = w;
                w = x;
                x = u;
                fv = fw;
                fw = fx;
                fx = fu;
            } else {
                if (u >= x) {
                    c = u;
                } else {
                    a = u;
                }
                if (fu <= fw || w == x) {
                    v = w;
                    w = u;
                    fv = fw;
                    fw = fu;
                } else {
                    if (fu <= fv || v == x || v == w) {
                        v = u;
                        fv = fu;
                    }
                }
            }
            result.add(new Trie(a, c, fun.apply((a + c) / 2)));
        }
        result.add(new Trie(a, c, fun.apply((a + c) / 2)));
        return result;
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
}