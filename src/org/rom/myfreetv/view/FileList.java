package org.rom.myfreetv.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.rom.myfreetv.files.RecordFile;

class FileList extends JList {

    private final static Color[] colors = new Color[] { new Color(0xfb, 0xf2, 0xd5), Color.WHITE };
    private FileListModel model;

    class TheCellRenderer extends FileCellPanel implements ListCellRenderer {

        public TheCellRenderer() {
            super();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            RecordFile rf = (RecordFile) value;
            set(rf);
            Color background = colors[index % colors.length];
            setBackground(isSelected ? getSelectionBackground() : background);
            return this;
        }
    }

    public FileList() {
        super();
        model = new FileListModel();
        setModel(model);
        setFixedCellHeight(40);
        setValueIsAdjusting(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellRenderer(new TheCellRenderer());
    }

    public FileListModel getModel() {
        return model;
    }

}
