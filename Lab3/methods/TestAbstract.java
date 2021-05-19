package methods;

import format.DenseMatrix;
import format.Matrix;
import format.MatrixFileException;
import format.MatrixFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class TestAbstract {

    public abstract void process(String arg, int k) throws MatrixFileException, MatrixFormatException;

    protected void testDense1() {
        try {
            System.out.println("n  | k | |x* - x| | |x* - x| / |x*|");
            for (int i = 10; i <= 1000; i *= 10) {
                for (int j = 1; j <= 10; j++) {
                    process("Lab3/tests/matrixDense" + i + "_" + j, j);
                }
            }
        } catch (MatrixFileException | MatrixFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void testDense2() {
        try {
            System.out.println("n  | |x* - x| | |x* - x| / |x*|");
            for (int j = 2; j <= 10; j++) {
                process("Lab3/tests/matrixGilbert" + j, -1);
            }
        } catch (MatrixFileException | MatrixFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    protected DenseMatrix readMatrix(String arg) throws MatrixFileException {
        DenseMatrix denseMatrix;
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(arg))) {
            String currentLine;
            try {
                currentLine = bufferedReader.readLine();
                int size = Integer.parseInt(currentLine);
                denseMatrix = new DenseMatrix(size);
                for (int i = 0; i < size; i++) {
                    currentLine = bufferedReader.readLine();
                    String[] elements = currentLine.split(" ");
                    int finalI = i;
                    IntStream.range(0, size)
                            .forEach(j -> denseMatrix.set(finalI, j, Double.parseDouble(elements[j])));
                }
                denseMatrix.setFreeVector(new VectorNumbers(Arrays.stream(bufferedReader
                        .readLine()
                        .split(" ")
                ).map(Double::parseDouble)
                        .collect(Collectors.toList())));
            } catch (IOException | NumberFormatException e) {
                throw new MatrixFileException("Error while reading file. " + e.getMessage());
            }
        } catch (
                IOException e) {
            throw new MatrixFileException("Input file error. " + e.getMessage());
        }
        return denseMatrix;
    }
}