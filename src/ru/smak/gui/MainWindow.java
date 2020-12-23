package ru.smak.gui;

import ru.smak.gui.graphics.FinishedListener;
import ru.smak.gui.graphics.FractalPainter;
import ru.smak.gui.graphics.SelectionPainter;
import ru.smak.gui.graphics.components.GraphicsPanel;
import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.coordinates.Converter;
import ru.smak.gui.graphics.fractalcolors.*;
import ru.smak.gui.graphics.menu.*;
import ru.smak.math.Mandelbrot;
import ru.smak.SaveProportions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
    GraphicsPanel mainPanel;

    static final Dimension MIN_SIZE = new Dimension(450, 350);
    static final Dimension MIN_FRAME_SIZE = new Dimension(600, 500);
    ColorScheme1 c1 = new ColorScheme1();
    ColorScheme2 c2 = new ColorScheme2();
    ColorScheme3 c3 = new ColorScheme3();
    ColorScheme4 c4 = new ColorScheme4();
    ColorScheme5 c5 = new ColorScheme5();
    Colorizer[] colorScheme = new Colorizer[]{c1,c2, c3, c4, c5};;
    Colorizer colorizer = c2;
    Mandelbrot mandelbrot = new Mandelbrot();
    FractalPainter fp;
    CartesianScreenPlane plane;

    public MainWindow(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(MIN_FRAME_SIZE);
        setTitle("Фракталы");

        mainPanel = new GraphicsPanel();

        mainPanel.setBackground(Color.WHITE);

        JMenuBar menuBar = new JMenuBar();
        MainMenu menu = new MainMenu(menuBar);

        JToolBar toolBar = new JToolBar();
        ToolBar tb = new ToolBar(toolBar);
        setJMenuBar(menuBar);

        GroupLayout gl = new GroupLayout(getContentPane());
        setLayout(gl);
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(4)
                .addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(4)
                .addComponent(mainPanel, (int)(MIN_SIZE.height*0.8), MIN_SIZE.height, GroupLayout.DEFAULT_SIZE)
                .addGap(4)
        );
        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGap(4)
                .addGroup(gl.createParallelGroup()
                        .addComponent(toolBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addGap(4)
                        .addComponent(mainPanel, MIN_SIZE.width, MIN_SIZE.width, GroupLayout.DEFAULT_SIZE)
                )
                .addGap(4)
        );
        pack();
        plane = new CartesianScreenPlane(
                mainPanel.getWidth(),
                mainPanel.getHeight(),
                -2, 1, -1, 1
        );
        mandelbrot.setPlane(plane);
        var fp = new FractalPainter(plane, mandelbrot);
        fp.col = colorizer;

        ImageSaver iSaver = new ImageSaver(plane, mandelbrot, colorizer);

        menu.setImageSaver(iSaver);

        fp.addFinishedListener(new FinishedListener() {
            @Override
            public void finished() {
                mainPanel.repaint();
            }
        });
        mainPanel.addPainter(fp);
        var sp = new SelectionPainter(mainPanel.getGraphics());

        mandelbrot.setStockParams(plane.xMin,plane.xMax,plane.yMin,plane.yMax);

        SaveProportions save = new SaveProportions(plane.xMax, plane.xMin, plane.yMax, plane.yMin, mainPanel.getWidth(), mainPanel.getHeight());

        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                save.Go(mainPanel.getWidth(), mainPanel.getHeight(), plane);

                plane.setWidth(mainPanel.getWidth());
                plane.setHeight(mainPanel.getHeight());
                sp.setGraphics(mainPanel.getGraphics());
                mainPanel.repaint();
            }
        });
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                sp.setVisible(true);
                sp.setStartPoint(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                sp.setVisible(false);
                var r = sp.getSelectionRect();
                if (r != null){
                    save.newScal(r,mainPanel.getWidth(), mainPanel.getHeight(), plane);
                    mainPanel.repaint();
                }
            }
        });

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                var dx = Converter.xScr2Crt(e.getX(), plane);
                var dy = Converter.yScr2Crt(e.getY(), plane);
                mandelbrot.setJuliaParams(dx, dy);
                mandelbrot.setJulia();
                mainPanel.repaint();
            }
        });

        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                sp.setCurrentPoint(e.getPoint());
            }
        });

        tb.addCChooseListener(new ColorChooseListener() {
            @Override
            public void chooseColor(int i) {
                colorizer = colorScheme[i];
                fp.col = colorizer;
                iSaver.setColorizer(colorizer);
                mainPanel.repaint();
            }
        });

        tb.addMChooseListener(new MandelbrotChooseListener() {
            @Override
            public void chooseFractal(int i) {
                mandelbrot.setIndex(i);
                iSaver.setFractal(mandelbrot);
                mainPanel.repaint();
            }
        });

        tb.addDynamicListener(new DynamicListener() {
            @Override
            public void setDynamic(boolean state) {
                mandelbrot.setDynamic(state);
                mainPanel.repaint();
            }
        });
    }
}
