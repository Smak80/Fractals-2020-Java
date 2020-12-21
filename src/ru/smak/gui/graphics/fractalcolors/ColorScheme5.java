package ru.smak.gui.graphics.fractalcolors;

import ru.smak.gui.graphics.fractalcolors.Colorizer;

import java.awt.*;

public class ColorScheme5 implements Colorizer {
    @Override
    public Color getColor(float x) {
        float r, g, b;
        r = (float) (0.5 + 0.5 * Math.cos(3 + x * 0.15 + 1.0));
        g = (float) (0.5 + 0.5 * Math.cos(3 + x * 0.15 + 0.6));
        b = (float) (0.5 + 0.5 * Math.cos(3 + x * 0.15));
        return new Color(r, g, b);
    }
}