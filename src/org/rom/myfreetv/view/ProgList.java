package org.rom.myfreetv.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.rom.myfreetv.process.Program;

class ProgList extends JList {

    private ProgListModel model;

    class TheCellRenderer extends ProgCellPanel implements ListCellRenderer {

        public TheCellRenderer() {
            super();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Program p = (Program) value;
            set(p);
            setBackground(isSelected ? getSelectionBackground() : Color.WHITE);
            return this;
        }
    }

    public ProgList() {
        super();
        model = new ProgListModel();
        setModel(model);
        setFixedCellHeight(48);
        setValueIsAdjusting(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellRenderer(new TheCellRenderer());
    }

    public ProgListModel getModel() {
        return model;
    }

}
