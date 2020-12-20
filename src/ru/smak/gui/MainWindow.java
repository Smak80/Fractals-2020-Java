package ru.smak.gui;

import ru.smak.gui.graphics.FinishedListener;
import ru.smak.gui.graphics.FractalPainter;
import ru.smak.gui.graphics.SelectionPainter;
import ru.smak.gui.graphics.components.GraphicsPanel;
import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.coordinates.Converter;
import ru.smak.gui.graphics.fractalcolors.Colorizer;
import ru.smak.gui.graphics.proportions.ProportionsSaver;
import ru.smak.gui.graphics.proportions.Transforms;
import ru.smak.gui.menu.*;
import ru.smak.gui.video.MediaFrame;
import ru.smak.gui.video.MediaTools;
import ru.smak.gui.video.videopanel.CatchListener;
import ru.smak.gui.video.processor.MediaProcessor;
import ru.smak.math.*;
import ru.smak.gui.graphics.fractalcolors.shemes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
    GraphicsPanel mainPanel;
    MediaFrame mediaFrame;

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

        mediaFrame = new MediaFrame();

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
        fp = new FractalPainter(plane, mandelbrot);
        fp.col = colorizer;

        ImageSaver iSaver = new ImageSaver(plane, mandelbrot, colorizer);
        menu.setSaver(iSaver);

        ProportionsSaver saver = new ProportionsSaver(plane.xMax, plane.xMin, plane.yMax, plane.yMin, mainPanel.getWidth(), mainPanel.getHeight());
        Transforms.compose(plane, mainPanel.getWidth(), mainPanel.getHeight());

        fp.addFinishedListener(new FinishedListener() {
            @Override
            public void finished() {
                mainPanel.repaint();
            }
        });
        mainPanel.addPainter(fp);
        var sp = new SelectionPainter(mainPanel.getGraphics());

        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                saver.maintain(mainPanel.getWidth(), mainPanel.getHeight(), plane);
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
                if(r != null && r.width > 3 && r.height > 3){
                    var xMin = Converter.xScr2Crt(r.x,plane);
                    var xMax = Converter.xScr2Crt(r.x+r.width,plane);
                    var yMin = Converter.yScr2Crt(r.y+r.height,plane);
                    var yMax = Converter.yScr2Crt(r.y,plane);
                    Transforms.addArea(plane);
                    saver.maintain(xMin, xMax, yMin, yMax, mainPanel.getWidth(), mainPanel.getHeight(), plane);
                    mainPanel.repaint();
                }
            }
        });

        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                sp.setCurrentPoint(e.getPoint());
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
                    var p = Transforms.executeLast();
                    if(p != null){
                        saver.maintain(p.xMin, p.xMax, p.yMin, p.yMax, mainPanel.getWidth(), mainPanel.getHeight(), plane);
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_H){
                    //Transforms.toHome(plane, mainPanel.getWidth(), mainPanel.getHeight());
                    var p = Transforms.toHome();
                    //saver.maintain(plane, p.xMin, p.xMax, p.yMin, p.yMax, mainPanel.getWidth(), mainPanel.getHeight());
                }
                mainPanel.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {

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
                //fp.setFractal(mandelbrot);
                //iSaver.setFractal(mandelbrot);
                mainPanel.repaint();
            }
        });

        tb.addGetPlaneListener(new RepaintListener() {
            @Override
            public void timeToRepaint() {
                //saver.maintain(plane, mainPanel.getWidth(), mainPanel.getHeight());
                mainPanel.repaint();
            }
        });
        tb.setPlane(plane);
        tb.addOpenMediaListener(new OpenMediaListener() {
            @Override
            public void openMedia() {
                mediaFrame.setVisible(true);
            }
        });

        mediaFrame.getVideoPanel().addCatchListener(new CatchListener() {
            @Override
            public void timeToCatch(MediaProcessor mediaProcessor) {
                mediaFrame.getVideoPanel().setData(mandelbrot, colorizer);
                mediaProcessor.catchImage(plane);
                mediaProcessor.setVideoScreen(MIN_FRAME_SIZE);
            }
        });
    }
}
