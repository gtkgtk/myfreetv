package org.rom.myfreetv.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.rom.myfreetv.process.RecordJob;

class RecordList extends JList {

    private RecordListModel model;

    class TheCellRenderer extends RecordCellPanel implements ListCellRenderer {

        public TheCellRenderer() {
            super();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            RecordJob rj = (RecordJob) value;
            set(rj);
            setBackground(isSelected ? getSelectionBackground() : Color.WHITE);
            return this;
        }
    }

    public RecordList() {
        super();
        model = new RecordListModel();
        setModel(model);
        setFixedCellHeight(40);
        setValueIsAdjusting(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellRenderer(new TheCellRenderer());
    }

    public RecordListModel getModel() {
        return model;
    }

}
