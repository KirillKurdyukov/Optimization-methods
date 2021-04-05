package methods;

import java.util.AbstractList;
import java.util.ArrayList;

public abstract class Vector <T> extends AbstractList<T> {

    protected final ArrayList<T> arguments;

    public Vector(ArrayList<T> arguments) {
        this.arguments = arguments;
    }

    public T get(int i) {
        return arguments.get(i);
    }

    public int size() {
        return arguments.size();
    }

    /**
     * For test method.
     */
    public void output() {
        for (var cur : arguments)
            System.out.println(cur);
    }

}
