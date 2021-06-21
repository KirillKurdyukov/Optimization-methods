package method;

import derivative.Gradient;

import java.util.Arrays;
import java.util.function.Function;

import static java.lang.Math.sqrt;

public abstract class AbstactNewtoneMethod implements NewtoneMethod {
    protected static double EPSILON = 0.0000000001;
    protected static int MAX_ITERATIONS = 4096;
    protected abstract double[] runImpl(Gradient gradient, Function<double[], Double> function, double[] point);

    @Override
    public double[] run(final Gradient gradient, final Function<double[], Double> function, final double[] point) {
        return runImpl(gradient, function, point);
    }

    /**
     * Returns length of vector
     */
    public double length(double[] vector) {
        return sqrt(Arrays.stream(vector).map(x -> x * x).sum());
    }

    /**
     * Generates matrix with 1 on diagonal
     */
    public double[][] generateI(int n) {
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }
        return result;
    }

    /**
     * Converts matrix to vector if possible
     */
    public double[] fromMatrixToVector(double[][] a) {
        assert (a[0].length == 1);
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i][0];
        }
        return result;
    }

    /**
     * Subtract vectors
     */
    public double[] subtract(double[] a, double[] b) {
        return fromMatrixToVector(subtractMatrix(vectorToMatrix(a), vectorToMatrix(b)));
    }

    /**
     * Sum vectors
     */
    public double[][] sumMatrix(double[][] a, double[][] b) {
        assert (a.length == b.length);
        assert (a[0].length == b[0].length);
        double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    /**
     * Mult matrixes
     */
    public double[][] multMatrix(double[][] a, double[][] b) {
        assert (a[0].length == b.length);
        double[][] result = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    /**
     * Mult matrix and vector
     */
    public double[] multMatrix(double[][] a, double[] b) {
        return fromMatrixToVector(multMatrix(a, vectorToMatrix(b)));
    }

    /**
     * Mult vector and scalar
     */
    public double[] multVector(double[] a, double b) { //
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b;
        }
        return result;
    }

    public double multVectors(double[] a, double[] b) {
        double result = 0;
        for (int i = 0; i < a.length; i++) {
            result += a[i] * b[i];
        }
        return result;
    }

    /**
     * Divides matrix on constant
     */
    public double[][] divideMatrix(double[][] a, double b) {
        double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                result[i][j] = a[i][j] / b;
            }
        }
        return result;
    }

    /**
     * Converts vector to matrix
     * [1,2,3] -> [[1], [2], [3]]
     */
    public double[][] vectorToMatrix(double[] a) {
        double[][] result = new double[a.length][1];
        for (int i = 0; i < a.length; i++) {
            result[i][0] = a[i];
        }
        return result;
    }

    /**
     * Subtracts matrixes
     */
    public double[][] subtractMatrix(double[][] a, double[][] b) {
        assert (a.length == b.length);
        assert (a[0].length == b[0].length);
        double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }

    /**
     * Scalar multiplication for vectors in "matrix form".
     */
    public double scalarMult(double[][] a, double[][] b) {
        assert (a.length == b.length);
        assert (a[0].length == 1);
        assert (b[0].length == 1);
        double result = 0;
        for (int i = 0; i < a.length; i++) {
            result += a[i][0] * b[i][0];
        }
        return result;
    }

    /**
     * Transpose matrix
     */
    public double[][] transpose(double[][] a) {
        double[][] result = new double[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                result[j][i] = a[i][j];
            }
        }
        return result;
    }
    /**
     * sum 2 vectors
     */
    protected double[] sumVectors(double[] firstVector, double[] secondVector) {
        assert firstVector.length == secondVector.length;
        double[] result = new double[firstVector.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = firstVector[i] + secondVector[i];
        }
        return result;
    }
}
