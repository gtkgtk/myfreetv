package org.rom.myfreetv.view;

import javax.swing.JComboBox;

import org.rom.myfreetv.config.DeinterlaceMode;

public class DeinterlaceComboBox extends JComboBox {

    private DeinterlaceComboBoxModel model;

    public DeinterlaceComboBox() {
        super();
        model = new DeinterlaceComboBoxModel();
        setModel(model);
    }

    public DeinterlaceComboBoxModel getComboBoxModel() {
        return model;
    }

    public void setSelectedDeinterlaceMode(DeinterlaceMode mode) {
        int i = 0;
        while(i < DeinterlaceComboBoxModel.modes.length && mode != DeinterlaceComboBoxModel.modes[i])
            i++;
        if(i < DeinterlaceComboBoxModel.modes.length)
            setSelectedIndex(i);
        else
            setSelectedIndex(0);
    }

}
