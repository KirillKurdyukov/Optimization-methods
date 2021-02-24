package graphic;

import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.function.Function;

public class CoordinatePlane {
    private final int INTERVAL_NUMBER = 40;
    private final int STEP = 1;
    private final int STROKE_SIZE = 3;

    private int scale = 70;

    private final int RADIUS = scale * INTERVAL_NUMBER + 1000;

    private double x_0;
    private double y_0;

    private final ArrayList<Function<Double, Double>> FUNCTIONS;

    public CoordinatePlane(double x_0, double y_0) {
        this.x_0 = x_0;
        this.y_0 = y_0;
        FUNCTIONS = new ArrayList<>();
    }

    public void addFunction(Function<Double, Double> newFunction) {
        FUNCTIONS.add(newFunction);
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
        }
        drawPoint(g, (float) x_0, (float) y_0);
    }

    public void draw(Graphics g) {
        drawAxis(g);
        for (var function : FUNCTIONS) {
            drawFunction(function, g);
        }
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
            g.drawLine(translateX(currentX), translateY(-currentValue),
                    translateX(nextX), translateY(-nextValue));
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

    public void changeScale(int value) {
        if (scale > -value + 10 && scale < 400 - value) {
            scale += value;
        }
    }
}
