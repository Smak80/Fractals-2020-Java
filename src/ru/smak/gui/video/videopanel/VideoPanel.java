package ru.smak.gui.video.videopanel;

import ru.smak.gui.graphics.fractalcolors.Colorizer;
import ru.smak.gui.video.processor.MediaProcessor;
import ru.smak.math.Fractal;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class VideoPanel extends JPanel {
    private final JScrollPane content;
    private final JList images;
    private final DefaultListModel<ImageIcon> dlm;
    private final ButtonsPanel buttonsPanel;
    private final Configurations configs;

    private final MediaProcessor mediaProcessor;

    private final JFileChooser fileChooser;

    private final ArrayList<CatchListener> catchListeners = new ArrayList<>();

    public VideoPanel(){
        setVisible(true);
        // Настройка списка, в котором будут храниться захваченные изображения
        dlm = new DefaultListModel<>();
        images = new JList(dlm);
        content = new JScrollPane(images);
        buttonsPanel = new ButtonsPanel();

        configs = new Configurations();

        mediaProcessor = new MediaProcessor();
        // Диалог при неправильном формате
        var dialog = new JDialog();
        dialog.setModal(false);
        dialog.setSize(200,80);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        // параметры filechooser
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Video(*.mp4)", "mp4"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory().getPath()+"\\MyVideo.mp4"));
        fileChooser.setMultiSelectionEnabled(false);

        GroupLayout groupLayout = new GroupLayout(this);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGap(5)
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(content, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addGap(5)
                        .addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(5)
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGap(5)
                .addComponent(content, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(5)
                .addComponent(buttonsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(5)
        );
        setLayout(groupLayout);
        buttonsPanel.addImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyCatchListeners(mediaProcessor);
                dlm.add(dlm.size(), mediaProcessor.getImage(dlm.size()));
            }
        });
        buttonsPanel.recordVideo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dlm.size() >= 2 && !mediaProcessor.isRecording()){
                    mediaProcessor.createVideo();
                    buttonsPanel.recordVideo.setSelected(false);
                    JOptionPane.showMessageDialog(null, "Запись видео началась! Пожалуйста, дождитесь записи до самого конца! " +
                            "Вы будете проинформированы в случае окончания записи!", "Запись начлась", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        mediaProcessor.setVideoOutput(fileChooser.getSelectedFile().getPath());
        // проверка формата файла
        mediaProcessor.addLock(buttonsPanel);
        buttonsPanel.getOutputPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showSaveDialog(null);
                try{
                    var name = fileChooser.getSelectedFile().getPath();
                    if (!name.isEmpty() && name.endsWith(".mp4")) {
                        mediaProcessor.setVideoOutput(name);
                    }else{
                        if(name.contains(".")){
                            var content = new JPanel();
                            content.add(new JLabel("Неправильный тип данных"));
                            dialog.setContentPane(content);
                            dialog.setVisible(true);
                        }
                        else {
                            if (name.isEmpty()) {
                                var content = new JPanel();
                                content.add(new JLabel("Пустая строка"));
                                dialog.setContentPane(content);
                                dialog.setVisible(true);
                            } else {
                                name += ".mp4";
                                mediaProcessor.setVideoOutput(name);
                            }
                        }
                    }
                    mediaProcessor.setVideoOutput(name);
                }catch (NullPointerException exception){}
            }
        });
        buttonsPanel.stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaProcessor.stopRecording();
                JOptionPane.showMessageDialog(null, "Вы прервали видеозапись. Запись видеопрекращена. Не делайте так больше", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonsPanel.removeImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dlm.size() > 0){
                    if(images.getSelectedIndex() != -1)
                        dlm.remove(images.getSelectedIndex());
                    images.setSelectedIndex(0);
                }
            }
        });
    }

    public void setData(Fractal fractal, Colorizer colorizer){
        mediaProcessor.loadFractalData(fractal, colorizer);
        mediaProcessor.setImageScreen(new Dimension(
                content.getWidth() - 5,
                (int)(0.45 * content.getHeight())
        ));
    }

    public void changeVisible(){
        setVisible(!isVisible());
    }
    public void breakThreads(){
        mediaProcessor.stopRecording();
    }
    public void addCatchListener(CatchListener listener){
        catchListeners.add(listener);
    }
    public void removeCatchListener(CatchListener listener){
        catchListeners.remove(listener);
    }
    public void notifyCatchListeners(MediaProcessor mediaProcessor){
        for(var l : catchListeners)
            l.timeToCatch(mediaProcessor);
    }
}
