package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.rom.myfreetv.guidetv.Emission;

class EmissionDetailsPanel extends JPanel {

    private final static Color colorTop = new Color(0xfb, 0xf2, 0xd5);
    private final static Color colorTitle = new Color(0xf9, 0xe0, 0x89);
    private final static DateFormat formatter = new SimpleDateFormat("HH:mm");
    
    private JPanel contentPane;
    private JLabel channelLabel;
    private JLabel timeLabel;
    private JLabel titleLabel;
    private LogoViewer photoLabel;
    private JLabel typeLabel;
    private JLabel showviewLabel;
    private JTextArea description;
    private JPanel typeShowviewDesc;

    public EmissionDetailsPanel() {
        super(new BorderLayout());

        channelLabel = new JLabel();
        timeLabel = new JLabel();
        titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        photoLabel = new LogoViewer(Emission.maxWidth, Emission.maxHeight);
        typeLabel = new JLabel();
        showviewLabel = new JLabel();

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(colorTop);
        top.add(channelLabel);
        top.add(timeLabel, BorderLayout.EAST);

        JPanel title = new JPanel(new BorderLayout());
        title.setBackground(colorTitle);
        title.add(titleLabel);

        JPanel typeShowview = new JPanel(new BorderLayout());
        typeShowview.setBackground(colorTop);
        typeShowview.add(typeLabel);
        typeShowview.add(showviewLabel, BorderLayout.EAST);

//        descPane = new JPanel(new BorderLayout());
        description = new JTextArea();
        description.setEditable(false);
        description.setWrapStyleWord(true);
        description.setLineWrap(true);

//        descPane.add(description);
//        descPane.setBackground(Color.WHITE);

        typeShowviewDesc = new JPanel(new BorderLayout());
        typeShowviewDesc.add(typeShowview, BorderLayout.NORTH);
        typeShowviewDesc.add(description);

        JPanel photoPane = new JPanel();
        photoPane.setBackground(Color.WHITE);
        photoPane.add(photoLabel);

        JPanel center = new JPanel(new BorderLayout());
        center.add(title, BorderLayout.NORTH);
        center.add(photoPane, BorderLayout.WEST);
        center.add(typeShowviewDesc);

        add(top, BorderLayout.NORTH);
        add(center);
    }

    public void set(Emission emission) {
        if(emission == null) {
            channelLabel.setText(null);
            timeLabel.setText(null);
            titleLabel.setText(null);
            photoLabel.setIcon(null);
            typeLabel.setText(null);
            showviewLabel.setText(null);
            description.setText(null);
        } else {
            channelLabel.setText("<html><b>" + emission.getChannel().getName() + "</b></html>");
            timeLabel.setText(formatter.format(emission.getStart().getTime()) + " - " + formatter.format(emission.getEnd().getTime()));
            // descriptionLabel.setIcon(emission.getImage() != null? new
            // ImageIcon(emission.getImage()): null);
            StringBuffer buf = new StringBuffer("<html><center><font size=\"4\"><b>");
            buf.append(emission.getTitle());
            buf.append("</b></font>");
            if(emission.getSubtitle() != null) {
                buf.append(" <font size=\"3\">");
                buf.append(emission.getSubtitle());
                buf.append("</font>");
            }
            buf.append("</center></html>");
            titleLabel.setText(new String(buf));
            if(emission.getImage() != null)
                photoLabel.setIcon(new ImageIcon(emission.getImage()));
            else
                photoLabel.setIcon(null);
            typeLabel.setText(emission.getType());
            if(emission.getShowview() != null)
                showviewLabel.setText("<html><font size=\"2\"><font color=\"#3f3f3f\">" + emission.getShowview() + "</font></font></html>");
            else
                showviewLabel.setText(null);
            description.setText(emission.getDetails());
        }
    }

}
