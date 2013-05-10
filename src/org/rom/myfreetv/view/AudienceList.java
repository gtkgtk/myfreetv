package org.rom.myfreetv.view;

import java.awt.Color;
import java.awt.Component;
// import java.util.ArrayList;
// import java.util.List;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.rom.myfreetv.audience.XMLChannel;

class AudienceList extends JList {

    private final static Color[] colors = new Color[] { new Color(0xfb, 0xf2, 0xd5), Color.WHITE };
    private AudienceListModel model;

    // private List<TheCellRenderer> renderers = new
    // ArrayList<TheCellRenderer>();

    class TheCellRenderer extends AudienceCellPanel implements ListCellRenderer {

        public TheCellRenderer() {
            super();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            XMLChannel ch = (XMLChannel) value;
            set(ch);
            Color background = colors[index % colors.length];
            setBackground(isSelected ? getSelectionBackground() : background);
            return this;
        }
    }

    public AudienceList() {
        super();
        model = new AudienceListModel();
        setModel(model);
        setFixedCellHeight(40);
        setValueIsAdjusting(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellRenderer(new TheCellRenderer());
    }

    public AudienceListModel getModel() {
        return model;
    }

}
