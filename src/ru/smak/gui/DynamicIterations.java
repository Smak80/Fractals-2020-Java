package ru.smak.gui;

import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.video.MediaTools;

public class DynamicIterations {

    public static int getIters(CartesianScreenPlane p){
        return (int)(Math.min(10000,200*(Math.abs(-2-1)/Math.abs(p.xMax-p.xMin))*(Math.abs(-1-1)/Math.abs(p.yMax-p.yMin))));
    }

}
