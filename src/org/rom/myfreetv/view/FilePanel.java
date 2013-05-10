package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.rom.myfreetv.files.FileUtils;
import org.rom.myfreetv.files.RecordFile;
import org.rom.myfreetv.files.RecordFileManager;
import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.streams.FileIn;

class FilePanel extends JPanel implements ActionListener, ListSelectionListener, Runnable {

    private MyFreeTV owner;
    private FileList recordFileList;
    private JTextField textfield;
    private JButton play, freeplay, open, delete, remove, rename, clean;
    private Thread runner;

    public FilePanel(MyFreeTV owner) {
        super();
        this.owner = owner;
        setLayout(new BorderLayout());
        // setBorder(new TitledBorder(null,"Entregistrements en cours"));
        recordFileList = new FileList();
        recordFileList.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    play();
                }
            }
        });
        recordFileList.addListSelectionListener(this);

        JPanel pan = new JPanel(new BorderLayout());
        JScrollPane scroll = new JScrollPane(recordFileList);
        scroll.setPreferredSize(new Dimension(350, 50));
        pan.add(scroll);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        clean = new JButton(ImageManager.getInstance().getImageIcon("clean_files"));
        delete = new JButton(ImageManager.getInstance().getImageIcon("file_delete"));
        remove = new JButton(ImageManager.getInstance().getImageIcon("file_remove"));
        rename = new JButton(ImageManager.getInstance().getImageIcon("file_rename"));
