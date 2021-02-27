package engine;

import graphic.CoordinatePlane;
import org.newdawn.slick.*;
import processing.Process;
import processing.Trie;

import java.util.function.Function;

public class Engine extends BasicGame {
    private final Function<Double, Double> FUNCTION;
    private final MinimizationImpl IMPLEMENTATION;

    private final Button switchButton;
    private final Button startButton;
    private final Button restartButton;
    private CoordinatePlane plane;
    private Process process;

    public static Mode currentMode;

    public static void main(String[] args) throws SlickException {
        AppGameContainer container = new AppGameContainer(new Engine("Optimization"));
        container.setDisplayMode(1280, 720, false);
        container.setShowFPS(false);
        container.setTargetFrameRate(128);
        container.start();
    }

    public Engine(String title) {
        super(title);
        switchButton = new Button(75, 50, 150, 30, "Switch mode");
        startButton = new Button(75, 100, 150, 30, "Next iteration");
        restartButton = new Button(75, 150, 150, 30, "Restart");
        FUNCTION = (x) -> x * Math.sin(x) + 2 * Math.cos(x);
        IMPLEMENTATION = new MinimizationImpl(FUNCTION);
        currentMode = Mode.DICHOTOMY;
    }

    private Function<Trie, Trie> getCurrentFunction() {
        switch (currentMode) {
            case DICHOTOMY:
                return IMPLEMENTATION::methodDichotomy;
            case GOLDEN_RATIO:
                return IMPLEMENTATION::methodGoldenRatio;
            case PARABOLAS:
                return IMPLEMENTATION::methodParabolas;
            case FIBONACCI_NUMBERS:
                return IMPLEMENTATION::methodFibonacciNumbers;
            case BRENT:
                return IMPLEMENTATION::brent;
            default:
                return null;
        }
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        plane = new CoordinatePlane(gameContainer.getWidth() / 2d, gameContainer.getHeight() / 2d);
        plane.addFunction(FUNCTION);
        plane.addFunction(Math::exp);
        process = new Process(getCurrentFunction(), new Trie(IMPLEMENTATION.getLeft(), IMPLEMENTATION.getRight()));
        gameContainer.getGraphics().setBackground(Color.white);
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        Input input = gameContainer.getInput();
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            checkButtons(input.getMouseX(), input.getMouseY());
        }
        changePlane(input);
    }

    private void checkButtons(int x, int y) {
        if (startButton.isTouched(x, y)) {
            process.process();
        }
        if (switchButton.isTouched(x, y)) {
            currentMode = Mode.values()[(currentMode.ordinal() + 1) % Mode.values().length];
            process = new Process(getCurrentFunction(), new Trie(IMPLEMENTATION.getLeft(), IMPLEMENTATION.getRight()));
        }
        if (restartButton.isTouched(x, y)) {
            process = new Process(getCurrentFunction(), new Trie(IMPLEMENTATION.getLeft(), IMPLEMENTATION.getRight()));
        }
    }

    private void changePlane(Input input) {
        int speed = 4;
        if (input.isKeyDown(Input.KEY_W)) {
            plane.moveCenter(0, speed);
        }
        if (input.isKeyDown(Input.KEY_A)) {
            plane.moveCenter(speed, 0);
        }
        if (input.isKeyDown(Input.KEY_S)) {
            plane.moveCenter(0, -speed);
        }
        if (input.isKeyDown(Input.KEY_D)) {
            plane.moveCenter(-speed, 0);
        }
        if (input.isKeyDown(Input.KEY_1)) {
            plane.changeScale(1);
        }
        if (input.isKeyDown(Input.KEY_2)) {
            plane.changeScale(-1);
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.setColor(Color.black);
        plane.draw(graphics);
        graphics.setColor(Color.red);
        process.draw(plane, graphics);
        switchButton.draw(graphics);
        startButton.draw(graphics);
        restartButton.draw(graphics);
    }
}
