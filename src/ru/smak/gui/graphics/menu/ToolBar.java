package ru.smak.gui.graphics.menu;

import ru.smak.gui.DynamicIterations;
import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.proportions.Transforms;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.TimerTask;

public class ToolBar extends JToolBar {
    private JToolBar toolBar;
    private JButton back;
    private JCheckBox dynamicDet;
    private JButton animation;
    private JComboBox fractal;
    private JComboBox color;
    private final ArrayList<ColorChooseListener> cc = new ArrayList<>();
    private final ArrayList<MandelbrotChooseListener> mc = new ArrayList<>();
   // private final ArrayList<RepaintListener> gpl = new ArrayList<>();
    private final ArrayList<OpenMediaListener>oml = new ArrayList<>();
    private final ArrayList<DynamicListener>dl = new ArrayList<>();
    private CartesianScreenPlane plane;

    public void setPlane(CartesianScreenPlane plane){
        this.plane = plane;
    }

    public ToolBar(JToolBar bar){
        toolBar = bar;
        toolBar.setRollover(true);
        back = new JButton(MainMenu.createIcon("icons/back.png"));
        back.setFocusable(false);
        toolBar.add(back);
        toolBar.addSeparator();
        dynamicDet = new JCheckBox("Детализация", false);
        dynamicDet.setBorderPaintedFlat(true);
        dynamicDet.setFocusable(false);
        toolBar.add(dynamicDet);
        toolBar.addSeparator();
        animation = new JButton(MainMenu.createIcon("icons/video.png"));
        animation.setFocusable(false);
        fractal = new JComboBox(new String[]{"z^2+c","z^3+c", "z^4+c", "z^9+c"});
        fractal.setFocusable(false);
        toolBar.add(fractal);
        toolBar.addSeparator();
        color = new JComboBox(new String[]{"Разноцветный", "Синий", "Зелёный", "Жёлтый", "Коричневый"});
        color.setFocusable(false);
        color.setSelectedItem(color.getItemAt(1));
        toolBar.add(color);
        toolBar.addSeparator();
        toolBar.add(animation);
        toolBar.setFloatable(false);

        back.addMouseListener(new MouseAdapter() {
            private int clickCnt = 0;
            java.util.Timer timer = new java.util.Timer("doubleClickTimer", false);
            @Override
            public void mouseClicked(final MouseEvent e) {
                clickCnt = e.getClickCount();
                if (e.getClickCount() == 1) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (clickCnt == 1) {
                                //вернуться на шаг раньше
                                //Transforms.executeLast(plane);
                            } else if (clickCnt > 1) {
                                //вернуться к исходному фракталу
                                //Transforms.toHome(plane);
                            }
                            //notifyRepaintListeners();
                            clickCnt = 0;
                        }
                        }, 300);
                }
            }
        });

        dynamicDet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //использовать динамическую детализацию
                notifyDynamicListener(dynamicDet.isSelected());
            }
        });

        color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeColor(color.getSelectedIndex());
            }
        });
        fractal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeView(fractal.getSelectedIndex());
            }
        });
        animation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //создать видео
                notifyOpenMedia();
            }
        });
    }
    public void addCChooseListener(ColorChooseListener c){
        cc.add(c);
    }
    public void changeColor(int i){
        for(var c: cc){
            c.chooseColor(i);
        }
    }
    public void addMChooseListener(MandelbrotChooseListener m){
        mc.add(m);
    }
    public void changeView(int i){
        for(var m : mc )
            m.chooseFractal(i);
    }
/*
    public void addGetPlaneListener(RepaintListener l){
        gpl.add(l);
    }
    public void removeGetPlaneListener(RepaintListener l){
        gpl.remove(l);
    }
    public void notifyRepaintListeners(){
        for(var l : gpl)
            l.timeToRepaint();
    }
*/
    public void addOpenMediaListener(OpenMediaListener l){
        oml.add(l);
    }
    public void removeOpenMediaListener(OpenMediaListener l){
        oml.remove(l);
    }
    public void notifyOpenMedia(){
        for(var l : oml)
            l.openMedia();
    }
    public void addDynamicListener(DynamicListener l){
        dl.add(l);
    }
    public void removeDynamicListener(DynamicListener l){
        dl.remove(l);
    }
    public void notifyDynamicListener(boolean state){
        for(var l : dl)
            l.setDynamic(state);
    }

}