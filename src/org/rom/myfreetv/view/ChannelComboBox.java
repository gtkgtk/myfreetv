package org.rom.myfreetv.view;

import javax.swing.JComboBox;

public class ChannelComboBox extends JComboBox {

    private ChannelComboBoxModel model;

    public ChannelComboBox() {
        super();
        model = new ChannelComboBoxModel();
        setModel(model);
    }

    public ChannelComboBoxModel getComboBoxModel() {
        return model;
    }

}
