package engine;

import graphic.CoordinatePlane;
import methods.FastestGradient;
import methods.VectorNumbers;
import org.newdawn.slick.*;

import static methods.Tester.function1;

public class Engine2 extends BasicGame {
    CoordinatePlane plane;

    public Engine2(String title) {
        super(title);
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
        //  for (int i = 0; i < ; i++) {

        //}

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
        if (input.isKeyPressed(Input.KEY_3)) {
            FastestGradient.run();
            for (VectorNumbers vectorNumber : FastestGradient.vectors) {
                plane.addVector(vectorNumber);
                double z = function1.apply(vectorNumber);
                plane.addFunction(x -> (1 / 64d) * (Math.sqrt(-127 * x * x + 2530 * x - 607 + 64 * z) - 63 * x - 15));
                plane.addFunction(x -> (1 / 64d) * (-Math.sqrt(-127 * x * x + 2530 * x - 607 + 64 * z) - 63 * x - 15));
            }
        }
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        Input input = gameContainer.getInput();
        changePlane(input);
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.setWorldClip(0, 0, gameContainer.getWidth(), gameContainer.getHeight());
        graphics.setBackground(Color.white);
        graphics.setColor(Color.black);
        plane.draw(graphics);
        graphics.clearWorldClip();
    }
}
