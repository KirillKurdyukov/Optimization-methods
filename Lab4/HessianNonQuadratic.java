import methods.VectorNumbers;

import java.util.function.Function;
import java.util.stream.IntStream;

public class HessianNonQuadratic implements Hessian {

    private final Function<VectorNumbers, Double> [][] hessian;
    private final int size;

    public HessianNonQuadratic(Function<VectorNumbers, Double> [][] hessian) {
        this.hessian = hessian;
        this.size = hessian.length;
    }

    @Override
    public double[][] getNumbersHessian(double[] x) {
        VectorNumbers v = new VectorNumbers(x);
        return (double[][]) IntStream.range(0, size)
                .mapToObj(i -> IntStream.range(0, size).
                        mapToDouble(j -> hessian[i][j].apply(v))
                        .toArray())
                .toArray();
    }

}
