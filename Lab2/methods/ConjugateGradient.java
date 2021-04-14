package methods;

public class ConjugateGradient {
    public static VectorNumbers run(double eps, SquareMatrix A, VectorNumbers x, VectorNumbers b) {
        int k = 0;
        VectorNumbers r_k = b.subtract(A.multiply(x));
        VectorNumbers p_k = b.subtract(A.multiply(x));
        VectorNumbers Apk;

        double RdR = r_k.scalar(r_k);
        do {
            Apk = A.multiply(p_k);
            double alpha_k = RdR /  p_k.scalar(Apk) ;

            x = x.add(p_k.multiplyConst(alpha_k));

            r_k = r_k.add(Apk.multiplyConst(-alpha_k));

            double newRdR = r_k.scalar(r_k);

            if(newRdR < eps*eps)
                return x;

            double beta_k = newRdR/RdR;

            p_k = p_k.multiplyConst(beta_k).add(r_k);

            RdR = newRdR;
        }
        while(k++ < A.rows());

        return x;
    }

}