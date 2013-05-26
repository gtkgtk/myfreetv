package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.rom.myfreetv.config.Config;
import org.rom.myfreetv.config.DeinterlaceMode;
import org.rom.myfreetv.config.MuxMode;
import org.rom.myfreetv.files.FileUtils;
//import org.rom.myfreetv.player.ExternVLC;

class ConfigDialog extends JDialog implements ActionListener {

    private MyFreeTV owner;
    private JTextField vlcTextField, autoTextField, kazerTextField;
    private JButton autoBut;
    private JCheckBox auto, embedded, checkUpdate, kazerEnable;
    private JRadioButton ts, ps;
    private DeinterlaceComboBox deint;

    private DeinterlaceMode deintAtLoad;
//    private boolean embeddedAtLoad;

    public ConfigDialog(MyFreeTV owner) {
        super(owner, "Configuration", true);
        this.owner = owner;
        setResizable(false);
        
//        SkinManager.decore(this,Config.getInstance().getDecoration());

        Point p = owner.getLocation();
        Dimension d = owner.getSize();
        int x1 = (int) p.getX();
        int y1 = (int) p.getY();
        int x2 = x1 + (int) d.getWidth();
        int y2 = y1 + (int) d.getHeight();

        JPanel vlcPan = new JPanel();
        vlcTextField = new JTextField(25);
        String path = Config.getInstance().getVlcPath();
        vlcTextField.setText(path);

        JButton but = new JButton("...");
        but.setToolTipText("Choix de l'exécutable VLC.");
        but.setActionCommand("vlc...");
        but.addActionListener(this);

//        JButton reset = new JButton(ImageManager.getInstance().getImageIcon("reset_vlc"));
//        reset.setToolTipText("Réinitialiser la configuration de VLC.");
//        reset.setActionCommand("reset");
//        reset.addActionListener(this);

//        vlcPan.add(new JLabel("Chemin de VLC : "));
//        vlcPan.add(vlcTextField);
//        vlcPan.add(but);
//        vlcPan.add(reset);

        JPanel autoPan = new JPanel();
        auto = new JCheckBox("Fichier automatique : ");
        auto.setSelected(Config.getInstance().getAutoPath().isEnabled());
        auto.setActionCommand("auto");
        auto.addActionListener(this);

        autoTextField = new JTextField(20);
        autoTextField.setText(Config.getInstance().getAutoPath().getUrl());

        autoBut = new JButton("...");
        autoBut.setToolTipText("Choix du répertoire d'enregistrement par défaut.");
        autoBut.setActionCommand("auto...");
        autoBut.addActionListener(this);

        autoPan.add(auto);
        autoPan.add(autoTextField);
        autoPan.add(autoBut);

        JPanel kazerPan = new JPanel();
        kazerEnable = new JCheckBox("Kazer URL pour guide TV :");
        kazerEnable.setSelected(Config.getInstance().getKazerPath().isEnabled());
        kazerEnable.setToolTipText("Ouvrir un compte sur http://www.kazer.org, puis choisir sa liste de programmes, puis recopier l'URL (non zippée) dans la zone texte");
        kazerEnable.setActionCommand("kazer");
        kazerEnable.addActionListener(this);

        kazerTextField = new JTextField(20);
        kazerTextField.setText(Config.getInstance().getKazerPath().getUrl());

        kazerPan.add(kazerEnable);
        kazerPan.add(kazerTextField);

        JPanel muxPan = new JPanel();
        ButtonGroup bg = new ButtonGroup();
        ts = new JRadioButton("MPEG-TS");
        ps = new JRadioButton("MPEG-PS");
        bg.add(ts);
        bg.add(ps);
        if(Config.getInstance().getMuxMode() == MuxMode.PS)
            ps.setSelected(true);
        else
            ts.setSelected(true);

        muxPan.add(new JLabel("Méthode d'encapsulation : "));
        muxPan.add(ts);
        muxPan.add(ps);

        JPanel deinterlacePan = new JPanel();
        deint = new DeinterlaceComboBox();
        deintAtLoad = Config.getInstance().getDeinterlaceMode();
        deint.setSelectedDeinterlaceMode(deintAtLoad);
        deinterlacePan.add(new JLabel("Méthode de désentrelacement (en lecture) : "));
        deinterlacePan.add(deint);

        JPanel checkUpdatePan = new JPanel();
        checkUpdate = new JCheckBox("Vérifier les mises à jour au démarrage");
        checkUpdate.setSelected(Config.getInstance().getCheckUpdate());
        checkUpdatePan.add(checkUpdate);

        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
//        pan.add(vlcPan);
        pan.add(kazerPan);
        pan.add(autoPan);
        pan.add(muxPan);
        pan.add(deinterlacePan);
        pan.add(checkUpdatePan);

        JPanel bottom = new JPanel();

        JButton ok = new JButton("OK");
        ok.setActionCommand("ok");
        ok.addActionListener(this);

        JButton cancel = new JButton("Annuler");
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);

