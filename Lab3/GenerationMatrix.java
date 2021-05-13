import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerationMatrix {
    private static final int UPPER_BOUND_OF_NUMBER_GENERATION = 4;

    public static void main(String[] args) {
        GenerationMatrix g = new GenerationMatrix();
        try {
            for (int i = 1; i < 5; i++)
                g.generateGilbertDenseMatrix("Lab3/tests/matrixGilbert" + i,  10);
            for (int i = 1; i < 5; i++)
                g.generateDenseMatrix("Lab3/tests/matrixDense" + i, 100 * i, 10);
        } catch (MatrixFileException e) {
            System.err.println(e.getMessage());
        }
    }

    public double [] generateFreeVector(double[][] matrix) {
        return Arrays.stream(matrix)
                .mapToDouble(this::getRowSum)
                .toArray();
    }

    private double getRowSum(double[] row) {
        return Arrays.stream(row)
                .reduce(Double::sum)
                .orElseThrow();
    }

    private String printRow(double[] row) {
        return Arrays.stream(row)
                .mapToObj(Double::toString)
                .collect(Collectors.joining(" "));
    }

    public void generateDenseMatrix(String arg, int n, int k) throws MatrixFileException {
        double[][] matrix = new double[n][n];
        Random random = new Random();
        IntStream.range(0, n).forEach(i ->
                IntStream.range(0, n).forEach(j -> {
                            if (i != j)
                                matrix[i][j] = -1.0 * random.nextInt(UPPER_BOUND_OF_NUMBER_GENERATION);
                        }
                )
        );

        matrix[0][0] = getRowSum(matrix[0]) + Math.pow(10.0, -k);

        IntStream.range(1, n).forEach(i ->
                matrix[i][i] = getRowSum(matrix[i])
        );

        readMatrixInFile(arg, n, matrix);
    }

    public void generateGilbertDenseMatrix(String arg, int n) throws MatrixFileException {
        double[][] matrix = new double[n][n];
        IntStream.range(0, n).forEach(i ->
                IntStream.range(0, n).forEach(j ->
                        matrix[i][j] = 1.0 / (i + j + 1)
                )
        );
        readMatrixInFile(arg, n, matrix);
    }

    private void readMatrixInFile(String arg, int n, double[][] matrix) throws MatrixFileException {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(arg))) {
            Exception exception = new IOException();
            AtomicBoolean wasException = new AtomicBoolean(false);
            bufferedWriter.write(Integer.toString(n));
            bufferedWriter.newLine();
            IntStream.range(0, n).forEach(i -> {
                try {
                    bufferedWriter.write(printRow(matrix[i]));
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    wasException.set(true);
                    exception.addSuppressed(e);
                }
            });

            double[] free = generateFreeVector(matrix);
            bufferedWriter.write(printRow(free));

            if (wasException.get()) {
                throw new MatrixFileException("An error occurred while writing the file: " + exception.getMessage());
            }

        } catch (IOException e) {
            throw new MatrixFileException("Error output file: " + e.getMessage());
        }
    }

}
