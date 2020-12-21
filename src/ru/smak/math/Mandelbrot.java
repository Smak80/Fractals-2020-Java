package ru.smak.math;

import ru.smak.gui.DynamicIterations;
import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;

public class Mandelbrot implements Fractal{

    private int maxIters = 200;
    private double r2 = 4;
    private boolean isDynamic = true;
    private CartesianScreenPlane plane;
    private double stockXMin,stockXMax,stockYMin,stockYMax;
    public void setStockParams(double xMin,double xMax,double yMin,double yMmax){
        stockXMin = xMin;
        stockXMax = xMax;
        stockYMin = yMin;
        stockYMax = yMmax;
    }
    public void setMaxIters(int value){
        maxIters = Math.max(5, value);
    }

    public void setR(int value){
        var r = Math.max(0.1, value);
        r2 = r*r;
    }
    public void setPlane(CartesianScreenPlane plane){
        this.plane = plane;
    }

    public void setDynamic(boolean isDynamic){
        this.isDynamic = isDynamic;
    }

    @Override
    public double isInSet(Complex c) {
        final var z = new Complex();
        var d= (isDynamic) ? DynamicIterations.getIters(plane,stockXMin,stockXMax,stockYMin,stockYMax) : maxIters;
            for (int i = 0; i < d; i++) {
                z.timesAssign(z);
                z.plusAssign(c);
                if (z.abs2() > r2)
                    return i - Math.log(Math.log(z.abs()) / Math.log(d)) / Math.log(2.0);
            }
            return 1.0F;

        }

}
