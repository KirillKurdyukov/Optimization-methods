package engine;

import graphic.CoordinatePlane;
import methods.FastestGradient;
import methods.Gradient;
import methods.GradientDescent;
import methods.VectorNumbers;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.function.Function;

import static methods.Tester.*;

public class Engine2 extends BasicGame {
    CoordinatePlane plane;
    private final Button changeMode;
    private final Button changeFunc;
    private final Button start;
    private Mode2 currentMode;
    public static FMode mode;
    private Function<VectorNumbers, Double> function;
    private Gradient gradient;

    public Engine2(String title) {
        super(title);
        currentMode = Mode2.FASTEST_GRADIENT;
        changeMode = new Button(75, 50, 150, 30, "Switch mode");
        changeFunc = new Button(75, 150, 150, 30, "Switch function");
        start = new Button(75, 100, 150, 30, "Draw");
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer container = new AppGameContainer(new Engine2("Optimization"));
        container.setDisplayMode(1280, 720, false);
        container.setShowFPS(false);
        container.setTargetFrameRate(128);
        container.start();
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        plane = new CoordinatePlane(gameContainer.getWidth() / 2d, gameContainer.getHeight() / 2d);
        mode = FMode.FUNCTION1;
        setFunction();
    }

    private void checkButtons(Input input) {
        int x1 = input.getMouseX();
        int y1 = input.getMouseY();
        if (changeMode.isTouched(x1, y1)) {
            plane.clear();
            currentMode = Mode2.values()[(currentMode.ordinal() + 1) % Mode2.values().length];
        }
        if (changeFunc.isTouched(x1, y1)) {
            plane.clear();
            mode = FMode.values()[(mode.ordinal() + 1) % FMode.values().length];
        }
        setFunction();
        if (start.isTouched(x1, y1)) {
            plane.clear();
            switch (currentMode) {
                case FASTEST_GRADIENT:
                    FastestGradient.run(function, gradient, 2);
                    addFunctions(FastestGradient.vectors);
                    break;
                case GRADIENT_DESCENT:
                    GradientDescent.run(function, gradient);
                    addFunctions(GradientDescent.vectors);
            }
        }
    }

    private void setFunction() {
        switch (mode) {
            case FUNCTION1:
                function = function1;
                gradient = gradient1;
                break;
            case FUNCTION2:
                function = function2;
                gradient = gradient2;
                break;
            default:
                function = function3;
                gradient = gradient3;
        }
    }

    private Function<Double, Double> first(double z) {
        switch (mode) {
            case FUNCTION1:
                return (x -> (1 / 64d) * (Math.sqrt(-127 * x * x + 2530 * x - 607 + 64 * z) - 63 * x - 15));
            case FUNCTION2:
                return (x -> (1 / 198d) * (Math.sqrt(-788 * x * x + 34092 * x - 35955 + 396 * z) - 196 * x + 9));
            default:
                return (x -> (1 / 2d) * (Math.sqrt(-39 * x * x + 14 * x - 23 + 4 * z) + x - 3));
        }
    }

    private Function<Double, Double> second(double z) {
        switch (mode) {
            case FUNCTION1:
                return (x -> (1 / 64d) * (-Math.sqrt(-127 * x * x + 2530 * x - 607 + 64 * z) - 63 * x - 15));
            case FUNCTION2:
                return (x -> (1 / 198d) * (-Math.sqrt(-788 * x * x + 34092 * x - 35955 + 396 * z) - 196 * x + 9));
            default:
                return (x -> (1 / 2d) * (-Math.sqrt(-39 * x * x + 14 * x - 23 + 4 * z) + x - 3));
        }
    }

    private void addFunctions(ArrayList<VectorNumbers> functions) {
        for (VectorNumbers vectorNumber : functions) {
            plane.addVector(vectorNumber);
            double z = function.apply(vectorNumber);
            plane.addFunction(first(z));
            plane.addFunction(second(z));
        }
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        Input input = gameContainer.getInput();
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            checkButtons(input);
        }
        plane.changePlane(input);
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.setWorldClip(0, 0, gameContainer.getWidth(), gameContainer.getHeight());
        graphics.setBackground(Color.white);
        graphics.setColor(Color.black);
        plane.draw(graphics);
        graphics.drawString("Mode: " + currentMode + " Function: " + mode, 10, 10);
        start.draw(graphics);
        changeMode.draw(graphics);
        changeFunc.draw(graphics);
        graphics.clearWorldClip();
    }
}
