package methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public SquareMatrix(double [][] matrix) {
        arguments = Arrays.stream(matrix).map(VectorNumbers::new).collect(Collectors.toCollection(ArrayList::new));
    }
}
