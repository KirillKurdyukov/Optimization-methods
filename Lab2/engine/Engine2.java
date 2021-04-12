package engine;

import graphic.CoordinatePlane;
import org.newdawn.slick.*;

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
        plane.addFunction(a -> Math.sqrt(1 - a * a));
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
    public void update(GameContainer gameContainer, int i) throws SlickException {
        Input input = gameContainer.getInput();
        changePlane(input);
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.setBackground(Color.white);
        graphics.setColor(Color.black);
        plane.draw(graphics);
    }
}
