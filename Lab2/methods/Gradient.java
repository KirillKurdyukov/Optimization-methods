package methods;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Gradient extends Vector<Function<VectorNumbers, Double>>  {

    public Gradient(List<Function<VectorNumbers, Double>> arguments) {
        super(new ArrayList<>(arguments));
    }

    public VectorNumbers evaluate(VectorNumbers vector) {
        return new VectorNumbers(IntStream.range(0, vector.size())
                .mapToObj(i -> arguments.get(i).apply(vector))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    public Double module(VectorNumbers vector) {
        return Math.sqrt(IntStream.range(0, this.size())
        .mapToObj(i -> this.arguments.get(i).apply(vector))
        .map(i -> i * i)
        .reduce(Double::sum)
                .orElseThrow());
    }

}
