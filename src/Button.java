import org.newdawn.slick.Graphics;

public class Button {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Button(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isTouched(int mouseX, int mouseY) {
        return Math.abs(mouseX - x) < width / 2 && Math.abs(mouseY - y) < height / 2;
    }

    public void draw(Graphics g) {
        g.fillRect(x - width / 2f, y - height / 2f, width, height);
    }

}
