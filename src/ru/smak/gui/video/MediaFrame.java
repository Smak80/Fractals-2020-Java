package ru.smak.gui.video;

import ru.smak.gui.graphics.FractalPainter;
import ru.smak.gui.graphics.SelectionPainter;
import ru.smak.gui.graphics.components.GraphicsPanel;
import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.fractalcolors.Colorizer;
import ru.smak.gui.video.videopanel.VideoPanel;
import ru.smak.math.Fractal;
import ru.smak.math.Mandelbrot;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MediaFrame extends JFrame {
    private static final Dimension MIN_FRAME_SIZE = new Dimension(300, 600);

    private VideoPanel videoPanel;

    public MediaFrame(){
        videoPanel = new VideoPanel();
        setMinimumSize(MIN_FRAME_SIZE);
        setVisible(false);
        setTitle("Редактор видео");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGap(5)
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(videoPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                )
                .addGap(5)
        );
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGap(5)
                .addComponent(videoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5)
        );
        setLayout(groupLayout);
        pack();
    }

    public VideoPanel getVideoPanel(){
        return videoPanel;
    }
}
