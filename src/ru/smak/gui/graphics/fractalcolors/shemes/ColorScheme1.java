package ru.smak.gui.graphics.fractalcolors.shemes;

import ru.smak.gui.graphics.fractalcolors.Colorizer;

import java.awt.*;

public class ColorScheme1 implements Colorizer {
    @Override
    public Color getColor(float x) {
        float r, g, b;
        r = (float)Math.abs(Math.sin(Math.cos(x)));
        g = (float)Math.abs(Math.cos(1-Math.sin(50-10*x)));
        b = (float)Math.abs(Math.sin(x));
        return new Color(r, g, b);
    }
}

