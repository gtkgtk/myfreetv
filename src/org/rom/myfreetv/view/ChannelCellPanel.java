package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.rom.myfreetv.streams.Channel;

class ChannelCellPanel extends JPanel {

    private final static DateFormat formatter = new SimpleDateFormat("hh:MM");

    // private LogoViewer logo;
    private Channel ch;
    private JLabel label;

    // private EmissionToolTip tooltip;

    public ChannelCellPanel() {
        // super();
        super(new BorderLayout());
        setName(toString());
        // tooltip = new EmissionToolTip();
        label = new JLabel();
        add(label);
        // ToolTipManager.sharedInstance().registerComponent(this);
    }

    public void set(Channel channel) {
        this.ch = channel;
        label.setText(channel.getHTML());
        // tooltip.set(this, GuideTVManager.getInstance().getCurrent(channel));
        // Emission emission = GuideTVManager.getInstance().getCurrent(channel);
        // if(emission == null)
        // setToolTipText(null);
        // else {
        // StringBuffer buf = new StringBuffer("<html><b>Actuellement:</b>");
        // buf.append(formatter.format(emission.getStart().getTime()));
        // buf.append(" - ");
        // buf.append(formatter.format(emission.getEnd().getTime()));
        // buf.append(" : ");
        // buf.append(emission.getTitle());
        // if(emission.getSubtitle() != null)
        // buf.append(" (" + emission.getSubtitle() + ")");
        // buf.append("</html>");
        // setToolTipText(new String(buf));
        // }
        // if(isSelected)
        // logo.setImage(ImageManager.getInstance().getLogoImageIcon(channel));
        // else
        // logo.setImage(null);
    }

    // public JToolTip createToolTip() {
    // System.out.println("create");
    // return tooltip;
    // }

    public String toString() {
        return ch != null ? ch.toString() : null;
    }

}
