package engine;

public enum FMode {
    FUNCTION1, FUNCTION2, FUNCTION3;

    @Override
    public String toString() {
        switch (this) {
            case FUNCTION1:
                return "64x^2 + 126xy + 64y^2 - 10x + 30y + 13";
            case FUNCTION2:
                return "99x^2 + 196xy + 99y^2 + 95x - 9y + 91";
            case FUNCTION3:
                return "10x^2 - xy + y^2 - 5x + 3y + 8";
        }
        return super.toString();
    }
}
