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
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.process.ProgramManager;
import org.rom.myfreetv.process.RecordJob;
import org.rom.myfreetv.streams.Playable;
import org.rom.myfreetv.streams.Recordable;

class RecordPanel extends JPanel implements Runnable, ActionListener, ListSelectionListener {

    private MyFreeTV owner;
    private JButton play, stop, prog;
    private RecordList recordList;
    private Thread runner;

    public RecordPanel(final MyFreeTV owner) {
        super();
        this.owner = owner;
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(null, "Enregistrements en cours"));
        recordList = new RecordList();
        recordList.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    int selectedIndex = recordList.locationToIndex(e.getPoint());
                    if(selectedIndex >= 0) {
                        RecordJob job = (RecordJob) recordList.getModel().getElementAt(selectedIndex);
                        if(job.getStream() instanceof Playable && !JobManager.getInstance().isPlaying((Playable) job.getStream()))
                            owner.getActions().play((Playable)job.getStream());
                    }
                }
            }
        });
        recordList.addListSelectionListener(this);
        JScrollPane scroll = new JScrollPane(recordList);
        scroll.setPreferredSize(new Dimension(350, 50));
        add(scroll);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        play = new JButton(ImageManager.getInstance().getImageIcon("play"));
        stop = new JButton(ImageManager.getInstance().getImageIcon("stop"));
        prog = new JButton(ImageManager.getInstance().getImageIcon("prog"));
//        play.setBorder(MyFreeTV.buttonBorder);
//        stop.setBorder(MyFreeTV.buttonBorder);
//        prog.setBorder(MyFreeTV.buttonBorder);
        play.setToolTipText("Regarder la chaîne qui correspond à l'enregistrement sélectionné.");
        stop.setToolTipText("Arrêter l'enregistrement sélectionné.");
        prog.setToolTipText("Programmer l'arrêt de l'enregistrement sélectionné.");
        play.setActionCommand("play");
        stop.setActionCommand("stop");
        prog.setActionCommand("prog");
        play.addActionListener(this);
        stop.addActionListener(this);
        prog.addActionListener(this);
        bottomPanel.add(play);
        bottomPanel.add(stop);
        bottomPanel.add(prog);
        add(bottomPanel, BorderLayout.SOUTH);

        if(runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void update() {
        recordList.getModel().refresh();
    }

    public void valueChanged(ListSelectionEvent e) {
        initButtons();
    }

    public void initButtons() {
        Object selectedValue = null;
        if(recordList.getSelectedIndex() < recordList.getModel().getSize())
            selectedValue = recordList.getSelectedValue();
        // int selectedIndex = recordList.getSelectedIndex();
        if(selectedValue == null) {
            play.setEnabled(false);
            stop.setEnabled(false);
            prog.setEnabled(false);
        } else {
            // RecordJob job =
            // JobManager.getInstance().getRecords().get(selectedIndex);
            RecordJob job = (RecordJob) selectedValue;
            play.setEnabled(job.getStream() instanceof Playable && !JobManager.getInstance().isPlaying((Playable) job.getStream()));
            stop.setEnabled(true);
//            boolean found = false;
//            List<Program> programs = ProgramManager.getInstance().getPrograms();
//            int i = 0;
//            while(i < programs.size() && !found) {
//                Program p = programs.get(i);
//                if(p.getJob() == job)
//                    found = true;
//                i++;
//            }
            prog.setEnabled(!ProgramManager.getInstance().isProgrammed(job));
        }
    }

    // public RecordList getRecordsList() {
    // return recordList;
    // }

    public void actionPerformed(ActionEvent e) {
        int selectedIndex = recordList.getSelectedIndex();
        if(selectedIndex >= 0) {
            String s = e.getActionCommand();
            RecordJob job = (RecordJob) recordList.getModel().getElementAt(selectedIndex);
            // ChannelManager.getInstance().getChannels().get(selectedIndex);
            if(s.equals("play")) {
                Recordable recordable = job.getRecordable();
                if(recordable instanceof Playable)
                    owner.getActions().play((Playable) recordable);
            } else if(s.equals("stop")) {
                owner.getActions().stopRecord(job);
            } else if(s.equals("prog")) {
                owner.getActions().prog(job);
            }
        }
    }

    public void run() {
        while(true) {
            recordList.getModel().refresh();
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {}
        }
    }

}
