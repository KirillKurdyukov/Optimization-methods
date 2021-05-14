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
        ProfileMatrix profileMatrix1 = new ProfileMatrix(new double[][]{
                {1, 0.5, 0.3333333333333333},
                {0.5, 0.3333333333333333, 0.25},
                {0.3333333333333333, 0.25, 0.2}});

        ProfileMatrix profileMatrix = new ProfileMatrix(new double[][]{
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}});

        double[] b = {1, 1, 1};

        System.out.println(Arrays.toString(LUMethod.solve(profileMatrix, b)));
    }
}


