package engine;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Button {
    private final String name;

    private final int STEP = 5;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Button(int x, int y, int width, int height, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public boolean isTouched(int mouseX, int mouseY) {
        return Math.abs(mouseX - x) < width / 2 && Math.abs(mouseY - y) < height / 2;
    }

    public void draw(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(x - width / 2f, y - height / 2f, width, height);
        g.setColor(Color.black);
        g.drawRect(x - width / 2f, y - height / 2f, width, height);
        g.drawString(name, x - width / 2f + STEP, y - height / 2f + STEP);
    }

}
