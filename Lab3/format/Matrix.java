package format;

import methods.VectorNumbers;

public interface Matrix {
    public int size();

    public double get(int i, int j);

    public void set(int i, int j, double element);

    public void setFreeVector(VectorNumbers b);

    public double getFreeVector(int i);

    public void setFreeVectorNum(int i, double el);

    public void swapRow(int i, int j);
}
