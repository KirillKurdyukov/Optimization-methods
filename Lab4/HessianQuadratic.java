public class HessianQuadratic implements Hessian {
    private final double[][] hessian;

    public HessianQuadratic(double [][] hessian) {
        this.hessian = hessian;
    }

    @Override
    public double[][] getNumbersHessian(double[] x) {
        return hessian;
    }

}
