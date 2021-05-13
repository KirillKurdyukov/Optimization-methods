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
        double[] D = {2.0, 2.0};
        double[] L = {2.0, 2.0};
        double[] U = {2.0, 2.0};
        int[] IL = {0, 1};
        int[] IU = {0, 1};
        ProfileMatrix profileMatrix = new ProfileMatrix(D, L, IL, U, IU);

        double[] b = {10.0, 5.0};

        System.out.println(Arrays.toString(LUMethod.solve(profileMatrix, b)));
    }
}
