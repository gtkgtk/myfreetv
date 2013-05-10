package org.rom.myfreetv.view;

import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;

import org.rom.myfreetv.streams.ChannelManager;
import org.rom.myfreetv.streams.ChannelsLoadException;

class ChannelListModel extends AbstractListModel {

    public Object getElementAt(int index) {
        return ChannelManager.getInstance().getChannels().get(index);
    }

    public int getSize() {
        return ChannelManager.getInstance().getChannels().size();
    }

    public void load(final MyFreeTV main) {
        new Thread() {

            public void run() {
                try {
                    ChannelManager.initialize();
                } catch(ChannelsLoadException e) {
                    JOptionPane.showMessageDialog(main, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                fireContentsChanged(ChannelListModel.this, 0, ChannelManager.getInstance().getChannels().size());
            }
        }.start();
    }

    public void refresh() {
        fireContentsChanged(this, 0, getSize());
    }

}
