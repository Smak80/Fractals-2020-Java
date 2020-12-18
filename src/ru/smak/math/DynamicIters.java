package ru.smak.math;

public class DynamicIters {
    private double xMin,xMax,yMin,yMax;
    private int Iters = 200;
    private boolean firstRect = true;
    public void setAll(double xMin,double xMax,double yMin, double yMax){
        if(firstRect){
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
            firstRect = false;
        }else{
            Iters = (int)(Math.min(10000,(this.xMax)));
        }
    }
}
