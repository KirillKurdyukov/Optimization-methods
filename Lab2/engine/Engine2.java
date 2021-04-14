package engine;

import graphic.CoordinatePlane;
import methods.FastestGradient;
import methods.GradientDescent;
import methods.VectorNumbers;
import org.newdawn.slick.*;

import java.util.ArrayList;

import static methods.Tester.function1;

public class Engine2 extends BasicGame {
    CoordinatePlane plane;
    private final Button changeMode;
    private final Button start;
    private Mode2 currentMode;

    public Engine2(String title) {
        super(title);
        currentMode = Mode2.FASTEST_GRADIENT;
        changeMode = new Button(75, 50, 150, 30, "Switch mode");
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
    }

    private void checkButtons(Input input) {
        int x1 = input.getMouseX();
        int y1 = input.getMouseY();
        if (changeMode.isTouched(x1, y1)) {
            plane.clear();
            currentMode = Mode2.values()[(currentMode.ordinal() + 1) % Mode2.values().length];
        }
        if (start.isTouched(x1, y1)) {
            plane.clear();
            switch (currentMode) {
                case FASTEST_GRADIENT:
                    FastestGradient.run();
                    addFunctions(FastestGradient.vectors);
                    break;
                case GRADIENT_DESCENT:
                    GradientDescent.run();
                    addFunctions(GradientDescent.vectors);
            }
        }
    }

    private void addFunctions(ArrayList<VectorNumbers> functions) {
        for (VectorNumbers vectorNumber : functions) {
            plane.addVector(vectorNumber);
            double z = function1.apply(vectorNumber);
            plane.addFunction(x -> (1 / 64d) * (Math.sqrt(-127 * x * x + 2530 * x - 607 + 64 * z) - 63 * x - 15));
            plane.addFunction(x -> (1 / 64d) * (-Math.sqrt(-127 * x * x + 2530 * x - 607 + 64 * z) - 63 * x - 15));
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
        graphics.drawString("Mode: " + currentMode, 10, 10);
        start.draw(graphics);
        changeMode.draw(graphics);
        graphics.clearWorldClip();
    }
}
