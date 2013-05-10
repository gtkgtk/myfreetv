package org.rom.myfreetv.view;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.FileIn;
import org.rom.myfreetv.streams.Stream;

public class ImageManager {

    private final static String imagesDir = "/img/";
    private final static String channelsImagesDir = "/img/channels/";
    private static ImageManager instance;

    private Map<String, ImageIcon> map;
    private Map<Channel, ImageIcon> channelsImages;
    private Map<ChannelDim, ImageIcon> scaledChannelsImages;

    class ChannelDim {

        private Channel channel;
        private int w;
        private int h;

        ChannelDim(Channel channel, int w, int h) {
            this.channel = channel;
            this.w = w;
            this.h = h;
        }

        public Channel getChannel() {
            return channel;
        }

        public int getWidth() {
            return w;
        }

        public int getHeight() {
            return h;
        }

        public boolean equals(Object o) {
            ChannelDim cd = (ChannelDim) o;
            return channel.getNum() == cd.channel.getNum() && w == cd.w && h == cd.h;
        }

        public int hashCode() {
            return channel.getNum() << 22 + w << 11 + h;
        }
    }

    private ImageManager() {
        map = new HashMap<String, ImageIcon>();
        channelsImages = new HashMap<Channel, ImageIcon>();
        scaledChannelsImages = new HashMap<ChannelDim, ImageIcon>();
        add("ico", "ico.png");
        add("logo", "logo.png");
        add("play", "play.png");
        add("record", "record.png");
        add("pause", "pause.png");
        add("stop", "stop.png");
        add("prog", "prog.png");
        add("prog_suppr", "prog_suppr.png");
        add("avatar", "MyAvatar.png");
        add("vlc", "vlc.png");
        add("activex-loading", "activex-loading.png");
        add("filelogo", "filelogo.png");
        add("file_delete", "file_delete.png");
        add("file_remove", "file_remove.png");
        add("file_rename", "file_rename.png");
        add("audience", "audience.png");
        add("guidetv", "guidetv.png");
        add("screen", "screen.png");
        add("reset_vlc", "reset_vlc.png");
        add("fullwindow", "fullwindow.png");
        add("fpico", "fpico.png");
        add("freeplayer", "freeplayer.png");
        add("point_interrogation", "point_interrogation.png");
        add("laf", "laf.png");
        add("clean_files","clean_files.png");
        add("shutdown_off","shutdown_off.png");
        add("shutdown_on","shutdown_on.png");
        add("TV","TV.png");
        add("radio","radio.png");
        add("favoris","favo.png");
    }

    public static ImageManager getInstance() {
        if(instance == null)
            instance = new ImageManager();
        return instance;
    }

    private void add(String name, String filename) {
        URL url = getClass().getResource(imagesDir + filename);
        if(url != null)
            map.put(name, new ImageIcon(url));
    }

    public ImageIcon getImageIcon(String name) {
        return map.get(name);
    }

    Image getImage(String name) {
        ImageIcon icon = getImageIcon(name);
        return icon != null ? icon.getImage() : null;
    }

    URL getURLIcon() {
        return getClass().getResource(imagesDir + "ico32.xmp");
    }

    Image getTrayIcon() {
        Image result = null;
        try {
            result = ImageIO.read(getURLIcon());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ImageIcon getLogoImageIcon(Stream stream) {
        ImageIcon ii = null;
        if(stream instanceof Channel) {
            Channel channel = (Channel) stream;
            ii = loadChannelIcon(channel);
        } else if(stream instanceof FileIn) {
            FileIn fileIn = (FileIn) stream;
            if(fileIn.getChannel() != null)
                ii = loadChannelIcon(fileIn.getChannel());
            else
                ii = getImageIcon("filelogo");
        }
        return ii;
    }

    ImageIcon getScaledLogoImageIcon(Stream stream, int w, int h) {
        ImageIcon ii = null;
        if(stream instanceof Channel) {
            Channel channel = (Channel) stream;
            ii = loadScaledChannelIcon(channel, w, h);
        } else if(stream instanceof FileIn) {
            FileIn fileIn = (FileIn) stream;
            if(fileIn.getChannel() != null)
                ii = loadScaledChannelIcon(fileIn.getChannel(), w, h);
            else
                ii = scale(getImageIcon("filelogo"), w, h);
        }
        return ii;
    }

    private ImageIcon loadChannelIcon(Channel channel) {
        if(channel.getNum() >= 300 && channel.getNum() <= 324)
            channel = new Channel(3, channel.getName(), channel.getUrl());
        ImageIcon ii = channelsImages.get(channel);
        if(ii == null) {
            URL url = getClass().getResource(channelsImagesDir + channel.getStringNum() + ".png");
            if(url == null) {
                url = getClass().getResource(channelsImagesDir + "noicon.png");
            }
            if(url != null) {
                ii = new ImageIcon(url);
                channelsImages.put(channel, ii);
            }
        }
        return ii;
    }

    private ImageIcon loadScaledChannelIcon(Channel channel, int w, int h) {
        if(channel.getNum() >= 300 && channel.getNum() <= 324)
            channel = new Channel(3, channel.getName(), channel.getUrl());
        ChannelDim cd = new ChannelDim(channel, w, h);
        ImageIcon ii = scaledChannelsImages.get(cd);
        if(ii == null) {
            ii = loadChannelIcon(channel);
            if(ii != null) {
                ii = scale(ii, w, h);
                scaledChannelsImages.put(cd, ii);
            }
        }
        return ii;
    }

    private ImageIcon scale(ImageIcon ii, int w, int h) {
        int dimX = ii.getIconWidth();
        int dimY = ii.getIconHeight();
        double rapportX = (double) dimX / w;
        double rapportY = (double) dimY / h;
        if(rapportX > 1 || rapportY > 1) {
            double maxRapport = Math.max(rapportX, rapportY);
            dimX /= maxRapport;
            dimY /= maxRapport;
            ii = new ImageIcon(ii.getImage().getScaledInstance(dimX, dimY, Image.SCALE_AREA_AVERAGING));
        }
        return ii;
    }

}
