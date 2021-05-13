package methods;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Vector <T> extends AbstractList<T> {

    protected final ArrayList<T> arguments;

    public Vector(ArrayList<T> arguments) {
        this.arguments = arguments;
    }

    public T get(int i) {
        return arguments.get(i);
    }

    public void toSet(int i, T el) {
        arguments.set(i, el);
    }

    public int size() {
        return arguments.size();
    }

    /**
     * For test method.
     */
    public void output() {
        System.out.println(arguments.stream()
                .map(Objects::toString)
                .collect(Collectors.joining( " "))
        );
    }

}
