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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.rom.myfreetv.guidetv.Emission;

class EmissionDialog extends JDialog implements ActionListener {

    private MyFreeTV owner;
    private JScrollPane scroll2;
    private EmissionDetailsPanel details;

    public EmissionDialog(MyFreeTV owner,Emission selectedEmission) {
        super(owner, "Détails émission", true);
        this.owner = owner;
        setResizable(false);
        
        Point p = owner.getLocation();
        Dimension d = owner.getSize();
        int x1 = (int) p.getX();
        int y1 = (int) p.getY();
        int x2 = x1 + (int) d.getWidth();
        int y2 = y1 + (int) d.getHeight();

        JPanel center = new JPanel(new BorderLayout());

        details = new EmissionDetailsPanel();
        
        details.set(selectedEmission);
        
        scroll2 = new JScrollPane(details);
        //scroll2.setHorizontalScrollBar(null);
        scroll2.setPreferredSize(new Dimension(600, 180));
        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll2.getVerticalScrollBar().setUnitIncrement(16);
        scroll2.getViewport().setViewPosition(new Point(1,1));

        JPanel bottom = new JPanel();

        JButton ok = new JButton("OK");
        ok.setActionCommand("ok");
        ok.addActionListener(this);

        bottom.add(ok);
        
        center.add(scroll2, BorderLayout.SOUTH);

        add(center);
        add(bottom, BorderLayout.SOUTH);

        pack();

        int posX = (x1 + x2 - (int) getSize().getWidth()) / 2;
        int posY = (y1 + y2 - (int) getSize().getHeight()) / 2;

        setLocation(posX, posY);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if(s.equals("ok")) {
            dispose();
        }
    }
}
