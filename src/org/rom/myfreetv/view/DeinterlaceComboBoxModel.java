package org.rom.myfreetv.view;

import javax.swing.DefaultComboBoxModel;

import org.rom.myfreetv.config.DeinterlaceMode;

class DeinterlaceComboBoxModel extends DefaultComboBoxModel {

    static DeinterlaceMode modes[] = { DeinterlaceMode.NONE, DeinterlaceMode.BLEND, DeinterlaceMode.BOB, DeinterlaceMode.DISCARD, DeinterlaceMode.LINEAR, DeinterlaceMode.MEAN, DeinterlaceMode.X };

    public Object getElementAt(int index) {
        return modes[index].getName();
    }

    public int getSize() {
        return modes.length;
    }

}