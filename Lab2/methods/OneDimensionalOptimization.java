package methods;


import engine.Mode;

import java.util.ArrayList;
import java.util.function.Function;

public class OneDimensionalOptimization {
    public static Double methodByMod(Function<Double, Double> function,
                                     double l,
                                     double r,
                                     double eps,
                                     Mode mode) {
        switch (mode) {
            case DICHOTOMY:
                return methodDichotomy(function, l, r, eps);
            case GOLDEN_RATIO:
                return methodGoldenRatio(function, l, r, eps);
            case PARABOLAS:
                return methodParabolas(function, l, r, eps);
            case FIBONACCI_NUMBERS:
                return methodFibonacciNumbers(function, l, r, eps);
            case BRENT:
                return methodBrent(function, l, r, eps);
            default:
                return 1d;
        }

    }

    /**
     * Метод дихотомии.
     * Алгоритм работает корректно на интервале [l, r], если
     * функция унимодальная на этом отрезке.
     * Дельта берется EPS / 2.
     * Значение для ответа берется из середины отрезка [l; r], когда границы
     * не различимы первыми 4 значащими цифрами после запятой.
     */
    public static Double methodDichotomy(Function<Double, Double> function,
                                         double l,
                                         double r,
                                         double eps) {
        double delta = eps / 2, x1, x2;
        while ((r - l) / 2 > eps) {
            x1 = (l + r) / 2 - delta;
            x2 = (l + r) / 2 + delta;
            if (function.apply(x1) <= function.apply(x2)) {
                r = x2;
            } else {
                l = x1;
            }
        }
        return (r + l) / 2;
    }

    /**
     * Метод золотого сечения.
     * Алгоритм работает корректно на интервале [l, r], если
     * функция унимодальная на этом отрезке.
     * За константу Ф берется пропорция золотого сечения.
     * Граница высчитывается один раз.
     * Значение для ответа берется из середины отрезка [l < x1; x2 < r], когда границы l, r
     * не различимы первыми 4 значащими цифрами после запятой.
     */
    public static Double methodGoldenRatio(Function<Double, Double> function,
                                           double l,
                                           double r,
                                           double eps) {
        final double phi = (1 + Math.sqrt(5)) / 2;
        final double resPhi = 2 - phi;
        double x1 = l + resPhi * (r - l),
                x2 = r - resPhi * (r - l),
                f1Val = function.apply(x1), f2Val = function.apply(x2);
        while ((r - l) > eps) {
            if (f1Val < f2Val) {
                r = x2;
                x2 = x1;
                f2Val = f1Val;
                x1 = l + resPhi * (r - l);
                f1Val = function.apply(x1);
            } else {
                l = x1;
                x1 = x2;
                f1Val = f2Val;
                x2 = r - resPhi * (r - l);
                f2Val = function.apply(x2);
            }
        }
        return (l + r) / 2;
    }

    /**
     * Метод Фибоначчи.
     * Улучшение реализации "поиска с помощью золотого сечения", так
     * как высчитывание коэффициентов происходит в типах Long, а Double.
     * Вычисления вспомогательного множителя происходит с помощью чисел Фибоначчи.
     * Алгоритм работает корректно на интервале [l, r], если
     * функция унимодальная на этом отрезке.
     * Значение для ответа берется из середины отрезка [l < x1; x2 < r], когда границы l, r
     * не различимы первыми 4 значащими цифрами после запятой.
     */
    public static Double methodFibonacciNumbers(Function<Double, Double> function,
                                                double l,
                                                double r,
                                                double eps) {
        long n = (long) ((r - l) / eps);
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
        double f1 = function.apply(x1), f2 = function.apply(x2);
        for (int i = s - 1; i > 2; i--) {
            if (f1 < f2) {
                r = x2;
                x2 = x1;
                f2 = f1;
                x1 = l + (double) fib.get(i - 2) / fib.get(i) * (r - l);
                f1 = function.apply(x1);
            } else {
                l = x1;
                x1 = x2;
                f1 = f2;
                x2 = l + (double) fib.get(i - 1) / fib.get(i) * (r - l);
                f2 = function.apply(x2);
            }
        }
        return (l + r) / 2;
    }

    /**
     * Метод парабол
     */
    public static Double methodParabolas(Function<Double, Double> function,
                                         double l,
                                         double r,
                                         double eps) {
        double fl = function.apply(l);
        double fr = function.apply(r);
        double x = (l + r) / 2;
        double fx = function.apply(x);
        while (r - l > eps) {
            double del = (2 * ((x - l) * (fx - fr) - (x - r) * (fx - fl)));
            if (del == 0) {
                return (l - r) / 2;
            }
            double u = x - ((x - l) * (x - l) * (fx - fr) - (x - r) * (x - r) * (fx - fl)) / del;
            double fu = function.apply(u);
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
        }
        return (l + r) / 2;
    }

    /**
     * метод Брента.
     */
    public static Double methodBrent(Function<Double, Double> function,
                                     double a,
                                     double c,
                                     double eps) {
        double k = (3 - Math.sqrt(5)) / 2;
        double x, w, v, dLast = -1;
        x = w = v = (a + c) / 2;
        double fx, fw, fv;
        fx = fw = fv = function.apply(x);
        double d, e;
        d = e = c - a;
        while (d > eps) {
            if (dLast == d) {
                return x;
            }
            dLast = d;
            double g;
            g = e;
            e = d;
            double u;
            if (!(fx == fw || fx == fv || fv == fw)) {
                u = x - (Math.pow((x - w), 2) * (fx - fv) - Math.pow((x - v), 2) * (fx - fw)) / (2 * ((x - w) * (fx - fv) - (x - v) * (fx - fw)));
                if (u >= a + eps && u <= c - eps && Math.abs(u - x) < g / 2) {
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
                if (Math.abs(u - x) < eps) {
                    u = x + Math.signum(u - x) * eps;
                }
            }
            double fu = function.apply(u);
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
        }
        return x;
    }
}
