package method;

import firstLab.method.GoldenRatioMethod;
import fourthLab.derivative.Gradient;
import hesse.Hesse;
import thirdLab.matrix.StandardMatrix;

import java.util.function.Function;

import static derivative.MatrixUtilities.len;

public class OneDimMinNewtonMethod extends AbstactNewtoneMethod {
    private final Hesse H;

    public OneDimMinNewtonMethod(Hesse H) {
        this.H = H;
    }

    @Override
    protected double[] runImpl(Gradient gr, Function<double[], Double> function, double[] point) {
        double[] s, d, x = point.clone();
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            double[] gradient = gr.getGradient(x);
            // s задает направление поиска, которое может не быть спуском
            d = new thirdLab.method.GaussMethod().solve(new StandardMatrix(H.evaluate(x)), multVector(gradient, -1));
            s = multVector(d, findArgMinGolden(x, d, function));
            // Сделать сумму векторов отдельно
            x = sumVectors(x, s);
            if (len(s) <= EPSILON) {
                break;
            }
        }
        return x;
    }

    private double findArgMinGolden(double[] point, double[] d, Function<double[], Double> function) {
        return new GoldenRatioMethod(x -> function.apply(sumVectors(point, multVector(d, x))))
                .run(-100, 100, 0.00001);
    }
}
