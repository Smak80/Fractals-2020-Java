package ru.smak.gui;

import ru.smak.gui.graphics.FinishedListener;
import ru.smak.gui.graphics.FractalPainter;
import ru.smak.gui.graphics.SelectionPainter;
import ru.smak.gui.graphics.components.GraphicsPanel;
import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.coordinates.Converter;
import ru.smak.gui.graphics.fractalcolors.*;
import ru.smak.gui.graphics.menu.*;
import ru.smak.gui.graphics.proportions.Transforms;
import ru.smak.gui.video.MediaFrame;
import ru.smak.gui.video.processor.MediaProcessor;
import ru.smak.gui.video.videopanel.CatchListener;
import ru.smak.math.Mandelbrot;
import ru.smak.SaveProportions;

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
        Transforms.compose(plane, mainPanel.getWidth(), mainPanel.getHeight());

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

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
                    var p = Transforms.executeLast();
                    if(p != null){
                        save.newScal(p.xMin, p.xMax, p.yMin, p.yMax, mainPanel.getWidth(), mainPanel.getHeight(), plane);
                    }
                }
                else if(e.getKeyCode() == KeyEvent.VK_H){
                    var p = Transforms.toHome();
                    if(p != null){
                        save.newScal(p.xMin, p.xMax, p.yMin, p.yMax, mainPanel.getWidth(), mainPanel.getHeight(), plane);
                    }
                }
                mainPanel.repaint();
            }
        });

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

        tb.addExectueListener(new ExecuteListener() {
            @Override
            public void executePlane(CartesianScreenPlane p) {
                if(plane != null){
                    save.newScal(p.xMin, p.xMax, p.yMin, p.yMax, mainPanel.getWidth(), mainPanel.getHeight(), plane);
                    mainPanel.repaint();
                }
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
                    var xMin = Converter.xScr2Crt(r.x,plane);
                    var xMax = Converter.xScr2Crt(r.x+r.width,plane);

                    var yMin = Converter.yScr2Crt(r.y+r.height,plane);
                    var yMax = Converter.yScr2Crt(r.y,plane);

                    Transforms.addArea(plane);

                    save.newScal(xMin, xMax, yMin, yMax,mainPanel.getWidth(), mainPanel.getHeight(), plane);
                    mainPanel.repaint();
                }
            }
        });

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(mediaFrame.isVisible()){
                    if(mandelbrot.isJulia())
                        mandelbrot.setJulia();
                }
                else if(!mediaFrame.isVisible()){
                    var dx = Converter.xScr2Crt(e.getX(), plane);
                    var dy = Converter.yScr2Crt(e.getY(), plane);
                    mandelbrot.setJuliaParams(dx, dy);
                    mandelbrot.setJulia();
                    if(mandelbrot.isJulia()){
                        toolBar.setVisible(false);
                        menuBar.setVisible(false);
                    }
                    else{
                        toolBar.setVisible(true);
                        menuBar.setVisible(true);
                    }
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

        tb.addCChooseListener(new ColorChooseListener() {
            @Override
            public void chooseColor(int i) {
                colorizer = colorScheme[i];
                fp.col = colorizer;
                iSaver.setColorizer(colorizer);
                mainPanel.repaint();
            }
        });
        mediaFrame = new MediaFrame();

        tb.addOpenMediaListener(new OpenMediaListener() {
            @Override
            public void openMedia() {
                mediaFrame.setVisible(true);
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
