package method;

import derivative.Gradient;
import hesse.Hesse;
import thirdLab.exception.NoExactSolutionException;
import thirdLab.exception.NoSolutionException;
import thirdLab.matrix.StandardMatrix;

import java.util.function.Function;

import static thirdLab.matrix.MatrixUtilities.len;


public class BaseNewtonMethod extends AbstactNewtoneMethod{
    private final Hesse H;

    public BaseNewtonMethod(Hesse H) {
        this.H = H;
    }

    @Override
    protected double[] runImpl(Gradient gradient,
                               Function<double[], Double> function,
                               double[] point) {
        double[] s, x = point.clone();
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            // s задает направление спуска (наверное)
            //s = new thirdLab.method.GaussMethod().solve(new StandardMatrix(H.evaluate(x)), multVector(gradient.getGradient(x), -1));
            // Сделать сумму векторов отдельно
           // x = sumVectors(x, s);
            //if (len(s) <= EPSILON) {
            //    break;
            //}
        }
        return x;
    }
}
