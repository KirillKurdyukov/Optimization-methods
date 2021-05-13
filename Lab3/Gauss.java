import format.DenseMatrix;
import format.Matrix;
import methods.VectorNumbers;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Gauss {

    private static final double eps = 0.0000001;

    public static void main(String[] args) {
        try {
            if (args == null || args.length != 1 || args[0] == null)
                throw new MatrixReadFileException("Incorrect arguments start program.");
            process(args[0]);
        } catch (MatrixReadFileException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void process(String arg) throws MatrixReadFileException {
        Matrix denseMatrix;
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
                throw new MatrixReadFileException("Error while reading file. " + e.getMessage());
            }
        } catch (
                IOException e) {
            throw new MatrixReadFileException("Input file error. " + e.getMessage());
        }

        VectorNumbers answer = new VectorNumbers(Collections.nCopies(denseMatrix.size(), null));
        switch (gauss(denseMatrix, answer)) {
            case 0 -> System.out.println("no solutions");
            case 1 -> System.out.println("many solutions");
            case 2 -> answer.output();
        }

    }

    /*
    0 - no solutions
    1 - many solutions
    2 - one solution
     */
    private static int gauss(Matrix matrix, VectorNumbers answer) {
        int size = matrix.size();
        for (int col = 0, row = 0; col < size && row < size; row++) {
            col = row;
            while (col < size && !pivoting(matrix, row, col))
                col++;
            if (col < size)
                if (madeTriangularView(matrix, row, col))
                    return 0;
        }
        for (int row = 0; row < size; row++)
            if (checkIncompatibleLine(matrix, row))
                return 0;
        for (int row = 0; row < size; row++) {
            if (checkManySolution(matrix, row))
                return 1;
        }
        for (int row = size - 1; row > 0; row--)
            madeDiagView(matrix, row);
        IntStream.range(0, size).forEach(i ->
                answer.toSet(i, matrix.getFreeVector(i) / matrix.get(i, i))
        );
        return 2;
    }

    private static void madeDiagView(Matrix matrix, int col) {
        for (int i = col - 1; i >= 0; i--) {
            double delta = matrix.get(i, col) / matrix.get(col, col);
            matrix.set(i, col, matrix.get(i, col) - delta * matrix.get(col, col));
            matrix.setFreeVectorNum(i, matrix.getFreeVector(i) - delta * matrix.getFreeVector(col));
        }
    }

    private static boolean checkManySolution(Matrix matrix, int row) {
        int size = matrix.size();
        for (int i = 0; i < size; i++) {
            if (Math.abs(matrix.get(row, i)) > eps)
                return false;
        }
        return true;
    }
    private static boolean pivoting(Matrix matrix, int row, int col) {
        int maxLineIndex = row;
        double localMax = 0.0;
        for (int i = row; i < matrix.size(); i++) {
            if (Math.abs(matrix.get(i, col)) > localMax) {
                maxLineIndex = i;
                localMax = Math.abs(matrix.get(i, col));
            }
        }
        if (localMax <= eps)
            return false;
        matrix.swapRow(row, maxLineIndex);
        return true;
    }

    private static boolean madeTriangularView(Matrix matrix, int row, int col) {
        int size = matrix.size();
        for (int i = row + 1; i < size; i++) {
            double delta = matrix.get(i, col) / matrix.get(row, col);
            for (int j = col; j < size; j++)
                matrix.set(i, j,  matrix.get(i, j) - delta * matrix.get(row, j));
            matrix.setFreeVectorNum(i, matrix.getFreeVector(i) - delta * matrix.getFreeVector(row));
            boolean checkZeroLine = true;
            for (int j = col; j < size; j++) {
                if (Math.abs(matrix.get(i, j)) > eps)
                    checkZeroLine = false;
            }
            if (checkZeroLine) {
                if (Math.abs(matrix.getFreeVector(i)) > eps)
                    return true;
            }
            if (checkZeroLine) {
                for (int j = 0; j < size; j++) {
                    matrix.set(i, j, 0.0);
                }
                matrix.setFreeVectorNum(i, 0.0);
            }
        }
        return false;
    }


    private static boolean checkIncompatibleLine(Matrix matrix, int row) {
        int size = matrix.size();
        for (int i = 0; i < size; i++) {
            if (Math.abs(matrix.get(row, i)) > eps)
                return false;
        }
        return !(Math.abs(matrix.getFreeVector(row)) <= eps);
    }
}
