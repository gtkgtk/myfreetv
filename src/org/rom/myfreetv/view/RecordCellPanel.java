package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.rom.myfreetv.process.RecordJob;
import org.rom.myfreetv.streams.Channel;

class RecordCellPanel extends JPanel implements Comparable<RecordCellPanel> {

    private RecordJob job;
    private JPanel top, bottom;
    private JLabel time, label, bottomLabel;
    private LogoViewer logo;

    private NumberFormat formatter = new DecimalFormat("00");

    public RecordCellPanel() {
        super();
        setName(toString());
        // this.job = job;
        setLayout(new BorderLayout());

        logo = new LogoViewer(64, 40);

        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        top = new JPanel(new BorderLayout());
        label = new JLabel();
        time = new JLabel();
        top.add(label);
        top.add(time, BorderLayout.EAST);

        bottom = new JPanel();
        bottomLabel = new JLabel();
        bottom.add(bottomLabel);

        // logo.setImage(ImageManager.getInstance().getLogoImageIcon(job.getPlayable()));

        pan.add(top);
        pan.add(bottom, BorderLayout.SOUTH);

        add(logo, BorderLayout.WEST);
        add(pan);
    }

    public void set(RecordJob job) {
        this.job = job;
        label.setIcon(ImageManager.getInstance().getImageIcon("record"));
        if(job.getRecordable() instanceof Channel)
            label.setText("<html><b>" + ((Channel) job.getRecordable()).getName() + "</b></html>");
        else
            label.setText(null);
        bottomLabel.setText("<html><center><i>" + job.getUrlOutput() + "</i></center></html>");
        logo.setLogo(job.getStream());
        refreshTime();
    }

    public void refreshTime() {
        int msDiff = (int) ((System.currentTimeMillis() - job.getStartDate().getTimeInMillis()) / 1000);
        int hours = msDiff / 3600;
        int minutes = (msDiff % 3600) / 60;
        int seconds = msDiff % 60;
        time.setText(hours + ":" + formatter.format(minutes) + ":" + formatter.format(seconds));
    }

    public void setBackground(Color color) {
        super.setBackground(color);
        if(logo != null)
            logo.setBackground(color);
        if(top != null)
            top.setBackground(color);
        if(bottom != null) {
            bottom.setBackground(color);
            for(Component c : bottom.getComponents())
                c.setBackground(color);
        }
    }

    public void setForeground(Color color) {
        super.setForeground(color);
        if(logo != null)
            logo.setForeground(color);
        if(top != null)
            top.setForeground(color);
        if(bottom != null) {
            bottom.setForeground(color);
            for(Component c : bottom.getComponents())
                c.setForeground(color);
        }
    }

    public RecordJob getJob() {
        return job;
    }

    public int compareTo(RecordCellPanel other) {
        return job.compareTo(other.job);
    }

    public String toString() {
        return job == null ? null : job.toString();
    }

}
