package org.rom.myfreetv.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.rom.myfreetv.view.MyFreeTV;

public class Config {

    private final static File PREF_DIR = new File(System.getProperty("user.home") + "/.myfreetv2");
    static {
        if(!PREF_DIR.exists()) {
            PREF_DIR.mkdirs();
        }
    }

    private final static String config = new File(PREF_DIR, "config.xml").getAbsolutePath();
    public final static File FAVORIS_FILE = new File(PREF_DIR, "favoris.m3u");
    public final static String PROGRAMMATION_FILENAME = new File(PREF_DIR, "prog.dat").getAbsolutePath();

    private final static String VLC_PATH = "vlc_path";
    private final static String CURRENT_PATH = "current_path";
    private final static String EMBEDDED = "embedded";
    private final static String DEINTERLACE_MODE = "deinterlace_mode";
    private final static String MUX_MODE = "mux_mode";
    private final static String AUTO_PATH_ENABLED = "auto_path_enabled";
    private final static String AUTO_PATH = "auto_path";
    private final static String CHECK_UPDATE = "check_update";
    private final static String HORIZONTAL_LOCATION = "horizontal_location";
    private final static String VERTICAL_LOCATION = "vertical_location";
    private final static String WIDTH = "width";
    private final static String HEIGHT = "height";
    private final static String VOLUME = "volume";
    private final static String ALWAYS_ON_TOP = "always_on_top";
    private final static String FW_WIDTH = "fw_width";
    private final static String FW_HEIGHT = "fw_height";
    private final static String DECORATION = "decoration";
//    private final static String PLAF = "plaf";
//    private final static String THEMEPACK = "themepack";

    private static Config instance;
    private final static String osName = System.getProperty("os.name").toLowerCase();

    private String vlcPath;
    private String currentPath;
    private boolean embedded = true;
    private DeinterlaceMode deinterlace;
    private MuxMode mux;
    private AutoPath autoPath;
    private boolean checkUpdate = true;
    private int horizontalLocation = 150;
    private int verticalLocation = 75;
    private int width = 0;
    private int height = 0;
    private int volume = 50;
    private boolean alwaysOnTop = false;
    private int fw_width = 0;
    private int fw_height = 0;
    private boolean decoration = false;

    private Config() {
        // properties = new Properties();
        loadProperties();
    }

    public static Config getInstance() {
        if(instance == null)
            instance = new Config();
        return instance;
    }

    public boolean isWindowsOS() {
        return osName.contains("windows");
    }

    public boolean isMacOS() {
        return osName.contains("mac");
    }

