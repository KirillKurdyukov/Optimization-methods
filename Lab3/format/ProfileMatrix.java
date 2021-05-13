package format;

public class ProfileMatrix {
    private final double[] diag;
    private final double[] al;
    private final double[] au;
    private final int[] ial;
    private final int[] iau;
    private boolean isLU = false;

    public ProfileMatrix(double[] diag, double[] al, int[] ial, double[] au, int[] iau) {
        this.diag = diag;
        this.al = al;
        this.ial = ial;
        this.au = au;
        this.iau = iau;
    }

    public int size() {
        return diag.length;
    }

    public double get(int i, int j) throws MatrixFormatException {
        if (isLU) {
            throw new MatrixFormatException("LU modification was made");
        }
        if (i == j) {
            return diag[i];
        }
        if (i > j) {
            return getByParams(al, ial, i, j);
        }
        return getByParams(au, iau, j, i);
    }

    public double getFromL(int i, int j) throws MatrixFormatException {
        if (!isLU) {
            throw new MatrixFormatException("LU modification wasn't made");
        }
        if (i == j) {
            return 1.0;
        }
        if (i > j) {
            return getByParams(al, ial, i, j);
        }
        return 0.0;
    }

    public double getFromU(int i, int j) throws MatrixFormatException {
        if (!isLU) {
            throw new MatrixFormatException("LU modification wasn't made");
        }
        if (i == j) {
            return diag[i];
        }
        if (i < j) {
            return getByParams(au, iau, j, i);
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
            diag[i] = value;
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
                setByParams(al, ial, i, j, get(i, j) - sum);
            }
            for (int j = 0; j < i; j++) {
                double sum = 0;
                for (int k = 0; k < j; k++) {
                    sum += get(j, k) * get(k, i);
                }
                setByParams(au, iau, i, j, (get(j, i) - sum) / get(j, j));
            }
        }
        isLU = true;
    }

    public static void main(String[] args) throws Exception {
        /*
        1, 5
        0, 1
         */
        double[] diag = {2.0, 2.0};
        double[] al = {2.0, 2.0};
        double[] au = {2.0, 2.0};
        int[] ial = {0, 1};
        int[] iau = {0, 1};
        ProfileMatrix profileMatrix = new ProfileMatrix(diag, al, ial, au, iau);
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
