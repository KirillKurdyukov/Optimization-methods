import java.util.Arrays;

class LU {
    int[][] mat;
    int n;
    int[][] lower;
    int[][] upper;

    public LU (int[][] mat) {
        this.mat = mat;
        this.n = mat.length;
    }

    public void luDecomposition() {
        lower = new int[n][n];
        upper = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int k = i; k < n; k++) {
                int sum = 0;
                for (int j = 0; j < i; j++)
                    sum += (lower[i][j] * upper[j][k]);
                upper[i][k] = mat[i][k] - sum;
            }
            for (int k = i; k < n; k++) {
                if (i == k)
                    lower[i][i] = 1;
                else {
                    int sum = 0;
                    for (int j = 0; j < i; j++)
                        sum += (lower[k][j] * upper[j][i]);
                    lower[k][i] = (mat[k][i] - sum) / upper[i][i];
                }
            }
        }
    }

    public static void main(String arr[]) {
        int mat[][] = { { 1, 2, -3 },
                        { 4, 5, 6 },
                        { 7, -8, 9 } };
        LU lu = new LU(mat);
        lu.luDecomposition();
        System.out.println(Arrays.deepToString(lu.lower));
        System.out.println(Arrays.deepToString(lu.upper));
    }
}
