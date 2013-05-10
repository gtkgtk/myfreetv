package org.rom.myfreetv.view;

import javax.swing.AbstractListModel;

import org.rom.myfreetv.audience.AudienceXML;

class AudienceListModel extends AbstractListModel {

    // private List<AudienceElementPanel> list;

    public Object getElementAt(int index) {
        return AudienceXML.getInstance().getList().get(index);
        // return list.get(index);
    }

    public int getSize() {
        return AudienceXML.getInstance().getList().size();
        // return list.size();
    }

    public void refresh() {
        new Thread() {

            public void run() {
                // int oldSize = list.size();
                try {
                    AudienceXML.getInstance().init();
                } catch(Exception e) {}
                // List<AudienceElementPanel> temp = new
                // ArrayList<AudienceElementPanel>();
                // //// list.clear();
                // for(XMLChannel ch : AudienceXML.getInstance().getList())
                // temp.add(new AudienceElementPanel(ch));
                // Collections.sort(temp);
                // list = temp;
                // // fireContentsChanged(AudienceListModel.this, 0, 0);
                // // fireIntervalAdded(AudienceListModel.this, 0,
                // Math.max(getSize(), oldSize));
                fireContentsChanged(AudienceListModel.this, 0, getSize());
            }
        }.start();
    }

    // public List<AudienceElementPanel> getList() {
    // return list;
    // }

}
