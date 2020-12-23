package ru.smak.math;

import ru.smak.gui.DynamicIterations;
import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;

public class Mandelbrot implements Fractal{

    private int maxIters = 200;
    private double r2 = 4;
    private boolean isDynamic = false;
    private int index = 0;
    private CartesianScreenPlane plane;

    private boolean isJulia = false;
    private Complex juliaParams;

    public void setJuliaParams(double x, double y){
        juliaParams = new Complex(x, y);
    }

    private double stockXMin,stockXMax,stockYMin,stockYMax;
    public void setStockParams(double xMin,double xMax,double yMin,double yMax){
        stockXMin = xMin;
        stockXMax = xMax;
        stockYMin = yMin;
        stockYMax = yMax;
    }

    public void setJulia(){
        isJulia = !isJulia;
    }

    public void setIndex(int index){
        this.index = index;
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
        var d= (isDynamic) ? DynamicIterations.getIters(plane,stockXMin,stockXMax,stockYMin,stockYMax) : maxIters;
        if(!isJulia){
            final var z = new Complex();
            var t = new Complex();
            for (int i = 0; i < d; i++) {
                if(index == 0){
                    z.timesAssign(z);
                    z.plusAssign(c);
                }
                else if(index == 1){
                    t = z.times(z);//t=z^2, z=z
                    z.timesAssign(t);//z=z*z^2 = z^3
                    z.plusAssign(c);
                }
                else if(index == 2){
                    z.timesAssign(z);
                    z.timesAssign(z);
                    z.plusAssign(c);
                }
                else{
                    t = z.times(z);//t=z^2, z=z
                    z.timesAssign(t);//z=z*z^2 = z^3
                    z.timesAssign(z);
                    z.plusAssign(c);
                }
                if (z.abs2() > r2)
                    return i - Math.log(Math.log(z.abs()) / Math.log(d)) / Math.log(2.0);
            }
            return 1.0F;

        }
        else{
            var z = juliaParams;
            for(int i = 0; i < d; i++){
                c.timesAssign(c);
                c.plusAssign(z);
                if (c.abs2() > r2)
                    return i - Math.log(Math.log(c.abs()) / Math.log(d)) / Math.log(2.0);
            }
            return 1.0F;
        }
    }

}