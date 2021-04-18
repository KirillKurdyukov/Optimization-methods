package methods;

import engine.Mode;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static methods.Tester.eps;

public class ConjugateGradient {
    public static ArrayList<VectorNumbers> vectors = new ArrayList<>();

    public static VectorNumbers run(double eps, SquareMatrix A, VectorNumbers x, VectorNumbers b, Mode mode) {
        double alpha, beta;
        VectorNumbers p, p1, x1, grad, grad1, Apk;
        grad = A.multiply(x).add(b);
        p = grad.multiplyConst(-1);
        while (true) {
            for (int i = 0; i < A.rows(); i++) {
                vectors.add(x);
                if (grad.module() < eps) {
                    return x;
                }
                Apk = A.multiply(p);
                alpha = grad.module() * grad.module() / Apk.scalar(p);
                x1 = x.add(p.multiplyConst(alpha));
                grad1 = grad.add(Apk.multiplyConst(alpha));
                beta = grad1.module() * grad1.module() / grad.module() / grad.module();
                p1 = grad1.multiplyConst(-1).add(p.multiplyConst(beta));
                x = x1;
                p = p1;
                grad = grad1;
            }
            grad = A.multiply(x).add(b);
        }
    }

    public static void run(SquareMatrix A, VectorNumbers b, Mode mode) {
        vectors.clear();
        run(eps, A, new VectorNumbers(List.of(0d, 0d)), b, mode);

    }
}