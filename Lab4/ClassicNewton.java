import format.DenseMatrix;
import methods.Gauss;
import methods.Gradient;
import methods.VectorNumbers;

public class ClassicNewton {

    public static double[] method(Hessian hessian,
                                  Gradient gradient,
                                  double [] x0,
                                  double eps) {
        int size = x0.length;
        VectorNumbers v0;
        VectorNumbers v1 = new VectorNumbers(x0);
        do {
            v0 = v1;
            double[][] matrixHessian = hessian.getNumbersHessian(v0.toMassive());
            VectorNumbers p = new VectorNumbers(size);
            if (Gauss.gauss(new DenseMatrix(matrixHessian, gradient.evaluate(v0).multiplyConst(-1)), p) != 2) {
                throw new RuntimeException("No one solution?!:(");
            }
            v1 = v0.add(p);
        } while (v1.subtract(v0).module() > eps);
        return v1.toMassive();
    }

}
