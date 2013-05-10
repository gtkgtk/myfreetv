package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.rom.myfreetv.files.FileUtils;
import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.streams.FileIn;

class FreePlayerPanel extends JPanel implements ActionListener {

    private MyFreeTV owner;
    private FreePlayPanel freeplay;
    private JTextField textfield;
    private JButton play;

    public FreePlayerPanel(MyFreeTV owner) {
        super(new BorderLayout());
        this.owner = owner;
        add(new JLabel(ImageManager.getInstance().getImageIcon("freeplayer")),BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout());
        freeplay = new FreePlayPanel(owner);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));

        textfield = new JTextField(30);
        textfield.setEnabled(false);
        textfield.setDisabledTextColor(Color.GRAY);
        JButton but = new JButton("...");
        // but.setBorder(MyFreeTV.buttonBorder);
        but.setToolTipText("Ouvrir un fichier...");
        but.setActionCommand("open...");
        but.addActionListener(this);

        play = new JButton(ImageManager.getInstance().getImageIcon("fpico"));
//      play.setBorder(MyFreeTV.buttonBorder);
        play.setToolTipText("Diffuser le fichier sélectionné sur la TV.");
        play.setActionCommand("freeplay");
        play.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Fichier : "));
        bottomPanel.add(textfield);
        bottomPanel.add(but);
        bottomPanel.add(play);
        bottomPanel.add(freeplay);


        mid.add(freeplay,BorderLayout.NORTH);
        mid.add(center);
        center.add(bottomPanel);

        add(mid);
    }

    public void initButtons() {
//        freeplay.initButtons();
        File file = new File(textfield.getText());
        boolean exists = file.exists();
        play.setEnabled(exists && (JobManager.getInstance().getFreePlay() == null || !new File(JobManager.getInstance().getFreePlay().getUrlInput()).equals(file)));
    }

    public void update() {
        freeplay.update();
        initButtons();
    }

    public void actionPerformed(ActionEvent e) {
        String com = e.getActionCommand();
        if(com.equals("freeplay")) {
            freeplay();
        } else if(com.equals("open...")) {
            String filename = FileUtils.chooseOpen(this, textfield.getText(), null, FileUtils.Type.FILE);
            if(filename != null) {
                textfield.setText(filename);
                initButtons();
            }
        }
    }

    private void freeplay() {
        FileIn file = new FileIn(textfield.getText());
        if(file.exists() && (JobManager.getInstance().getFreePlay() == null || !new File(JobManager.getInstance().getFreePlay().getUrlInput()).equals(file)))
            owner.getActions().freePlay(file);
    }
}
