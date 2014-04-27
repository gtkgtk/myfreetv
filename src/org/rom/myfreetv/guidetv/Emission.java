package org.rom.myfreetv.guidetv;

import java.awt.Image;
import java.util.Calendar;

import org.rom.myfreetv.streams.Channel;

public class Emission implements Comparable<Emission> {

    public final static int maxWidth = 125;
    public final static int maxHeight = 115;

    private int id;
    private Channel channel;
    private Calendar start;
    private Calendar end;
    private String title;
    private String name;
    private String subtitle;
    private String type;
    private String details;
    private String showview;
    private Image image;

    public Emission(int id, Channel channel, Calendar start, Calendar end, String title, String subtitle, String type, String details, String showview, Image image) {
        this.id = id;
        this.channel = channel;
        this.start = start;
        this.end = end;
        this.title = title;
        this.subtitle = subtitle;
        this.type = type;
        this.details = details;
        this.showview = showview;
        this.image = image;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getDetails() {
        return details;
    }

    public Calendar getEnd() {
        return end;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public Calendar getStart() {
        return start;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String inName) {
        this.name = inName;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int compareTo(Emission other) {
        int diff = channel.compareTo(other.channel);
        if(diff == 0)
            return end.compareTo(other.end);
        else
            return diff;
    }

    public String getShowview() {
        return showview;
    }

    public void setShowview(String showview) {
        this.showview = showview;
    }

}
