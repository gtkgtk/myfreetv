package org.rom.myfreetv.view;

import javax.swing.AbstractListModel;

import org.rom.myfreetv.process.ProgramManager;

class ProgListModel extends AbstractListModel {

    public Object getElementAt(int index) {
        return ProgramManager.getInstance().getPrograms().get(index);
    }

    public int getSize() {
        return ProgramManager.getInstance().getPrograms().size();
    }

    public void refresh() {
        // for(int i = 0; i < getSize(); i++) {
        // Program p = (Program)getElementAt(i);
        // if(p.getRecordable() != null)
        // fireContentsChanged(this, i, i+1);
        // }
        fireContentsChanged(this, 0, getSize());
    }

    // public void remove(ProgElementPanel pep) {
    // list.remove(pep);
    // fireContentsChanged(this, 0, getSize());
    // }

    // public void refresh() {
    // // list.clear();
    // List<Program> programs = ProgramManager.getInstance().getPrograms();
    // List<ProgElementPanel> aSuppr = new ArrayList<ProgElementPanel>();
    // Program p;
    // for(ProgElementPanel pep : list) {
    // p = pep.getProgram();
    // if(!programs.contains(p)) {
    // aSuppr.add(pep);
    // }
    // }
    // for(ProgElementPanel pep : aSuppr) {
    // list.remove(pep);
    // }
    // aSuppr = null;
    // for(Program prog : programs) {
    // boolean found = false;
    // int i = 0;
    // while(i < list.size() && !found) {
    // // System.out.println(list.get(i).getProgram() == prog);
    // if(list.get(i).getProgram() == prog)
    // found = true;
    // i++;
    // }
    // if(!found)
    // list.add(new ProgElementPanel(prog));
    // }
    // Collections.sort(list);
    // fireContentsChanged(this, 0, list.size());
    // }
    //    
    // public void refreshTime() {
    // for(ProgElementPanel pep : list) {
    // pep.refreshTime();
    // }
    // }

    // public List<ProgElementPanel> getList() {
    // return list;
    // }

}
