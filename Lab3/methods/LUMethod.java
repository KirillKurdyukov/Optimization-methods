package methods;

import format.*;

public class LUMethod extends TestAbstract {
    public static double[] solve(ProfileMatrix matrix, double[] b) throws MatrixFormatException {
        matrix.LUDecomposition();
        int size = matrix.size();
        double[] y = new double[size];
        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int p = 0; p < i; p++) {
                sum += matrix.getFromL(i, p) * y[p];
            }
            y[i] = b[i] - sum;
        }

        double[] x = new double[size];
        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int p = 0; p < i; p++) {
                sum += matrix.getFromU(size - i - 1, size - p - 1) * x[size - p - 1];
            }
            x[size - i - 1] = (y[size - i - 1] - sum) / matrix.getFromU(size - i - 1, size - i - 1);
        }
        return x;
    }

    public static void main(String[] args) {
        LUMethod luMethod = new LUMethod();
        luMethod.testDense1();
        luMethod.testDense2();
        luMethod.testDense3();
    }

    public void process(String arg, int k) throws MatrixFileException, MatrixFormatException {
        DenseMatrix matrix = readMatrix(arg);
        GenerationMatrix.test(LUMethod.solve(new ProfileMatrix(matrix.getMatrix()), matrix.getFreeVector()), matrix.size(), k);
    }

}
