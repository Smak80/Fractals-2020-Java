package ru.smak.gui.graphics.fractalcolors.shemes;

import ru.smak.gui.graphics.fractalcolors.Colorizer;

import java.awt.*;

public class ColorScheme2 implements Colorizer {

    @Override
    public Color getColor(float x) {
        var r = (float)(0.5 + 0.5*Math.cos(3 + x*0.15));
        var g = (float)(0.5 + 0.5*Math.cos(3 + x*0.15 + 0.6));
        var b = (float)(0.5 + 0.5*Math.cos(3 + x*0.15 + 1.0));
        return new Color(r, g, b);
    }
}
