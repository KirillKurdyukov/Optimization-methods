package methods;


import format.Matrix;
import format.MatrixFileException;

import java.util.Collections;
import java.util.stream.IntStream;


public class Gauss extends TestAbstract {

    private static final double eps = 0.000000000001;

    public static void main(String[] args) {
        Gauss gauss = new Gauss();
        gauss.testDense1();
        gauss.testDense2();
        gauss.testDense3();
    }


    public void process(String arg, int k) throws MatrixFileException {
        Matrix denseMatrix = readMatrix(arg);
        VectorNumbers answer = new VectorNumbers(Collections.nCopies(denseMatrix.size(), null));
       // switch (gauss(denseMatrix, answer)) {
         //   case 0 -> System.out.println("no solutions");
        //    case 1 -> System.out.println("many solutions");
        //    case 2 -> GenerationMatrix.test(answer.toMassive(), answer.size(), k);
       // }

    }

    /*
    0 - no solutions
    1 - many solutions
    2 - one solution
     */
    public static int gauss(Matrix matrix, VectorNumbers answer) {
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
                matrix.set(i, j, matrix.get(i, j) - delta * matrix.get(row, j));
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
