package processing;

public class Trie {

    private final double X1;
    private final double X2;
    private final double Y;

    public Trie(double x1, double x2, double y) {
        X1 = x1;
        X2 = x2;
        Y = y;
    }

    public Trie(double x1, double x2) {
        this(x1, x2, 0);
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



