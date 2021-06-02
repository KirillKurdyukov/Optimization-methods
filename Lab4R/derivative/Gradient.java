package derivative;

public interface Gradient {
    double[] getGradient(double[] point);
    double[] getAntiGradient(double[] point);
}
