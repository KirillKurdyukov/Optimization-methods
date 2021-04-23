package graphic;

import methods.VectorNumbers;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CoordinatePlane {
    private final int INTERVAL_NUMBER = 30;
    private final int STROKE_SIZE = 3;

    private int scale = 70;

    private final int RADIUS = scale * INTERVAL_NUMBER + 1000;
    private int currentIter;
    private double x_0;
    private double y_0;
    private final ArrayList<Iteration> iterations;
    private final ArrayList<FuncWrap> FUNCTIONS;
    private final ArrayList<VectorNumbers> VECTORS;

    private static class Iteration {
        ArrayList<FuncWrap> functions;
        ArrayList<VectorNumbers> vectors;

        public Iteration(ArrayList<FuncWrap> functions, ArrayList<VectorNumbers> vectors) {
            this.functions = functions;
            this.vectors = vectors;
        }
    }

    public CoordinatePlane(double x_0, double y_0) {
        this.x_0 = x_0;
        this.y_0 = y_0;
        currentIter = -1;
        FUNCTIONS = new ArrayList<>();
        VECTORS = new ArrayList<>();
        iterations = new ArrayList<>();
    }

    private static class FuncWrap {
        private final Function<Double, Double> function;
        private final double x1;
        private final double x2;

        private double searchL(double l, double r) {
            if (r - l < 0.0000001d) {
                return r;
            }
            double mX = (l + r) / 2;
            double mid = function.apply(mX);
            if (Double.isNaN(mid)) {
                return searchL(mX, r);
            } else {
                return searchL(l, mX);
            }
        }

        private double searchR(double l, double r) {
            if (r - l < 0.0000001d) {
                return l;
            }
            double mX = (l + r) / 2;
            double mid = function.apply(mX);
            if (Double.isNaN(mid)) {
                return searchR(l, mX);
            } else {
                return searchR(mX, r);
            }
        }

        public FuncWrap(Function<Double, Double> function) {
            this.function = function;
            boolean setX1 = false;
            double step = 0.1d;
            double l1 = -100;
            double r1 = 100;
            for (double i = -100; i <= 100; i += step) {
                double nextX = i + step;
                double currentValue = function.apply(i);
                double nextValue = function.apply(nextX);
                if (!Double.isNaN(currentValue) && !setX1) {
                    l1 = i - step;
                    setX1 = true;
                }
                if (!Double.isNaN(currentValue) && Double.isNaN(nextValue)) {
                    r1 = i + step;
                    break;
                }
            }
            x1 = searchL(l1, r1);
            x2 = searchR(x1, 100);
        }

        public Function<Double, Double> getFunction() {
            return function;
        }

        public double getX1() {
            return x1;
        }

        public double getX2() {
            return x2;
        }
    }

    public void addFunction(Function<Double, Double> newFunction) {
        FUNCTIONS.add(new FuncWrap(newFunction));
    }

    public void addIteration(List<Function<Double, Double>> functions, ArrayList<VectorNumbers> vectors) {
        ArrayList<FuncWrap> funcWraps = new ArrayList<>();
        for (var f : functions) {
            funcWraps.add(new FuncWrap(f));
        }
        iterations.add(new Iteration(funcWraps, vectors));
    }

    public void addVector(VectorNumbers vector) {
        VECTORS.add(vector);
    }

    public void drawPoint(Graphics g, float x, float y) {
        float rad = 4;
        g.fillOval(x - rad, y - rad, rad * 2, rad * 2);
    }

    public void drawVerticalLine(Graphics g, float x) {
        g.drawLine(x, (float) (-RADIUS + y_0), x, (float) (RADIUS + y_0));
    }

    public void drawHorizontalLine(Graphics g, float y) {
        g.drawLine((float) (-RADIUS + x_0), y, (float) (RADIUS + x_0), y);
    }

    private void drawAxis(Graphics g) {
        drawHorizontalLine(g, (float) y_0);
        drawVerticalLine(g, (float) x_0);
        for (int i = -INTERVAL_NUMBER; i <= INTERVAL_NUMBER; i++) {
            float currentPositionX = translateX(i);
            float currentPositionY = translateY(i);
            g.drawLine(currentPositionX, (float) (y_0 - STROKE_SIZE), currentPositionX, (float) (y_0 + STROKE_SIZE));
            g.drawLine((float) (x_0 - STROKE_SIZE), currentPositionY, (float) (x_0 + STROKE_SIZE), currentPositionY);
            g.drawString(String.valueOf(i), currentPositionX, (float) (y_0 + STROKE_SIZE));
            if (i != 0) {
                g.drawString(String.valueOf(-i), (float) (x_0 + STROKE_SIZE), currentPositionY);
            }
        }
        drawPoint(g, (float) x_0, (float) y_0);
    }

    public void draw(Graphics g) {
        drawAxis(g);
        ArrayList<FuncWrap> fs;
        ArrayList<VectorNumbers> vs;
        if (currentIter == -1) {
            fs = FUNCTIONS;
            vs = VECTORS;
        } else {
            fs = iterations.get(currentIter).functions;
            vs = iterations.get(currentIter).vectors;
        }
        for (var function : fs) {
            drawFunction(function, g);
        }
        g.setColor(Color.red);
        drawIntervals(g, vs);
        g.setColor(Color.black);
    }

    public void drawIntervals(Graphics g, ArrayList<VectorNumbers> vectors) {
        if (vectors.isEmpty()) {
            return;
        }
        double currentX = 0;
        double nextX = 0;
        double currentValue = 0;
        double nextValue = 0;
        for (int i = 0; i < vectors.size() - 1; i++) {
            currentX = vectors.get(i).get(0);
            nextX = vectors.get(i + 1).get(0);
            currentValue = vectors.get(i).get(1);
            nextValue = vectors.get(i + 1).get(1);
            drawArrow(g, currentX, currentValue, nextX, nextValue);
            g.setColor(Color.red);
        }
        drawArrow(g, currentX, currentValue, nextX, nextValue);
        g.setColor(Color.red);

    }

    public void nextIteration() {
        if (iterations.size() == 0) {
            return;
        }
        currentIter = (currentIter + 1) % iterations.size();
    }

    public float translateX(double x) {
        return (float) (x * scale + x_0);
    }

    public float translateY(double y) {
        return (float) (y * scale + y_0);
    }

    private double getStart(Function<Double, Double> function, Graphics g, double x1, double x2, boolean left) {
        double step = 0.0001d;
        for (double i = -x1; i <= x2; i += step) {
            double nextX = i + step;
            double currentValue = function.apply(i);
            double nextValue = function.apply(nextX);
            if (left) {
                if (!Double.isNaN(currentValue)) {
                    return i;
                }
            } else {
                if (!Double.isNaN(nextValue)) {
                    return nextX;
                }
            }
        }
        return left ? x1 : x2;
    }

    private void drawFunction(FuncWrap function, Graphics g) {
        Function<Double, Double> f = function.getFunction();
        double step = 0.01d;
        for (double i = function.getX1(); i <= function.getX2(); i += step) {
            double currentValue = f.apply(i);
            double nextX = i + step;
            if (nextX > function.getX2()) {
                nextX = function.getX2();
            }
            double nextValue = f.apply(nextX);
            g.drawLine(translateX(i), translateY(-currentValue),
                    translateX(nextX), translateY(-nextValue));
        }
    }

    private void drawArrow(Graphics g, double x1, double y1, double x2, double y2) {
        double size = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double xAn = Math.acos((x2 - x1) / size);
        double yAn = Math.asin((y2 - y1) / size);
        float newCos1 = (float) Math.cos(xAn + Math.PI / 8) * 10;
        float newCos2 = (float) Math.cos(xAn - Math.PI / 8) * 10;
        float newSin1 = (float) Math.sin(yAn + Math.PI / 8) * 10;
        float newSin2 = (float) Math.sin(yAn - Math.PI / 8) * 10;
        g.drawLine(translateX(x1), translateY(-y1),
                translateX(x2), translateY(-y2));
        g.drawLine(translateX(x2), translateY(-y2), -newCos1 +
                translateX(x2), newSin1 + translateY(-y2));
        g.drawLine(translateX(x2), translateY(-y2),
                -newCos2 + translateX(x2), newSin2 + translateY(-y2));
    }

    public int getScale() {
        return scale;
    }

    public double getX_0() {
        return x_0;
    }

    public double getY_0() {
        return y_0;
    }

    public void moveCenter(double x, double y) {
        x_0 += x;
        y_0 += y;
    }

    public void changePlane(Input input) {
        int speed = 4;
        if (input.isKeyDown(Input.KEY_W)) {
            moveCenter(0, speed);
        }
        if (input.isKeyDown(Input.KEY_A)) {
            moveCenter(speed, 0);
        }
        if (input.isKeyDown(Input.KEY_S)) {
            moveCenter(0, -speed);
        }
        if (input.isKeyDown(Input.KEY_D)) {
            moveCenter(-speed, 0);
        }
        if (input.isKeyDown(Input.KEY_1)) {
            changeScale(1);
        }
        if (input.isKeyDown(Input.KEY_2)) {
            changeScale(-1);
        }
    }

    public void changeScale(int value) {
        if (scale > -value + 10 && scale < 400 - value) {
            scale += value;
        }
    }

    public void clear() {
        iterations.clear();
        FUNCTIONS.clear();
        VECTORS.clear();
        currentIter = -1;
    }
}
