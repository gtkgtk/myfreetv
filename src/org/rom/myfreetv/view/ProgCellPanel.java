package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.rom.myfreetv.process.HebdoProgramRules;
import org.rom.myfreetv.process.Program;
import org.rom.myfreetv.process.ProgramRules;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.Recordable;

class ProgCellPanel extends JPanel implements Comparable<ProgCellPanel> {

    private static enum State {
        WAITING, RECORDING, DISABLED
    }

    private static DateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm");

    private Program program;
    private JLabel channelLabel, timeLabel;
    private JPanel top, bottom, progressPan, logoPan;
    private JProgressBar progress;
    private LogoViewer logo;

    private State state;

    public ProgCellPanel() {
        super();
        setName(toString());
        // this.program = program;
        setLayout(new BorderLayout());

        logo = new LogoViewer(64, 40);
        logoPan = new JPanel();
        logoPan.add(logo);

        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        top = new JPanel(new BorderLayout());
        channelLabel = new JLabel();
        // label.setIcon(ImageManager.getInstance().getImageIcon("prog"));

        progress = new JProgressBar();
        top.add(channelLabel);
        progressPan = new JPanel();
        progressPan.add(progress);
        top.add(progressPan, BorderLayout.EAST);

        timeLabel = new JLabel();

        bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(timeLabel);
        // bottom.add(new JLabel(new String(period)));

        pan.add(top);
        pan.add(bottom);

        add(logoPan, BorderLayout.WEST);
        add(pan);

    }

    public void set(Program program) {
        this.program = program;
        Recordable recordable = program.getRecordable();
        String name;
        if(recordable instanceof Channel && ((Channel) recordable).getUrl() != null)
            name = recordable.getName();
        else
            name = "Chaîne actuellement indisponible";
        channelLabel.setText("<html><b>" + name + "</b></html>");
        logo.setLogo(program.getRecordable());
        refreshState();
        refreshTime();
    }

    private void refreshState() {
        if(program.getRecordable() != null) {
            boolean started = program.isStarted();
            if(!started) { // !program.isComplete()
                if(state != State.WAITING) {
                    state = State.WAITING;
                    channelLabel.setIcon(ImageManager.getInstance().getImageIcon("prog"));
                    progress.setValue(0);
                }
            } else { // !program.isComplete()
                if(state != State.RECORDING) {
                    state = State.RECORDING;
                    channelLabel.setIcon(ImageManager.getInstance().getImageIcon("record"));
                }
                int percent;
                long start = program.getStart().getTimeInMillis();
                long stop = program.getStop().getTimeInMillis();
                long current = System.currentTimeMillis();
                int totalLength = (int) (stop - start);
                int currentLength = (int) (current - start);
                percent = currentLength * 100 / totalLength;
                if(percent < 0)
                    percent = 0;
                else if(percent > 100)
                    percent = 100;
                progress.setValue(percent);
            }
        } else {
            if(state != State.DISABLED) {
                state = State.DISABLED;
                channelLabel.setIcon(ImageManager.getInstance().getImageIcon("prog_suppr"));
                progress.setValue(0);
            }
        }
    }

    private void refreshTime() {
        timeLabel.setText(program.getHTML());
        StringBuffer buf = new StringBuffer("<html><b>Destination :</b> ");
        ProgramRules pr = program.getProgramRules();
        if(pr instanceof HebdoProgramRules) {
            buf.append(((HebdoProgramRules) pr).getPath());
            buf.append("<br><b>D�marrage :</b> ");
            buf.append(formatter.format(program.getStart().getTime()));
        } else { // if(pr instanceof OnceProgramRules) {
            buf.append(pr.getFilename());
        }
        buf.append("</html>");
        setToolTipText(new String(buf));
    }

    public void setBackground(Color color) {
        super.setBackground(color);
        if(top != null)
            top.setBackground(color);
        if(bottom != null) {
            bottom.setBackground(color);
            for(Component c : bottom.getComponents())
                c.setBackground(color);
        }
        if(progressPan != null)
            progressPan.setBackground(color);
        if(logoPan != null)
            logoPan.setBackground(color);
    }

    public void setForeground(Color color) {
        super.setForeground(color);
        if(top != null)
            top.setForeground(color);
        if(bottom != null) {
            bottom.setForeground(color);
            for(Component c : bottom.getComponents())
                c.setForeground(color);
        }
        if(progressPan != null)
            progressPan.setForeground(color);
        if(logoPan != null)
            logoPan.setForeground(color);
    }

    // public Program getProgram() {
    // return program;
    // }

    public int compareTo(ProgCellPanel other) {
        return program.compareTo(other.program);
    }

    public String toString() {
        return program == null ? null : program.toString();
    }

}