    public String getVlcPath() {
        return vlcPath;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public DeinterlaceMode getDeinterlaceMode() {
        return deinterlace;
    }

    public MuxMode getMuxMode() {
        return mux;
    }

    public AutoPath getAutoPath() {
        return autoPath;
    }

    public boolean getCheckUpdate() {
        return checkUpdate;
    }

    public int getHorizontalLocation() {
        return horizontalLocation;
    }

    public int getVerticalLocation() {
        return verticalLocation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVolume() {
        return volume;
    }

    public boolean getAlwaysOnTop() {
        return alwaysOnTop;
    }

    public int getFWWidth() {
        return fw_width;
    }

    public int getFWHeight() {
        return fw_height;
    }

    public boolean getDecoration() {
        return decoration;
    }

    public void setVlcPath(String vlcPath) {
        this.vlcPath = vlcPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    public void setDeinterlaceMode(DeinterlaceMode mode) {
        deinterlace = mode;
    }

    public void setMuxMode(MuxMode mode) {
        mux = mode;
    }

    public void setCheckUpdate(boolean checkUpdate) {
        this.checkUpdate = checkUpdate;
    }

    // public void setAutoPath(AutoPath autoPath) { this.autoPath = autoPath; }
    public void setHorizontalLocation(int x) {
        horizontalLocation = x;
    }

    public void setVerticalLocation(int y) {
        verticalLocation = y;
    }

    public void setWidth(int x) {
        width = x;
    }

    public void setHeight(int x) {
        height = x;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
    }

    public void setFWWidth(int fw_width) {
        this.fw_width = fw_width;
    }

    public void setFWHeight(int fw_height) {
        this.fw_height = fw_height;
    }

    public void setDecoration(boolean decoration) {
        this.decoration = decoration;
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try {
            properties.loadFromXML(new FileInputStream(config));
        } catch(IOException e) { /* System.err.println(e.getMessage()); */}

        vlcPath = properties.getProperty(VLC_PATH);
        currentPath = properties.getProperty(CURRENT_PATH);
        String embeddedString = properties.getProperty(EMBEDDED);
        if(embeddedString != null) {
            try {
                embedded = isWindowsOS() && Boolean.parseBoolean(embeddedString);
            } catch(Exception e) {
                embedded = isWindowsOS();
            }
        }
        deinterlace = DeinterlaceMode.getDeinterlaceMode(properties.getProperty(DEINTERLACE_MODE));
        mux = MuxMode.getMuxMode(properties.getProperty(MUX_MODE));
        boolean autoPathEnabled;
        try {
            autoPathEnabled = Boolean.parseBoolean(properties.getProperty(AUTO_PATH_ENABLED));
        } catch(Exception e) {
            autoPathEnabled = false;
        }
        autoPath = new AutoPath(autoPathEnabled, properties.getProperty(AUTO_PATH));
        String checkUpdateString = properties.getProperty(CHECK_UPDATE);
        if(checkUpdateString != null) {
            checkUpdate = Boolean.parseBoolean(checkUpdateString);
        }
        String hLoc = properties.getProperty(HORIZONTAL_LOCATION);
        try {
            horizontalLocation = Integer.parseInt(hLoc);
        } catch(NumberFormatException e) {}
        String vLoc = properties.getProperty(VERTICAL_LOCATION);
        try {
            verticalLocation = Integer.parseInt(vLoc);
        } catch(NumberFormatException e) {}
        String w = properties.getProperty(WIDTH);
        try {
            width = Integer.parseInt(w);
        } catch(NumberFormatException e) {}
        String h = properties.getProperty(HEIGHT);
        try {
            height = Integer.parseInt(h);
        } catch(NumberFormatException e) {}
        String vol = properties.getProperty(VOLUME);
        try {
            volume = Integer.parseInt(vol);
        } catch(NumberFormatException e) {}
        String onTop = properties.getProperty(ALWAYS_ON_TOP);
        if(onTop != null) {
            alwaysOnTop = Boolean.parseBoolean(onTop);
        }
        w = properties.getProperty(FW_WIDTH);
        try {
            fw_width = Integer.parseInt(w);
        } catch(NumberFormatException e) {}
        h = properties.getProperty(FW_HEIGHT);
        try {
            fw_height = Integer.parseInt(h);
        } catch(NumberFormatException e) {}
        String deco = properties.getProperty(DECORATION);
        if(deco != null) {
            decoration = Boolean.parseBoolean(deco);
        }

        if(vlcPath == null) {
            if(isWindowsOS())
                vlcPath = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe";
            else if(isMacOS())
                vlcPath = "/Applications/VLC.app/Contents/MacOS/VLC";
            else
                vlcPath = "/usr/bin/vlc";
        }
        if(currentPath == null) {
            currentPath = "";
        }
        if("".equals(autoPath.getUrl())) {
            autoPath.setUrl(System.getProperty("user.home"));
        }
    }

    public void saveProperties() {
        Properties properties = new Properties();
        properties.put(VLC_PATH, vlcPath);
        properties.put(CURRENT_PATH, currentPath);
        properties.put(EMBEDDED, "" + embedded);
        properties.put(DEINTERLACE_MODE, deinterlace.getName());
        properties.put(MUX_MODE, mux.getName());
        properties.put(AUTO_PATH_ENABLED, "" + autoPath.isEnabled());
        properties.put(AUTO_PATH, autoPath.getUrl());
        properties.put(CHECK_UPDATE, "" + checkUpdate);
        properties.put(HORIZONTAL_LOCATION, "" + horizontalLocation);
        properties.put(VERTICAL_LOCATION, "" + verticalLocation);
        properties.put(WIDTH, "" + width);
        properties.put(HEIGHT, "" + height);
        properties.put(VOLUME, "" + volume);
        properties.put(ALWAYS_ON_TOP, "" + alwaysOnTop);
        properties.put(FW_WIDTH, "" + fw_width);
        properties.put(FW_HEIGHT, "" + fw_height);
        properties.put(DECORATION, "" + decoration);

        try {
            properties.storeToXML(new FileOutputStream(config), MyFreeTV.name + " " + MyFreeTV.version + " - fichier de configuration");
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
