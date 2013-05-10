package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.rom.myfreetv.audience.XMLChannel;

public class AudienceCellPanel extends JPanel implements Comparable<AudienceCellPanel> {

    private final static NumberFormat formatter = new DecimalFormat("0.00");
    private final static NumberFormat formatter00 = new DecimalFormat("00");
    private JPanel center, marketSharePanel;
    private XMLChannel ch;
    private LogoViewer logo;
    private JLabel titleLabel, marketShareLabel;

    public AudienceCellPanel() {
        super();
        setName(toString());

        setLayout(new BorderLayout());

        logo = new LogoViewer(64, 40);

        center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleLabel = new JLabel();
        center.add(titleLabel);

        marketShareLabel = new JLabel();
        marketSharePanel = new JPanel();
        marketSharePanel.add(marketShareLabel);
        add(logo, BorderLayout.WEST);
        add(center);
        add(marketSharePanel, BorderLayout.EAST);

        // refreshState();
    }

    public void set(XMLChannel ch) {
        this.ch = ch;
        StringBuffer buf = new StringBuffer("<html><b>");
        buf.append(ch.getChannel().getName());
        buf.append("</b><br>");
        // StringBuffer buf = new StringBuffer("<html><font size=\"2\"><font
        // color=\"#3f3f3f\">");
        if(ch.getStart() != null && ch.getStop() != null) {
            buf.append("<font size=\"2\"><font color=\"#3f3f3f\">");
            buf.append(formatter00.format(ch.getStart().get(Calendar.HOUR_OF_DAY)));
            buf.append(':');
            buf.append(formatter00.format(ch.getStart().get(Calendar.MINUTE)));
            buf.append(" - ");
            buf.append(formatter00.format(ch.getStop().get(Calendar.HOUR_OF_DAY)));
            buf.append(':');
            buf.append(formatter00.format(ch.getStop().get(Calendar.MINUTE)));
            buf.append("</font></font> ");
        }
        if(ch.getTitle() != null)
            buf.append(ch.getTitle());
        buf.append("</html>");
        titleLabel.setText(new String(buf));
        marketShareLabel.setText("<html><h3>" + formatter.format(ch.getMarketShare()) + " %</h3></html>");
        logo.setLogo(ch.getChannel());
    }

    // public XMLChannel getXMLChannel() {
    // return ch;
    // }

    // public void refreshState() {
    // int percent;
    // long start = ch.getStart().getTimeInMillis();
    // long stop = ch.getStop().getTimeInMillis();
    // long current = System.currentTimeMillis();
    // int totalLength = (int)(stop - start);
    // int currentLength = (int)(current - start);
    // percent = currentLength * 100 / totalLength;
    // if(percent < 0)
    // percent = 0;
    // else if(percent > 100)
    // percent = 100;
    // progress.setValue(percent);
    // }

    public void setBackground(Color color) {
        super.setBackground(color);
        if(logo != null)
            logo.setBackground(color);
        if(center != null)
            center.setBackground(color);
        // if(bottom != null)
        // bottom.setBackground(color);
        if(marketSharePanel != null)
            marketSharePanel.setBackground(color);
    }

    public void setForeground(Color color) {
        super.setForeground(color);
        if(logo != null)
            logo.setForeground(color);
        if(center != null)
            center.setForeground(color);
        // if(bottom != null)
        // bottom.setForeground(color);
        if(marketSharePanel != null)
            marketSharePanel.setForeground(color);
    }

    public int compareTo(AudienceCellPanel other) {
        return -ch.compareTo(other.ch);
    }

    public String toString() {
        return ch == null ? null : ch.toString();
    }

}
