package method;

import method.GoldenRatioMethod;
import derivative.Gradient;
import hesse.Hesse;
import thirdLab.matrix.StandardMatrix;

import java.util.function.Function;

import static thirdLab.matrix.MatrixUtilities.len;

public class CoolNewtonMethod extends AbstactNewtoneMethod{
    private final Hesse H;

    public CoolNewtonMethod(Hesse H) {
        this.H = H;
    }

    @Override
    protected double[] runImpl(Gradient gradient, Function<double[], Double> function, double[] point) {
        double[] s, d, x = point.clone();
        d = multVector(gradient.getGradient(x), -1);
        s = multVector(d, findArgMinGolden(x, d, function));
        x = sumVectors(x, s);
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            double[] grad = gradient.getGradient(x);
            double[] antiGradient = multVector(grad, -1);
            // s задает направление спуска
            s = new thirdLab.method.GaussMethod().solve(new StandardMatrix(H.evaluate(x)), antiGradient);
            if (multVectors(s, grad) < 0) {
                d = s;
            } else {
                d = antiGradient;
            }
            s = multVector(d, findArgMinGolden(x, d, function));
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
