package processing;

import engine.Engine;
import graphic.CoordinatePlane;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.function.Function;

public class Process {
    private ArrayList<Trie> results;
    private int currentIndex;
    private Color[] colors;

    public Process(Function<Trie, ArrayList<Trie>> function, Trie start) {
        this.results = function.apply(start);
        results.add(0, start);
        currentIndex = 0;
        setColors();
    }

    private void setColors() {
        colors = new Color[results.size()];
        assert colors.length > 0;
        colors[0] = Color.red;
        for (int i = 1; i < results.size(); i++) {
            int rgbValue = java.awt.Color.HSBtoRGB((float) Math.random(), 1, 1);
            colors[i] = new Color(rgbValue);
        }
    }

    public void draw(CoordinatePlane plane, Graphics g) {
        Trie currentStage = results.get(currentIndex);
        g.setColor(colors[currentIndex]);
        plane.drawVerticalLine(g, plane.translateX(currentStage.getX1()));
        plane.drawVerticalLine(g, plane.translateX(currentStage.getX2()));
        plane.drawHorizontalLine(g, plane.translateY(-currentStage.getY()));
        g.setColor(Color.black);
        g.drawString("Mode: " + Engine.currentMode + " Segment: [" + currentStage.getX1() + ", "
                        + currentStage.getX2() + "] Value: " + currentStage.getY(),
                10, 10);
    }

    public void process() {
        if (currentIndex < results.size() - 1) {
            currentIndex++;
        }
    }
}
