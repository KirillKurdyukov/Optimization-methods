package methods;

import format.MatrixFileException;

public abstract class TestAbstract {

    public abstract void process(String arg, int k) throws MatrixFileException;

    protected void testDense1() {
        try {
            System.out.println("n  | k | |x* - x| | |x* - x| / |x*|");
            for (int i = 10; i <= 1000; i *= 10) {
                for (int j = 1; j <= 10; j++) {
                    process("Lab3/tests/matrixDense" + i + "_" + j, j);
                }
            }
        } catch (MatrixFileException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void testDense2() {
        try {
            System.out.println("n  | |x* - x| | |x* - x| / |x*|");
            for (int j = 2; j <= 10; j++) {
                process("Lab3/tests/matrixGilbert" + j, -1);
            }
        } catch (MatrixFileException e) {
            System.err.println(e.getMessage());
        }
    }
}
