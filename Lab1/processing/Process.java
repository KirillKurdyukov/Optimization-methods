package processing;

import engine.Engine;
import graphic.CoordinatePlane;
import org.newdawn.slick.Graphics;

import java.util.function.Function;

public class Process {
    private final Function<Trie, Trie> function;
    private Trie currentResults;

    public Process(Function<Trie, Trie> function, Trie currentResults) {
        this.function = function;
        this.currentResults = currentResults;
    }

    public void draw(CoordinatePlane plane, Graphics g) {
        plane.drawVerticalLine(g, plane.translateX(currentResults.getX1()));
        plane.drawVerticalLine(g, plane.translateX(currentResults.getX2()));
        plane.drawHorizontalLine(g, plane.translateY(-currentResults.getY()));
        g.drawString("Mode: " + Engine.currentMode + " Segment: [" + currentResults.getX1() + ", " + currentResults.getX2() + "] Value: " + currentResults.getY(),
                10, 10);
    }

    public void process() {
        if (!currentResults.isCOMPLETE()) {
            currentResults = function.apply(new Trie(currentResults.getX1(), currentResults.getX2()));
        }
    }
}
