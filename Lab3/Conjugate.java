import format.SparseMatrix;

import java.util.Arrays;

public class Conjugate {
    private static int MAX_ITERATIONS = 3000;
    public static int lastIterations;

    double[] mult(SparseMatrix A, double[] vect) {
        double[] res = new double[A.getColumnNumbers()];
        for (int i = 0; i < A.getColumnNumbers(); i++) {
            for (int j = 0; j < A.getColumnNumbers(); j++) {
                res[i] += A.getElement(i, j) * vect[j];
            }
        }
        return res;
    }

    double[] subtract(double[] a, double[] b) {
        double[] res = Arrays.copyOf(a, a.length);
        for (int i = 0; i < a.length; i++) {
            res[i] -= b[i];
        }
        return res;
    }

    double scalar(double[] a, double[] b) {
        double res = 0;
        for (int i = 0; i < a.length; i++) {
            res += a[i] * b[i];
        }
        return res;
    }

    double[] sum(double[] a, double[] b) {
        double[] res = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = b[i] + a[i];
        }
        return res;
    }

    double[] mult(double alpha, double[] b) {
        double[] res = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            res[i] = b[i] * alpha;
        }
        return res;
    }


    public double[] solve(SparseMatrix A, double[] f, double epsilon) {
        double[] x0 = new double[f.length];
        x0[0] = 1;
        //MatrixUtilities.checkSymmetric(A);
        double[] r0 = subtract(f, A.smartMultiplication(x0));
        double[] z0 = r0;
        for (int k = 1; k < MAX_ITERATIONS; k++) {
            double[] Az0 = A.smartMultiplication(z0);
            double alphaK = scalar(r0, r0) / scalar(Az0, z0);

            double[] xK = sum(x0, mult(alphaK, z0));
            double[] rK = subtract(r0, mult(alphaK, Az0));


            double betaK = scalar(rK, rK) / scalar(r0, r0);
            double[] zK = sum(rK, mult(betaK, z0));
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
        double[][] matrixOneSolution = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}; // -1, 1, 3
        double[][] matrixOneSolution2 = {{1, 2, 2}, {2, 1, 1}, {2, 1, 1}}; // -1, 1, 3
        double[] b  = {2, 2, 2};
        double[] f = new Conjugate().solve(new SparseMatrix(matrixOneSolution2), b, 0.0000001);
        for (double x : f) {
            System.out.println(x);
        }
    }
}
