package format;

public class LUMethod {
    public static double[] solve(ProfileMatrix matrix, double[] b) throws MatrixFormatException {
            matrix.LUDecomposition();

            // Ly = b
            int size = matrix.size();
            double[] y = new double[size];
            for (int i = 0; i < size; i++) {
                double sum = 0;
                for (int p = 0; p < i; p++) {
                    sum += matrix.getFromL(i, p) * y[p];
                }
                y[i] = b[i] - sum;
            }

            // Ux = y
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
}
