package ru.smak.gui.graphics;

import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.coordinates.Converter;
import ru.smak.gui.graphics.fractalcolors.Colorizer;
import ru.smak.math.Complex;
import ru.smak.math.Fractal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class FractalPainter extends Painter {

    private final CartesianScreenPlane plane;
    private final Fractal fractal;

    public Colorizer col;

    public FractalPainter(CartesianScreenPlane plane,
                          Fractal f){
        this.plane = plane;
        fractal = f;
    }

    ArrayList<Thread> ts = new ArrayList<>();

    @Override
    public void paint(Graphics graphics) {
        //var t1 = System.currentTimeMillis();
        var stripCount = Runtime.getRuntime().availableProcessors();
        ts.clear();
        for (int i = 0; i<stripCount; i++){
            var fsp = new FractalStripPainter(graphics, i, stripCount);
            ts.add(new Thread(fsp));
            ts.get(i).start();
        }
        for (var t : ts){
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
        //var t2 = System.currentTimeMillis();
    }

    class FractalStripPainter implements Runnable{

        private int begPx;
        private int endPx;
        private final Graphics graphics;
        private BufferedImage bi;

        public FractalStripPainter(
                Graphics g,
                int stripId,
                int stripCount
        ){
            graphics = g;
            var width = plane.getWidth() / stripCount;
            begPx = stripId * width;
            if (stripId == stripCount - 1)
                width += plane.getWidth() % stripCount;
            endPx = begPx + width;
            bi = new BufferedImage(width, plane.getHeight(), BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public void run() {
            var g = bi.getGraphics();
            for (int i = begPx; i < endPx; i++){
                for (int j = 0; j < plane.getHeight(); j++){
                    var x = Converter.xScr2Crt(i, plane);
                    var y = Converter.yScr2Crt(j, plane);
                    var is = fractal.isInSet(new Complex(x, y));
                    Color c = (col!=null)?col.getColor(is):((is==1.0F)?Color.BLACK:Color.WHITE);
                    g.setColor(c);
                    g.fillRect(i-begPx, j, 1, 1);
                }
            }
            synchronized (graphics) {
                graphics.drawImage(bi, begPx, 0, null);
            }
        }
    }
}
