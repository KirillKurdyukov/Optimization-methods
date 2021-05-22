package methods;

import format.DenseMatrix;
import format.MatrixFileException;
import format.SparseMatrix;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Conjugate extends TestAbstract {
    public static int lastIterations;

    double[] subtractVectors(double[] a, double[] b) {
        double[] res = Arrays.copyOf(a, a.length);
        IntStream.range(0, a.length).forEach(i -> res[i] -= b[i]);
        return res;
    }

    double scalar(double[] a, double[] b) {
        return IntStream.range(0, a.length).mapToDouble(i -> a[i] * b[i]).sum();
    }

    double[] sumVectors(double[] a, double[] b) {
        double[] res = new double[a.length];
        IntStream.range(0, a.length).forEach(i -> res[i] = b[i] + a[i]);
        return res;
    }

    double[] multiply(double alpha, double[] b) {
        double[] res = new double[b.length];
        IntStream.range(0, b.length).forEach(i -> res[i] = b[i] * alpha);
        return res;
    }

    public double[] solve(SparseMatrix A, double[] f) {
        double[] x0 = new double[f.length];
        x0[0] = 1;
        double[] r0 = subtractVectors(f, A.smartMultiplication(x0));
        double[] z0 = r0;
        int MAX_ITERATIONS = 3000;
        for (int k = 1; k < MAX_ITERATIONS; k++) {
            double[] Az0 = A.smartMultiplication(z0);
            double alphaK = scalar(r0, r0) / scalar(Az0, z0);
            double[] xK = sumVectors(x0, multiply(alphaK, z0));
            double[] rK = subtractVectors(r0, multiply(alphaK, Az0));
            double betaK = scalar(rK, rK) / scalar(r0, r0);
            double epsilon = 0.000000000001;
            double[] zK = sumVectors(rK, multiply(betaK, z0));
            if (Math.sqrt(scalar(rK, rK) / scalar(f, f)) < epsilon) {
                return xK;
            }
            x0 = xK;
            z0 = zK;
            r0 = rK;
            lastIterations = k;
        }
        return x0;
    }


    public static void main(String[] args) {
        Conjugate conjugate = new Conjugate();
        conjugate.testDense1();
        conjugate.testDense2();
        conjugate.testDense3();
    }

    @Override
    public void process(String arg, int k) throws MatrixFileException {
        DenseMatrix denseMatrix = readMatrix(arg);
        GenerationMatrix.test(solve(new SparseMatrix(denseMatrix.getMatrix()), denseMatrix.getFreeVector()), denseMatrix.size(), lastIterations);
    }
}