package methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SquareMatrix {
    protected ArrayList<VectorNumbers> arguments;

    public SquareMatrix (List<VectorNumbers> arguments) {
        this.arguments = new ArrayList<>(arguments);
    }

    public int rows() {
        if (arguments.size() > 0) {
            return arguments.get(0).size();
        }
        return 0;
    }

    public VectorNumbers multiply(VectorNumbers vec) {
        ArrayList<Double> ans = new ArrayList<>(0);
        for (int i = 0; i < vec.size(); i++) {
            ans.add(arguments.get(i).scalar(vec));
        }
        return new VectorNumbers(ans);
    }

    public static void main(String[] args) {
        SquareMatrix a = new SquareMatrix(
                new ArrayList<>(List.of(
                        new VectorNumbers(List.of(1.0, 1.0)),
                        new VectorNumbers(List.of(1.0, 1.0)))));
        VectorNumbers vec = new VectorNumbers(List.of(1.0, 2.0));
        System.out.println(a.multiply(vec));
    }
}