        bottom.add(ok);
        bottom.add(cancel);

        getContentPane().add(pan);
        getContentPane().add(bottom, BorderLayout.SOUTH);

        initButtons();

        pack();

        int posX = (x1 + x2 - (int) getSize().getWidth()) / 2;
        int posY = (y1 + y2 - (int) getSize().getHeight()) / 2;

        setLocation(posX, posY);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if(s.equals("vlc...")) {
            // String filename = chooseVLCFile();
            String filename = FileUtils.chooseOpen(this, vlcTextField.getText(), "Choix de l'exécutable VLC", FileUtils.Type.FILE);
            if(filename != null) {
                vlcTextField.setText(filename);
            }
        } else if(s.equals("auto...")) {
            // String filename = chooseAutoPath();
            String filename = FileUtils.chooseOpen(this, autoTextField.getText(), "Choix du répertoire d'enregistrement par d�faut", FileUtils.Type.DIRECTORY);
            if(filename != null) {
                autoTextField.setText(filename);
            }
        } else if(s.equals("kazer")) {
            initButtons();
        } else if(s.equals("auto")) {
            initButtons();
        } else if(s.equals("ok")) {
            DeinterlaceMode deintAtEnd = DeinterlaceMode.getDeinterlaceMode((String) deint.getSelectedItem());
            if (Config.getInstance().isEmbedded())
            {
            	boolean embeddedAtEnd = embedded.isSelected();
                Config.getInstance().setEmbedded(embeddedAtEnd);
            }
            Config.getInstance().setVlcPath(vlcTextField.getText());
            Config.getInstance().getAutoPath().setEnabled(auto.isSelected());
            Config.getInstance().getAutoPath().setUrl(autoTextField.getText());
            Config.getInstance().getKazerPath().setUrl(kazerTextField.getText());
		    try {
				   URL adresse = new URL(Config.getInstance().getKazerPath().getUrl());
				   InputStream stream = adresse.openStream();
				   Config.getInstance().getKazerPath().setEnabled(kazerEnable.isSelected());
		    } catch (Exception e1)
		    {
       		    System.out.println("Bad URL ("+Config.getInstance().getKazerPath().getUrl()+") for tvguide database :\nEnter a valid kazer url in Config panel shall be http://www.kazer.org/tvguide.xml?u=xxxxxxx");
				Config.getInstance().getKazerPath().setEnabled(false);
		    }
	        Config.getInstance().setMuxMode(ts.isSelected() ? MuxMode.TS : MuxMode.PS);
            Config.getInstance().setDeinterlaceMode(deintAtEnd);
            Config.getInstance().setCheckUpdate(checkUpdate.isSelected());
            Config.getInstance().saveProperties();
            owner.ActivateTvGuide(Config.getInstance().getKazerPath().isEnabled());
            owner.initButtons();
            dispose();
        } else if(s.equals("cancel")) {
            dispose();
        }
    }

    private void initButtons() {
        autoBut.setEnabled(auto.isSelected());
        autoTextField.setEnabled(auto.isSelected());
        kazerTextField.setEnabled(kazerEnable.isSelected());
    }

}