//        delete.setBorder(MyFreeTV.buttonBorder);
//        remove.setBorder(MyFreeTV.buttonBorder);
//        rename.setBorder(MyFreeTV.buttonBorder);
        clean.setToolTipText("Nettoyer la liste des fichiers inexistants.");
        delete.setToolTipText("Supprimer le fichier du disque dur.");
        remove.setToolTipText("Enlever le fichier de la liste.");
        rename.setToolTipText("Renommer le fichier.");
        clean.setActionCommand("clean");
        delete.setActionCommand("delete");
        remove.setActionCommand("remove");
        rename.setActionCommand("rename");
        clean.addActionListener(this);
        delete.addActionListener(this);
        remove.addActionListener(this);
        rename.addActionListener(this);
        buttonsPanel.add(clean);
        buttonsPanel.add(delete);
        buttonsPanel.add(remove);
        buttonsPanel.add(rename);
        pan.add(buttonsPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel();
        textfield = new JTextField(30);
        textfield.setEnabled(false);
        textfield.setDisabledTextColor(Color.GRAY);
        JButton but = new JButton("...");
        // but.setBorder(MyFreeTV.buttonBorder);
        but.setToolTipText("Ouvrir un fichier...");
        but.setActionCommand("open...");
        but.addActionListener(this);

        play = new JButton(ImageManager.getInstance().getImageIcon("play"));
//        play.setBorder(MyFreeTV.buttonBorder);
        play.setToolTipText("Lire le fichier sélectionné.");
        play.setActionCommand("play");
        play.addActionListener(this);
        freeplay = new JButton(ImageManager.getInstance().getImageIcon("fpico"));
        freeplay.setToolTipText("Diffuser le fichier sélectionné sur la TV.");
        freeplay.setActionCommand("freeplay");
        freeplay.addActionListener(this);
        bottomPanel.add(new JLabel("Fichier : "));
        bottomPanel.add(textfield);
        bottomPanel.add(but);
        bottomPanel.add(play);
        bottomPanel.add(freeplay);

        add(pan);
        add(bottomPanel, BorderLayout.SOUTH);

        if(runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void refresh() {
        recordFileList.getModel().refresh();
        initButtons();
    }

    // public void refreshOnlyValues() {
    // recordFileList.getModel().refreshOnlyValues();
    // }

    public FileList getRecordFileList() {
        return recordFileList;
    }

    public void update() {
        initMoAndRefresh();
    }

    public void initButtons() {
        Object selectedValue = null;
        if(recordFileList.getSelectedIndex() < recordFileList.getModel().getSize())
            selectedValue = recordFileList.getSelectedValue();
        if(selectedValue != null) {
            RecordFile rf = (RecordFile) selectedValue;
            boolean fileExists = rf.getFile().exists();
            boolean hasNoJob = rf.getJob() == null;
            delete.setEnabled(hasNoJob && fileExists);
            remove.setEnabled(hasNoJob);
            rename.setEnabled(hasNoJob && fileExists);
        } else {
            delete.setEnabled(false);
            remove.setEnabled(false);
            rename.setEnabled(false);
        }
        File file = new File(textfield.getText());
        boolean exists = file.exists();
        play.setEnabled(exists && (JobManager.getInstance().getPlay() == null || !new File(JobManager.getInstance().getPlay().getUrlInput()).equals(file)));
        freeplay.setEnabled(exists && (JobManager.getInstance().getFreePlay() == null || !new File(JobManager.getInstance().getFreePlay().getUrlInput()).equals(file)));
    }

//    public String getSelectedUrl() {
//        return textfield.getText();
//    }

    public void valueChanged(ListSelectionEvent e) {
        valueChanged();
    }

    private void valueChanged() {
        int selectedIndex = recordFileList.getSelectedIndex();
        if(selectedIndex >= 0 && selectedIndex < recordFileList.getModel().getSize()) {
            Object selectedValue = recordFileList.getSelectedValue();
            if(selectedValue != null) {
                RecordFile rf = (RecordFile) selectedValue;
                textfield.setText(rf.getFile().getAbsolutePath());
            }
        }
        // refresh();
        initButtons();
    }

    public void actionPerformed(ActionEvent e) {
        String com = e.getActionCommand();
        if(com.equals("play")) {
            play();
        } else if(com.equals("freeplay")) {
            freeplay();
        } else if(com.equals("open...")) {
            String filename = FileUtils.chooseOpen(this, textfield.getText(), null, FileUtils.Type.FILE);
            if(filename != null) {
                textfield.setText(filename);
                initButtons();
            }
        } else if(com.equals("delete")) {
            Object selectedValue = recordFileList.getSelectedValue();
            if(selectedValue != null) {
                RecordFile rf = (RecordFile) selectedValue;
                if(rf.getJob() == null) {
                    if(JOptionPane.showConfirmDialog(owner, "Voulez-vous vraiment supprimer ce fichier?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        if(rf.getFile().delete()) {
                            RecordFileManager.getInstance().remove(rf);
                        } else {
                            JOptionPane.showMessageDialog(owner, "Impossible de supprimer ce fichier.", "Erreur", JOptionPane.OK_OPTION);
                        }
                    }
                }
            }
            valueChanged();
        } else if(com.equals("remove")) {
            Object selectedValue = recordFileList.getSelectedValue();
            if(selectedValue != null) {
                RecordFile rf = (RecordFile) selectedValue;
                if(rf.getJob() == null) {
                    RecordFileManager.getInstance().remove(rf);
                }
            }
            valueChanged();
        } else if(com.equals("clean")) {
            RecordFileManager.getInstance().clean();
            valueChanged();
        } else if(com.equals("rename")) {
            Object selectedValue = recordFileList.getSelectedValue();
            if(selectedValue != null) {
                RecordFile rf = (RecordFile) selectedValue;
                if(rf.getJob() == null) {
                    String dest = (String) JOptionPane.showInputDialog(owner, "Renommer le fichier :", "Renommer", JOptionPane.QUESTION_MESSAGE, null, null, rf.getFile().getAbsolutePath());
                    if(dest != null) {
                        File file = new File(dest);
                        if(!RecordFileManager.getInstance().rename(rf, file)) {
                            JOptionPane.showMessageDialog(owner, "Impossible de renommer ce fichier.", "Erreur", JOptionPane.OK_OPTION);
                        } else {
                            textfield.setText(file.getAbsolutePath());
                            initButtons();
                        }
                    }
                }
            }
        }
    }

    public void initMoAndRefresh() {
        FileListModel model = recordFileList.getModel();
        for(int i = 0; i < model.getSize(); i++) {
            RecordFile rf = (RecordFile) model.getElementAt(i);
            // if(rf.getJob() != null)
            rf.initMo();
            refresh();
        }
    }

    private void play() {
        FileIn file = new FileIn(textfield.getText());
        if(file.exists() && (JobManager.getInstance().getPlay() == null || !new File(JobManager.getInstance().getPlay().getUrlInput()).equals(file)))
            owner.getActions().play(file);
    }

    private void freeplay() {
        FileIn file = new FileIn(textfield.getText());
        if(file.exists() && (JobManager.getInstance().getFreePlay() == null || !new File(JobManager.getInstance().getFreePlay().getUrlInput()).equals(file)))
            owner.getActions().freePlay(file);
    }

    public void run() {
        while(true) {
            if(owner.isFilesPanelVisible()) {
                initMoAndRefresh();
            }
            try {
                Thread.sleep(5000);
            } catch(InterruptedException e) {}
        }
    }

}
