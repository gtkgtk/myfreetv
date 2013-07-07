package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.rom.myfreetv.config.Config;
import org.rom.myfreetv.files.RecordFileManager;
import org.rom.myfreetv.guidetv.Emission;
import org.rom.myfreetv.guidetv.GuideTVManager;
import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.process.ProgramManager;
import org.rom.myfreetv.process.RecordJob;
import org.rom.myfreetv.shutdown.ShutdownManager;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.ChannelManager;
import org.rom.myfreetv.update.Updater;

public class MyFreeTV extends JFrame implements ActionListener, ChangeListener, ListSelectionListener, Observer {

    private final static DateFormat formatter = new SimpleDateFormat("HH:mm");
    public final static String name = "MyFreeTV";
    public final static String version = "2.30 beta 4 - PGU - KAZER Enabled";
    public final static String url = "https://github.com/gtkgtk/myfreetv";
    public final static String mail = "rom1v@yahoo.fr";

    //private static enum Plaf {
    // SYSTEM, JRE, PGS, SKIN
    // }

    public static Border buttonBorder = new LineBorder(Color.GRAY, 1);
//    private static DateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm");

    private static MyFreeTV instance;
    // private static NumberFormat formatter = new DecimalFormat("00");

    //private boolean decoration;
    private Actions actions;
    //private SkinManager skinManager;

    private ChannelPanel channelsPanel;
    private PlayPanel playPanel;
    private JTabbedPane tab;
    private RecordPanel recordPanel;
    private ProgPanel progPanel;
    private JPanel vlcPanel, mftPanel;
    //private AudiencePanel audiencePanel;
    private FilePanel filePanel;
    private GuideTVPanel guideTVPanel;
    private boolean GuideTvVisible;
    private FreePlayerPanel freeplayerPanel;
    private LogoViewer logoViewer;
    private JCheckBox alwaysOnTop;

    private JDialog dialog;
    // private JButton shutdown;

    private JLabel CurProgLabel;
    private JPanel pan;
    private JPanel back;
    private CardLayout layout;
    private boolean fw;
    
    private MyFreeTV(boolean visible) {
        super(name + " " + version);

        int x = Config.getInstance().getHorizontalLocation();
        int y = Config.getInstance().getVerticalLocation();
        setLocation(x, y);
        Image image = ImageManager.getInstance().getImage("ico");
        if (image != null) {
            setIconImage(image);
        }
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        actions = new Actions(this);
        //skinManager = new SkinManager(this);

        // skinManager.setPlaf(Config.getInstance().getPlaf());
        // ((SkinPlaf)Plaf.SKIN).setThemepack(Config.getInstance().getThemepack());

        // this.decoration = decoration;
        // if(Config.getInstance().getDecoration()) {
        //     setDefaultLookAndFeelDecorated(false);
        //     getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.FRAME);
        //     setUndecorated(true);
        // }
       
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                ChannelManager.getInstance().saveFavoris();
                exit();
            }
        });

        // ProgramsManager.getInstance().addObserver(this);
        // JobsManager.getInstance().addObserver(this);

        channelsPanel = new ChannelPanel(this);
        // tab = new JTabbedPane();
        // playPanel = new PlayPanel(this);
        // recordPanel = new RecordPanel(this);
        // progPanel = new ProgPanel(this);
        // audiencePanel = new AudiencePanel(this);
        // logoViewer = new LogoViewer(80,80);
        channelsPanel.getChannelsList().getModel().load(this);

        // channelsPanel.getChannelsList().addListSelectionListener(this);

