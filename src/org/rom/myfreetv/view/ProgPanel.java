package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.rom.myfreetv.process.Program;
import org.rom.myfreetv.process.ProgramManager;

class ProgPanel extends JPanel implements ActionListener, Runnable, ListSelectionListener {

    private MyFreeTV owner;
    private JButton modif, del;
    private ProgList progList;
    private Thread runner;

    public ProgPanel(MyFreeTV owner) {
        super();
        this.owner = owner;
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(null, "Programmations"));
        progList = new ProgList();
        progList.addListSelectionListener(this);
        JScrollPane scroll = new JScrollPane(progList);
        scroll.setPreferredSize(new Dimension(350, 80));
        add(scroll);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        modif = new JButton(ImageManager.getInstance().getImageIcon("prog"));
//        modif.setBorder(MyFreeTV.buttonBorder);
        modif.setToolTipText("Modifier la programmation sélectionnée.");
        modif.setActionCommand("modif");
        modif.addActionListener(this);

        del = new JButton(ImageManager.getInstance().getImageIcon("prog_suppr"));
//        del.setBorder(MyFreeTV.buttonBorder);
        del.setToolTipText("Supprimer la programmation sélectionnée.");
        del.setActionCommand("del");
        del.addActionListener(this);

        bottomPanel.add(modif);
        bottomPanel.add(del);

        add(bottomPanel, BorderLayout.SOUTH);

        if(runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void update() {
        progList.getModel().refresh();
    }

    public void valueChanged(ListSelectionEvent e) {
        initButtons();
    }

    //    
    // public void refreshTime() {
    // progList.getModel().refreshTime();
    // }

    public void initButtons() {
        int selectedIndex = progList.getSelectedIndex();
        boolean validRange = selectedIndex >= 0 && selectedIndex < progList.getModel().getSize();
        boolean noJob = true;
        del.setEnabled(validRange);
        if(validRange) {
            Program p = (Program) progList.getModel().getElementAt(selectedIndex);
            noJob = p.getJob() == null;
        }
        modif.setEnabled(validRange && noJob);
    }

//    public ProgList getProgList() {
//        return progList;
//    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        Object o = null;
        if(progList.getSelectedIndex() < progList.getModel().getSize())
            o = progList.getSelectedValue();
        if(s.equals("del")) {
            if(o != null) {
                Program prog = (Program) o;
                ProgramManager.getInstance().remove(prog);
            }
        } else if(s.equals("modif")) {
            if(o != null) {
                Program prog = (Program) o;
                new ProgAddDialog(owner, prog);
            }
        }
    }

    public void run() {
        while(true) {
            progList.getModel().refresh();
            // List<ProgElementPanel> peps = progList.getModel().getList();
            // int i = 0;
            // synchronized(this) {
            // while(i < peps.size()) {
            // ProgElementPanel pep = peps.get(i);
            // pep.refreshState();
            // i++;
            // }
            // }
            // repaint();
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {}
        }
    }

}
