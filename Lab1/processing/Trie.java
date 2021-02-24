package processing;

public class Trie {

    private final boolean COMPLETE;
    private final double X1;
    private final double X2;
    private final double Y;

    public Trie(boolean COMPLETE, double x1, double x2, double y) {
        this.COMPLETE = COMPLETE;
        X1 = x1;
        X2 = x2;
        Y = y;
    }

    public Trie(double x1, double x2) {
        this(false, x1, x2, 0);
    }

    public boolean isCOMPLETE() {
        return COMPLETE;
    }

    public double getX1() {
        return X1;
    }

    public double getX2() {
        return X2;
    }

    public double getY() {
        return Y;
    }
}



