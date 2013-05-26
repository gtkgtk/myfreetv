package org.rom.myfreetv.view;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JToolTip;

import org.rom.myfreetv.guidetv.Emission;

public class EmissionToolTip extends JToolTip {

    private EmissionDetailsPanel panel;

    public EmissionToolTip() {
        super();
        setLayout(new BorderLayout());
        panel = new EmissionDetailsPanel();
        add(panel);
        // setFocusTraversable(true);
        // setComponent(panel);
    }

    public void set(JComponent comp, Emission emission) {
        setComponent(comp);
        panel.set(emission);
    }
}
