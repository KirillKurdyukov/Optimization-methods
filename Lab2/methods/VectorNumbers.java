package methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VectorNumbers extends Vector<Double> {

    public VectorNumbers(List<Double> arguments) {
        super(new ArrayList<>(arguments));
    }

    public VectorNumbers(double [] a) {
        super(new ArrayList<>(Arrays.stream(a).boxed().collect(Collectors.toList())));
    }

    public VectorNumbers(int size) {
        super(new ArrayList<>(Collections.nCopies(size, null)));
    }

    public VectorNumbers add(Vector<Double> vector) {
        return new VectorNumbers(IntStream.range(0, this.size())
                .mapToObj(i ->  this.arguments.get(i) + vector.get(i))
                .collect(Collectors.toList()));
    }

    public VectorNumbers addConst(double val) {
        return new VectorNumbers(IntStream.range(0, this.size())
                .mapToObj(i -> this.arguments.get(i) + val)
                .collect(Collectors.toList()));
    }

    public VectorNumbers subtract(VectorNumbers vector) {
        return add(vector.multiplyConst(-1));
    }

    public VectorNumbers multiplyConst(double val) {
        return new VectorNumbers(IntStream.range(0, this.size())
                .mapToObj(i -> this.arguments.get(i) * val)
                .collect(Collectors.toList()));
    }

    public Double module() {
        return Math.sqrt(IntStream.range(0, this.size())
                .mapToObj(i -> this.arguments.get(i) * this.arguments.get(i))
                .reduce(Double::sum)
                .orElseThrow());
    }

    public double scalar(VectorNumbers vec) {
        return IntStream.range(0, this.size())
                .mapToObj(i -> this.arguments.get(i) * vec.get(i))
                .reduce(Double::sum)
                .orElseThrow();
    }

    public double [] toMassive() {
        return arguments.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

}
