package ru.smak.gui.video.videopanel;

import ru.smak.gui.video.processor.MediaProcessor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Configurations extends JFrame {

    private JTextField width, height;
    private JButton setDim;
    private JTextField videoTime;
    private JButton setVideoTime;
    private JFileChooser fileChooser;
    private JTextField chosenFile;
    private JButton chooseFile;

    private  static MediaProcessor mediaProcessor;

    public static void setMediaProcessor(MediaProcessor mp){
        mediaProcessor = mp;
    }
    public Configurations(){

        setMaximumSize(new Dimension(300, 200));
        setMinimumSize(new Dimension(300, 200));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(false);
        width = new JTextField();
        width.setMinimumSize(new Dimension(70, 20));
        height = new JTextField();
        height.setMinimumSize(new Dimension(70, 20));
        videoTime = new JTextField();
        videoTime.setMinimumSize(new Dimension(70, 20));
        setDim = new JButton("Сохранить размер");
        setVideoTime = new JButton("Сохранить время");
        fileChooser = new JFileChooser();
        chosenFile = new JTextField();
        chosenFile.setMinimumSize(new Dimension(70, 20));
        chooseFile = new JButton("Выбрать папку");
        var dimPanel = new JPanel();
        dimPanel.setLayout(new GroupLayout(dimPanel));
        dimPanel.add(width, GroupLayout.DEFAULT_SIZE);
        dimPanel.add(height, GroupLayout.DEFAULT_SIZE);
        dimPanel.add(setDim, GroupLayout.DEFAULT_SIZE);
        var videoTimePanel = new JPanel();
        videoTimePanel.setLayout(new GroupLayout(videoTimePanel));
        videoTimePanel.add(videoTime, GroupLayout.DEFAULT_SIZE);
        videoTimePanel.add(setVideoTime, GroupLayout.DEFAULT_SIZE);
        var filePanel = new JPanel();
        filePanel.setLayout(new GroupLayout(filePanel));
        filePanel.add(chosenFile, GroupLayout.DEFAULT_SIZE);
        filePanel.add(chooseFile, GroupLayout.DEFAULT_SIZE);

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
        fileChooser.setCurrentDirectory(new File("D:\\myVideo.mp4"));
        fileChooser.setSelectedFile(new File("D:\\myVideo.mp4"));
        fileChooser.setMultiSelectionEnabled(false);

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGap(5)
                .addGroup(groupLayout.createParallelGroup()
//                        .addGroup(groupLayout.createParallelGroup()
//                                .addComponent(width, 100, 100, 100)
//                                .addGap(5)
//                                .addComponent(height,100, 100, 100)
//                                .addGap(5)
//                                .addComponent(setDim, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
//                        )
                        .addComponent(dimPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addGap(5)
//                        .addGroup(groupLayout.createParallelGroup()
//                                .addComponent(videoTime, 100, 100, 100)
//                                .addGap(5)
//                                .addComponent(setVideoTime, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
//                        )
                        .addComponent(videoTimePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addGap(5)
//                        .addGroup(groupLayout.createParallelGroup()
//                                .addComponent(chooseFile, 100, 100, 100)
//                                .addGap(5)
//                                .addComponent(chooseFile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
//                                .addGap(5)
//                        )
                        .addComponent(filePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                )
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGap(5)
//                .addGroup(groupLayout.createParallelGroup()
//                        .addComponent(width, 50, 50, 50)
//                        .addGap(5)
//                        .addComponent(height,50, 50, 50)
//                        .addGap(5)
//                        .addComponent(setDim, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
//                )
                .addComponent(dimPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5)
//                .addGroup(groupLayout.createParallelGroup()
//                        .addComponent(videoTime, 50, 50, 50)
//                        .addGap(5)
//                        .addComponent(setVideoTime, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
//                )
                .addComponent(videoTimePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5)
//                .addGroup(groupLayout.createParallelGroup()
//                        .addComponent(chooseFile, 50, 50, 50)
//                        .addGap(5)
//                        .addComponent(chooseFile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
//                        .addGap(5)
//                )
                .addComponent(filePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5)
        );
        setLayout(groupLayout);
        pack();

        chooseFile.addActionListener(new ActionListener() {
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
                }catch (NullPointerException exception){}
            }
        });
    }
}
