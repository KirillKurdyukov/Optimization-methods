package methods;

import format.MatrixFileException;

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
        genDenseMatrices();
        genGilbertMatrices();
    }

    private static void genDenseMatrices() {
        GenerationMatrix g = new GenerationMatrix();
        try {
            for (int i = 10; i <= 1000; i*=10) {
                for (int j = 1; j <= 10; j++)
                    g.generateDenseMatrix("Lab3/tests/matrixDense" + i + "_" + j, i, j);
            }

        } catch (MatrixFileException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void genGilbertMatrices() {
        GenerationMatrix g = new GenerationMatrix();
        try {
            for (int i = 2; i <= 10; i++) {
                g.generateGilbertDenseMatrix("Lab3/tests/matrixGilbert" + i, i);
            }
        } catch (MatrixFileException e) {
            System.err.println(e.getMessage());
        }

    }

    public static void test(double [] x, int size, int k) {
        double [] xStar = IntStream.range(0, size)
                .mapToDouble(i -> i + 1)
                .toArray();
        double [] subtract = IntStream.range(0, size)
                .mapToDouble(i -> x[i] - xStar[i])
                .toArray();
        if (k != -1)
            System.out.println(size + " | " + k + " | " + module(subtract) + " | " + module(subtract) / module(xStar));
        else
            System.out.println(size + " | " + module(subtract) + " | " + module(subtract) / module(xStar));
    }

    public static void testBonus(double [] x, int size, int k, double [] free, double [][] a) {
        double [] xStar = IntStream.range(0, size)
                .mapToDouble(i -> i + 1)
                .toArray();
        double [] subtract = IntStream.range(0, size)
                .mapToDouble(i -> x[i] - xStar[i])
                .toArray();
        System.out.println(size + " | " + k + " | " + module(subtract) + " | " +  module(subtract) / module(xStar) + " | "
                + (module(subtract) / module(xStar)) / (module(multiply(a, free)) / module(free)));
    }

    private static double[] multiply(double [][] a, double [] x) {
        return Arrays.stream(a)
                .mapToDouble(i -> IntStream
                        .range(0, x.length)
                        .mapToDouble(j -> x[j] * i[j])
                        .reduce(Double::sum)
                        .orElseThrow())
                .toArray();
    }

    private static double module(double [] v) {
        return Math.sqrt(Arrays.stream(v)
                .map(i -> i * i)
                .reduce(Double::sum)
                .orElseThrow());
    }

    public double [] generateFreeVector(double[][] matrix) {
        return Arrays.stream(matrix)
                .mapToDouble(this::getRowSum)
                .toArray();
    }

    private double getRowSum(double[] row) {
        return IntStream.range(0, row.length)
                .mapToDouble(i -> (i + 1) * row[i])
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

        writeMatrixInFile(arg, n, matrix);
    }

    public void generateGilbertDenseMatrix(String arg, int n) throws MatrixFileException {
        double[][] matrix = new double[n][n];
        IntStream.range(0, n).forEach(i ->
                IntStream.range(0, n).forEach(j ->
                        matrix[i][j] = 1.0 / (i + j + 1)
                )
        );
        writeMatrixInFile(arg, n, matrix);
    }

    private void writeMatrixInFile(String arg, int n, double[][] matrix) throws MatrixFileException {
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
