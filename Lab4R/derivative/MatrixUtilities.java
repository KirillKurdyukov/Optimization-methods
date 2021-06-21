package derivative;

import format.Matrix;
import format.ProfileMatrix;
import format.SparseMatrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class MatrixUtilities {
    private static final Random random = new Random();
    private static final int MAX_PROFILE = 20;

    private static int randSize() {
        return 10 + abs(random.nextInt(991));
    }

    private static int bigRandSize() {
        return 10 + abs(random.nextInt(10000));
    }

    public static double EPSILON = 0.0000000000000000000000000000000000001;

    private static double[][] matrixA = null;

    private static ProfileMatrix profileMatrixA = null;
    private static SparseMatrix sparseMatrixA = null;

    public static double[][] generateMatrix() {
        int n = abs(random.nextInt()) % 100;
        double[][] matrix = new double[n][n];
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < j; k++) {
                if ((j + k) % 5 == 1) {
                    matrix[j][k] = 0;
                    matrix[k][j] = 0;
                } else {
                    matrix[j][k] = max(0, random.nextDouble());
                    matrix[k][j] = max(0, random.nextDouble());
                }
            }
            matrix[j][j] = random.nextDouble();
        }
        return matrix;
    }

    public static double len(double[] vect) {
        double result = 0;
        for (double v : vect) {
            result += v * v;
        }
        return sqrt(result);
    }

    /**
     * Генерирует первую матрицу Ak в формате SparseMatrix (в симметричном виде)
     * Для генерация k матрицы необходимо, чтобы была сгенерирована k - 1 матрица
     */
    public static SparseMatrix generateSparseMatrix(int k, int n) {
        if (sparseMatrixA != null) {
            if (k != 0) {
                double firstElement = sparseMatrixA.getElement(0, 0);
                sparseMatrixA.replaceD(0, firstElement - Math.pow(0.1, k - 1) + Math.pow(0.1, k));
            }
            return sparseMatrixA;
        }
        //int n = bigRandSize();
        double[] al;
        double[] au;
        ArrayList<Double> alList = new ArrayList<>();
        ArrayList<Double> auList = new ArrayList<>();
        ArrayList<Integer> jaList = new ArrayList<>();
        double[] di = new double[n];
        int[] ia = new int[n + 2];
        int[] ja;
        ia[0] = 0;
        ia[1] = 0;
        di[0] = 0;
        // Добавление внедиагональных элементов
        for (int i = 1; i < n; i++) {
            di[i] = 0;
            int count = random.nextInt(Math.min(i + 1, MAX_PROFILE));
            Set<Integer> notNullElements = new TreeSet<>();
            for (int j = 0; j < count; j++) {
                notNullElements.add(random.nextInt(i));
            }
            // Количество элементов на i-й строчке/столбце
            ia[i + 1] = ia[i] + notNullElements.size();
            for (Integer index : notNullElements) {
                // a[i][index]
                // идет в сумму a[i][i]
                double a = getNotNullAij();
                alList.add(a);
                di[i] -= a;
                // a[index][i]
                // идет в сумму a[index][index]
                auList.add(a);
                di[index] -= a;
                jaList.add(index);
            }
        }
        al = new double[alList.size()];
        au = new double[auList.size()];
        ja = new int[jaList.size()];
        for (int i = 0; i < alList.size(); i++) {
            al[i] = alList.get(i);
        }
        for (int i = 0; i < auList.size(); i++) {
            au[i] = auList.get(i);
        }
        for (int i = 0; i < jaList.size(); i++) {
            ja[i] = jaList.get(i);
        }
        // k == 0, так как первая генерация
        di[0] += 1;
        /* sparseMatrixA = new SparseMatrix(al, au, di, ia, ja);
        return sparseMatrixA;*/
        return new SparseMatrix(al, au, di, ia, ja);
    }

    private static final double[] el = new double[]{0.0, -1.0, -2.0, -3.0, -4.0};

    private static double getAij() {
        return el[abs(random.nextInt(5))];
    }

    private static double getNotNullAij() {
        return el[abs(random.nextInt(4)) + 1];
    }

    private static double[][] generateAk(int k) {
        if (matrixA != null) {
            matrixA[0][0] = 0;
            matrixA[0][0] = -Arrays.stream(matrixA[0]).sum();
            matrixA[0][0] += Math.pow(0.1, k);
            return matrixA;
        }
        int n = randSize();
        matrixA = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                matrixA[i][j] = getAij();
                matrixA[j][i] = getAij();
                if (!equals(matrixA[i][j], 0) && equals(matrixA[j][i], 0)) {
                    matrixA[j][i] = -1.0;
                }
                if (equals(matrixA[i][j], 0) && !equals(matrixA[j][i], 0)) {
                    matrixA[i][j] = -1.0;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            matrixA[i][i] = -Arrays.stream(matrixA[i]).sum();
        }
        matrixA[0][0] += Math.pow(0.1, k);
        return matrixA;
    }

    public static void checkSymmetric(Matrix matrix) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.size(); j++) {
                if (matrix.get(i, j) != matrix.get(j , i)) {
                    throw new IllegalStateException();
                }
            }
        }
    }
    private static double[][] generateGilbert() {
        int n = randSize();
        double[][] res = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = 1.0 / (i + j + 1);
            }
        }
        return res;
    }

    public static double[] generateX(int n) {
        double[] res = new double[n];
        for (int i = 0; i < n; i++) {
            res[i] = 1 + i;
        }
        return res;
    }

    public static double[] multMatrVect(double[][] m, double[] v) {
        double[] res = new double[v.length];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[i] += m[i][j] * v[j];
            }
        }
        return res;
    }



    public static double[] multMatrVect(Matrix m, double[] v) {
        double[] res = new double[v.length];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[i] += m.get(i, j) * v[j];
            }
        }
        return res;
    }


    public static String vectorToString(double[] v) {
        return Arrays.stream(v).mapToObj(Double::toString).collect(Collectors.joining(" ", "", "\n"));
    }
    private static void genWriteSoLE(String fileName, double[][] m) {
        int n = m.length;
        double[] x = generateX(n);
        double[] f = multMatrVect(m, x);
        ProfileMatrix matrix = new ProfileMatrix(m);
        //matrix.writeInFile(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fileName), StandardOpenOption.APPEND);
             BufferedWriter mWriter = Files.newBufferedWriter(Path.of(fileName.substring(0, fileName.indexOf('.')) + "matr" + ".txt"))) {
            writer.write(vectorToString(f) + vectorToString(x));
            mWriter.write(n + "\n");
            Arrays.stream(m).forEach(v -> {
                try {
                    mWriter.write(MatrixUtilities.vectorToString(v));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void genWriteSparseMatrix(String fileName, int k) {
        SparseMatrix m = generateSparseMatrix(k, 4);
        int n = m.getRowNumbers();
        double[] x = generateX(n);
        double[] f = m.smartMultiplication(x);
       // m.writeInFile(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fileName), StandardOpenOption.APPEND)) {
            writer.write(vectorToString(f) + vectorToString(x));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void genWriteAkSoLE(String fileName, int k) {
        genWriteSoLE(fileName, generateAk(k));
    }

    public static void genWriteGilbertSoLE(String fileName, int k) {
        genWriteSoLE(fileName, generateGilbert());
    }

    public static void generateMatrixWriteInFile(String fileName) {
        ProfileMatrix a = new ProfileMatrix(generateMatrix());
        //a.writeInFile(fileName);
    }

    public static double[] readDoubleVector(BufferedReader reader) {
        try {
            return Arrays.stream(reader.readLine().split("\\s+")).filter(x -> !x.isEmpty()).mapToDouble(Double::valueOf).toArray();
        } catch (IOException ignored) {
        }
        return new double[0];
    }

    public static int[] readIntVector(BufferedReader reader) {
        try {
            return Arrays.stream(reader.readLine().split("\\s+")).filter(x -> !x.isEmpty()).mapToInt(Integer::valueOf).toArray();
        } catch (IOException ignored) {
        }
        return new int[0];
    }

    public static double dist(double[] v1, double[] v2) {
        double result = 0;
        for (int i = 0; i < v1.length; i++) {
            result += Math.pow(v1[i] - v2[i], 2);
        }
        return sqrt(result);
    }

    public static boolean equals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    public static double[] subtract(double[] a, double[] b) {
        double[] result = new double[a.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = a[i] - b[i];
        }
        return result;
    }


    public static void swapLines(double[][] matrix, int fromLine, int toLine) {
        double[] temp = new double[matrix[0].length];
        System.arraycopy(matrix[fromLine], 0, temp, 0, matrix[0].length);
        matrix[fromLine] = matrix[toLine];
        matrix[toLine] = temp;
    }

    public static void swapElements(double[] a, int indexA, int indexB) {
        double x = a[indexA];
        a[indexA] = a[indexB];
        a[indexB] = x;
    }

    public static void subtract(double[] where, double[] what, double coefficient) {
        for (int i = 0; i < where.length; i++) {
            where[i] -= coefficient * what[i];
        }
    }

    public static void subtract(double[] a, int where, int what, double coefficient) {
        a[where] -= a[what] * coefficient;
    }

    public static int countNoZeroElements(double[][] matrix, int row) {
        int cnt = 0;
        for (int i = 0; i < matrix.length - 1; i++) {
            if (!equals(matrix[row][i], 0)) {
                cnt++;
            }
        }
        return cnt;
    }

    public static double[][] readMatrix(String fileName) {
        double[][] res = new double[0][];
        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName))) {
            int n = Integer.parseInt(reader.readLine());
            res = new double[n][];
            for (int i = 0; i < n; i++) {
                res[i] = readDoubleVector(reader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static double[] multVector(double[] a, double b) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b;
        }
        return result;
    }
}
