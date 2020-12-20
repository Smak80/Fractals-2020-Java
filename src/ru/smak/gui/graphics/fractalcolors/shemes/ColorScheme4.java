package ru.smak.gui.graphics.fractalcolors.shemes;

import ru.smak.gui.graphics.fractalcolors.Colorizer;

import java.awt.*;

public class ColorScheme4 implements Colorizer {

    @Override
    public Color getColor(float x) {
        float r, g, b;
        r = (float) Math.abs(Math.sin(Math.cos(x * 0.01)));
        g = (float) Math.abs(Math.cos(x * 0.05));
        b = (float) Math.abs(Math.sin(0.5 * x));
        return new Color(r, g, b);
    }
}
