package org.rom.myfreetv.view;

import javax.swing.AbstractListModel;

import org.rom.myfreetv.files.RecordFileManager;

class FileListModel extends AbstractListModel {

    public Object getElementAt(int index) {
        return RecordFileManager.getInstance().getRecordFiles().get(index);
    }

    public int getSize() {
        return RecordFileManager.getInstance().getRecordFiles().size();
    }

    public void refresh() {
        fireContentsChanged(this, 0, getSize());
    }

    // public void refreshOnlyValues() {
    // for(RecordFileElementPanel rfep : list) {
    // rfep.refresh();
    // }
    // fireContentsChanged(this, 0, list.size());
    // }
    //    
    // public void forceRefresh() {
    // for(RecordFileElementPanel rfep : list) {
    // rfep.forceRefresh();
    // }
    // fireContentsChanged(this, 0, list.size());
    // }
    //    
    // public void refresh() {
    // int oldSize = list.size();
    // List<RecordFileElementPanel> temp = new
    // ArrayList<RecordFileElementPanel>();
    // for(RecordFile rf : RecordFileManager.getInstance().getRecordFiles()) {
    // temp.add(new RecordFileElementPanel(rf));
    // }
    // Collections.sort(temp);
    // list = temp;
    // fireContentsChanged(this, 0, Math.max(list.size(),oldSize));
    // }

    // public List<RecordFileElementPanel> getList() {
    // return list;
    // }

}
