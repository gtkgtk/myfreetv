package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXDatePicker;
import org.rom.myfreetv.guidetv.Emission;
import org.rom.myfreetv.guidetv.GuideTVManager;
import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.process.PlayJob;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.Playable;
import org.rom.myfreetv.view.EmissionList;

class GuideTVPanel extends JPanel implements ActionListener, ListSelectionListener {

    private static DateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy");

    private MyFreeTV owner;
    private JXDatePicker date;
    private EmissionList emissions;
    private EmissionDetailsPanel details;
    private JButton play, rec, prog;
    private JScrollPane scroll2;
    private JSplitPane jsp;

    private boolean refreshed;

    // private EmissionDetailsDialog dialog;

    public GuideTVPanel(final MyFreeTV owner) {
        super(new BorderLayout());
        this.owner = owner;
        // tooltip = new EmissionDetailsDialog();
        date = new JXDatePicker();
        date.setFormats(new DateFormat[] { formatter });
        emissions = new EmissionList();
        emissions.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    int selectedIndex = emissions.locationToIndex(e.getPoint());
                    if(selectedIndex >= 0) {
                    	int ChannelIndex = owner.channelsPanel.getCurrentChannelIndex();
                        Emission emission = (Emission) emissions.getModel().getElementAt(selectedIndex);
                        owner.getActions().prog(emission,ChannelIndex);
                    }
                }
            }
        });
        details = new EmissionDetailsPanel();

        // emissions.addMouseListener(new MouseAdapter() {
        //
        // public void mouseClicked(MouseEvent e) {
        // if(e.getClickCount() == 2) {
        // Point pt = e.getPoint();
        // int index = emissions.locationToIndex(pt);
        // if(index >= 0 && index < emissions.getModel().getSize()) {
        // new
        // EmissionDetailsDialog(owner,(Emission)emissions.getModel().getElementAt(index));
        // }
        // }
        // }
        // });

        // emissions.addMouseMotionListener(new MouseMotionAdapter() {
        // public void mouseMoved(MouseEvent e)
        // {
        // Point pt = e.getPoint();
        // //somehow in your implementation, get the OMGraphicList
        // int index = emissions.locationToIndex(pt);
        // if(index >= 0 && index < emissions.getModel().getSize()) {
        // tooltip.showAt(GuideTVPanel.this,(Emission)emissions.getSelectedValue(),pt.x,pt.y);
        // }
        // else
        // tooltip.setVisible(false);
        // }
        // });

        JTextField jtf = date.getEditor();
        jtf.setColumns(15);
        jtf.setHorizontalAlignment(JTextField.CENTER);

        JPanel top = new JPanel();
        top.add(date);

        JPanel center = new JPanel(new BorderLayout());

        JPanel emissionsPanel = new JPanel(new BorderLayout());

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        prog = new JButton(ImageManager.getInstance().getImageIcon("prog"));
        prog.setBorder(MyFreeTV.buttonBorder);
        prog.setToolTipText("Programmer l'émission sélectionnée.");
        prog.setActionCommand("prog");
        prog.addActionListener(this);
        buttonsPanel.add(prog);
        // emissions.setPreferredSize(new Dimension(350, 250));
        JScrollPane scroll = new JScrollPane(emissions);
        // scroll.setPreferredSize(new Dimension(350, 250));
        // scroll.setMinimumSize(new Dimension(150, 200));
        emissionsPanel.add(scroll);
        emissionsPanel.add(buttonsPanel, BorderLayout.SOUTH);
        // emissionsPanel.setPreferredSize(new Dimension(350, 250));

        // details.setPreferredSize(new Dimension(350,150));
        scroll2 = new JScrollPane(details);
        scroll2.setPreferredSize(new Dimension(350, 180));
        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll2.getVerticalScrollBar().setUnitIncrement(16);
        scroll2.getViewport().setViewPosition(new Point(1,1));

        // scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // scroll2.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
        // viewport = scroll2.getViewport();
        // scroll2.setViewport(viewport);

        // jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true);
        // jsp.setTopComponent(emissionsPanel);
        // jsp.setBottomComponent(scroll2);

        center.add(emissionsPanel);
        center.add(scroll2, BorderLayout.SOUTH);
        // center.add(jsp);
        // center.add(details);

        date.addActionListener(this);
        emissions.addListSelectionListener(this);

        add(top, BorderLayout.NORTH);
        add(center);

        new Thread() {

            public void run() {
                while(true) {
                    refreshed = false;
                    checkCacheDate();
                    try {
                        Thread.sleep(3600000);
                    } catch(InterruptedException e) {}
                }
            }
        }.start();
    }

    public void checkCacheDate() {
        if(!refreshed && owner.isGuideTVVisible()) {
            GuideTVManager.getInstance().initCache();
            refreshed = true;
        }
    }

    public void refresh() {
        checkCacheDate();
        Channel channel = owner.getSelectedChannel();
        if(channel == null) {
            emissions.getModel().setEmissions(null);
        } else {
            Calendar day = Calendar.getInstance();
            Date ess=date.getDate() ;
            if (ess!=null)
            	day.setTimeInMillis(ess.getTime());
            emissions.getModel().setEmissions(GuideTVManager.getInstance().getEmissions(channel, day));
        }
        refreshDetails();
    }

    public void refreshDetails() {
        Emission emission = emissions.getSelectedIndex() < emissions.getModel().getSize() ? (Emission) emissions.getSelectedValue() : null;
        details.set(emission);
        // scroll2.getViewport().setViewPosition(scroll2.getViewport().toViewCoordinates(new
        // Point(1, 1)));
        // scroll2 = new JScrollPane(details);
        //jsp.setBottomComponent(scroll2);
        initButtons();
        // scroll2.getViewport().setViewPosition(new Point(0,0));
    }

    public void valueChanged(ListSelectionEvent e) {
        refreshDetails();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == date)
            refresh();
        else if(e.getActionCommand() == "prog") {
        	int selectedIndex = emissions.getSelectedIndex();
            if(selectedIndex >= 0) {
            	int ChannelIndex = owner.channelsPanel.getCurrentChannelIndex();
                Emission emission = (Emission) emissions.getModel().getElementAt(selectedIndex);
                owner.getActions().prog(emission, ChannelIndex);
            }
        }
    }

 //EmissionList getEmissionsList() {
     //return emissions;
     //}

    public void initButtons() {
        Object selectedValue = emissions.getSelectedIndex() < emissions.getModel().getSize() ? emissions.getSelectedValue() : null;
        if(selectedValue != null) {
            Emission emission = (Emission) selectedValue;
            prog.setEnabled(emission.getEnd().compareTo(Calendar.getInstance()) > 0);
        } else {
            prog.setEnabled(false);
        }
    }
}
