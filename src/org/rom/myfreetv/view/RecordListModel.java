package org.rom.myfreetv.view;

import javax.swing.AbstractListModel;

import org.rom.myfreetv.process.JobManager;

class RecordListModel extends AbstractListModel {

    public Object getElementAt(int index) {
        return JobManager.getInstance().getRecords().get(index);
    }

    public int getSize() {
        return JobManager.getInstance().getRecords().size();
    }

    public void refresh() {
        fireContentsChanged(this, 0, getSize());
        // List<RecordJob> records = JobManager.getInstance().getRecords();
        // List<RecordElementPanel> aSuppr = new
        // ArrayList<RecordElementPanel>();
        // RecordJob j;
        // for(RecordElementPanel rep : list) {
        // j = rep.getJob();
        // if(!records.contains(j)) {
        // aSuppr.add(rep);
        // }
        // }
        // for(RecordElementPanel rep : aSuppr) {
        // list.remove(rep);
        // }
        // aSuppr = null;
        // for(RecordJob job : records) {
        // boolean found = false;
        // int i = 0;
        // while(i < list.size() && !found) {
        // if(list.get(i).getJob() == job)
        // found = true;
        // i++;
        // }
        // if(!found)
        // list.add(new RecordElementPanel(job));
        // }
        // Collections.sort(list);
        // fireContentsChanged(this, 0, list.size());
    }

    // public List<RecordElementPanel> getList() {
    // return list;
    // }

}
