package org.rom.myfreetv.view;

import java.util.List;

import javax.swing.AbstractListModel;

import org.rom.myfreetv.guidetv.Emission;

class EmissionListModel extends AbstractListModel {

    private List<Emission> list;

    public Object getElementAt(int index) {
        if(list != null)
            return list.get(index);
        else
            return null;
    }

    public int getSize() {
        if(list != null)
            return list.size();
        else
            return 0;
    }

    public void setEmissions(List<Emission> list) {
        this.list = list;
        refresh();
    }

    public void refresh() {
        fireContentsChanged(this, 0, getSize());
    }
}