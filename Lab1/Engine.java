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
    private CoordinatePlane plane;
    private Process process;

    public static void main(String[] args) throws SlickException {
        AppGameContainer container = new AppGameContainer(new Engine("Optimization"));
        container.setDisplayMode(1280, 720, false);
        container.setShowFPS(false);
        container.setTargetFrameRate(128);
        container.start();
    }

    public Engine(String title) {
        super(title);
        switchButton = new Button(50, 50, 100, 30);
        startButton = new Button(50, 100, 100, 30);
        FUNCTION = (x) -> x * Math.sin(x) + 2 * Math.cos(x);
        IMPLEMENTATION = new MinimizationImpl(FUNCTION);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        plane = new CoordinatePlane(gameContainer.getWidth() / 2d, gameContainer.getHeight() / 2d);
        plane.addFunction(FUNCTION);
        plane.addFunction(Math::exp);
        process = new Process(IMPLEMENTATION::methodDichotomy, new Trie(-6, -4));
        gameContainer.getGraphics().setBackground(Color.white);
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        Input input = gameContainer.getInput();
        if (startButton.isTouched(input.getMouseX(), input.getMouseY()) &&
                input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            process.process();
        }
        changePlane(input);
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
        switchButton.draw(graphics);
        startButton.draw(graphics);
        plane.draw(graphics);
        graphics.setColor(Color.red);
        process.draw(plane, graphics);
    }


}