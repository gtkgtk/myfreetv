package org.rom.myfreetv.view;

import javax.swing.DefaultComboBoxModel;

import org.rom.myfreetv.streams.ChannelManager;

class ChannelComboBoxModel extends DefaultComboBoxModel {

  public Object getElementAt(int index) {
    return ChannelManager.getInstance().getChannels().get(index).getHTML();
  }

  public int getSize() {
    return ChannelManager.getInstance().getChannels().size();
  }

}
