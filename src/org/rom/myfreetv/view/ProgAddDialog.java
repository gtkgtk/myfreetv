package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXDatePicker;
import org.rom.myfreetv.config.Config;
import org.rom.myfreetv.files.FileUtils;
import org.rom.myfreetv.process.HebdoProgramRules;
import org.rom.myfreetv.process.OnceProgramRules;
import org.rom.myfreetv.process.Program;
import org.rom.myfreetv.process.ProgramAddException;
import org.rom.myfreetv.process.ProgramManager;
import org.rom.myfreetv.process.ProgramRules;
import org.rom.myfreetv.process.RecordJob;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.ChannelManager;

class ProgAddDialog extends JDialog implements ActionListener {

    private static DateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy");

    private MyFreeTV owner;
    private ChannelComboBox channels;
    private JRadioButton once, hebdo;
    private JXDatePicker startDate;
    private DateSpinner /* startDate stopDate, */startTime, stopTime;
    private JCheckBox lundi, mardi, mercredi, jeudi, vendredi, samedi,
            dimanche;
    private JButton fileBut;
    private JCheckBox auto;
    private JTextField filePath;
    // private File file;
    private RecordJob job;
    private Program program;
    private LogoViewer logo;

    private ProgAddDialog(MyFreeTV owner) {
        super(owner, "Programmation", true);
        this.owner = owner;
        setResizable(false);

//        SkinManager.decore(this,Config.getInstance().getDecoration());

        Point p = owner.getLocation();
        Dimension d = owner.getSize();
        int x1 = (int) p.getX();
        int y1 = (int) p.getY();
        int x2 = x1 + (int) d.getWidth();
        int y2 = y1 + (int) d.getHeight();

        logo = new LogoViewer(64, 32);

        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        JPanel channelPanel = new JPanel();
        channelPanel.setBorder(new TitledBorder(null, "Chaîne"));
        channels = new ChannelComboBox();
        channels.setActionCommand("channel");
        channels.addActionListener(this);
        channels.setEnabled(job == null);
        channelPanel.add(channels);
        channelPanel.add(logo);

        JPanel horairesPanel = new JPanel();
        horairesPanel.setLayout(new BoxLayout(horairesPanel, BoxLayout.Y_AXIS));
        horairesPanel.setBorder(new TitledBorder(null, "Horaires"));

        ButtonGroup bg = new ButtonGroup();
        JPanel freqPanel = new JPanel();
        once = new JRadioButton("Unique");
        hebdo = new JRadioButton("Hebdomadaire");
        once.setActionCommand("fréquence");
        hebdo.setActionCommand("fréquence");
        once.addActionListener(this);
        hebdo.addActionListener(this);
        freqPanel.add(once);
        freqPanel.add(hebdo);
        bg.add(once);
        bg.add(hebdo);

        JPanel tmp1 = new JPanel();
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        // datePanel.setBorder(new TitledBorder(null,"Unique"));
        // Date current = new Date();
        // Date min = new Date(new
        // GregorianCalendar(2006,0,1).getTimeInMillis());
        // Date max = new Date(new
        // GregorianCalendar(2099,11,31,23,59,59).getTimeInMillis());
        startDate = new JXDatePicker();
        startDate.setFormats(new DateFormat[] { formatter });
        // dd-MM-yyyy")});
        JTextField jtf = startDate.getEditor();
        jtf.setColumns(15);
        jtf.setHorizontalAlignment(JTextField.CENTER);
        // startDate = new DateSpinner(DateSpinner.DATE_ONLY);
        // stopDate = new DateSpinner(DateSpinner.DATE_ONLY);
        // JPanel dePanel = new JPanel();
        // dePanel.add(new JLabel("De"));
        // datePanel.add(dePanel);
        datePanel.add(startDate);
        // JPanel aPanel = new JPanel();
        // aPanel.add(new JLabel("à"));
        // datePanel.add(aPanel);
        // datePanel.add(stopDate);
        tmp1.add(datePanel);

        JPanel tmp2 = new JPanel();
        JPanel hebPanel = new JPanel();
        hebPanel.setLayout(new BoxLayout(hebPanel, BoxLayout.X_AXIS));
        // hebPanel.setBorder(new TitledBorder(null,"Hebdomadaire"));
        tmp2.add(hebPanel);

        JPanel tmpDate = new JPanel();
        tmpDate.setLayout(new BoxLayout(tmpDate, BoxLayout.Y_AXIS));
        tmpDate.add(tmp1);
        tmpDate.add(tmp2);

        lundi = new JCheckBox("Lun");
        mardi = new JCheckBox("Mar");
        mercredi = new JCheckBox("Mer");
        jeudi = new JCheckBox("Jeu");
        vendredi = new JCheckBox("Ven");
        samedi = new JCheckBox("Sam");
        dimanche = new JCheckBox("Dim");
        hebPanel.add(lundi);
        hebPanel.add(mardi);
        hebPanel.add(mercredi);
        hebPanel.add(jeudi);
        hebPanel.add(vendredi);
        hebPanel.add(samedi);
        hebPanel.add(dimanche);

        JPanel tmpTime = new JPanel();
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
        startTime = new DateSpinner(DateSpinner.TIME_ONLY);
        stopTime = new DateSpinner(DateSpinner.TIME_ONLY);
        JPanel dePanel = new JPanel();
        dePanel.add(new JLabel("De"));
        JPanel aPanel = new JPanel();
        aPanel.add(new JLabel("à"));
        timePanel.add(dePanel);
        timePanel.add(startTime);
        timePanel.add(aPanel);
        timePanel.add(stopTime);
        tmpTime.add(timePanel);
        // hebPanel.add(timePanel);

        horairesPanel.add(freqPanel);
        horairesPanel.add(tmpDate);
        horairesPanel.add(tmpTime);

        JPanel destPanel = new JPanel();
        destPanel.setLayout(new BoxLayout(destPanel, BoxLayout.Y_AXIS));
        destPanel.setBorder(new TitledBorder(null, "Destination"));

        JPanel autoPanel = new JPanel();
        auto = new JCheckBox("Automatique");
        // boolean hasAutoPath = job == null &&
        // Config.getInstance().getAutoPath().isEnabled();
        // auto.setSelected(hasAutoPath);
        // auto.setEnabled(hasAutoPath);
        auto.setActionCommand("auto");
        auto.addActionListener(this);
        autoPanel.add(auto);

        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
        filePath = new JTextField(20);
        filePath.setMaximumSize(new Dimension(Integer.MAX_VALUE, filePath.getMinimumSize().height));
        // filePath.setText(job == null ?
        // Config.getInstance().getAutoPath().getUrl() : job.getUrlOutput());
        filePath.setDisabledTextColor(Color.GRAY);
        fileBut = new JButton("...");
        fileBut.setActionCommand("...");
        fileBut.addActionListener(this);
        filePanel.add(new JLabel("Fichier : "));
        filePanel.add(filePath);
        filePanel.add(fileBut);

        destPanel.add(autoPanel);
        destPanel.add(filePanel);

        pan.add(channelPanel);
        pan.add(horairesPanel);
        pan.add(destPanel);

        JPanel bottom = new JPanel();

        JButton ok = new JButton("OK");
        ok.setActionCommand("ok");
        ok.addActionListener(this);

        JButton cancel = new JButton("Annuler");
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);

