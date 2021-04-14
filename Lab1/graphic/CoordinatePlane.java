package graphic;

import methods.VectorNumbers;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.function.Function;

public class CoordinatePlane {
    private final int INTERVAL_NUMBER = 30;
    private final double STEP = 4;
    private final int STROKE_SIZE = 3;

    private int scale = 70;

    private final int RADIUS = scale * INTERVAL_NUMBER + 1000;

    private double x_0;
    private double y_0;

    private final ArrayList<Function<Double, Double>> FUNCTIONS;
    private final ArrayList<VectorNumbers> VECTORS;

    public CoordinatePlane(double x_0, double y_0) {
        this.x_0 = x_0;
        this.y_0 = y_0;
        FUNCTIONS = new ArrayList<>();
        VECTORS = new ArrayList<>();
    }

    public void addFunction(Function<Double, Double> newFunction) {
        FUNCTIONS.add(newFunction);
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
        for (var function : FUNCTIONS) {
            drawFunction(function, g);
        }
        g.setColor(Color.red);
        drawIntervals(g);
        g.setColor(Color.black);
    }

    public float translateX(double x) {
        return (float) (x * scale + x_0);
    }

    public float translateY(double y) {
        return (float) (y * scale + y_0);
    }

    private void drawFunction(Function<Double, Double> function, Graphics g) {
        for (double i = -INTERVAL_NUMBER * scale; i <= INTERVAL_NUMBER * scale; i += STEP) {
            double currentX = i / scale;
            double nextX = (i + STEP) / scale;
            double currentValue = function.apply(currentX);
            double nextValue = function.apply(nextX);
            if (!Double.isNaN(currentValue) && !Double.isNaN(nextValue)) {
                g.drawLine(translateX(currentX), translateY(-currentValue),
                        translateX(nextX), translateY(-nextValue));
            }
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

    public void drawIntervals(Graphics g) {
        for (int i = 0; i < VECTORS.size() - 1; i++) {
            double currentX = VECTORS.get(i).get(0);
            double nextX = VECTORS.get(i + 1).get(0);
            double currentValue = VECTORS.get(i).get(1);
            double nextValue = VECTORS.get(i + 1).get(1);
            drawArrow(g, currentX, currentValue, nextX, nextValue);
        }
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
        FUNCTIONS.clear();
        VECTORS.clear();
    }
}
