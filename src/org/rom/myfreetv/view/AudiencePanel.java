package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.rom.myfreetv.audience.XMLChannel;
import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.streams.Channel;

class AudiencePanel extends JPanel implements Runnable, ActionListener, ListSelectionListener {

    private MyFreeTV owner;
    private AudienceList audienceList;
    private JButton play, rec, prog;
    private Thread runner;
    private boolean refreshed;

    public AudiencePanel(final MyFreeTV owner) {
        super();
        this.owner = owner;
        setLayout(new BorderLayout());
        // setBorder(new TitledBorder(null,"Entregistrements en cours"));
        audienceList = new AudienceList();
        audienceList.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    int selectedIndex = audienceList.locationToIndex(e.getPoint());
                    if(selectedIndex >= 0) {
                        Channel channel = ((XMLChannel) audienceList.getModel().getElementAt(selectedIndex)).getChannel();
                        if(!JobManager.getInstance().isPlaying(channel))
                            owner.getActions().play(channel);
                    }
                }
            }
        });
        audienceList.addListSelectionListener(this);
        // recordsList.addListSelectionListener(owner);
        JScrollPane scroll = new JScrollPane(audienceList);
        scroll.setPreferredSize(new Dimension(350, 50));
        add(scroll);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        play = new JButton(ImageManager.getInstance().getImageIcon("play"));
        rec = new JButton(ImageManager.getInstance().getImageIcon("record"));
        prog = new JButton(ImageManager.getInstance().getImageIcon("prog"));
//        play.setBorder(MyFreeTV.buttonBorder);
//        rec.setBorder(MyFreeTV.buttonBorder);
//        prog.setBorder(MyFreeTV.buttonBorder);
        play.setToolTipText("Regarder la chaîne sélectionnéé.");
        rec.setToolTipText("Enregistrer la chaîne sélectionnée dans un fichier.");
        prog.setToolTipText("Programmer un enregistrement.");
        play.setActionCommand("play");
        rec.setActionCommand("rec");
        prog.setActionCommand("prog");
        play.addActionListener(this);
        rec.addActionListener(this);
        prog.addActionListener(this);
        bottomPanel.add(play);
        bottomPanel.add(rec);
        bottomPanel.add(prog);
        add(bottomPanel, BorderLayout.SOUTH);

        if(runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void refresh() {
        audienceList.getModel().refresh();
        refreshed = true;
        // audienceList.repaint();
        initButtons();
    }

    // public AudienceList getAudienceList() {
    // return audienceList;
    // }

    public void initButtons() {
        Object selectedValue = null;
        if(audienceList.getSelectedIndex() < audienceList.getModel().getSize())
            selectedValue = audienceList.getSelectedValue();
        if(selectedValue == null) {
            play.setEnabled(false);
            rec.setEnabled(false);
            prog.setEnabled(false);
        } else {
            Channel channel = ((XMLChannel) selectedValue).getChannel();
            if(channel.getUrl() == null) {
                play.setEnabled(false);
                rec.setEnabled(false);
                prog.setEnabled(false);
            } else {
                play.setEnabled(!JobManager.getInstance().isPlaying(channel));
                rec.setEnabled(!JobManager.getInstance().isRecording(channel));
                prog.setEnabled(true);
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        initButtons();
    }

    public boolean isRefreshed() {
        return refreshed;
    }

    public void actionPerformed(ActionEvent e) {
        int selectedIndex = audienceList.getSelectedIndex();
        if(selectedIndex >= 0) {
            String s = e.getActionCommand();
            Channel channel = ((XMLChannel) audienceList.getModel().getElementAt(selectedIndex)).getChannel();
            // ChannelManager.getInstance().getChannels().get(selectedIndex);
            if(channel != null) {
                if(s.equals("play")) {
                    owner.getActions().play(channel);
                } else if(s.equals("rec")) {
                    owner.getActions().record(channel);
                } else if(s.equals("prog")) {
                    owner.getActions().prog(channel);
                }
            }
        }
    }

    public void run() {
        while(true) {
            if(owner.isAudiencePanelVisible()) {
                refresh();
            } else {
                refreshed = false;
            }
            try {
                Thread.sleep(60000);
            } catch(InterruptedException e) {}
        }
    }

}
