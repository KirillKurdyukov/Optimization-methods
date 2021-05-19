package format;

import java.util.ArrayList;

public class ProfileMatrix {
    private final double[] D;
    private final double[] L;
    private final double[] U;
    private final int[] IL;
    private final int[] IU;
    private boolean isLU = false;

    public ProfileMatrix(double[][] matrix) {
        D = new double[matrix.length];
        ArrayList<Double> al1 = new ArrayList<>();
        ArrayList<Integer> ial1 = new ArrayList<>();
        ArrayList<Double> au1 = new ArrayList<>();
        ArrayList<Integer> iau1 = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            D[i] = matrix[i][i];
            int j = 0;
            while(matrix[i][j] == 0) {
                j++;
            }
            if (i == 0) {
                ial1.add(0);
            } else {
                ial1.add(ial1.get(i - 1) + i - j + 1);
            }
            for (; j <= i; j++) {
                al1.add(matrix[i][j]);
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            int j = 0;
            while(matrix[j][i] == 0) {
                j++;
            }
            if (i == 0) {
                iau1.add(0);
            } else {
                iau1.add(iau1.get(i - 1) + i - j + 1);
            }
            for (; j <= i; j++) {
                au1.add(matrix[j][i]);
            }
        }
        L = al1.stream().mapToDouble(i -> i).toArray();
        U = au1.stream().mapToDouble(i -> i).toArray();
        IL = ial1.stream().mapToInt(i -> i).toArray();
        IU = iau1.stream().mapToInt(i -> i).toArray();
    }

    public ProfileMatrix(double[] D, double[] L, int[] IL, double[] U, int[] IU) {
        this.D = D;
        this.L = L;
        this.IL = IL;
        this.U = U;
        this.IU = IU;
    }

    public int size() {
        return D.length;
    }

    public double get(int i, int j) throws MatrixFormatException {
        if (isLU) {
            throw new MatrixFormatException("LU modification was made");
        }
        if (i == j) {
            return D[i];
        }
        if (i > j) {
            return getByParams(L, IL, i, j);
        }
        return getByParams(U, IU, j, i);
    }

    public double getFromU(int i, int j) throws MatrixFormatException {
        if (!isLU) {
            throw new MatrixFormatException("LU modification wasn't made");
        }
        if (i == j) {
            return D[i];
        }
        if (i < j) {
            return getByParams(U, IU, j, i);
        }
        return 0.0;
    }

    public double getFromL(int i, int j) throws MatrixFormatException {
        if (!isLU) {
            throw new MatrixFormatException("LU modification wasn't made");
        }
        if (i == j) {
            return 1;
        }
        if (i > j) {
            return getByParams(L, IL, i, j);
        }
        return 0.0;
    }

    private double getByParams(double[] matrix, int[] indexes, int i, int j) {
        int size;
        if (i == 0) {
            size = 1;
        } else {
            size = indexes[i] - indexes[i - 1];
        }
        int index = j - (i - size + 1);
        if (index < 0) {
            return 0;
        }
        return matrix[indexes[i - 1] + 1 + index];
    }

    private void setByParams(double[] matrix, int[] indexes, int i, int j, double value) {
        if (i == j) {
            D[i] = value;
            return;
        }
        int size = indexes[i] - indexes[i - 1];
        int index = j - (i - size + 1);
        if (index < 0) {
            return;
        }
        matrix[indexes[i - 1] + 1 + index] = value;
    }

    public void LUDecomposition() throws MatrixFormatException {
        if (isLU) {
            throw new MatrixFormatException("LU modification was made");
        }
        for (int i = 1; i < size(); i++) {
            for (int j = 0; j < i; j++) {
                double sum = 0;
                for (int k = 0; k < j; k++) {
                    sum += get(i, k) * get(k, j);
                }
                //System.out.println("In L: " + i + " " + j + " " + (get(i, j) - sum));
                setByParams(L, IL, i, j, (get(i, j) - sum) / get(j, j));
            }
            for (int j = 0; j < i; j++) {
                double sum = 0;
                for (int k = 0; k < j; k++) {
                    sum += get(j, k) * get(k, i);
                }
                setByParams(U, IU, i, j, (get(j, i) - sum));
            }
            double sum = 0;
            for (int k = 0; k < i; k++) {
                sum += get(i, k) * get(k, i);
            }
            setByParams(L, IL, i, i, get(i, i) - sum);
        }
        isLU = true;
    }

    public void printLU() throws MatrixFormatException {
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                System.out.print(getFromL(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();

        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                System.out.print(getFromU(i, j) + " ");
            }
            System.out.println();
        }
    }

    public void print() throws MatrixFormatException {
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                System.out.print(get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws MatrixFormatException {
        ProfileMatrix profileMatrix = new ProfileMatrix(new double[][]{
                {1, 0.5,  0.3333333333333333},
                {0.5,  0.3333333333333333, 0.25},
                {0.3333333333333333, 0.25, 0.2}});

        int sz = profileMatrix.size();
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                System.out.print(profileMatrix.get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();

        profileMatrix.LUDecomposition();

        //System.out.println(Arrays.toString(profileMatrix.L));
        //System.out.println(Arrays.toString(profileMatrix.U));
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                System.out.print(profileMatrix.getFromL(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();

        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                System.out.print(profileMatrix.getFromU(i, j) + " ");
            }
            System.out.println();
        }
    }
}
