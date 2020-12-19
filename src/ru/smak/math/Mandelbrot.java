package ru.smak.math;

public class Mandelbrot implements Fractal{

    private int maxIters = 200;
    private double r2 = 4;
    private DynamicIters d;
    private boolean dynDetalization = true;
    public Mandelbrot(){
        d = new DynamicIters();
    }

    public void setDynDetalization(boolean b){
        dynDetalization = b;
    }
    public void setBordersForDetalization(double xMin,double xMax,double yMin, double yMax){
        if(dynDetalization | d.getFirstRect()){
            d.setAll(xMin,xMax,yMin,yMax);
        }
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
        if(dynDetalization) {
            for (int i = 0; i < this.d.getIters(); i++) {
                z.timesAssign(z);
                z.plusAssign(c);
                if (z.abs2() > r2)
                    return i - Math.log(Math.log(z.abs()) / Math.log(this.d.getIters())) / Math.log(2.0);
            }
            return 1.0F;
        }else{
            for (int i = 0; i < maxIters; i++) {
                z.timesAssign(z);
                z.plusAssign(c);
                if (z.abs2() > r2)
                    return i - Math.log(Math.log(z.abs()) / Math.log(maxIters)) / Math.log(2.0);
            }
            return 1.0F;
        }
    }
}
