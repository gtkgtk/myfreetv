package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.rom.myfreetv.guidetv.Emission;

class EmissionCellPanel extends JPanel {

    private final static DateFormat formatter = new SimpleDateFormat("HH:mm");
    private JLabel label;

    EmissionCellPanel() {
        super(new BorderLayout());
        label = new JLabel();
        add(label);
    }

    public void set(Emission emission) {
        StringBuffer buf = new StringBuffer("<html><b>");
        buf.append(formatter.format(emission.getStart().getTime()));
        buf.append(" - ");
        buf.append(formatter.format(emission.getEnd().getTime()));
        buf.append("</b> ");
        buf.append(emission.getTitle());
        if(emission.getSubtitle() != null)
            buf.append(" (" + emission.getSubtitle() + ")");
        buf.append("</html>");
        label.setText(new String(buf));
    }

}
