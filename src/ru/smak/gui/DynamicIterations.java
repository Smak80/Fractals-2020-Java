package ru.smak.gui;

import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;

public class DynamicIterations {

    public static int getIters(CartesianScreenPlane p,double stockXMin,double stockXMax,double stockYMin,double stockYMax){

        //     return (int)(Math.min(10000,100+Math.sqrt(Math.min((Math.abs(-2-1)/Math.abs(p.xMax-p.xMin)),(Math.abs(-1-1)/Math.abs(p.yMax-p.yMin))))));
        return (int)(Math.min(10000,100+150*Math.max(0,Math.log((Math.abs(stockXMax-stockXMin)/Math.abs(p.xMax-p.xMin))*(Math.abs(stockYMax-stockYMin)/Math.abs(p.yMax-p.yMin))))));
    }
}
