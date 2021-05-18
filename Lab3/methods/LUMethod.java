package methods;

import format.MatrixFileException;
import format.MatrixFormatException;
import format.ProfileMatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.IntStream;

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

    public static void main(String[] args) {
        try {
            System.out.println("n  | k | |x* - x| | |x* - x| / |x*|");
            for (int i = 10; i <= 1000; i*=10) {
                for (int j = 1; j <= 10; j++) {
                    process("Lab3/tests/matrixDense" + i + "_" + j, j);
                }
            }
        } catch (MatrixFileException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void process(String arg, int k) throws MatrixFileException {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(arg))) {
            String currentLine;
            double[][] matrix;
            try {
                currentLine = bufferedReader.readLine();
                int size = Integer.parseInt(currentLine);
                matrix = new double[size][size];
                for (int i = 0; i < size; i++) {
                    currentLine = bufferedReader.readLine();
                    String[] elements = currentLine.split(" ");
                    int finalI = i;
                    IntStream.range(0, size)
                            .forEach(j -> matrix[finalI][j] = Double.parseDouble(elements[j]));
                }
                double[] free = Arrays.stream(bufferedReader
                        .readLine()
                        .split(" ")
                ).mapToDouble(Double::parseDouble)
                        .toArray();
                System.out.println(Arrays.toString(LUMethod.solve(new ProfileMatrix(matrix), free)));
            //    GenerationMatrix.test(LUMethod.solve(new ProfileMatrix(matrix), free), size, k);
            } catch (IOException | NumberFormatException e) {
                throw new MatrixFileException("Error while reading file. " + e.getMessage());
            } catch (MatrixFormatException e) {
                e.printStackTrace();
            }
        } catch (
                IOException e) {
            throw new MatrixFileException("Input file error. " + e.getMessage());
        }
    }


}
