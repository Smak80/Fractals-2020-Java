package ru.smak.gui.graphics.fractalcolors.shemes;

import ru.smak.gui.graphics.fractalcolors.Colorizer;

import java.awt.*;

public class ColorScheme3 implements Colorizer {

    @Override
    public Color getColor(float x) {
        float r, g, b;
        r = (float)(12*x/Math.exp(10*x));
        g = (float)Math.abs(Math.sin(x*x/5) * Math.cos(x));
        b = (float)Math.abs(Math.sin(3*x*x*x/7)/Math.cosh(9*x+3));
        return new Color(r, g, b);
    }
}
