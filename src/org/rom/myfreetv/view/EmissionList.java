package org.rom.myfreetv.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.rom.myfreetv.guidetv.Emission;

class EmissionList extends JList {

    private final static Color[] colors = new Color[] { new Color(0xfb, 0xf2, 0xd5), Color.WHITE };

    private EmissionListModel model;

    class TheCellRenderer extends EmissionCellPanel implements ListCellRenderer {

        public TheCellRenderer() {
            super();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Emission emission = (Emission) value;
            set(emission);
            // setBackground(isSelected ? getSelectionBackground() :
            // Color.WHITE);
            Color background = colors[index % colors.length];
            setBackground(isSelected ? getSelectionBackground() : background);
            return this;
        }
    }

    public EmissionList() {
        super();
        model = new EmissionListModel();
        setModel(model);
        setFixedCellHeight(18);
        setValueIsAdjusting(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellRenderer(new TheCellRenderer());
    }

    public EmissionListModel getModel() {
        return model;
    }

}
