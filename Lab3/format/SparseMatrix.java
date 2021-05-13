package format;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class SparseMatrix {
    private double[] al;      // эл-ты нижнего треуг
    private double[] au;      // эл-ты верхнего треуг
    private double[] di;      // хранит диагонльные элементы
    private int[] ia;      //ia[i + 1] - ia[i] - кол-во внедиагоныльных эл-ов, на i-ой строчке
    private int[] ja; // номера столбцов, нде лежат эти элементы
    private int n; //размерность


    public SparseMatrix(String filename) {
        Path path = Path.of(filename);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            al = MatrixUtilities.readDoubleVector(reader);
            au = MatrixUtilities.readDoubleVector(reader);
            di = MatrixUtilities.readDoubleVector(reader);
            ia = MatrixUtilities.readIntVector(reader);
            ja = MatrixUtilities.readIntVector(reader);
        } catch (IOException e) {
            System.err.println("IO failed");
        }
    }

    public SparseMatrix(double[] al, double[] au, double[] di, int[] ia, int[] ja) {
        this.al = al;
        this.au = au;
        this.di = di;
        this.ia = ia;
        this.ja = ja;
        this.n = di.length;
    }

    public SparseMatrix(double[][] matrix) {
        assert (matrix.length == matrix[0].length);
        n = matrix.length;
        di = new double[matrix.length];
        ia = new int[matrix.length + 2];
        ArrayList<Double> alList = new ArrayList<>();
        ArrayList<Double> auList = new ArrayList<>();
        ArrayList<Integer> jaList = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            di[i] = matrix[i][i];
        }

        ia[0] = 0;
        ia[1] = 0;
        for (int i = 1; i < matrix.length; i++) {
            // сколько эл-ов в профиле в i-ой строке
            ia[i + 1] = ia[i] + getRowProfileLen(matrix, i, alList, auList, jaList);
        }

        // заполнили индексы для строк
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
    }


    public void writeInFile(String filename) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filename))) {
            writer.write(Arrays.stream(al).mapToObj(Double::toString).collect(Collectors.joining(" ", "", "\n")));
            writer.write(Arrays.stream(au).mapToObj(Double::toString).collect(Collectors.joining(" ", "", "\n")));
            writer.write(Arrays.stream(di).mapToObj(Double::toString).collect(Collectors.joining(" ", "", "\n")));
            writer.write(Arrays.stream(ia).mapToObj(Integer::toString).collect(Collectors.joining(" ", "", "\n")));
            writer.write(Arrays.stream(ja).mapToObj(Integer::toString).collect(Collectors.joining(" ", "", "\n")));
        } catch (IOException ignored) {

        }
    }

    public double getElement(int a, int b) {
        if (a == b) {
            return di[a];
        }
        boolean flag = true;
        if (b > a) { // now minimum is a
            int temp = b;
            b = a;
            a = temp;
            flag = false;
        }
        int countInRow = ia[a + 1] - ia[a];
        List<Integer> getAllColumnsInRow = new ArrayList<>();
        for (int i = ia[a]; i < ia[a] + countInRow; i++) {
            getAllColumnsInRow.add(ja[i]);
        }
        if (getAllColumnsInRow.contains(b)) {
            if (flag) {
                return al[ia[a] + getAllColumnsInRow.indexOf(b)];
            } else {
                return au[ia[a] + getAllColumnsInRow.indexOf(b)];
            }
        } else {
            return 0;
        }
    }

    private int getRowProfileLen(double[][] matrix, int row, List<Double> alList, List<Double> auList, List<Integer> jaList) {
        int index = 0;
        int count = 0;
        while (index != row) {
            if (!MatrixUtilities.equals(matrix[row][index], 0)) {
                alList.add(matrix[row][index]);
                auList.add(matrix[index][row]);
                jaList.add(index);
                count++;
            }
            index++;
        }
        return count;
    }

    public double[] smartMultiplication(double[] vector) {
        int leftBorderInJa = 0;
        double[] result = new double[vector.length];
        for (int i = 0; i < n; i++) {
            int cnt = ia[i + 1] - ia[i];
            result[i] += di[i] * vector[i];
            for (int j = 0; j < cnt; j++) {
                if (leftBorderInJa + j > ja.length - 1) {
                    System.out.println("");
                }
                int column = ja[leftBorderInJa + j];
                result[i] += al[leftBorderInJa + j] * vector[column];
                result[column] += au[leftBorderInJa + j] * vector[i];
            }
            leftBorderInJa += cnt;
        }
        return result;
    }

    public static void main(String[] args) { // тесты для матрицы проходят, значит оно работает
        double[][][] matrixes = new double[100][][];
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            int n = abs(random.nextInt()) % 100;
            matrixes[i] = new double[n][n];
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < j; k++) {
                    if ((j + k) % 5 == 1) {
                        matrixes[i][j][k] = 0;
                        matrixes[i][k][j] = 0;
                    } else {
                        matrixes[i][j][k] = max(1, random.nextDouble());
                        matrixes[i][k][j] = max(1, random.nextDouble());
                    }
                }
                matrixes[i][j][j] = random.nextDouble();
            }
            SparseMatrix a = new SparseMatrix(matrixes[i]);
            a.writeInFile("matr/test.txt");
            SparseMatrix b = new SparseMatrix("matr/test.txt");
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (matrixes[i][j][k] != a.getElement(j, k) || matrixes[i][j][k] != b.getElement(j, k)) {
                        System.out.println("FAILED");
                    }
                }
            }
        }
    }


    public int getColumnNumbers() {
        return n;
    }

    public int getRowNumbers() {
        return n;
    }

    public void replace(int i, int j, double x) {

    }

    /**
     * Костыль чтобы менять d[i]
     */
    public void replaceD(int i, double x) {
        di[i] = x;
    }
}
