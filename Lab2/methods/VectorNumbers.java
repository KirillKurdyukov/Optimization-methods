package methods;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class VectorNumbers extends Vector<Double> {

    public VectorNumbers(List<Double> arguments) {
        super(new ArrayList<>(arguments));
    }

    public VectorNumbers add(Vector<Double> vector) {
        IntStream.range(0, this.size())
                .forEach((i) -> this.arguments.set(i, this.arguments.get(i) + vector.get(i)));
        return this;
    }

    public VectorNumbers addConst(Double c) {
        IntStream.range(0, this.size())
                .forEach((i) -> this.arguments.set(i, this.arguments.get(i) + c));
        return this;
    }

    public VectorNumbers multiplyConst(Double c) {
        IntStream.range(0, this.size())
                .forEach((i) -> this.arguments.set(i, this.arguments.get(i) * c));
        return this;
    }

    public Double module() {
        return Math.sqrt(IntStream.range(0, this.size())
                .mapToObj(i -> this.arguments.get(i) * this.arguments.get(i))
                .reduce(Double::sum).orElseThrow());
    }

}
