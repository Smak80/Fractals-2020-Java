package ru.smak.math;

import ru.smak.gui.DynamicIterations;

public class Mandelbrot implements Fractal{

    private int maxIters = 200;
    private double r2 = 4;

    private int index = 0;

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

    @Override
    public double isInSet(Complex c) {
        final var z = new Complex();
        var t = new Complex();
        for (int i = 0; i<maxIters; i++){
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
                return i - Math.log(Math.log(z.abs())/Math.log(maxIters))/Math.log(2.0);
        }
        return 1.0F;
    }
}
