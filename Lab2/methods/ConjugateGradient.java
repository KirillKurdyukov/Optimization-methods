package methods;

import engine.Mode;
import format.DenseMatrix;
import format.Matrix;
import format.MatrixFileException;
import format.MatrixFormatException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConjugateGradient extends TestAbstract {
    public static ArrayList<VectorNumbers> vectors = new ArrayList<>();
    private static int c = 0;

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
                c++;
            }
            grad = A.multiply(x).add(b);
        }
    }

    public static void run(SquareMatrix A, VectorNumbers b, Mode mode, Double eps) {
        vectors.clear();
        run(eps, A, new VectorNumbers(List.of(0d, 0d)), b, mode);

    }

    public static void main(String[] args) {
        ConjugateGradient c = new ConjugateGradient();
        c.testDense1();
        c.testDense2();
        c.testDense3();
    }

    @Override
    public void process(String arg, int k) throws MatrixFileException {
        DenseMatrix matrix = readMatrix(arg);
        GenerationMatrix.testBonus(run(0.000000001,
                new SquareMatrix(matrix.getMatrix()),
                new VectorNumbers(DoubleStream.generate(() -> 0)
                        .limit(matrix.size())
                        .toArray()),
                new VectorNumbers(Arrays.stream(matrix.getFreeVector())
                        .map(i -> -1 * i)
                        .toArray()),
                Mode.BRENT).toMassive(),
                matrix.size(),
                c,
                matrix.getFreeVector(),
                matrix.getMatrix()
        );
        c = 0;
    }
}