        bottom.add(ok);
        bottom.add(cancel);

        getContentPane().add(pan);
        getContentPane().add(bottom, BorderLayout.SOUTH);

        pack();

        int posX = (x1 + x2 - (int) getSize().getWidth()) / 2;
        int posY = (y1 + y2 - (int) getSize().getHeight()) / 2;

        setLocation(posX, posY);

        initLogo();
        initButtons();

        // setVisible(true);
    }

    public ProgAddDialog(MyFreeTV owner, Channel channel) {
        this(owner);
        init(channel);
        setVisible(true);
    }

    public ProgAddDialog(MyFreeTV owner, RecordJob job) {
        this(owner);
        init(job);
        setVisible(true);
    }

    public ProgAddDialog(MyFreeTV owner, Program old) {
        this(owner);
        init(old);
        setVisible(true);
    }

    private void init(Channel channel) {
        channels.setSelectedIndex(ChannelManager.getInstance().getChannels().indexOf(channel));

        once.setSelected(true);

        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        lundi.setSelected(day == Calendar.MONDAY);
        mardi.setSelected(day == Calendar.TUESDAY);
        mercredi.setSelected(day == Calendar.WEDNESDAY);
        jeudi.setSelected(day == Calendar.THURSDAY);
        vendredi.setSelected(day == Calendar.FRIDAY);
        samedi.setSelected(day == Calendar.SATURDAY);
        dimanche.setSelected(day == Calendar.SUNDAY);

        boolean hasAutoPath = job == null && Config.getInstance().getAutoPath().isEnabled();
        auto.setSelected(hasAutoPath);
        auto.setEnabled(hasAutoPath);

        filePath.setText(Config.getInstance().getAutoPath().getUrl());

        initButtons();
    }

    private void init(RecordJob job) {
        this.job = job;

        channels.setSelectedIndex(ChannelManager.getInstance().getChannels().indexOf(job.getRecordable()));

        once.setSelected(true);
        hebdo.setEnabled(false);

        startDate.setEnabled(false);
        startTime.setEnabled(false);
        auto.setEnabled(false);
        filePath.setEnabled(false);

        auto.setSelected(false);
        filePath.setText(job.getUrlOutput());

        initButtons();
    }

    private void init(Program prog) {
        program = prog;
        // this.job = prog.getJob();

        channels.setSelectedIndex(ChannelManager.getInstance().getChannels().indexOf(prog.getRecordable()));
        ProgramRules rules = prog.getProgramRules();
        Date theStartTime;
        Date theStopTime;
        if(rules instanceof OnceProgramRules) {
            OnceProgramRules onceRules = (OnceProgramRules) rules;
            once.setSelected(true);
            Calendar start = onceRules.getStart();
            Calendar stop = onceRules.getStop();
            Date theStartDate = new GregorianCalendar(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH)).getTime();
            theStartTime = start.getTime();
            theStopTime = stop.getTime();
            // Date date = new Date(cal.getTimeInMillis());
            startDate.setDate(theStartDate);
            filePath.setText(onceRules.getFilename());
        } else {
            HebdoProgramRules hebdoRules = (HebdoProgramRules) rules;
            hebdo.setSelected(true);
            Calendar cal = Calendar.getInstance();
            Calendar start = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            start.add(Calendar.MINUTE, hebdoRules.getStartMin());
            Calendar stop = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            stop.add(Calendar.MINUTE, hebdoRules.getStopMin());
            if(hebdoRules.getStopMin() < hebdoRules.getStartMin())
                stop.add(Calendar.DAY_OF_MONTH, 1);
            lundi.setSelected(hebdoRules.isActiveOnDay(Calendar.MONDAY));
            mardi.setSelected(hebdoRules.isActiveOnDay(Calendar.TUESDAY));
            mercredi.setSelected(hebdoRules.isActiveOnDay(Calendar.WEDNESDAY));
            jeudi.setSelected(hebdoRules.isActiveOnDay(Calendar.THURSDAY));
            vendredi.setSelected(hebdoRules.isActiveOnDay(Calendar.FRIDAY));
            samedi.setSelected(hebdoRules.isActiveOnDay(Calendar.SATURDAY));
            dimanche.setSelected(hebdoRules.isActiveOnDay(Calendar.SUNDAY));
            theStartTime = start.getTime();
            theStopTime = stop.getTime();
            filePath.setText(hebdoRules.getPath());
        }
        startTime.setValue(theStartTime);
        stopTime.setValue(theStopTime);
        auto.setSelected(false);

        initButtons();
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        Channel channel = job == null ? ChannelManager.getInstance().getChannels().get(channels.getSelectedIndex()) : job.getStream().getChannel();
        if(s.equals("cancel")) {
            dispose();
        } else if(s.equals("...")) {
            FileUtils.Type type = once.isSelected() ? FileUtils.Type.FILE : FileUtils.Type.DIRECTORY;
            String filename = FileUtils.chooseDestination(this, filePath.getText(), type, true, channel);
            if(filename != null) {
                filePath.setText(filename);
            }
        } else if(s.equals("auto") || s.equals("fréquence")) {
            initButtons();
        } else if(s.equals("channel")) {
            initLogo();
        } else if(s.equals("ok")) {
            if(verifJob()) {
                File file = new File(filePath.getText());
                boolean cont = true;
                Program prog = null;
                channel = job == null ? ChannelManager.getInstance().getChannels().get(channels.getSelectedIndex()) : job.getStream().getChannel();
                if(once.isSelected()) {
                    Calendar start;
                    if(job == null) {
                        start = Calendar.getInstance();
                        Calendar std = new GregorianCalendar();
                        std.setTimeInMillis(startDate.getDate().getTime());
                        Calendar stt = startTime.getCalendar();
                        start = new GregorianCalendar(std.get(Calendar.YEAR), std.get(Calendar.MONTH), std.get(Calendar.DAY_OF_MONTH), stt.get(Calendar.HOUR_OF_DAY), stt.get(Calendar.MINUTE));
                        if(auto.isSelected())
                            file = new File(Config.getInstance().getAutoPath().getUrl() + File.separatorChar + FileUtils.generateAutoFilename(start, channel));
                        // file = owner.generateFile(start,channel,false);
                    } else {
                        start = Calendar.getInstance();
                        file = new File(job.getUrlOutput());
                    }
                    Calendar stop = new GregorianCalendar(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH), stopTime.getCalendar().get(Calendar.HOUR_OF_DAY), stopTime.getCalendar().get(Calendar.MINUTE));
                    if(stop.compareTo(start) <= 0)
                        stop.add(Calendar.DAY_OF_MONTH, 1);
                    if(file.isDirectory()) {
                        cont = false;
                        JOptionPane.showMessageDialog(this, "Un fichier doit être spécifié pour l'enregistrement, non un répertoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else {
                        prog = new Program(channel, new OnceProgramRules(start, stop, file.getAbsolutePath()));
                        prog.setJob(job);
                    }
                } else if(hebdo.isSelected()) {
                    int days = 0;
                    if(lundi.isSelected())
                        days += HebdoProgramRules.LUNDI;
                    if(mardi.isSelected())
                        days += HebdoProgramRules.MARDI;
                    if(mercredi.isSelected())
                        days += HebdoProgramRules.MERCREDI;
                    if(jeudi.isSelected())
                        days += HebdoProgramRules.JEUDI;
                    if(vendredi.isSelected())
                        days += HebdoProgramRules.VENDREDI;
                    if(samedi.isSelected())
                        days += HebdoProgramRules.SAMEDI;
                    if(dimanche.isSelected())
                        days += HebdoProgramRules.DIMANCHE;
                    Calendar startCal = startTime.getCalendar();
                    Calendar stopCal = stopTime.getCalendar();
                    if(auto.isSelected()) {
                        file = new File(Config.getInstance().getAutoPath().getUrl());
                    } else {
                        file = new File(filePath.getText());
                        if(!file.isDirectory()) {
                            file = file.getParentFile();
                        }
                    }

                    prog = new Program(channel, new HebdoProgramRules(channel, days, startCal.get(Calendar.HOUR_OF_DAY), startCal.get(Calendar.MINUTE), stopCal.get(Calendar.HOUR_OF_DAY), stopCal.get(Calendar.MINUTE), file.getAbsolutePath()));
                    if(!prog.init()) {
                        cont = false;
                        JOptionPane.showMessageDialog(this, "Au moins un enregistrement doit �tre planifi�.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else
                    cont = false;
                if(cont) {
                    if(program == null) {
                        try {
                            ProgramManager.getInstance().add(prog);
                        } catch(ProgramAddException exc) {
                            cont = false;
                            JOptionPane.showMessageDialog(this, exc.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        ProgramManager.getInstance().modif(program, prog);
                    }
                }
                if(cont)
                    dispose();
                // }
            } else
                dispose();
        }
    }

    // private boolean verifFutur(Calendar start, Calendar stop) {
    // Calendar cal = new GregorianCalendar();
    // return (start.compareTo(cal) >= 0 && job == null) || stop.compareTo(cal)
    // >= 0;
    // }

    private boolean verifJob() {
        boolean isOk;
        if(job != null)
            isOk = job.isRunning();
        else
            isOk = true;
        return isOk;
    }

    private void initButtons() {
        startDate.setEnabled(job == null && once.isSelected());
        // stopDate.setEnabled(once.isSelected());
        lundi.setEnabled(hebdo.isSelected());
        mardi.setEnabled(hebdo.isSelected());
        mercredi.setEnabled(hebdo.isSelected());
        jeudi.setEnabled(hebdo.isSelected());
        vendredi.setEnabled(hebdo.isSelected());
        samedi.setEnabled(hebdo.isSelected());
        dimanche.setEnabled(hebdo.isSelected());
        startTime.setEnabled(job == null);
        // stopTime.setEnabled(hebdo.isSelected());
        fileBut.setEnabled(job == null && !auto.isSelected());
        filePath.setEnabled(job == null && !auto.isSelected());
        // if(file != null) {
        // if(once.isSelected())
        // filePath.setText(file.getAbsolutePath());
        // else
        // filePath.setText(file.isDirectory() ? file.getAbsolutePath() :
        // file.getParent());
        // }
    }

    private void initLogo() {
        int selected = channels.getSelectedIndex();
        Channel chan = (selected >= 0) ? ChannelManager.getInstance().getChannels().get(selected) : null;
        logo.setLogo(chan);
    }

}
