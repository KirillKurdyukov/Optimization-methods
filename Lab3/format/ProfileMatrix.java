package format;

public class ProfileMatrix {
    private final double[] D;
    private final double[] L;
    private final double[] U;
    private final int[] IL;
    private final int[] IU;
    private boolean isLU = false;

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

    public double getFromL(int i, int j) throws MatrixFormatException {
        if (!isLU) {
            throw new MatrixFormatException("LU modification wasn't made");
        }
        if (i == j) {
            return 1.0;
        }
        if (i > j) {
            return getByParams(L, IL, i, j);
        }
        return 0.0;
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

    private double getByParams(double[] matrix, int[] indexes, int i, int j) {
        int size = indexes[i] - indexes[i - 1];
        int index = j - (i - size + 1);
        if (index < 0) {
            return 0;
        }
        return matrix[indexes[i - 1] + 1 + index];
    }

    private void setByParams(double[] matrix, int[] indexes, int i, int j, double value) {
        if (i == j) {
            D[i] = value;
        }
        int size = indexes[i] - indexes[i - 1];
        int index = j - (i - size + 1);
        matrix[indexes[i - 1] + 1 + index] = value;
    }

    public void LUDecomposition() throws MatrixFormatException {
        if (isLU) {
            throw new MatrixFormatException("LU modification was made");
        }
        for (int i = 1; i < size(); i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0;
                for (int k = 0; k < j; k++) {
                    sum += get(i, k) * get(k, j);
                }
                setByParams(L, IL, i, j, get(i, j) - sum);
            }
            for (int j = 0; j < i; j++) {
                double sum = 0;
                for (int k = 0; k < j; k++) {
                    sum += get(j, k) * get(k, i);
                }
                setByParams(U, IU, i, j, (get(j, i) - sum) / get(j, j));
            }
        }
        isLU = true;
    }

    public static void main(String[] args) throws Exception {
        /*
        2, 0
        0, 2
         */
        double[] D = {2.0, 2.0};
        double[] L = {2.0, 2.0};
        double[] U = {2.0, 2.0};
        int[] IL = {0, 1};
        int[] IU = {0, 1};
        ProfileMatrix profileMatrix = new ProfileMatrix(D, L, IL, U, IU);
        int sz = profileMatrix.size();
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                System.out.print(profileMatrix.get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
        profileMatrix.LUDecomposition();
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
