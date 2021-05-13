import format.LUMethod;
import format.MatrixFormatException;
import format.ProfileMatrix;

import java.util.Arrays;

public class Main {
    public static void main(String args[]) throws MatrixFormatException {
        /*
        2, 5
        0, 2
         */
        double[] diag = {2.0, 2.0};
        double[] al = {2.0, 2.0};
        double[] au = {2.0, 2.0};
        int[] ial = {0, 1};
        int[] iau = {0, 1};
        ProfileMatrix profileMatrix = new ProfileMatrix(diag, al, ial, au, iau);

        double[] b = {10.0, 5.0};

        System.out.println(Arrays.toString(LUMethod.solve(profileMatrix, b)));
    }
}