//        System.out.println("supported="+SystemTray.isSupported());
//        if (SystemTray.isSupported()) {
//            // get the SystemTray instance
//            Image icon = ImageManager.getInstance().getTrayIcon();
//
//            SystemTray tray = SystemTray.getSystemTray();
//
//            PopupMenu popup = new PopupMenu();
//            // create menu item for the default action
//            MenuItem itemExit = new MenuItem("Quitter");
//            itemExit.setActionCommand("exit");
//            itemExit.addActionListener(this);
//
//            MenuItem itemAbout = new MenuItem("À propos");
//            itemAbout.setActionCommand("about");
//            itemAbout.addActionListener(this);
//
//            TrayIcon trayIcon = new TrayIcon(icon, "MyFreeTV", popup);
//            try {
//                tray.add(trayIcon);
//            } catch (AWTException e) {
//                e.printStackTrace();
//            }
//            // trayIcon.addActionListener(this);
//        }

        // if(SysTrayMenu.isAvailable()) {
        // SysTrayMenuIcon icon = new
        // SysTrayMenuIcon(ImageManager.getInstance().getURLIcon());
        // icon.addSysTrayMenuListener(this);
        //      
        // SysTrayMenuItem itemExit = new SysTrayMenuItem("Quitter");
        // itemExit.setActionCommand("exit");
        // itemExit.addSysTrayMenuListener(this);
        //      
        // SysTrayMenuItem itemAbout = new SysTrayMenuItem("à propos");
        // itemAbout.setActionCommand("about");
        // itemAbout.addSysTrayMenuListener(this);
        //      
        // systray = new SysTrayMenu(icon, name + " " + version);
        // systray.addItem(itemExit);
        // systray.addItem(itemAbout);
        // // systray.hideIcon();
        // }

        tab = new JTabbedPane();
        playPanel = new PlayPanel(this);
        recordPanel = new RecordPanel(this);
        progPanel = new ProgPanel(this);
        filePanel = new FilePanel(this);
        // PGU : le site ne fonctionne plus
        //audiencePanel = new AudiencePanel(this);
        if (Config.getInstance().getKazerPath().isEnabled())
        	guideTVPanel = new GuideTVPanel(this);
        freeplayerPanel = new FreePlayerPanel(this);
        logoViewer = new LogoViewer(150, 80);

        JPanel leftPanel = new JPanel(new BorderLayout());
        JLabel logoLabel = new JLabel(ImageManager.getInstance().getImageIcon("logo"));
        logoLabel.setVerticalAlignment(JLabel.TOP);
        JPanel channelsWithLogosPanel = new JPanel(new BorderLayout());
        channelsWithLogosPanel.add(channelsPanel);
        channelsWithLogosPanel.add(logoViewer, BorderLayout.SOUTH);
        leftPanel.add(logoLabel, BorderLayout.WEST);
        leftPanel.add(channelsWithLogosPanel);
        // leftPanel.add(logoViewer,BorderLayout.SOUTH);
        
        CurProgLabel = new JLabel("Programme courant non disponible...");
        CurProgLabel.addMouseListener(new MouseAdapter() {
        	@Override
             public void mouseClicked(MouseEvent e) {
                int selected = channelsPanel.getChannelsList().getSelectedIndex();
                Channel chan=getSelectedChannel();
                //Channel chan = (selected >= 0) ? ChannelManager.getInstance().getChannels().get(selected) : null;
            	int hauteurLabel=CurProgLabel.getHeight();
            	int y = e.getY();
            	if (selected>=0)
            	{
	            	// 1 ligne de  texte a 18 pixel de hauteur
	            	if (y >= 18)
	            	{
	            		// cas : à suivre
	                	Emission nextEmission = GuideTVManager.getInstance().getNext(chan);
	            		new EmissionDialog(instance,nextEmission);
	            	}
	            	else
	            	{
	            		// cas : actuellement
	                	Emission curEmission = GuideTVManager.getInstance().getCurrent(chan);
	            		new EmissionDialog(instance,curEmission);
	            	}
            	}
             };
          });


        //CurProgLabel.setVerticalAlignment(JLabel.TOP);

        JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        downPanel.add(CurProgLabel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new java.awt.Dimension(400, 300));
        rightPanel.add(playPanel, BorderLayout.NORTH);

        JPanel recPanel = new JPanel();
        recPanel.setLayout(new BoxLayout(recPanel, BoxLayout.Y_AXIS));
        recPanel.add(recordPanel);
        recPanel.add(progPanel);

        vlcPanel = new JPanel(new BorderLayout());

        tab.addTab("Enregistrements", ImageManager.getInstance().getImageIcon("record"), recPanel);
        tab.addTab("Fichiers", ImageManager.getInstance().getImageIcon("file_rename"), filePanel);
        // PGU : le site http://audience.free.fr/ ne fonctionne plus
        //tab.addTab("Audience", ImageManager.getInstance().getImageIcon("audience"), audiencePanel);
        if (Config.getInstance().getKazerPath().isEnabled())
        {
	        tab.addTab("Guide TV",
	        ImageManager.getInstance().getImageIcon("guidetv"), guideTVPanel);
	        GuideTvVisible = true;
        }
        else
        	GuideTvVisible = false;
        tab.addTab("Écran", ImageManager.getInstance().getImageIcon("screen"), vlcPanel);
        tab.addTab("FreePlayer", ImageManager.getInstance().getImageIcon("fpico"), freeplayerPanel);
        tab.addChangeListener(this);

        rightPanel.add(tab);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        alwaysOnTop = new JCheckBox();
        alwaysOnTop.setToolTipText("Toujours au-dessus.");
        alwaysOnTop.setActionCommand("always_on_top_mft");
        alwaysOnTop.addActionListener(this);
        // shutdown = new JButton();
        // initShutdown();
        // shutdown.setActionCommand("shutdown");
        // shutdown.addActionListener(this);
        JButton vlc = new JButton(ImageManager.getInstance().getImageIcon("vlc"));
        vlc.setToolTipText("Configurer " + name + ".");
        vlc.setActionCommand("config");
        vlc.addActionListener(this);
        JButton skin = new JButton(ImageManager.getInstance().getImageIcon("laf"));
        skin.setToolTipText("Changer l'apparence de " + name + ".");
        skin.setActionCommand("skin");
        skin.addActionListener(this);
        JButton about = new JButton(ImageManager.getInstance().getImageIcon("point_interrogation"));
        about.setToolTipText("À propos de " + name + ".");
        about.setActionCommand("about");
        about.addActionListener(this);
        southPanel.add(CurProgLabel);
        southPanel.add(alwaysOnTop);
