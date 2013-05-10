package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.rom.myfreetv.files.RecordFile;

class FileCellPanel extends JPanel implements Comparable<FileCellPanel> {

    private final static DateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss");

    private RecordFile recordFile;
    private LogoViewer logo;
    private JPanel center;
    private JLabel label;
    private boolean hasJob;

    public FileCellPanel() {
        super();
        setName(toString());
        // this.recordFile = recordFile;
        setLayout(new BorderLayout());
        // setBorder(new LineBorder(Color.GRAY));

        center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // center.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        label = new JLabel();
        center.add(label);
        // state = new JLabel();

        logo = new LogoViewer(64, 40);

        // add(state,BorderLayout.WEST);
        add(center);
        add(logo, BorderLayout.EAST);
    }

    public void set(RecordFile recordFile) {
        this.recordFile = recordFile;
        logo.setLogo(recordFile.getChannel());
        // logo.setScaledImage(ImageManager.getInstance().getScaledLogoImageIcon(recordFile.getChannel()));
        hasJob = recordFile.getJob() != null;
        label.setIcon(hasJob ? ImageManager.getInstance().getImageIcon("record") : null);
        forceRefresh();
    }

    // public void refresh() {
    // if(recordFile.getJob() != null)
    // forceRefresh();
    // refreshIcon();
    // }

    public void forceRefresh() {
        File file = recordFile.getFile();
        File parent = file.getParentFile();
        int mo = recordFile.getMo(); // file.exists() ? ((int) (file.length()
        // / 1048576)) : -1;
        StringBuffer buf = new StringBuffer("<html><b>");
        buf.append(file.getName());
        buf.append("</b>");
        if(parent != null) {
            buf.append(" <i>(");
            buf.append(parent.getAbsolutePath());
            buf.append(")</i>");
        }
        buf.append("<br><i>");
        buf.append(formatter.format(new Date(recordFile.getStartDate().getTimeInMillis())));
        buf.append("</i> - <font size=\"2\"><font color=\"#3f3f3f\">");
        if(mo >= 0) {
            buf.append(mo);
            buf.append(" Mo");
        } else {
            buf.append("non existant");
        }
        buf.append("</font></font>");
        buf.append("</html>");
        label.setText(new String(buf));
    }

    // public void refreshIcon() {
    // if((recordFile.getJob() == null && hasJob) || (recordFile.getJob() !=
    // null && !hasJob)) {
    // hasJob = !hasJob;
    // refreshIcon();
    // }
    // }

    public RecordFile getRecordFile() {
        return recordFile;
    }

    public void setBackground(Color color) {
        super.setBackground(color);
        if(logo != null)
            logo.setBackground(color);
        if(center != null)
            center.setBackground(color);
    }

    public void setForeground(Color color) {
        super.setForeground(color);
        if(logo != null)
            logo.setForeground(color);
        if(center != null)
            center.setForeground(color);
    }

    public int compareTo(FileCellPanel other) {
        return recordFile.compareTo(other.recordFile);
    }

    public String toString() {
        return recordFile == null ? null : recordFile.toString();
    }

}
