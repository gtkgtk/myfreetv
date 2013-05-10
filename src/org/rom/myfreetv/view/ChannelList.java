package org.rom.myfreetv.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.rom.myfreetv.streams.Channel;

class ChannelList extends JList {

  // private final static Color[] colors = new Color[] { new Color(0xfb, 0xf2,
  // 0xd5), Color.WHITE};
  private ChannelListModel model;

  class TheCellRenderer extends ChannelCellPanel implements ListCellRenderer {

    public TheCellRenderer() {
      super();
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      Channel channel = (Channel) value;
      set(channel);
      setBackground(isSelected ? getSelectionBackground() : Color.WHITE);
      // Color background = colors[index % colors.length];
      // setBackground(isSelected ? getSelectionBackground() :
      // background);
      return this;
    }
  }

  public ChannelList() {
    super();
    model = new ChannelListModel();
    setModel(model);
    setFixedCellHeight(18);
    setValueIsAdjusting(true);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setCellRenderer(new TheCellRenderer());
  }

  public ChannelListModel getModel() {
    return model;
  }
  public void refresh() {
    removeAll();
    setModel(model);
    updateUI();
    repaint();
  }
}