//        southPanel.add(shutdown);
        southPanel.add(vlc);
//        southPanel.add(skin);
        southPanel.add(about);

        boolean onTop = Config.getInstance().getAlwaysOnTop();
        alwaysOnTop.setSelected(onTop);
        setAlwaysOnTop(onTop);

        // JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        // jsp.setLeftComponent(leftPanel);
        // jsp.setRightComponent(rightPanel);
        // jsp.setOneTouchExpandable(true);
        // jsp.setDividerSize(5);
        // jsp.setContinuousLayout(true);

        mftPanel = new JPanel(new BorderLayout());

        mftPanel.add(leftPanel, BorderLayout.WEST);
        mftPanel.add(rightPanel);
        // getContentPane().add(jsp);
        mftPanel.add(southPanel, BorderLayout.SOUTH);

        back = new JPanel(new BorderLayout());
        layout = new CardLayout();
        pan = new JPanel(layout);
        pan.add(mftPanel, "mft");
        pan.add(back, "vlc");
        setContentPane(pan);

        // setContentPane(mftPanel);
        update();

        ProgramManager.getInstance().addObserver(this);
        JobManager.getInstance().addObserver(this);
        RecordFileManager.getInstance().addObserver(this);
        if (Config.getInstance().getKazerPath().isEnabled())
        	GuideTVManager.getInstance().addObserver(this);
        ShutdownManager.getInstance().addObserver(this);

        ProgramManager.getInstance().runThread();

        loadSize();
        // int w = Config.getInstance().getWidth();
        // int h = Config.getInstance().getHeight();
        // if(w == 0 && h == 0)
        // pack();
        // else
        // setSize(w, h);

        // new Thread() {
        //      
        // public void run() {
        // initVLC();
        // }
        // }.start();
        // initVLC();

        if (Config.getInstance().getCheckUpdate()) {
            new Thread() {

                public void run() {
                    String lastVersion = Updater.getInstance().getLastVersion();
                    if (lastVersion != null && !lastVersion.equals(version)) {
                        float last = 0, cur = 0;
                        try {
                            cur = Float.parseFloat(version);
                            last = Float.parseFloat(lastVersion);
                        } catch (NumberFormatException e) {}
                        if (last > cur)
                            JOptionPane.showMessageDialog(MyFreeTV.this, "<html><center>La version " + lastVersion + " de " + name + " est disponible.<br>" + url + "</center></html>", "Mise à jour", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }.start();
        }

        setVisible(visible || SystemTray.isSupported());// !SysTrayMenu.isAvailable());
    }

    public static MyFreeTV getInstance() {
        // if(instance == null)
        // instance = new MyFreeTV(true, false);
        return instance;
    }

    public static void create(boolean visible) {
        instance = new MyFreeTV(visible);
    }

    public void exit() {
        if ((JobManager.getInstance().getPlay() == null && JobManager.getInstance().getFreePlay() == null && JobManager.getInstance().getRecords().isEmpty() && ProgramManager.getInstance().getPrograms().isEmpty())
                || JOptionPane.showConfirmDialog(this, "Toutes les instances de VLC en cours d'exécution seront quittées,\net les programmations planifiées seront inactives.\nVoulez-vous vraiment quitter?", "Confirmation",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // Config.getInstance().saveProperties();
            // ProgramsManager.getInstance().save();
            if (JobManager.getInstance().getPlay() != null) {
                try {
                    JobManager.getInstance().getPlay().stop();
                } catch (Exception e) {}
            }
            if (JobManager.getInstance().getFreePlay() != null) {
                try {
                    JobManager.getInstance().getFreePlay().stop();
                } catch (Exception e) {}
            }
            for (RecordJob rj : JobManager.getInstance().getRecords())
                try {
                    rj.stop();
                } catch (Exception e) {}
            Point p = getLocation();
            Config.getInstance().setHorizontalLocation((int) p.getX());
            Config.getInstance().setVerticalLocation((int) p.getY());
            if (fw)
                saveFullWindowSize();
            else
                saveSize();
            // GuideTVManager.getInstance().closeDB();
            // Dimension d = getSize();
            // Config.getInstance().setWidth((int) d.getWidth());
            // Config.getInstance().setHeight((int) d.getHeight());
            // Config.getInstance().saveProperties();
            // if(vlc != null)
            // vlc.setVLCVisible(false);
            // vlc.stop();
            System.exit(0);
        }
    }

    public void update(Observable o, Object arg) {
        // if(arg instanceof String && "program_hour".equals(arg)) {
        // progPanel.refresh();
        // }
        if("guidetv".equals(arg))
            if (Config.getInstance().getKazerPath().isEnabled())
            	updateGuideTVOnly();
        // else if("shutdown".equals(arg))
        // prepareShutdown();
        // else
        update();
    }

    public Actions getActions() {
        return actions;
    }

    //public SkinManager getSkinManager() {
    //   return skinManager;
    //}

    //public void reloadSkin() {
    //    new Thread() {
//
  //          public void run() {
    //            // try { Thread.sleep(1000); } catch(InterruptedException e) {}
    //            dialog = new SkinChooserDialog(MyFreeTV.this);
     //           dialog.setVisible(true);
    //            dialog = null;
     //       }
    //    }.start();
        // dialog = new SkinChooserDialog(MyFreeTV.this);
        // dialog.setVisible(true);
        // dialog = null;
     //   setVisible(true);
    //}

    public JDialog getDialog() {
        return dialog;
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("config")) {
            new ConfigDialog(this);
        } else if (s.equals("about")) {
            about();
        }
    }

    private void about() {
        JOptionPane.showMessageDialog(this, "<html><center><b>" + name + " " + version + "</b><br>" + url + "</center><br>Pour me contacter : " + mail + ".</html>", "À propos", JOptionPane.OK_OPTION, ImageManager.getInstance()
                .getImageIcon("avatar"));
    }

    // private void initShutdown() {
    // Calendar date = ShutdownManager.getInstance().getDate();
    // if(date == null) {
    // shutdown.setIcon(ImageManager.getInstance().getImageIcon("shutdown_off"));
    // shutdown.setToolTipText("Programmer l'arrêt du système.");
    // } else {
    // shutdown.setIcon(ImageManager.getInstance().getImageIcon("shutdown_on"));
    // shutdown.setToolTipText("<html>Programmer l'arrêt du système.<br><b>Programmé :</b> "
    // + formatter.format(new Date(date.getTimeInMillis())) + "</html>");
    // }
    // }

    // void initVLC() {
    // PlayJob pj = JobManager.getInstance().getPlay();
    // if(pj != null) {
    // try {
    // JobManager.getInstance().stopPlay();
    // } catch(Exception e) {}
    // }
    // if(Config.getInstance().isEmbedded() && vlc == null) {
    // // final ActiveXLoadingDialog ax = isVisible() ? new
    // // ActiveXLoadingDialog(MyFreeTV.this) : null;
    // new ActiveXLoadingDialog();
    // // pan.add(vlcPanel,"vlc");
    // // layout.show(pan,"vlc");
    // }
    // if(pj != null) {
    // if(Config.getInstance().isEmbedded()) {
    // try {
    // Thread.sleep(500);
    // } catch(InterruptedException e) {}
    // }
    // getActions().play(pj.getPlayable());
    // }
    // }
    //  
    // class ActiveXLoadingDialog extends JDialog {
    //    
    // public ActiveXLoadingDialog() {
    // super(MyFreeTV.this, "ActiveX", true);
    // setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    // // addWindowListener(new WindowAdapter() {});
    // setResizable(false);
    //      
    // if(Config.getInstance().getDecoration()) {
    // setDefaultLookAndFeelDecorated(false);
    // getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.PLAIN_DIALOG);
    // setUndecorated(true);
    // }
    //      
    // Point p = MyFreeTV.this.getLocation();
    // Dimension d = MyFreeTV.this.getSize();
    // int x1 = (int) p.getX();
    // int y1 = (int) p.getY();
    // int x2 = x1 + (int) d.getWidth();
    // int y2 = y1 + (int) d.getHeight();
    //      
    // getContentPane().add(new
    // JLabel(ImageManager.getInstance().getImageIcon("activex-loading")));
    //      
    // pack();
    //      
    // int posX = (x1 + x2 - (int) getSize().getWidth()) / 2;
    // int posY = (y1 + y2 - (int) getSize().getHeight()) / 2;
    //      
    // setLocation(posX, posY);
    //      
    // new Thread() {
    //        
    // public void run() {
    // try {
    // vlc = new MyVLC(MyFreeTV.this);
    // } catch(Throwable e) {
    // vlc = null;
    // Config.getInstance().setEmbedded(false);
    // }
    // if(vlc != null)
    // vlcPanel.add(vlc);
    // tab.setEnabledAt(tab.indexOfComponent(vlcPanel), vlc != null);
    // dispose();
    // }
    // }.start();
    //      
    // if(MyFreeTV.this.isVisible())
    // setVisible(true);
    // }
    // }

    // public void showVLC() {
    // if(tab.isEnabledAt(SCR_POS)) {
    // tab.setSelectedIndex(SCR_POS);
    // }
    // }

    // public MyVLC getVLC() {
    // return vlc;
    // }

    // public JTabbedPane getTab() {
    // return tab;
    // }

    public void valueChanged(ListSelectionEvent e) {
        int selected = channelsPanel.getChannelsList().getSelectedIndex();
        Channel chan = (selected >= 0) ? ChannelManager.getInstance().getChannels().get(selected) : null;
        logoViewer.setLogo(chan);
        // à re-implementer + tard :(
        //if(isGuideTVVisible())
        //{
        	guideTVPanel.refresh();

        //}
        updateCurProg();
        initButtons();
    }

    public void initButtons() {
        channelsPanel.initButtons();
        playPanel.initButtons();
        recordPanel.initButtons();
        progPanel.initButtons();
        //audiencePanel.initButtons();
        // à re-implementer + tard :(
        if (Config.getInstance().getKazerPath().isEnabled())
        	guideTVPanel.initButtons();
        freeplayerPanel.initButtons();
        // if(vlc != null)
        // vlc.initButtons();
        // int srcPos = tab.indexOfComponent(vlcPanel);
        // if(tab.getTabCount() > SCR_POS) {
        int index = tab.indexOfComponent(vlcPanel);
        if (index >= 0) {
            if (Config.getInstance().isEmbedded()) {
                tab.setEnabledAt(index, true);
            } else {
                tab.setEnabledAt(index, false);
                if (tab.getSelectedComponent() == vlcPanel)
                    tab.setSelectedIndex(0);
            }
            tab.setEnabledAt(index, Config.getInstance().isEmbedded());

        }
        // }
    }

    private void updateCurProg()
    {
        int selected = channelsPanel.getChannelsList().getSelectedIndex();
        Channel chan = (selected >= 0) ? ChannelManager.getInstance().getChannels().get(selected) : null;
    	Emission curEmission = GuideTVManager.getInstance().getCurrent(chan);
    	Emission nextEmission = GuideTVManager.getInstance().getNext(chan);
        StringBuffer buf = new StringBuffer("<html><b>Actuellement : </b>");
       	if (curEmission!=null)
    	{
    	    buf.append(formatter.format(curEmission.getStart().getTime()));
    	    buf.append(" - ");
    	    buf.append(formatter.format(curEmission.getEnd().getTime()));
    	    buf.append(" : ");
    	    buf.append(curEmission.getTitle());
    	    if(curEmission.getSubtitle() != null)
    	    {
    		        buf.append(" (" + curEmission.getSubtitle() + ")");
    	    }
           	if (nextEmission!=null)
        	{
           		buf.append("<br><b>A suivre     : </b>");
        	    buf.append(formatter.format(nextEmission.getStart().getTime()));
        	    buf.append(" - ");
        	    buf.append(formatter.format(nextEmission.getEnd().getTime()));
        	    buf.append(" : ");
        	    buf.append(nextEmission.getTitle());
        	    if(nextEmission.getSubtitle() != null)
        	    {
        		        buf.append(" (" + nextEmission.getSubtitle() + ")");
        	    }
        	}
    	    
            buf.append("</html>");

    	    CurProgLabel.setText(buf.toString());
    	}
       	else
       		CurProgLabel.setText("Programme courant non disponible...");

    }
    public void update() {
        playPanel.update();
        recordPanel.update();
        progPanel.update();
        filePanel.update();
        if (Config.getInstance().getKazerPath().isEnabled())
        {
        	guideTVPanel.refresh();
        }
        freeplayerPanel.update();
        initButtons();
    }

    private void updateGuideTVOnly() {
    	channelsPanel.getChannelsList().getModel().refresh();
       	guideTVPanel.refresh();
    	initButtons();
    }
    //  
    // private void prepareShutdown() {
    // new ShutdownWaiter(20000);
    // initShutdown();
    // }

    public void stateChanged(ChangeEvent e) {
        int ind = tab.getSelectedIndex();
        //if (ind == tab.indexOfComponent(audiencePanel) && !audiencePanel.isRefreshed())
        //    audiencePanel.refresh();
        //else 
        if (ind == tab.indexOfComponent(filePanel))
            filePanel.initMoAndRefresh();
        // à re-implementer + tard :(
        else if (Config.getInstance().getKazerPath().isEnabled())
        	if(ind == tab.indexOfComponent(guideTVPanel))
        		guideTVPanel.refresh();
    }

    // public void menuItemSelected(SysTrayMenuEvent e) {
    // String s = e.getActionCommand();
    // if(s.equals("exit")) {
    // exit();
    // } else if(s.equals("about")) {
    // about();
    // }
    // }
    //  
    // public void iconLeftClicked(SysTrayMenuEvent e) {
    // if(isVisible()) {
    // setVisible(false);
    // } else {
    // setVisible(true);
    // toFront();
    // // systray.hideIcon();
    // }
    // }

    // public void iconLeftDoubleClicked(SysTrayMenuEvent e) {
    // // if(isVisible())
    // // setVisible(false);
    // // else {
    // // setVisible(true);
    // // }
    // }

    private void saveSize() {
        Dimension d = getSize();
        Config.getInstance().setWidth((int) d.getWidth());
        Config.getInstance().setHeight((int) d.getHeight());
        Config.getInstance().saveProperties();
    }

    private void loadSize() {
        int w = Config.getInstance().getWidth();
        int h = Config.getInstance().getHeight();
        if (w == 0 && h == 0)
            pack();
        else
            setSize(w, h);
    }

    private void saveFullWindowSize() {
        Dimension d = getSize();
        Config.getInstance().setFWWidth((int) d.getWidth());
        Config.getInstance().setFWHeight((int) d.getHeight());
        Config.getInstance().saveProperties();
    }

    private void loadFullWindowSize() {
        int w = Config.getInstance().getFWWidth();
        int h = Config.getInstance().getFWHeight();
        if (w != 0 || h != 0)
            setSize(w, h);
    }

    // public void fullWindow() {
    // Playable p = null;
    // float pos = 0.0f;
    // PlayJob pj = JobManager.getInstance().getPlay();
    // if(pj != null) {
    // p = pj.getPlayable();
    // try {
    // pos = vlc.getControls().getPosition();
    // getActions().stopPlay();
    // // JobManager.getInstance().stopPlay();
    // } catch(Exception e) {
    // e.printStackTrace();
    // }
    // }
    // if(fw) {
    // fw = false;
    // saveFullWindowSize();
    // // if(vlc != null)
    // pan.remove(vlc);
    // layout.show(pan,"mft");
    // vlcPanel.add(vlc);
    // // vlcPanel.add(new JLabel("abc"));
    // // tab.add("abc",vlcPanel);
    // // tab.setComponentAt(tab.indexOfComponent(vlcPanel),vlcPanel);
    // // tab.addTab("écran", ImageManager.getInstance().getImageIcon("screen"),
    // vlcPanel);
    // // pan.remove(vlc);
    // // layout.show(pan,"mft");
    // // vlcPanel.add(vlc);
    // tab.setSelectedIndex(tab.indexOfComponent(vlcPanel));
    // loadSize();
    // // setVisible(false);
    // // setVisible(true);
    // } else {
    // fw = true;
    // saveSize();
    // // tab.remove(vlcPanel);
    // // pan.add(vlcPanel, "vlc");
    // // vlcPanel.remove(vlc);
    // back.add(vlc);
    // layout.show(pan, "vlc");
    // loadFullWindowSize();
    // }
    // validate();
    // // setVisible(false);
    // // setVisible(true);
    // if(p != null) {
    // try {
    // getActions().play(p, pos);
    // // JobManager.getInstance().startPlay(p, pos);
    // } catch(Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }

    JPanel getVLCPanel() {
        return vlcPanel;
    }

    JTabbedPane getTabbedPane() {
        return tab;
    }

    // PGU : le site http://audience.free.fr/ ne fonctionne plus
    //public boolean isAudiencePanelVisible() {
    //    return tab.getSelectedComponent() == audiencePanel;
    //}

    public boolean isScreenPanelVisible() {
        return tab.getSelectedComponent() == vlcPanel;
    }

    public boolean isFilesPanelVisible() {
        return tab.getSelectedComponent() == filePanel;
    }

    public boolean isRecordPanelVisible() {
        return tab.getSelectedComponent() == recordPanel;
    }

    public boolean isGuideTVVisible() {
        return tab.getSelectedComponent() == guideTVPanel;
    }
    
    public void ActivateTvGuide(boolean isEnabled)
    {
    	if (GuideTvVisible && !isEnabled)
    	{
    		// remove the tv guide tab
    		tab.remove(guideTVPanel);
    		GuideTvVisible=false;
    	}
    	else
    	{
    		if (!GuideTvVisible && isEnabled)
    		{
    			if (guideTVPanel== null)
    			{
    				guideTVPanel = new GuideTVPanel(this);
    		        GuideTVManager.getInstance().addObserver(this);
    			}

    			tab.addTab("Guide TV",
    	    	        ImageManager.getInstance().getImageIcon("guidetv"), guideTVPanel);
        		GuideTvVisible=true;   			
    		}
    	}
    	
    }

    Channel getSelectedChannel() {
        return (Channel) channelsPanel.getChannelsList().getSelectedValue();
    }

    // public boolean getDecoration() {
    // return decoration;
    // }

    public static void main(final String[] args) {
        // System.out.println(System.getProperty("os.name"));
        boolean v = true;
        // boolean decoration = false;
        // Plaf skin = Plaf.PGS;
        for (String s : args) {
            // if(s.equals("--system-laf"))
            // skin = Plaf.SYSTEM;
            // else if(s.equals("--jre-laf"))
            // skin = Plaf.JRE;
            // else if(s.equals("--skin-laf"))
            // skin = Plaf.SKIN;
            if (s.equals("--systray-only"))
                v = false;
            // else if(s.equals("--decoration"))
            // decoration = true;
            else
                System.err.println("paramètre ignoré : " + s);
        }
        final boolean visible = v;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MyFreeTV.create(visible);
            }
        });

        // try {
        // /* see www.javootoo.com for more LookAndFeels */
        // if(skin == Plaf.PGS) {
        // UIManager.setLookAndFeel("com.pagosoft.plaf.PgsLookAndFeel");
        // } else if(skin == Plaf.SKIN) {
        // Skin theSkinToUse = SkinLookAndFeel.loadThemePack("themepack.zip");
        // SkinLookAndFeel.setSkin(theSkinToUse);
        // UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
        // } else if(skin == Plaf.SYSTEM)
        // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // } catch(Exception e) {}

    }

}
