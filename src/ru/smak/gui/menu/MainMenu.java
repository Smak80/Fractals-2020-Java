package ru.smak.gui.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class MainMenu extends JFrame {
    private JMenuBar menuBar;
    private ImageSaver saver;

    public MainMenu(JMenuBar m){
        menuBar = m;
        menuBar.add(createSaveMenu());
        menuBar.add(createOpenMenu());
    }

    public void setSaver(ImageSaver saver){
        this.saver = saver;
    }

    public ImageSaver getSaver () throws NullPointerException{
        return saver;
    }

    public JMenu createSaveMenu(){
        JMenu save = new JMenu("Сохранить");
        JMenuItem asImage = new JMenuItem("Как изображение...");
        JMenuItem inInternalFormat = new JMenuItem("Во внутреннем формате программы...");
        save.setIcon(createIcon("icons/save.png"));
        save.add(asImage);
        save.add(inInternalFormat);
        asImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //открыть диалоговое окно для сохранения картинки
                saver.saveImage();
            }
        });
        inInternalFormat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //сохранить информацию о фрактале в файле
            }
        });
        return save;
    }
    public JMenu createOpenMenu(){
        JMenu open = new JMenu("Открыть");
        JMenuItem fromFile = new JMenuItem("Из внутреннего формата программы...");
        open.add(fromFile);
        open.setIcon(createIcon("icons/open.png"));
        fromFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //открыть диалоговое окно для открытия картинки по информации из файла
            }
        });
        return open;
    }
    protected static ImageIcon createIcon(String path) {
        URL imgURL = MainMenu.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("File not found " + path);
            return null;
        }
    }
}
